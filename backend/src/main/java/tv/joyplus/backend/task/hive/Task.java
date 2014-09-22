package tv.joyplus.backend.task.hive;

public interface Task {

	abstract void handle(String date);
}
