
package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.whatsappclone.contact.ContactActivity;
import com.example.whatsappclone.fragments.CallFragment;
import com.example.whatsappclone.fragments.CameraFragment;
import com.example.whatsappclone.fragments.ChatFragment;
import com.example.whatsappclone.fragments.StatusFragment;
import com.example.whatsappclone.settings.SettingsActivity;
import com.example.whatsappclone.status.AddStatusPicActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TabLayout tablayout;
    ViewPager viewPager;
    Toolbar toolbar;
    FloatingActionButton fabaction;
    ImageButton btnaddstatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabaction = findViewById(R.id.fab_action);
        tablayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);
        btnaddstatus = findViewById(R.id.btn_add_status);


        setSupportActionBar(toolbar);
        setUpWithViewPager(viewPager);
        tablayout.setupWithViewPager(viewPager);

        View tab1 = LayoutInflater.from(this).inflate(R.layout.custom_camera_tab, null);
        try {
            tablayout.getTabAt(0).setCustomView(tab1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewPager.setCurrentItem(1);

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

    private void setUpWithViewPager(ViewPager viewPager) {

        MainActivity.SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CameraFragment(), "CAMERA");
        adapter.addFragment(new ChatFragment(), "CHATS");
        adapter.addFragment(new StatusFragment(), "STATUS");
        adapter.addFragment(new CallFragment(), "CALLS");
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

        public void addFragment(Fragment fragment, String title) {

            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }

        public CharSequence getPageTitle(int position) {

            return fragmentTitle.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_search:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ac_menu_group:
                Toast.makeText(this, "group", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ac_broadcast:
                Toast.makeText(this, "broadcast", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ac_whatsappweb:
                Toast.makeText(this, "web whatsapp", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ac_strmessage:
                Toast.makeText(this, "stared message", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ac_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void ChangeFabIcon(final int index) {

        fabaction.hide();
        btnaddstatus.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                switch (index) {

                    case 0:
                        fabaction.hide();
                        break;

                    case 1:

                        fabaction.show();

                         fabaction.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_message_24));
//
//                        fabaction.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                startActivity(new Intent(MainActivity.this, ContactActivity.class));
//                            }
//                        });
                        break;
                    case 2:
                        btnaddstatus.setVisibility(View.VISIBLE);
                        fabaction.show();
                        fabaction.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_photo_camera_24));
                        break;
                    case 3:
                        fabaction.show();
                        fabaction.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_call_24));
                        break;
                }

            }
        }, 400);

        performOnClick(index);
    }

    private void performOnClick(final int index) {

        fabaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (index == 0) {
                    checkCameraPermission();
                } else if (index == 1) {
                    startActivity(new Intent(MainActivity.this, ContactActivity.class));
                } else if (index == 2) {
                    checkCameraPermission();
                }
                else {
                    Toast.makeText(MainActivity.this, "Call.. ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnaddstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Add Status.. ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    231);


        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    232);


        } else {
            opencamera();
        }
    }

    public static Uri imageUri = null;

    private void opencamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_ " + timeStamp + ".jpg";

        try {
            File file = File.createTempFile("IMG_" + timeStamp, "jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("listPhotoName", imageFileName);
            startActivityForResult(intent, 440);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 440
                && resultCode == RESULT_OK) {

            if (imageUri != null) {

                startActivity(new Intent(MainActivity.this, AddStatusPicActivity.class)
                .putExtra("image",imageUri));
            }
        }
    }

    private String GetFileExtension(Uri uri) {
        ContentResolver content = getContentResolver();
        MimeTypeMap mimeType = MimeTypeMap.getSingleton();
        return mimeType.getExtensionFromMimeType(content.getType(uri));
    }
}




