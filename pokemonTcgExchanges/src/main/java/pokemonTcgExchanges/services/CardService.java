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
import pokemonTcgExchanges.exceptions.CardException;
import pokemonTcgExchanges.repositories.CardRepository;
import pokemonTcgExchanges.repositories.ExchangeRepository;

@Service
public class CardService {

	@Autowired
	private Validator validator;
	@Autowired
	private CardRepository cardRepo;
	@Autowired
	private ExchangeService exchangeSrv;
	@Autowired
	private ExchangeRepository exchangeRepo;

	public List<Card> getAll() {
		return cardRepo.findAll();
	}
	
	public List<Card> getAllByCollections(List<Integer> collections) {
	    return cardRepo.findAllCardExchangable(collections);
	}
	
	public List<Card> getAllExchangabale() {
		List<Integer> collections = new ArrayList<>();
		collections.add(0);
		collections.add(1);
		collections.add(2);
		collections.add(3);
	    return getAllByCollections(collections);
	}

	public Card getById(Long id) {
		return cardRepo.findById(id).orElseThrow(CardException::new);
	}
	
	public List<Card> getWishedByRarityUser(Long userId, Integer rarity) {
		return cardRepo.findWishCardByRarity(userId, rarity);
	}
	
	public Card getByGivedCard(Long CardId) {
		Card givedCard = cardRepo.findGivedCardById(CardId);
		return givedCard;
	}
	
	public Long getGiverByCard(Long CardId) {
		Card givedcard = cardRepo.findGivedCardById(CardId);
	    if (givedcard != null && !givedcard.getGiver().isEmpty()) {
	        return givedcard.getGiver().iterator().next().getId();
	    } else {
	        return null;
	    }
	}
	
	public List<Long> getGiversIdsByCard(Long cardId) {
	    Card givedcard = cardRepo.findGivedCardById(cardId);
	    if (givedcard != null && !givedcard.getGiver().isEmpty()) {
	        List<Long> giverIds = new ArrayList<>();
	        for (User giver : givedcard.getGiver()) {
	            giverIds.add(giver.getId());
	        }
	        return giverIds;
	    } else {
	        return new ArrayList<>();
	    }
	}

	
	public List<Card> getWishedEachRarityUser(Long userId){
		List<Card> CardsRarity0 = cardRepo.findWishCardByRarity(userId, 0);
		List<Card> firstCards0 = CardsRarity0.subList(0, Math.min(5, CardsRarity0.size()));
		List<Card> CardsRarity1 = cardRepo.findWishCardByRarity(userId, 1);
		List<Card> firstCards1 = CardsRarity1.subList(0, Math.min(5, CardsRarity1.size()));
		List<Card> CardsRarity2 = cardRepo.findWishCardByRarity(userId, 2);
		List<Card> firstCards2 = CardsRarity2.subList(0, Math.min(5, CardsRarity2.size()));
		List<Card> CardsRarity3 = cardRepo.findWishCardByRarity(userId, 3);
		List<Card> firstCards3 = CardsRarity3.subList(0, Math.min(5, CardsRarity3.size()));
		List<Card> CardsRarity4 = cardRepo.findWishCardByRarity(userId, 4);
		List<Card> firstCards4 = CardsRarity4.subList(0, Math.min(5, CardsRarity4.size()));
		
		List<Card> combinedCards = new ArrayList<>();
		combinedCards.addAll(firstCards0);
		combinedCards.addAll(firstCards1);
		combinedCards.addAll(firstCards2);
		combinedCards.addAll(firstCards3);
		combinedCards.addAll(firstCards4);
		return combinedCards;
	}
	
	public List<Card> getByCollection(Integer collection) {
		return cardRepo.findByCollection(collection);
	}

	public List<Card> getByExchange(Long id) {
		Exchange exchange = exchangeSrv.getById(id);
		List<Card> cardExchange = new ArrayList<>();
		cardExchange = cardRepo.findByExchange(exchange.getId());
		return cardExchange;
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
			cardEnBase.setGiver(card.getGiver());
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
