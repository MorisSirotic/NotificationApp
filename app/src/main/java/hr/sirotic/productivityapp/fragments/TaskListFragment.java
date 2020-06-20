package hr.sirotic.productivityapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hr.sirotic.productivityapp.AlarmReceiver;
import hr.sirotic.productivityapp.R;
import hr.sirotic.productivityapp.adapters.TaskAdapter;
import hr.sirotic.productivityapp.database.entities.Task;
import hr.sirotic.productivityapp.repository.TaskRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskListFragment extends BaseFragment implements TaskAdapter.OnItemClick {
    private RecyclerView recyclerView;
    private TaskRepository taskRepository;
    private FragmentActivity parent;
    private TaskAdapter taskAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parent = getParentActivity();
        setHasOptionsMenu(true);
        recyclerView = parent.findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(parent));

        taskAdapter = new TaskAdapter();
        taskAdapter.setListener(this);

        taskRepository = new TaskRepository(parent.getApplication());

        //Observe for changes in the tasks list
        taskRepository.getTasks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(taskAdapter::setTasks);


        recyclerView.setAdapter(taskAdapter);
        FloatingActionButton fab = parent.findViewById(R.id.fab);
        fab.setOnClickListener(view -> startNewFragment(new AddUpdateTaskFragment())
        );


    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public void onFinishClick(Task task) {

        taskRepository.delete(task).subscribeOn(Schedulers.io()).subscribe();
        new AlarmReceiver().cancelAlarm(getParentActivity(), task.getId());


    }

}
