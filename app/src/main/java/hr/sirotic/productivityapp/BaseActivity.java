package hr.sirotic.productivityapp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;

public class BaseActivity extends AppCompatActivity {
    public CompositeDisposable disposable = new CompositeDisposable();
    private TaskNotificationsManager taskNotificationsManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskNotificationsManager = new TaskNotificationsManager(this);
    }


    public <T extends Fragment> void startNewFragment(T t) {
        int id = findViewById(R.id.fragContainer).getId();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(id, t)
                .commit();
    }


}
