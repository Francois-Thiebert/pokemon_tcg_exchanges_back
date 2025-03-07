package pokemonTcgExchanges.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pokemonTcgExchanges.entities.CanceldExchange;

public interface CanceldExchangeRepository extends JpaRepository<CanceldExchange, Long>  {
	
	@Query("SELECT c FROM CanceldExchange c JOIN c.exchange e "
			+ "WHERE (e.user1.id = :idUserA AND e.user2.id = :idUserB AND e.card1.id = :idCardA AND e.card2.id = :idCardB) "
			+ "OR (e.user1.id = :idUserB AND e.user2.id = :idUserA AND e.card1.id = :idCardA AND e.card2.id = :idCardB) "
			+ "OR (e.user1.id = :idUserA AND e.user2.id = :idUserB AND e.card1.id = :idCardB AND e.card2.id = :idCardA) "
			+ "OR (e.user1.id = :idUserB AND e.user2.id = :idUserA AND e.card1.id = :idCardB AND e.card2.id = :idCardA)")
		CanceldExchange findCancelHistory(@Param("idUserA") Long idUserA, 
		                                         @Param("idUserB") Long idUserB, 
		                                         @Param("idCardA") Long idCardA, 
		                                         @Param("idCardB") Long idCardB);

}
	