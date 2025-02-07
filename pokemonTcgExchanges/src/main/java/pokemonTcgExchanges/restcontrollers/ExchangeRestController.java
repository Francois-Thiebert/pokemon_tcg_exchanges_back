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
import pokemonTcgExchanges.entities.Exchange;
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
	@JsonView(JsonViews.Simple.class)
	public Exchange getById(@PathVariable Long id) {
		Exchange exchange = null;
		exchange = exchangeSrv.getById(id);
		return exchange;
	}

	@GetMapping("/exchange/{id}")
	@JsonView(JsonViews.Simple.class)
	public List<Exchange> getByCard(@PathVariable Long id) {
		Card card = cardSrv.getById(id);
		List<Exchange> exchanges = null;
		exchanges = exchangeSrv.getByCard(card.getId());
		return exchanges;
	}
	
	@GetMapping("/exchange/user/{id}")
	@JsonView(JsonViews.Simple.class)
	public List<Exchange> getByUser(@PathVariable Long id) {
		List<Exchange> exchanges = null;
		exchanges=exchangeSrv.getByUserId(id);
		return exchanges;
	}

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
		exchangeSrv.update(exchangeEnBase);
		return exchangeEnBase;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		exchangeSrv.deleteById(id);
	}

}
