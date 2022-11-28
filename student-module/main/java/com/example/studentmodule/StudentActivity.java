package com.example.studentmodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.studentmodule.databinding.ActivityMainBinding;
import com.example.studentmodule.databinding.ActivityStudentBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StudentActivity extends AppCompatActivity {

    private ActivityStudentBinding binding;

    public void switchToMainActivity() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        switchActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(switchActivityIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(StudentModuleCommunicator.getInstance().getStudentAccount().getName());
        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        StudentViewPagerAdapter studentViewPagerAdapter =
                new StudentViewPagerAdapter(this);

        TabLayout studentTabLayout = binding.studentTabLayout;
        ViewPager2 studentViewPager2 = binding.studentViewPager2;

        studentViewPager2.setAdapter(studentViewPagerAdapter);

        TabLayoutMediator studentTabLayoutMediator =
            new TabLayoutMediator(studentTabLayout, studentViewPager2,
                    new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            if (position == 1)
                                tab.setText("My Timeline");
                            else
                                tab.setText("Dashboard");
                        }
                    });

        studentTabLayoutMediator.attach();

    }
}