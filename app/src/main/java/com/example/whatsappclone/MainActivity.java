
package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageSwitcher;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.example.whatsappclone.R.id.fab_action;

public class MainActivity extends AppCompatActivity {

    TabLayout tablayout;
    ViewPager viewPager;
    Toolbar toolbar;
    FloatingActionButton fabaction;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabaction = findViewById(R.id.fab_action);
        tablayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        setUpWithViewPager(viewPager);
        tablayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ChangeFabIcon(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpWithViewPager(ViewPager viewPager){

        MainActivity.SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChatFragment(),"CHATS");
        adapter.addFragment(new StatusFragment(),"STATUS");
        adapter.addFragment(new CallFragment(),"CALLS");
        viewPager.setAdapter(adapter);
    }

    private static class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> fragmentList = new ArrayList();
        private final ArrayList<String> fragmentTitle = new ArrayList();

        public SectionsPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){

            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }

        public CharSequence getPageTitle(int position){

            return fragmentTitle.get(position);
        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu ,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id){
            case R.id.menu_search:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
            break;
            case R.id.menu_more:
                Toast.makeText(this, "main", Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void ChangeFabIcon(final int index){


        fabaction.hide();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                switch (index){
                    case 0:
                        //fabaction.setImageDrawable(getDrawable(R.drawable.ic_baseline_search_24));
                        fabaction.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_message_24));
                        break;
                    case 1:
                        fabaction.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_photo_camera_24));
                        break;
                    case 2:
                        fabaction.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_call_24));
                        break;
                }
                fabaction.show();
            }
        },400);
    }

}