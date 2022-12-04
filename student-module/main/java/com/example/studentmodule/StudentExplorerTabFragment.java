package com.example.studentmodule;

import android.graphics.drawable.AdaptiveIconDrawable;
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
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmodule.databinding.FragmentSecondBinding;
import com.example.studentmodule.databinding.FragmentStudentCoursesTabBinding;
import com.example.studentmodule.databinding.FragmentStudentExplorerTabBinding;

import java.util.ArrayList;


public class StudentExplorerTabFragment extends Fragment {

    private FragmentStudentExplorerTabBinding binding;

    private ArrayAdapter<Course> possibleFutureCoursesArrayAdapter;
    private static StudentModuleCommunicator comm;

    private void makeToast(String s) {
        Toast toast = Toast.makeText(binding.getRoot().getContext(), s, Toast.LENGTH_SHORT);
        toast.show();
    }

    private static String bottomTextViewTextGenerator(int numberOfCourses) {
        String s = "";

        if (numberOfCourses == 1) {
            s = "1 course selected.";
        } else if (numberOfCourses > 1){
            s = "" + numberOfCourses + " courses selected.";
        } else {
            s = "No courses selected so far!";
        }

        return s;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("studentExplorerTab", "onResume() is called");
        possibleFutureCoursesArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentStudentExplorerTabBinding.inflate(inflater, container, false);

        comm = StudentModuleCommunicator.getInstance();

        possibleFutureCoursesArrayAdapter = new ArrayAdapter<Course>(
                this.getContext(),
                androidx.appcompat.R.layout.abc_list_menu_item_checkbox,  //support_simple_spinner_dropdown_item,
                comm.getPossibleFutureCoursesArray()
        );
        binding.futureCoursesListView.setAdapter(possibleFutureCoursesArrayAdapter);
        binding.futureCoursesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //binding.futureCoursesListView.setClickable(true);
        binding.futureCoursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                comm.alterCourseStateInFutureCourses(possibleFutureCoursesArrayAdapter.getItem(position));
                //android:background="?android:attr/activatedBackgroundIndicator" in xml
                binding.explorerBottomTextView.setText(
                        bottomTextViewTextGenerator(comm.getFutureCoursesArray().size())
                );
            }
        });

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
