package hr.fer.progi.zelenitim.Raspored;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import hr.fer.progi.zelenitim.Raspored.obj.*;
import hr.fer.progi.zelenitim.Raspored.service.JwtUserDetailsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RasporedApplicationTests {

	private Employee dir;
	private Group g0;
	private Task t0;
	private Activity be;
	
	@Test
	void contextLoads() {
		
	}
	
	/**Postavljanje svjezeg djelatnika, grupe i zadatka i medjusobno povezivanje
	 * 
	 */
	@BeforeEach
	void setUp() {
		dir = new Employee("Direktor","Direktorović", "direktor@gmail.com", "0101010101", "direktor", "superSecret");
		be = new Activity("BE Development", 30.5);
		g0 = new Group(dir,"grupa0",be);
		t0 = new Task("Omogućiti Security",20,g0,Date.valueOf(LocalDate.of(2021,9,23)),Date.valueOf(LocalDate.of(2022, 1, 12)));
	}
	
	//	UNIT TESTS
	
	@Test
	void testGroupGetLeader() {
		assertEquals(dir,g0.getLeader());
	}
	
	@Test
	void testGroupGetMembers() {
		//adding new member
		Employee novi = new Employee("Tin","Misic","tin.misic@fer.hr","xxxxx","tin","tinPass");
		Set<Employee> emps = g0.getMembers();
		emps.add(novi);
		g0.setMembers(emps);
		
		Set<Employee> expected = new HashSet<>();
		expected.add(novi);
		expected.add(dir);
		
		assertEquals(expected,g0.getMembers());
	}

	@Test
	void testTaskGetGroup() {assertEquals(g0,t0.getGroup());}

	@Test
	void testTaskSetLocation() {
		Location loc = new Location(10000,10000);
		t0.setLocation(loc);
		assertEquals(loc,t0.getLocation());
	}
	
	@Test
	void testSetPassword() {
		dir.setPassword("novaLozinka");
		BCryptPasswordEncoder coder = new BCryptPasswordEncoder();
		assertEquals(true, coder.matches("novaLozinka", dir.getPassword()));
	}
	
	@Test
	void testGetGroupsIAmMemberOf() {
		
		
		Set<Group> expected = new HashSet<>();
		expected.add(new Group(dir, "grupa0", be));
		dir.setGroupsIAmAMemberOf(expected);
		
		assertEquals(expected, dir.getGroupsIAmAMemberOf());
		
	}
}
