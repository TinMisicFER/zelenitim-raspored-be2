package hr.fer.progi.zelenitim.Raspored.repository;

import java.util.List;
import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import hr.fer.progi.zelenitim.Raspored.obj.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	Optional<Employee> findByUsername(String username);
	
	@Query("SELECT e FROM Employee e "
			 + "WHERE LOWER(e.name) LIKE LOWER(:s)"
			 + "OR LOWER(e.lastName) LIKE LOWER(:s)"
			 + "OR LOWER(e.email) LIKE LOWER(:s)"
			 + "OR LOWER(e.OIB) LIKE LOWER(:s)"
			 + "OR LOWER(e.username) LIKE LOWER(:s)")
	List<Employee> likeQuery(@Param("s") String query);
	
	
	
	@Transactional
	@Modifying
	@Query(value =  "UPDATE Employee "
			+ "SET name = ?2 "
			+ "WHERE id = ?1", nativeQuery = true)
	void updateEmployee(Integer id, String name, String lastName, String email, String OIB, String username, String password);
}