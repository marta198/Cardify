package com.application.cardify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    String[] tabTitles;
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView logout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tabTitles = getResources().getStringArray(R.array.tabTitles);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        logout_button = findViewById(R.id.logout_button);
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
            finish();
        }

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
                finish();
            }
        });

        ViewPager2 viewPage = findViewById(R.id.ViewPager);
        viewPage.setAdapter( new SampleAdapter(this));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPage, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabTitles[position]);
            }
        }).attach();
    }

    class SampleAdapter extends FragmentStateAdapter {


        public SampleAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public SampleAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        public SampleAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0){
                return new TabCards(tabTitles[position]);
            }
            if (position == 1){
                return new TabTrade(tabTitles[position]);
            }
            if (position == 2){
                return new TabTraded(tabTitles[position]);
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
