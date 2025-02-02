package pokemonTcgExchanges.services;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pokemonTcgExchanges.entities.Card;
import pokemonTcgExchanges.exceptions.CardException;
import pokemonTcgExchanges.repositories.CardRepository;

@Service
public class CardService {

	@Autowired
	private Validator validator;
	@Autowired
	private CardRepository cardRepo;

	public List<Card> getAll() {
		return cardRepo.findAll();
	}

	public Card getById(Long id) {
		return cardRepo.findById(id).orElseThrow(CardException::new);
	}

	public Card create(Card card) {
		Set<ConstraintViolation<Card>> violations = validator.validate(card);
		if (violations.isEmpty()) {
			cardRepo.save(card);
			return card;
		} else {
			throw new CardException();
		}
	}

	public Card update(Card card) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Card>> violations = validator.validate(card);
		if (violations.isEmpty()) {
			Card cardEnBase = getById(card.getId());
			cardEnBase.setCollection(card.getCollection());
			cardEnBase.setName(card.getName());
			cardEnBase.setPicture(card.getPicture());
			cardEnBase.setRarity(card.getRarity());
			cardEnBase.setSerialNumber(card.getSerialNumber());
			cardEnBase.setType(card.getType());
			cardEnBase.setWisher(card.getWisher());
			return cardRepo.save(cardEnBase);
		} else {
			throw new CardException();
		}
	}

	public void delete(Card card) {
		cardRepo.delete(card);
	}

	public void deleteById(Long Id) {
		Card cardEnBase = cardRepo.findById(Id).orElseThrow(CardException::new);
		cardRepo.delete(cardEnBase);
	}

}
