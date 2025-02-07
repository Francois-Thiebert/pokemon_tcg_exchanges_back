package pokemonTcgExchanges.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pokemonTcgExchanges.entities.Card;
import pokemonTcgExchanges.entities.Exchange;
import pokemonTcgExchanges.entities.User;
import pokemonTcgExchanges.exceptions.ExchangeException;
import pokemonTcgExchanges.repositories.ExchangeRepository;

@Service
public class ExchangeService {

	@Autowired
	private Validator validator;
	@Autowired
	private ExchangeRepository exchangeRepo;

	public List<Exchange> getAll() {
		return exchangeRepo.findAll();
	}

	public Exchange create(Exchange exchange) {
		Set<ConstraintViolation<Exchange>> violations = validator.validate(exchange);
		if (violations.isEmpty()) {
			exchangeRepo.save(exchange);
			return exchange;
		} else {
			throw new ExchangeException();
		}
	}

	public Exchange getById(Long id) {
		return exchangeRepo.findById(id).orElseThrow(ExchangeException::new);
	}
	
	public List<Exchange> getByCard(Long id) {
		Card card = new Card();
		card.setId(id);
		List<Exchange> exchanges = new ArrayList<>();
		exchanges = exchangeRepo.findExchangesByCard(card.getId());
		return exchanges;
	}

	public List<Exchange> getByUserId(Long id) {
		User user = new User();
		user.setId(id);
		List<Exchange> exchanges = new ArrayList<>();
		List<Exchange> exchangesUser1 = new ArrayList<>();
		List<Exchange> exchangesUser2 = new ArrayList<>();
		exchangesUser1 = exchangeRepo.findByUser1(user);
		exchangesUser2 = exchangeRepo.findByUser2(user);
		exchangesUser1.forEach((m) -> exchanges.add(m));
		exchangesUser2.forEach((m) -> exchanges.add(m));
		return exchanges;
	}

	public List<Exchange> getAllExchangeUser1(Long id) {
		User user = new User();
		user.setId(id);
		List<Exchange> exchangesUser1 = new ArrayList<>();
		exchangesUser1 = exchangeRepo.findByUser1(user);
		return exchangesUser1;
	}

	public List<Exchange> getAllExchangeUser2(Long id) {
		User user = new User();
		user.setId(id);
		List<Exchange> exchangesUser2 = new ArrayList<>();
		exchangesUser2 = exchangeRepo.findByUser2(user);
		return exchangesUser2;
	}

	public List<Exchange> getExchangeByUsersId(Long idUser1, Long idUser2) {
		List<Exchange> exchanges = new ArrayList<>();
		exchanges = exchangeRepo.findExchangesByUserIds(idUser1, idUser2);
		return exchanges;
	}

	public Exchange update(Exchange exchange) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Exchange>> violations = validator.validate(exchange);
		if (violations.isEmpty()) {
			Exchange exchangeEnBase = getById(exchange.getId());
			exchangeEnBase.setUser1(exchange.getUser1());
			exchangeEnBase.setUser2(exchange.getUser2());
			exchangeEnBase.setCard1(exchange.getCard1());
			exchangeEnBase.setCard2(exchange.getCard2());
			exchangeEnBase.setDate(exchange.getDate());
			return exchangeRepo.save(exchangeEnBase);
		} else {
			throw new ExchangeException();
		}
	}

	public void delete(Exchange exchange) {
		exchangeRepo.delete(exchange);
	}

	public void deleteById(Long Id) {
		Exchange exchangeEnBase = exchangeRepo.findById(Id).orElseThrow(ExchangeException::new);
		exchangeRepo.delete(exchangeEnBase);
	}

}
