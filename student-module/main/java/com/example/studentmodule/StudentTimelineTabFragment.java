package com.example.studentmodule;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentmodule.databinding.FragmentSecondBinding;
import com.example.studentmodule.databinding.FragmentStudentTimelineTabBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StudentTimelineTabFragment extends Fragment {

    private FragmentStudentTimelineTabBinding binding;
    private StudentModuleCommunicator comm;
    private ArrayAdapter<String> timelineCoursesStringArrayAdapter;
    public static boolean forceTimelineListViewReset;

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void visuallyAdjustTimelineListView() {
        if (forceTimelineListViewReset) {
            binding.timelineListView.setAdapter(timelineCoursesStringArrayAdapter);
            forceTimelineListViewReset = false;
        } else {
            timelineCoursesStringArrayAdapter.notifyDataSetChanged();
        }
        /*
        for (int i=0; i < timelineCoursesStringArrayAdapter.getCount(); i++) {
            if (timelineCoursesStringArrayAdapter.getItem(i).contains("~~")) {
                View view = getViewByPosition(i, binding.timelineListView);
                view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setClickable(false);
            }
        }
        */

        if (timelineCoursesStringArrayAdapter.isEmpty())
            binding.timelineTopTextView
                    .setText("You can start planning by choosing courses in the explorer tab!");
        else {
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat yearFormat =
                    new SimpleDateFormat("yyyy", Locale.getDefault());
            SimpleDateFormat monthFormat =
                    new SimpleDateFormat("MM", Locale.getDefault());
            int year = Integer.parseInt(yearFormat.format(date));
            int month = Integer.parseInt(monthFormat.format(date));


            String semester;
            if (4 < month && month < 9)
                semester = "Summer ";
            else if (month > 8)
                semester = "Fall ";
            else
                semester = "Winter ";
            binding.timelineTopTextView
                    .setText("Your current semester is: " + semester + year + ".\n\nHere's your timeline starting from the upcoming semester!");
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentStudentTimelineTabBinding.inflate(inflater, container, false);

        comm = StudentModuleCommunicator.getInstance();


        timelineCoursesStringArrayAdapter = new ArrayAdapter<String>(
                this.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, //support_simple_spinner_dropdown_item,
                comm.getTimelineCoursesStringArray()
        );
        visuallyAdjustTimelineListView();
        binding.timelineListView.setAdapter(this.timelineCoursesStringArrayAdapter);
        binding.timelineListView.setClickable(false);
        binding.timelineListView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange (int visibility) {
                visuallyAdjustTimelineListView();
            }
        });

        visuallyAdjustTimelineListView();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        visuallyAdjustTimelineListView();
    }

    @Override
    public void onResume() {
        super.onResume();
        visuallyAdjustTimelineListView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}