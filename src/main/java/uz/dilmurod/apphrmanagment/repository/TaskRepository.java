package uz.dilmurod.apphrmanagment.repository;

import uz.dilmurod.apphrmanagment.entity.Task;
import uz.dilmurod.apphrmanagment.entity.User;
import uz.dilmurod.apphrmanagment.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findAllByTaskTaker(User taskTaker);
    List<Task> findAllByTaskGiver(User taskTaker);
    List<Task> findByTaskTakerAndIdNot(User taskTaker, UUID id);
    List<Task> findAllByTaskGiverAndCreatedAtBetweenAndStatus(User taskGiver, Timestamp startTime, Timestamp endTime, TaskStatus status);
}
