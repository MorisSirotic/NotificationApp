package hr.sirotic.productivityapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import hr.sirotic.productivityapp.fragments.TaskListFragment;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startNewFragment(new TaskListFragment());


    }

    @Override
    public <T extends Fragment> void startNewFragment(T t) {
        super.startNewFragment(t);
    }


}
