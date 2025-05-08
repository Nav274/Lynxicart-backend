package repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import entities.User;
import entities.Userdetails;
import jakarta.transaction.Transactional;

@Repository
public interface UserdetailsRepository extends JpaRepository<Userdetails, Integer> {
	
		Optional<Userdetails> findByuserid(User user);
		
		Optional<Userdetails> findBycontactno(String contactno);
		
		@Modifying
		@Transactional
		@Query("delete from Userdetails ud where ud.userid = :user")
		void deleteByuser(User user);

}
