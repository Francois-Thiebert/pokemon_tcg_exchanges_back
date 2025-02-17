package pokemonTcgExchanges.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pokemonTcgExchanges.entities.Card;
import pokemonTcgExchanges.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByLogin(String login);
	
	boolean existsByLogin(String login);
	
	@Query("SELECT u FROM User u JOIN u.toGiveList c WHERE c.id = :idCard")
	List<User> findGiverByCard(@Param("idCard") Long idCard);

	
//	@Query("SELECT u FROM Card c JOIN c.giver u WHERE c.id = :idCard")
//	List<User> findGiverByCard(@Param("idCard") Long idCard);
	

}
