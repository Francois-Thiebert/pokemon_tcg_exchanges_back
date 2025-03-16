package pokemonTcgExchanges.restcontrollers;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import pokemonTcgExchanges.entities.User;
import pokemonTcgExchanges.jsonviews.JsonViews;
import pokemonTcgExchanges.services.UserService;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthentificationRestController {
	
	@Autowired
	private UserService userSrv;

	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthentificationRestController.class);
	
	@GetMapping("")
	@JsonView(JsonViews.User.class)
	public User authentification(@AuthenticationPrincipal User user) {
		LOGGER.info("connexion, "+user.getLogin());
		user.setLastLogging(LocalDateTime.now());
		userSrv.setUserVisibility(user);
		userSrv.update(user);
		return user;
	}

}
