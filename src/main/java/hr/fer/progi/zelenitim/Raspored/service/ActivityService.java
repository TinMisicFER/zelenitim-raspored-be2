package hr.fer.progi.zelenitim.Raspored.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.progi.zelenitim.Raspored.obj.Activity;
import hr.fer.progi.zelenitim.Raspored.obj.Employee;
import hr.fer.progi.zelenitim.Raspored.repository.ActivityRepository;

@Service
public class ActivityService {
	@Autowired
	private ActivityRepository activityRepository;
	
	public List<Activity> getAllActivities(){
		return activityRepository.findAll();
	}
	
	public void addActivity(Activity activity) {
		activityRepository.save(activity);
	}

	public Optional<Activity> findActivityById(int id) {
		return activityRepository.findById(id);
	}

	public boolean removeActivity(Activity activity) {
		if(activity != null) {
			activityRepository.delete(activity);
			return true;
		} else {
			return false;
		}
	}

	public void updateActivity(Activity activity) {
		activityRepository.updateActivity(activity.getId(), activity.getDescriptionOfActivity(), activity.getPricePerHour());
		
	}
}
