package pokemonTcgExchanges.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pokemonTcgExchanges.entities.Card;
import pokemonTcgExchanges.entities.Exchange;

public interface CardRepository extends JpaRepository<Card, Long> {

	Optional<Card> findById(Long id);
	
	List<Card> findByCollection(Integer collection);
	
	@Query("SELECT c FROM Card c JOIN c.wisher u WHERE u.id = :idUser AND c.rarity = :rarity")
	List<Card> findWishCardByRarity(@Param("idUser") Long idUser, @Param("rarity") Integer rarity);

	
	@Query("SELECT e.card1, e.card2 FROM Exchange e WHERE e.id = :exchangeId")
	List<Card> findByExchange(@Param("exchangeId") Long exchangeId);
	
	@Query("SELECT c FROM Card c JOIN c.giver u WHERE c.id = :idCard")
	Card findGivedCardById(@Param("idCard") Long idCard);
	
	@Query("SELECT c FROM Card c WHERE c.collection IN :idCollection")
	List<Card> findAllCardExchangable(@Param("idCollection") List<Integer> idCollection);

}
