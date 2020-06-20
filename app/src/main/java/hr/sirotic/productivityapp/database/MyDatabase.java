package hr.sirotic.productivityapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import hr.sirotic.productivityapp.database.dao.TaskDAO;
import hr.sirotic.productivityapp.database.entities.Task;

@Database(entities = {Task.class}, version = 3, exportSchema = false)

public abstract class MyDatabase extends RoomDatabase {
    public static final String DB_NAME = "appDB";
    private static MyDatabase instance;

    public synchronized static MyDatabase getInstance(Context context) {
        if (instance == null) {

            instance = Room.databaseBuilder(context, MyDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract TaskDAO taskDAO();
}
