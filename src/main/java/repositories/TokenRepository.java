package repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import entities.Token;
import entities.User;
import jakarta.transaction.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

	Optional<Token> findByjwttoken(String token);

	Optional<Token> findByuser(User user);

	Optional<Token> findByuser_userid(int id);

	@Modifying
	@Transactional
	@Query("Delete from Token t where t.user.userid = :userid")
	void deleteByUserid(@Param("userid") int userid);

}
