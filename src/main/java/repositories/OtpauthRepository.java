package repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import entities.Otpauth;
import entities.User;
import jakarta.transaction.Transactional;

@Repository
public interface OtpauthRepository extends JpaRepository<Otpauth, Integer> {
	
	@Query("Select oa from Otpauth oa where oa.userid.userid = :userid AND oa.otp = :otp ")
	Optional<Otpauth> findByuseridANDotp(int userid, String otp);
	
	Optional<Otpauth> findByuserid(User userid);
	
	@Transactional
    @Modifying
	@Query("delete from  Otpauth oa where oa.userid = :user")
	void deleteAllByUser(User user);

}
