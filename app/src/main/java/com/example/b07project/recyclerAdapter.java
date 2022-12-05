package com.example.b07project;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder>{
    private final ArrayList<Course> courseList;
    private final AdminMainActivity activity;

    public recyclerAdapter(ArrayList<Course> courseList, AdminMainActivity activity){
        this.courseList = courseList;
        this.activity = activity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView coursename;
        private final TextView coursecode;

        public MyViewHolder(final View view){
            super(view);
            coursename = view.findViewById(R.id.textView2);
            coursecode = view.findViewById(R.id.course_code);

            view.findViewById(R.id.button).setOnClickListener(view1 -> {
                Intent intent = new Intent(view1.getContext(),EditWindow.class);
                intent.putExtra("course", courseList.get(getAdapterPosition()));
                intent.putParcelableArrayListExtra("courseList", courseList);
                view1.getContext().startActivity(intent);
                RealtimeDatabase.unsyncCourseList(activity.getActiveListener());
                activity.finish();
            });
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String cn = courseList.get(position).getName();
        String cc = courseList.get(position).getCourseCode();
        holder.coursename.setText(cn);
        holder.coursecode.setText(cc);

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
