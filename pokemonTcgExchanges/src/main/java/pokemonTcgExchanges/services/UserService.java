package pokemonTcgExchanges.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pokemonTcgExchanges.entities.Blocking;
import pokemonTcgExchanges.entities.Exchange;
import pokemonTcgExchanges.entities.Role;
import pokemonTcgExchanges.entities.User;
import pokemonTcgExchanges.exceptions.UserException;
import pokemonTcgExchanges.repositories.BlockingRepository;
import pokemonTcgExchanges.repositories.ExchangeRepository;
import pokemonTcgExchanges.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private Validator validator;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ExchangeRepository exchangeRepo;
	@Autowired
	private BlockingRepository blockingRepo;
	@Autowired
	private ExchangeService exchangeSrv;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> getAll() {
		return userRepo.findAll();
	}

	public User getById(Long id) {
		return userRepo.findById(id).orElseThrow(UserException::new);
	}
	
	public List<User> getByGiver(Long CardId) {
		List<User> users = userRepo.findGiverByCard(CardId);
		return users;
	}

	// Retourne la liste des users impliqués en echange avec le user en paramètre
	public List<User> getByExchange(User user) {
		List<User> usersExchange = new ArrayList<>();
		List<User> usersExchange1 = new ArrayList<>();
		List<User> usersExchange2 = new ArrayList<>();
		List<Exchange> exchangehUser1 = exchangeSrv.getAllExchangeUser1(user.getId());
		List<Exchange> exchangehUser2 = exchangeSrv.getAllExchangeUser2(user.getId());
		for (Exchange e : exchangehUser1) {
			User user2 = e.getUser2();
			usersExchange2.add(user2);
		}
		for (Exchange e : exchangehUser2) {
			User user1 = e.getUser1();
			usersExchange1.add(user1);
		}
		usersExchange.addAll(usersExchange1);
		usersExchange.addAll(usersExchange2);
		return usersExchange;
	}

	// Retourne la liste des ID des users impliqués en echange avec le user en
	// paramètre
	public List<Long> getIDsByExchange(User user) {
		List<User> usersExchange = new ArrayList<>();
		List<User> usersExchange1 = new ArrayList<>();
		List<User> usersExchange2 = new ArrayList<>();
		List<Exchange> exchangehUser1 = exchangeSrv.getAllExchangeUser1(user.getId());
		List<Exchange> exchangehUser2 = exchangeSrv.getAllExchangeUser2(user.getId());
		for (Exchange e : exchangehUser1) {
			User user2 = e.getUser2();
			usersExchange2.add(user2);
		}
		for (Exchange e : exchangehUser2) {
			User user1 = e.getUser1();
			usersExchange1.add(user1);
		}
		usersExchange.addAll(usersExchange1);
		usersExchange.addAll(usersExchange2);
		List<Long> userIds = usersExchange.stream().map(User::getId).collect(Collectors.toList());

		return userIds;
	}
	
	public boolean checkLoginExists(String login) {
        return userRepo.existsByLogin(login);
    }

	public User create(User user) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		if (violations.isEmpty()) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRole(Role.ROLE_USER);
			user.setIsVisible(true);
			return userRepo.save(user);
		} else {
			throw new UserException();
		}
	}

	public User update(User user) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		if (violations.isEmpty()) {
			User userEnBase = getById(user.getId());
			userEnBase.setExchanges1(user.getExchanges1());
			userEnBase.setExchanges2(user.getExchanges2());
			userEnBase.setLogin(user.getLogin());
			userEnBase.setFriendCode(user.getFriendCode());
			userEnBase.setToGiveList(user.getToGiveList());
			userEnBase.setWishList(user.getWishList());
			userEnBase.setIsVisible(user.getIsVisible());
			userEnBase.setLastLogging(user.getLastLogging());
			return userRepo.save(userEnBase);
		} else {
			throw new UserException();
		}
	}
	
	public void connectionSinceUpdate(User user) {
		user.setConnectedSinceUpdate(true);
		userRepo.save(user);
	}

	public void deleteByUser(User user) {
		User userEnBase = userRepo.findById(user.getId()).orElseThrow(UserException::new);
		exchangeRepo.deleteExchangeByUser(userEnBase.getId());
		userRepo.delete(userEnBase);
	}

	public void deleteByUserId(Long userId) {
		User userEnBase = userRepo.findById(userId).orElseThrow(UserException::new);
		exchangeRepo.deleteExchangeByUser(userEnBase.getId());
		userRepo.delete(userEnBase);
	}

	public boolean loginExist(String login) {
		return userRepo.findByLogin(login).isPresent();
	}

	public User findByLogin(String login) {
		return userRepo.findByLogin(login).orElseThrow(() -> {
			throw new UserException();
		});
	}
	
	public void checkActiveUsers() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime limitDate = now.minusHours(72);
		List<User> inactiveUsers = new ArrayList<>();
		inactiveUsers = userRepo.findInactiveUsers(limitDate);
		for(User u: inactiveUsers) {
			u.setIsVisible(false);
			userRepo.save(u);
		}
	}
	
	public boolean IsBlockedUser(Long userID) {
		Long blockingID = blockingRepo.findBlockedIDByUserID(userID);
		if(blockingID != null) {
			return true;
		}else {
			return false;
		}
	}
	
	public void setUserVisibility(User user){
		Long exchangeNumber = exchangeSrv.getCurrentExchangeNumberByUser(user);
		if(!IsBlockedUser(user.getId()) && exchangeNumber < 7) {
			user.setIsVisible(true);
		}
	}
	
	public void blockingManagment(User user){
		Blocking blocking = blockingRepo.findBlockingByUserID(user.getId());
		if(blocking != null) {
			blocking.setReports((short) (blocking.getReports()+1));
			if(blocking.getReports() % 5 == 0) {
				blocking.setIsBlocked(true);
				blocking.setAskedUnblocking(false);
			}
		}else {
			blocking = new Blocking();
			blocking.setReports((short) 1);
			blocking.setUser(user);
			blocking.setIsBlocked(false);
		}
		blockingRepo.save(blocking);
	}
	
	public boolean hasAskedUnblocking(User user) {
		Blocking blocking = blockingRepo.findBlockingByUserID(user.getId());
		if(blocking != null && blocking.getAskedUnblocking() == true) {
			return true;
		}else {
			return false;
		}
	}
	
	public void askUnblocking(User user) {
		Blocking blocking = blockingRepo.findBlockingByUserID(user.getId());
		if(blocking != null && blocking.getAskedUnblocking() == false) {
			blocking.setAskedUnblocking(true);
			blockingRepo.save(blocking);
		}
	}
	
	public Long countUsers() {
		return userRepo.count();
	}
	
	public Long countActiveUsers() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime limitDate = now.minusHours(72);
		return userRepo.countActiveUsers(limitDate);
	}
	
	public Long countBlockedUsers() {
		return userRepo.countBlockedUsers();
	}
	
	public Long countWishedCardsByUser(Long userID) {
		return userRepo.countWishedCardsByUser(userID);
	}
	
	public int countToGiveCardsByUser(Long userID) {
		return userRepo.countToGiveCardsByUser(userID);
	}
	
	public void deleteInactiveUsers() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime limitDate = now.minusMonths(3);
		userRepo.deleteInactiveUsers(limitDate);
	}

}
