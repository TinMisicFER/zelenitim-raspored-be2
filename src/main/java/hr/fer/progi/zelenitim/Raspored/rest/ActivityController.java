package hr.fer.progi.zelenitim.Raspored.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.progi.zelenitim.Raspored.dto.ActivityDTO;
import hr.fer.progi.zelenitim.Raspored.dto.CreateActivityDTO;
import hr.fer.progi.zelenitim.Raspored.obj.Activity;
import hr.fer.progi.zelenitim.Raspored.service.ActivityService;


@RestController
@RequestMapping("/activity")
public class ActivityController {
	private final ActivityService activityService;
	
	@Autowired
	public ActivityController(ActivityService activityService) {
		this.activityService = activityService;
	}
	
	@GetMapping("")
	public List<ActivityDTO> index(){
		List<ActivityDTO> allActivities = activityService.getAllActivities().stream().map(a -> new ActivityDTO(a)).collect(Collectors.toList());
		return allActivities;
	}
	
	@PostMapping("")
	public ResponseEntity<Activity> addNewActivity(@RequestBody CreateActivityDTO createActivityDTO,  @AuthenticationPrincipal User user) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			activityService.addActivity(createActivityDTO.createActivityFromCreateActivityDTO());
			return new ResponseEntity<Activity>(HttpStatus.OK);
		}
		return new ResponseEntity<Activity>(HttpStatus.FORBIDDEN);
	}
	
	@PostMapping("/{id}/changeRecords") 
	public ResponseEntity<Activity> changeRecords(@PathVariable String id, @RequestBody CreateActivityDTO createActivityDTO, @AuthenticationPrincipal User user) {
		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return new ResponseEntity<Activity>(HttpStatus.FORBIDDEN);
		}
		
		int idn;
		try {
			idn = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}

		Optional<Activity> optAct = activityService.findActivityById(idn);
		
		if(optAct.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Activity activity = optAct.get();
		if(createActivityDTO.getDescriptionOfActivity() != null) {
			 activity.setDescriptionOfActivity(createActivityDTO.getDescriptionOfActivity());
		 }
		
		if(createActivityDTO.getPricePerHour() >= 0) {
			activity.setPricePerHour(createActivityDTO.getPricePerHour());
		 }
		
		activityService.updateActivity(activity);
		
		return new ResponseEntity<Activity>(HttpStatus.OK);
		
		
	}
	

	@PostMapping("/{id}/delete")
	public ResponseEntity<Activity> deleteById(@PathVariable String id, @AuthenticationPrincipal User user) {
		
		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return new ResponseEntity<Activity>(HttpStatus.FORBIDDEN);
		}
		
		int idn;
		try {
			idn = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}
	
		Optional<Activity> optAct = activityService.findActivityById(idn);
		if(optAct.isEmpty()) { 
			return new ResponseEntity<Activity>(HttpStatus.BAD_REQUEST);
		}

		Activity activity = optAct.get();
		if(!activityService.removeActivity(activity)) return new ResponseEntity<Activity>(HttpStatus.EXPECTATION_FAILED);
		return new ResponseEntity<Activity>(HttpStatus.OK);
		

	}
	
	@GetMapping("/{id}")
	public ActivityDTO getActivitybyId(@PathVariable String id, @AuthenticationPrincipal User user) {
		int idn;
		try {
			idn = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}
	
		Optional<Activity> optAct = activityService.findActivityById(idn);
		if(optAct.isEmpty()) { 
			return null;
		}
		
		return new ActivityDTO(optAct.get());
	}
}
