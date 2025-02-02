package pokemonTcgExchanges.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pokemonTcgExchanges.entities.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

	Optional<Card> findById(Long id);
	
	@Query("SELECT e.card1, e.card2 FROM Exchange e WHERE e.id = :exchangeId")
	List<Card> findByExchange(@Param("exchangeId") Long exchangeId);

}
