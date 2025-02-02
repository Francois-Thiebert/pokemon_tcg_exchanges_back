package pokemonTcgExchanges.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ExchangeException extends RuntimeException{
	
public ExchangeException() {
		
	}
	
	public ExchangeException(String message) {
		super (message);
	}

}
