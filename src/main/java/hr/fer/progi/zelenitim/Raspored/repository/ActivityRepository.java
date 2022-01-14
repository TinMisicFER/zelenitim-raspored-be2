package hr.fer.progi.zelenitim.Raspored.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import hr.fer.progi.zelenitim.Raspored.obj.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer>{
	
	@Transactional
	@Modifying
	@Query(value =  "UPDATE Activity "
			+ "SET description_of_activity = ?2 "
			+ "WHERE id = ?1", nativeQuery = true)
	void updateActivity(Integer id, String descriptionOfActivity, double pricePerHour);

}
