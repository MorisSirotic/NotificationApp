package hr.sirotic.productivityapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {


    @PrimaryKey
    private int id;
    private String title;
    private String content;
    private long alarmTime;


    public Task(int id, String title, String content, long alarmTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.alarmTime = alarmTime;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }
}
