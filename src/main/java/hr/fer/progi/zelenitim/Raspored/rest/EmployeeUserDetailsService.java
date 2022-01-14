//package hr.fer.progi.zelenitim.Raspored.rest;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import hr.fer.progi.zelenitim.Raspored.obj.Employee;
//import hr.fer.progi.zelenitim.Raspored.service.EmployeeService;
//
//import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//@Service
//public class EmployeeUserDetailsService implements UserDetailsService{
//	
//	@Autowired
//	private EmployeeService empService;
//		
//	@Override
//	public UserDetails loadUserByUsername(String username) {
//		Optional<Employee> optEmp = empService.findByUsername(username);
//		if(optEmp.isPresent()) {
//			Employee emp = optEmp.get();
//			String fetchedPass = emp.getPassword();
//		
//			return new User(username,fetchedPass,authorities(emp));
//		}
//		else {
//			throw new UsernameNotFoundException("Invalid username.");
//		}
//	}
//	
//	private List<GrantedAuthority> authorities(Employee user){
//		if(user.getUsername().equals("direktor"))
//			return commaSeparatedStringToAuthorityList("ROLE_ADMIN, ROLE_EMPLOYEE");
//		return commaSeparatedStringToAuthorityList("ROLE_EMPLOYEE");
//	}
//
//}
