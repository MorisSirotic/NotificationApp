package hr.sirotic.productivityapp.repository;

import android.content.Context;

import java.util.List;

import hr.sirotic.productivityapp.database.MyDatabase;
import hr.sirotic.productivityapp.database.dao.TaskDAO;
import hr.sirotic.productivityapp.database.entities.Task;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class TaskRepository {
    private TaskDAO dao;
    private Flowable<List<Task>> tasks;

    public TaskRepository(Context context) {
        MyDatabase db = MyDatabase.getInstance(context);
        dao = db.taskDAO();

        tasks = dao.getAllTasks();


    }


    public Flowable<List<Task>> getTasks() {
        return tasks;
    }

    public Completable insert(Task task) {
        return dao.insert(task);
    }

    public Completable delete(Task task) {
        return dao.delete(task);
    }

    public Completable update(Task task) {
        return dao.update(task);
    }

    public Single<Task> getTask(int id) {
        return dao.getTask(id);
    }

    public Completable deleteAll() {
        return dao.deleteAll();
    }


}
