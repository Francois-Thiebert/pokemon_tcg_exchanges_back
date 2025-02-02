package pokemonTcgExchanges.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserException extends RuntimeException{
	
public UserException() {
		
	}
	
	public UserException(String message) {
		super (message);
	}


}
