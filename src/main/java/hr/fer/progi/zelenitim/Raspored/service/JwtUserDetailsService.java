package hr.fer.progi.zelenitim.Raspored.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

import hr.fer.progi.zelenitim.Raspored.obj.Employee;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private EmployeeService empService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		Optional<Employee> optEmp = empService.findByUsername(username);
		if(optEmp.isPresent()) {
			Employee emp = optEmp.get();
			String fetchedPass = emp.getPassword(); //ovdje ne mmogu raditi auth pass jer ga nemam
		
			return new User(username,fetchedPass,authorities(emp));//authorities(emp)
		}
		else {
			throw new UsernameNotFoundException("Invalid username.");
		}
		
//		if ("admin".equals(username)) 
//		{
//			return new User("admin", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", new ArrayList<>());
//		} else {
//			throw new UsernameNotFoundException("User not found with username: " + username);
//		}
	}
	
	private List<GrantedAuthority> authorities(Employee user){
		if(user.getUsername().equals("direktor"))
			return commaSeparatedStringToAuthorityList("ROLE_ADMIN, ROLE_EMPLOYEE");
		return commaSeparatedStringToAuthorityList("ROLE_EMPLOYEE");
	}
}
