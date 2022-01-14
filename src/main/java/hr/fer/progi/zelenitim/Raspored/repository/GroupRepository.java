package hr.fer.progi.zelenitim.Raspored.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hr.fer.progi.zelenitim.Raspored.obj.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {
	
	@Query("SELECT g FROM Group g "
		 + "WHERE LOWER(g.name) LIKE LOWER(:name)")
	List<Group> likeQuery(@Param("name") String query);
}