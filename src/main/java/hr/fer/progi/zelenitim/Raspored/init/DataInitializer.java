package hr.fer.progi.zelenitim.Raspored.init;

import java.time.LocalDate;
import java.sql.Date;
import java.util.Set;

//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.annotation.Configuration;

import hr.fer.progi.zelenitim.Raspored.obj.Activity;
import hr.fer.progi.zelenitim.Raspored.obj.Employee;
import hr.fer.progi.zelenitim.Raspored.obj.Group;
import hr.fer.progi.zelenitim.Raspored.obj.Task;
import hr.fer.progi.zelenitim.Raspored.service.ActivityService;
//import hr.fer.progi.zelenitim.Raspored.repository.EmployeeRepository;
import hr.fer.progi.zelenitim.Raspored.service.EmployeeService;
import hr.fer.progi.zelenitim.Raspored.service.GroupService;
import hr.fer.progi.zelenitim.Raspored.service.TaskService;

@Configuration
public class DataInitializer {
	@Autowired
	private EmployeeService empService;
	
	@Autowired
	private ActivityService actService;
	
	@Autowired
	private GroupService grpService;
	
	@Autowired
	private TaskService tskService;
	
	@EventListener
	public void appReady(ApplicationReadyEvent event) {
		Employee[] empList = {
		new Employee("Direktor","Direktorović", "direktor@gmail.com", "0101010101", "direktor", "superSecret"),
		new Employee("August","Šenoa", "augustus@gmail.com", "989898989", "augustus", "augustin"),
		new Employee("Marko","Marulić", "marko@gmail.com", "32784323", "markec", "darkMark"),
		new Employee("Ivana","Brlić-Mažuranić", "ivana@gmail.com", "1841874", "ivana", "hlapić"),
		new Employee("Stephen","King","sking@gmail.com","09211947","stephen","king"),
		new Employee("Marcus","Aurelius","stoic@gmail.com","0426121","emperor","philosopher"),
		new Employee("George","Orwell","ingsoc@gmail.com","1984","animal","farm")
		};
		
		for(Employee emp: empList) {
			empService.addNewEmployee(emp);
		}
		
		Activity be = new Activity("BE Development", 30.5);
		Activity fe = new Activity("FE Development", 30.5);
		actService.addActivity(be);
		actService.addActivity(fe);
		
		Group g1 = new Group(empList[0],"BackEndGrupa",be);
		Set<Employee> g1mems = g1.getMembers();
		for(int i=1;i<4;i++)
			g1mems.add(empList[i]);
		g1.setMembers(g1mems);
		
		grpService.saveGroup(g1);
		
		Group g2 = new Group(empList[4],"FrontEndGrupa",fe);
		Set<Employee> g2mems = g2.getMembers();
		for(int i=5;i<7;i++)
			g2mems.add(empList[i]);
		g2.setMembers(g2mems);
		
		grpService.saveGroup(g2);

		
		Task[] tasks = {
				new Task("Omogućiti Security",20,g1,Date.valueOf(LocalDate.of(2021,9,23)),Date.valueOf(LocalDate.of(2022, 2, 1))),
				new Task("Napraviti DTO",20,g1,Date.valueOf(LocalDate.of(2021,9,23)),Date.valueOf(LocalDate.of(2022, 1, 5))),
				new Task("Intranet",20,g2,Date.valueOf(LocalDate.of(2021,9,23)),Date.valueOf(LocalDate.of(2022, 1, 12))),
				new Task("Search",20,g2,Date.valueOf(LocalDate.of(2021,9,23)),Date.valueOf(LocalDate.of(2022, 1, 6)))
		};
		
		for(Task tsk: tasks) {
			tskService.saveTask(tsk);
		}
		
		tskService.newAssignment(empList[0], tasks[0],10);
		tskService.newAssignment(empList[1], tasks[1],10);
		tskService.newAssignment(empList[5], tasks[2],11);
		tskService.newAssignment(empList[6], tasks[3],11);
	}
//	
//	@Bean
//	CommandLineRunner clr() {
//		return args -> {
//			Employee marko = new Employee("Marko", "marko@gmail.com");
//
//			employeeService.addNewEmployee(marko);
//		};
//	}


}
