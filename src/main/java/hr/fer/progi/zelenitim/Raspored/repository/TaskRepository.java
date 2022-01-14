package hr.fer.progi.zelenitim.Raspored.repository;

import hr.fer.progi.zelenitim.Raspored.obj.Employee;
import hr.fer.progi.zelenitim.Raspored.obj.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Integer> {
}
