package hr.sirotic.productivityapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hr.sirotic.productivityapp.AlarmReceiver;
import hr.sirotic.productivityapp.R;
import hr.sirotic.productivityapp.database.entities.Task;
import hr.sirotic.productivityapp.repository.TaskRepository;
import io.reactivex.schedulers.Schedulers;

import static hr.sirotic.productivityapp.constants.TaskConstants.TASK_CONTENT_KEY;
import static hr.sirotic.productivityapp.constants.TaskConstants.TASK_TIME_KEY;
import static hr.sirotic.productivityapp.constants.TaskConstants.TASK_TITLE_KEY;
import static java.util.Calendar.MONTH;

public class AddUpdateTaskFragment extends BaseFragment {

    public static int ID = 0;
    Calendar ic;
    private TaskRepository taskRepository;
    private EditText title;
    private EditText content;
    private ImageButton btnCalendar;
    private ImageButton btnTime;
    private EditText etDate;
    private EditText etTime;

    public AddUpdateTaskFragment() {


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskRepository = new TaskRepository(this.getContext().getApplicationContext());

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_new_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.etTaskTitle);
        content = view.findViewById(R.id.etTaskContent);

        btnCalendar = view.findViewById(R.id.btnCalendarAdd);
        btnTime = view.findViewById(R.id.btnTimeAdd);


        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);

        ic = Calendar.getInstance(Locale.ENGLISH);


        Bundle bundle = this.getArguments();
        if (bundle != null && !bundle.isEmpty()) {
            String t = bundle.getString(TASK_TITLE_KEY);
            String c = bundle.getString(TASK_CONTENT_KEY);
            long l = bundle.getLong(TASK_TIME_KEY);
            ic.setTime(new Date(l));
            title.setText(t);
            content.setText(c);
            updateDateView();
            updateHourView();

        }
        view.findViewById(R.id.btnAddUpdateTask).setOnClickListener(view1 -> {
            Bundle bundle1 = AddUpdateTaskFragment.this.getArguments();

            String t = title.getText().toString();
            String c = content.getText().toString();

            // I couldn't find a way to get the task id(it's primary key in this case). It was set to autogenerate before, but now it's manual generation.
            // If on autogenerate, the task get's it's primary key(id) assigned when Room runs on runtime. Notifications are tied to the ID of the task so I need it.
            // If anyone ever reads this, and actually bothers to find a way around this... reach out to me and tell me how you did it :)
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            int value = sharedPref.getInt("ID", 0);


            Task task = new Task(value, t, c, ic.getTimeInMillis());
            task.setId(value);
            int alarmId = sharedPref.getInt("ID", 0);


            if (bundle1 == null) {
                if (!(etDate.getText().toString().isEmpty() && title.getText().toString().isEmpty())) {

                    taskRepository.insert(task).subscribeOn(Schedulers.io()).subscribe(() -> {

                    });
                }

            }

            if (!etDate.getText().toString().isEmpty() && !title.getText().toString().isEmpty()) {
                new AlarmReceiver().setRepeatingAlarm(getParentActivity(), ic, task.getId(), task.getTitle(), task.getContent());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("ID", value + 1);
                editor.commit();
                startNewFragment(new TaskListFragment());


            } else {
                Toast.makeText(getContext(), "Title and date must be set.", Toast.LENGTH_SHORT).show();
            }

        });

        btnCalendar.setOnClickListener(v -> {


            LinearLayout parent = new LinearLayout(getParentActivity());
            parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250));
            View cv = LayoutInflater.from(v.getContext()).inflate(R.layout.calendar_dialog_layout, parent, true);

            CalendarView calendarView = cv.findViewById(R.id.cvDialog);
            TextView tvDialogYear = cv.findViewById(R.id.tvDialogYear);
            TextView tvTitle = cv.findViewById(R.id.tvDialogTitle);


            calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                ic.set(Calendar.YEAR, year);
                ic.set(Calendar.MONTH, month);
                ic.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String dayName = ic.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
                String monthName = ic.getDisplayName(MONTH, Calendar.SHORT, Locale.getDefault());
                int dayOfMontha = ic.get(Calendar.DAY_OF_MONTH);
                String str = dayName + ", " + monthName + " " + dayOfMontha;
                tvTitle.setText(str);
                tvDialogYear.setText(String.valueOf(year));

            });

            new AlertDialog.Builder(view.getContext())
                    .setView(cv)
                    .setPositiveButton("Set", (dialog, which) -> {
                        updateDateView();
                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .show();


        });

        btnTime.setOnClickListener(v -> {
            View tp = LayoutInflater.from(view.getContext()).inflate(R.layout.time_dialog_layout, new LinearLayout(view.getContext()));
            TimePicker timePicker = tp.findViewById(R.id.tpTimePicker);
            timePicker.setIs24HourView(true);


            timePicker.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
                ic.set(Calendar.HOUR_OF_DAY, hourOfDay);
                ic.set(Calendar.MINUTE, minute);
            });

            new AlertDialog.Builder(view.getContext())
                    .setView(tp)
                    .setPositiveButton("Set", (dialog, which) -> {
                        updateHourView();
                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .show();
        });

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            startNewFragment(new TaskListFragment());
        });


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void updateDateView() {

        String dayName = ic.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        String monthName = ic.getDisplayName(MONTH, Calendar.SHORT, Locale.getDefault());
        int dayOfMonthNum = ic.get(Calendar.DAY_OF_MONTH);
        int yearNum = ic.get(Calendar.YEAR);
        String str = dayName + ", " + monthName + " " + dayOfMonthNum + ", " + yearNum;
        etDate.setText(str);
    }

    private void updateHourView() {
        int hour = ic.get(Calendar.HOUR);
        int minutes = ic.get(Calendar.MINUTE);
        String hourStr = String.valueOf(hour < 10 ? "0" + hour : hour);
        String minutesStr = String.valueOf(minutes < 10 ? "0" + minutes : minutes);
        String str = hourStr + ":" + minutesStr;
        etTime.setText(str);

    }

}
