package com.example.studentmodule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StudentViewPagerAdapter extends FragmentStateAdapter {
    public StudentViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new StudentCoursesTabFragment();
            case 1:
                return new StudentTimelineTabFragment();
            default:
                return createFragment(0);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
