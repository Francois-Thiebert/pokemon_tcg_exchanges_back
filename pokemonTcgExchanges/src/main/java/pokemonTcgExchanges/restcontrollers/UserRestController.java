package pokemonTcgExchanges.restcontrollers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonView;

import pokemonTcgExchanges.entities.User;
import pokemonTcgExchanges.jsonviews.JsonViews;
import pokemonTcgExchanges.services.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserRestController {

	@Autowired
	private UserService userSrv;

	@GetMapping("")
	@JsonView(JsonViews.User.class)
	public List<User> getAll() {
		return userSrv.getAll();
	}

	@GetMapping("/{id}")
	@JsonView(JsonViews.UserWithAll.class)
	public User getById(@PathVariable Long id) {
		User user = null;
		user = userSrv.getById(id);
		return user;
	}

	@GetMapping("/all/{id}")
	@JsonView(JsonViews.UserWithAll.class)
	public User getUserAllById(@PathVariable Long id) {
		User user = null;
		user = userSrv.getById(id);
		return user;
	}
	
	@GetMapping("/giverCard/{id}")
	@JsonView(JsonViews.UserWithAll.class)
	public List<User> getUserByGiveCard(@PathVariable Long Cardid) {
		List<User> users = null;
		users = userSrv.getByGiver(Cardid);
		return users;
	}

	// Retourne la liste des users impliqués en echange avec le user en paramètre
	@GetMapping("/exchange/{id}")
	@JsonView(JsonViews.UserWithAll.class)
	public List<User> getUsersMatch(@PathVariable Long id) {
		User user = getById(id);
		List<User> users = null;
		users = userSrv.getByExchange(user);
		return users;
	}

	// Retourne la liste des ID des users impliqués dans l'echange
	@GetMapping("/idByExchange/{id}")
	@JsonView(JsonViews.Simple.class)
	public List<Long> getUsersIdByMatchs(@PathVariable Long id) {
		User user = getById(id);
		List<Long> usersIds = null;
		usersIds = userSrv.getIDsByExchange(user);
		return usersIds;
	}

//	@GetMapping("/login/check/{login}")
//	public boolean loginExist(@PathVariable String login) {
//		return userSrv.loginExist(login);
//	}
//	
	@GetMapping("/login/check/{login}")
    public ResponseEntity<Boolean> checkLogin(@PathVariable String login) {
        boolean exists = userSrv.checkLoginExists(login);
        return ResponseEntity.ok(exists);
    }
	
	@GetMapping("/activityCheck")
    public void checkActivity() {
        userSrv.checkActiveUsers();
    }
	
	@GetMapping("/isBlocked/{id}")
	@JsonView(JsonViews.Simple.class)
	public boolean isBlockedUser(@PathVariable Long id) {
		return userSrv.IsBlockedUser(id);
	}
	
	@GetMapping("/hasAskedUnblocking/{id}")
	@JsonView(JsonViews.Simple.class)
	public boolean hasAskedUnblocking(@PathVariable Long id) {
		User user = getById(id);
		return userSrv.hasAskedUnblocking(user);
	}
	
	@GetMapping("/askUnblocking/{id}")
	public void askUnblocking(@PathVariable Long id) {
		User user = getById(id);
		userSrv.askUnblocking(user);
	}

	@PostMapping({ "", "/inscription" })
	@JsonView(JsonViews.User.class)
	@ResponseStatus(code = HttpStatus.CREATED)
	public User create(@Valid @RequestBody User user, BindingResult br) {
		if (br.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		userSrv.create(user);
		return user;
	}

	@PutMapping("/{id}")
	@JsonView(JsonViews.UserWithAll.class)
	public User update(@RequestBody User user, @PathVariable Long id) {
		User userEnBase = userSrv.getById(id);
		if (user.getLogin() != null) {
			userEnBase.setLogin(user.getLogin());
		}
		if (user.getPassword() != null) {
			userEnBase.setPassword(user.getPassword());
		}
		if (user.getExchanges1() != null) {
			userEnBase.setExchanges1(user.getExchanges1());
		}
		if (user.getExchanges2() != null) {
			userEnBase.setExchanges1(user.getExchanges2());
		}
		if (user.getFriendCode() != null) {
			userEnBase.setFriendCode(user.getFriendCode());
		}
		if (user.getToGiveList() != null) {
			userEnBase.setToGiveList(user.getToGiveList());
		}
		if (user.getWishList() != null) {
			userEnBase.setWishList(user.getWishList());
		}
		userSrv.update(userEnBase);
		return userEnBase;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		userSrv.deleteByUserId(id);
	}

}
