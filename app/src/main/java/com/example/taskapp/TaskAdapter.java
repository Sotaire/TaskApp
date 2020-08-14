package com.example.taskapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.models.Task;
import com.example.taskapp.ui.ElementLIstener;
import com.example.taskapp.ui.dashboard.ImageListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> tasks;
    public ElementLIstener listener;
    public TaskAdapter(ArrayList<Task> list){
    tasks = list;
}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task,parent,false);
    ViewHolder vh = new ViewHolder(view);
    vh.listener = listener;
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(tasks.get(position),position);
    }

    public void setElement(int position, Task task){
        tasks.set(position,task);
        notifyDataSetChanged();
    }
    public void deleteElement(int position){
        tasks.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ElementLIstener listener;
        private TextView textView;
        private TextView time;
        int position;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.rv_text);
            time = itemView.findViewById(R.id.text_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(textView.getText().toString(),position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.fragmentOpen(position);
                    return true;
                }
            });
        }

        public void bind(Task task,int position) {
            this.position = position;
            textView.setText(task.getTitle());
            time.setText(getDate(task.getCreatedAt()));
        }

        private String getDate(long time){
            DateFormat dateFormat = new SimpleDateFormat("dd.MMM yyyy HH:mm", Locale.getDefault());
            return dateFormat.format(time);
        }
    }


}
