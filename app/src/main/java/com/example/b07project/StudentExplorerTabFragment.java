package com.example.b07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.b07project.databinding.FragmentStudentExplorerTabBinding;


public class StudentExplorerTabFragment extends Fragment {

    private FragmentStudentExplorerTabBinding binding;

    private ArrayAdapter<Course> possibleFutureCoursesArrayAdapter;
    private static com.example.b07project.StudentModuleCommunicator comm;

    public static boolean forceFutureCoursesListViewReset;

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

    private void visuallyAdjustExplorerTab() {
        /*
        for (int i=0; i < possibleFutureCoursesArrayAdapter.getCount(); i++) {
            View view = getViewByPosition(i, binding.futureCoursesListView);
            view.setSelected(false);
        }
         */
        if (forceFutureCoursesListViewReset) {
            binding.futureCoursesListView.setAdapter(possibleFutureCoursesArrayAdapter);
            forceFutureCoursesListViewReset = false;
        }
        else {
            possibleFutureCoursesArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        visuallyAdjustExplorerTab();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentStudentExplorerTabBinding.inflate(inflater, container, false);

        comm = com.example.b07project.StudentModuleCommunicator.getInstance();

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
        visuallyAdjustExplorerTab();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}
