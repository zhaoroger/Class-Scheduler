package com.example.studentmodule;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.studentmodule.databinding.FragmentStudentCoursesTabBinding;


public class StudentCoursesTabFragment extends Fragment {

    private FragmentStudentCoursesTabBinding binding;

    private ArrayAdapter<Course> studentCoursesArrayAdapter;
    private ArrayAdapter<Course> allCoursesArrayAdapter;
    private static StudentModuleCommunicator comm;

    private static Course promptedCourse;

    private void visuallyAdjustCoursesTab() {
        allCoursesArrayAdapter.notifyDataSetChanged();
        studentCoursesArrayAdapter.notifyDataSetChanged();
    }

    private void promptToApplyChanges(String message) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Log.i("prompt", "clicked true");
                        removeCourseFromStudentCourses(StudentModuleCommunicator.getInstance());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        promptedCourse = null;
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void addCourseToStudentCourses(StudentModuleCommunicator comm, Course newCourse) {
        if (!comm.getSortedStudentCoursesArray().contains(newCourse)) {
            Log.i("addCourse", "passed if");
            comm.addCourseToSortedStudentCourses(newCourse);
            StudentExplorerTabFragment.forceFutureCoursesListViewReset = true;
            StudentTimelineTabFragment.forceTimelineListViewReset = true;
            visuallyAdjustCoursesTab();
        }
    }

    private void removeCourseFromStudentCourses(StudentModuleCommunicator comm) {
        if (promptedCourse != null) {
            if (comm.getSortedStudentCoursesArray().contains(promptedCourse)) {
                comm.removeCourseFromSortedStudentCourses(promptedCourse);
                if (comm.getFutureCoursesArray().contains(promptedCourse)) {
                    comm.alterCourseStateInFutureCourses(promptedCourse);
                }

                StudentExplorerTabFragment.forceFutureCoursesListViewReset = true;
                StudentTimelineTabFragment.forceTimelineListViewReset = true;
                visuallyAdjustCoursesTab();
            }
            promptedCourse = null;
        }
    }

    private void makeToast(String s) {
        Toast toast = Toast.makeText(binding.getRoot().getContext(), s, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentStudentCoursesTabBinding.inflate(inflater, container, false);
        comm = StudentModuleCommunicator.getInstance();

        allCoursesArrayAdapter = new ArrayAdapter<Course>(
                this.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                comm.getSortedAllCoursesArray()
                //comm.getPossibleFutureCoursesArray()
        );
        binding.coursesSpinner.setAdapter(allCoursesArrayAdapter);

        studentCoursesArrayAdapter = new ArrayAdapter<Course>(
                this.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                comm.getSortedStudentCoursesArray()
        );
        binding.coursesListView.setAdapter(studentCoursesArrayAdapter);

        binding.coursesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                promptedCourse = comm.getSortedStudentCoursesArray().get(i);
                promptToApplyChanges(
                        "Are you sure to delete \""
                        +  promptedCourse.toString()
                        + "\" ?"
                );
                return false;
            }
        });
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        visuallyAdjustCoursesTab();

        binding.addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!allCoursesArrayAdapter.isEmpty()) {
                    Course newCourse;
                    newCourse = (Course) binding.coursesSpinner.getSelectedItem();
                    if (comm.getSortedStudentCoursesArray().contains(newCourse)) {
                        makeToast(newCourse.getCourseCode() + " is already added.");
                    } else {
                        addCourseToStudentCourses(comm, newCourse);
                        visuallyAdjustCoursesTab();
                    }
                }
            }
        });

        binding.studentSaveCoursesFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comm.updateDataBase();
                StudentExplorerTabFragment.forceFutureCoursesListViewReset = true;
                StudentTimelineTabFragment.forceTimelineListViewReset = true;
                visuallyAdjustCoursesTab();
                makeToast("All saved!");
            }
        });

        binding.studentLogoutFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getActivity(), MainActivity.class);
                switchActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StudentActivity.isInitiated = false;
                startActivity(switchActivityIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        visuallyAdjustCoursesTab();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}