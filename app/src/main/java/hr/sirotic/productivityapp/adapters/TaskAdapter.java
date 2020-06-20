package hr.sirotic.productivityapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hr.sirotic.productivityapp.R;
import hr.sirotic.productivityapp.database.entities.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private OnItemClick listener;


    private List<Task> taskList = new ArrayList<>();

    public TaskAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task task = taskList.get(position);

        holder.title.setText(task.getTitle());
        holder.content.setText(task.getContent());

        holder.time.setText(fromattedDate(task.getAlarmTime()));
        holder.image.setOnClickListener(v -> {
            listener.onFinishClick(task);
        });


    }

    @Override
    public int getItemCount() {

        return taskList.size();


    }

    public void setListener(OnItemClick listener) {
        this.listener = listener;
    }

    public void setTasks(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    private String fromattedDate(long time) {
        Calendar ic = Calendar.getInstance();
        ic.setTime(new Date(time));
        int dayName = ic.get(Calendar.DAY_OF_WEEK);
        int monthName = ic.get(Calendar.MONTH);
        int dayOfMontha = ic.get(Calendar.DAY_OF_MONTH);
        int year = ic.get(Calendar.YEAR);

        int hour = ic.get(Calendar.HOUR);
        int minutes = ic.get(Calendar.MINUTE);

        String dateStr = dayOfMontha + "/" + monthName + "/" + year + "    " + hour + ":" + minutes;
        return dateStr;
    }

    public interface OnItemClick {
        void onFinishClick(Task task);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView time;
        ImageView image;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            time = itemView.findViewById(R.id.tvTime);
            content = itemView.findViewById(R.id.tvContent);
            image = itemView.findViewById(R.id.ivFinish);

        }
    }
}