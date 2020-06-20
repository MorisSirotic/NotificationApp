package hr.sirotic.productivityapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hr.sirotic.productivityapp.database.entities.Task;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface TaskDAO {

    @Insert
    Completable insert(Task user);

    @Delete
    Completable delete(Task task);

    @Update
    Completable update(Task task);

    @Query("SELECT * FROM task")
    Flowable<List<Task>> getAllTasks();

    @Query("SELECT * FROM task WHERE :id")
    Single<Task> getTask(int id);

    @Query("DELETE  FROM task ")
    Completable deleteAll();
}
