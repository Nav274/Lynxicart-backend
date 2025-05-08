package repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Optional<Category> findBycategoryname(String categname);

}
