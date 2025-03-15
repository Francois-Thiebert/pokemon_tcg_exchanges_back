package pokemonTcgExchanges.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pokemonTcgExchanges.entities.Blocking;

public interface BlockingRepository extends JpaRepository<Blocking, Long>{
	
	@Query("SELECT b.id FROM Blocking b WHERE b.user.id = :userID AND b.isBlocked = true")
	Long findBlockedIDByUserID(@Param("userID") Long userID);
	
	@Query("SELECT b FROM Blocking b WHERE b.user.id = :userID")
	Blocking findBlockingByUserID(@Param("userID") Long userID);

}
