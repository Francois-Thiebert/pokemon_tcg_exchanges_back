package pokemonTcgExchanges.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CardException extends RuntimeException{
	
public CardException() {
		
	}
	
	public CardException(String message) {
		super (message);
	}

}
