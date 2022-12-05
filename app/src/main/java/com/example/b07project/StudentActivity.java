package com.example.b07project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.b07project.databinding.ActivityStudentBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/*

Make sure to add the following to the manifest:

<activity
    android:name=".StudentActivity"
    android:exported="false">
    <meta-data
        android:name="android.app.lib_name"
        android:value="" />
</activity>


 */

public class StudentActivity extends AppCompatActivity {

    private ActivityStudentBinding binding;

    public static boolean isInitiated;

    public void switchToMainActivity() {
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        switchActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(switchActivityIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isInitiated = true;
        //StudentModuleCommunicator.activity = StudentActivity.this;
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
                                tab.setText("Explorer");
                            else if (position == 2)
                                tab.setText("My Timeline");
                            else
                                tab.setText("Dashboard");
                        }
                    });

        studentTabLayoutMediator.attach();

    }

    @Override
    protected void onDestroy() {
        isInitiated = false;
        super.onDestroy();
    }
}