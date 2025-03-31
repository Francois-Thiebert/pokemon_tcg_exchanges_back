package pokemonTcgExchanges.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pokemonTcgExchanges.entities.Exchange;
import pokemonTcgExchanges.entities.User;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

	@Transactional
	@Modifying
	@Query("delete from Exchange e where e.user1.id = :userId or e.user2.id = :userId")
	void deleteExchangeByUser(@Param("userId") Long userId);
	
	
	@Query("SELECT e FROM Exchange e WHERE (e.user1.id = :idUser1 AND e.user2.id = :idUser2) OR (e.user1.id = :idUser2 AND e.user2.id = :idUser1)")
	List<Exchange> findExchangesByUserIds(@Param("idUser1") Long idUser1, @Param("idUser2") Long idUser2);
	
	@Query("SELECT e FROM Exchange e WHERE e.card1 = :idCard OR e.card2 = :idCard")
	List<Exchange> findExchangesByCard(@Param("idCard") Long idCard);
	
	@Query("SELECT e FROM Exchange e WHERE e.user1 = :idUser OR e.user2 = :idUser")
	List<Exchange> findExchangesByUserID(@Param("idUser") Long idUser);
	
	@Query("SELECT e FROM Exchange e WHERE e.user1 = :idUser")
	List<Exchange> findExchangesUser1ByUserID(@Param("idUser") Long idUser);
	
	@Query("SELECT e FROM Exchange e WHERE e.user2 = :idUser")
	List<Exchange> findExchangesUser2ByUserID(@Param("idUser") Long idUser);
	
	@Query("SELECT COUNT(e) FROM Exchange e WHERE (e.state = 0 OR e.state = 1) AND (e.user1.id = :idUser OR e.user2.id = :idUser)")
	Long countCurrentExchangeByUser(@Param("idUser") Long idUser);
	
	@Query("SELECT COUNT(e) FROM Exchange e WHERE (e.user1.id = :idUser OR e.user2.id = :idUser)")
	Long countExchangeByUser(@Param("idUser") Long idUser);
	
	@Query("SELECT COUNT(e) FROM Exchange e WHERE e.state = 0 OR e.state = 1")
	Long countCurrentExchange();

	@Query("SELECT COUNT(e) FROM Exchange e WHERE e.state = 2")
	Long countFinishedExchange();
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Exchange e WHERE e.date < :limitDate")
	void deleteOldExchanges(@Param("limitDate") LocalDateTime limitDate);

	
	Exchange findByUser1AndUser2(User user1, User user2);
	List<Exchange> findByUser1(User user);
	List<Exchange> findByUser2(User user);
	
	

}
