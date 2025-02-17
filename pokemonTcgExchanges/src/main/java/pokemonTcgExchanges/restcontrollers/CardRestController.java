package pokemonTcgExchanges.restcontrollers;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import pokemonTcgExchanges.entities.Card;
import pokemonTcgExchanges.entities.Exchange;
import pokemonTcgExchanges.entities.User;
import pokemonTcgExchanges.jsonviews.JsonViews;
import pokemonTcgExchanges.services.CardService;
import pokemonTcgExchanges.services.ExchangeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/card")
public class CardRestController {

	@Autowired
	private CardService cardSrv;
	@Autowired
	private ExchangeService exchangeSrv;

	@GetMapping("")
	@JsonView(JsonViews.Simple.class)
	public List<Card> getAll() {
		return cardSrv.getAll();
	}
	
	@GetMapping("/collection/{collection}")
	@JsonView(JsonViews.Simple.class)
	public List<Card> getByCollection(@PathVariable Integer collection) {
		return cardSrv.getByCollection(collection);
	}

	@GetMapping("/{id}")
	@JsonView(JsonViews.Simple.class)
	public Card getById(@PathVariable Long id) {
		Card card = null;
		card = cardSrv.getById(id);
		return card;
	}
	
	@GetMapping("/gived/{id}")
	@JsonView(JsonViews.Card.class)
	public Card getGivedCard(@PathVariable Long id) {
		Card givedcard = null;
		givedcard = cardSrv.getByGivedCard(id);
		return givedcard;
	}
	
	@GetMapping("/giver/{id}")
	@JsonView(JsonViews.Card.class)
	public Long getGiverByCard(@PathVariable Long id) {
	    Long giverId = cardSrv.getGiverByCard(id);
	    return giverId;
	}
	
	@GetMapping("/givers/{id}")
	@JsonView(JsonViews.Card.class)
	public List<Long> getGiversByCard(@PathVariable Long id) {
		List<Long> giversIds = cardSrv.getGiversIdsByCard(id);
	    return giversIds;
	}
	
//	@GetMapping("/giver/{id}")
//	@JsonView(JsonViews.Card.class)
//	public Long getFirstGiverId(@PathVariable Long id) {
//	    Card givedcard = cardSrv.getByGivedCard(id);
//	    if (givedcard != null && !givedcard.getGiver().isEmpty()) {
//	        return givedcard.getGiver().iterator().next().getId();
//	    } else {
//	        return null;
//	    }
//	}


	@GetMapping("/exchange/{id}")
	@JsonView(JsonViews.Simple.class)
	public List<Card> getByExchange(@PathVariable Long id) {
		Exchange exchange = exchangeSrv.getById(id);
		List<Card> cards = null;
		cards = cardSrv.getByExchange(exchange.getId());
		return cards;
	}
	
	@GetMapping("/wished/user/{Userid}/rarity/{rarity}")
	@JsonView(JsonViews.Card.class)
	public List<Card> getByxWishedByRarity(@PathVariable Long Userid, Integer rarity) {
		List<Card> cards = null;
		cards = cardSrv.getWishedByRarityUser(Userid, rarity);
		return cards;
	}
	
	@GetMapping("/topwished/user/{Userid}")
	@JsonView(JsonViews.Simple.class)
	public List<Card> getTopWishedByUser(@PathVariable Long Userid) {
		List<Card> cards = null;
		cards = cardSrv.getWishedEachRarityUser(Userid);
		return cards;
	}

	@PostMapping({ "", "/create" })
	@JsonView(JsonViews.Simple.class)
	@ResponseStatus(code = HttpStatus.CREATED)
	public Card create(@Valid @RequestBody Card card, BindingResult br) {
		if (br.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		cardSrv.create(card);
		return card;
	}

	@PutMapping("/{id}")
	@JsonView(JsonViews.Simple.class)
	public Card update(@RequestBody Card card, @PathVariable Long id) {
		Card cardEnBase = cardSrv.getById(id);
		if (card.getName() != null) {
			cardEnBase.setName(card.getName());
		}
		if (card.getPicture() != null) {
			cardEnBase.setPicture(card.getPicture());
		}
		if (card.getSerialNumber() != null) {
			cardEnBase.setSerialNumber(card.getSerialNumber());
		}
		if (card.getType() != null) {
			cardEnBase.setType(card.getType());
		}
		cardEnBase.setCollection(card.getCollection());
		cardEnBase.setRarity(card.getRarity());
		cardSrv.update(cardEnBase);
		return cardEnBase;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		cardSrv.deleteById(id);
	}

}
