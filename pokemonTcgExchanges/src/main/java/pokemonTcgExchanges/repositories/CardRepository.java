package pokemonTcgExchanges.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pokemonTcgExchanges.entities.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

	Optional<Card> findById(Long id);

}
