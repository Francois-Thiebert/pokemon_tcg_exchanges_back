package pokemonTcgExchanges.restcontrollers;

import java.util.List;

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
import pokemonTcgExchanges.entities.Cause;
import pokemonTcgExchanges.entities.Exchange;
import pokemonTcgExchanges.entities.User;
import pokemonTcgExchanges.jsonviews.JsonViews;
import pokemonTcgExchanges.services.CardService;
import pokemonTcgExchanges.services.ExchangeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/exchange")
public class ExchangeRestController {

	@Autowired
	private ExchangeService exchangeSrv;
	@Autowired
	private CardService cardSrv;

	@GetMapping("")
	@JsonView(JsonViews.Simple.class)
	public List<Exchange> getAll() {
		return exchangeSrv.getAll();
	}

	@GetMapping("/{id}")
	@JsonView(JsonViews.Exchange.class)
	public Exchange getById(@PathVariable Long id) {
		Exchange exchange = null;
		exchange = exchangeSrv.getById(id);
		return exchange;
	}

	@GetMapping("card/{id}")
	@JsonView(JsonViews.Simple.class)
	public List<Exchange> getByCard(@PathVariable Long id) {
		Card card = cardSrv.getById(id);
		List<Exchange> exchanges = null;
		exchanges = exchangeSrv.getByCard(card.getId());
		return exchanges;
	}
	
	@GetMapping("/user/{id}")
	@JsonView(JsonViews.Simple.class)
	public List<Exchange> getByUser(@PathVariable Long id) {
		List<Exchange> exchanges = null;
		exchanges=exchangeSrv.getByUserId(id);
		return exchanges;
	}
	
	@GetMapping("/new/{id}")
	@JsonView(JsonViews.Exchange.class)
	public List<Exchange> getNewExchanges(@PathVariable Long id) {
		List<Exchange> exchanges = null;
		exchanges=exchangeSrv.getNewExchanges(id);
		return exchanges;
	}
	
	@GetMapping("/adm/cancel_history/{idUserA}/{idUserB}/{idCardA}/{idCardB}")
	@JsonView(JsonViews.Simple.class)
	public Boolean getCancelHistory(@PathVariable Long idUserA, @PathVariable Long idUserB, @PathVariable Long idCardA, @PathVariable Long idCardB) {
		return exchangeSrv.isCancelHistory(idCardA, idCardB, idUserA, idUserB);
	}
	
	@GetMapping("/adm/numberCurrent")
	public Long currentExchangeNumber() {
		return exchangeSrv.countCurrentExchange();
	}
	@GetMapping("/adm/numberFinished")
	public Long finishedExchangeNumber() {
		return exchangeSrv.countFinishedExchange();
	}
	
//	@GetMapping("/new/givers/{id}")
//	@JsonView(JsonViews.Simple.class)
//	public List<User> getNewGivers(@PathVariable Long id) {
//		List<User> givers = null;
//		givers=exchangeSrv.getGivers(id);
//		return givers;
//	}
//	
//	@GetMapping("/new/wishedFind/{id}")
//	@JsonView(JsonViews.Simple.class)
//	public List<Card> getWishedCardsFind(@PathVariable Long id) {
//		List<Card> wishedFind = null;
//		wishedFind=exchangeSrv.getWishedFind(id);
//		return wishedFind;
//	}

	@PostMapping({ "", "/create" })
	@JsonView(JsonViews.Simple.class)
	@ResponseStatus(code = HttpStatus.CREATED)
	public Exchange create(@Valid @RequestBody Exchange exchange, BindingResult br) {
		if (br.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		exchangeSrv.create(exchange);
		return exchange;
	}

	@PutMapping("/{id}")
	@JsonView(JsonViews.Simple.class)
	public Exchange update(@RequestBody Exchange exchange, @PathVariable Long id) {
		Exchange exchangeEnBase = exchangeSrv.getById(id);
		if (exchange.getCard1() != null) {
			exchangeEnBase.setCard1(exchange.getCard1());
		}
		if (exchange.getCard2() != null) {
			exchangeEnBase.setCard2(exchange.getCard2());
		}
		if (exchange.getDate() != null) {
			exchangeEnBase.setDate(exchange.getDate());
		}
		if (exchange.getUser1() != null) {
			exchangeEnBase.setUser1(exchange.getUser1());
		}
		if (exchange.getUser2() != null) {
			exchangeEnBase.setUser2(exchange.getUser2());
		}
		if (exchange.getState() != null) {
			exchangeEnBase.setState(exchange.getState());
		}
		exchangeSrv.update(exchangeEnBase);
		return exchangeEnBase;
	}
	
	@PutMapping("/cancel/{idCanceldExchange}/{idUser}/{cause}")
	@JsonView(JsonViews.Simple.class)
	public Exchange cancel(
	        @PathVariable Long idCanceldExchange, 
	        @PathVariable Long idUser, 
	        @PathVariable Cause cause) {
	    Exchange exchangeEnBase = exchangeSrv.getById(idCanceldExchange);
	    exchangeSrv.cancelExchange(exchangeEnBase, idUser, cause);
	    return exchangeEnBase;
	}


	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		exchangeSrv.deleteById(id);
	}

}
