package estonia.dev.hendrik.soundboard_eng;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.content.SharedPreferences;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import estonia.dev.hendrik.Soundboard_ENG.R;
import estonia.dev.hendrik.soundboard_eng.EventHandlerClass;
import estonia.dev.hendrik.soundboard_eng.SoundObject;
import estonia.dev.hendrik.soundboard_eng.SoundboardRecyclerAdapter;

public class SoundboardActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SoundboardActivity";
    AdView mAdview;

    Toolbar toolbar;

    ArrayList<SoundObject> soundList = new ArrayList<>();

    RecyclerView SoundView;
    SoundboardRecyclerAdapter SoundAdapter = new SoundboardRecyclerAdapter(soundList);
    RecyclerView.LayoutManager SoundLayoutManager;


    private View mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundboard);

        if (appUpdate())
            Log.d(LOG_TAG, "App update available!");

        MobileAds.initialize(this, "ca-app-pub-5698320091626063~9547043538");

        mAdview = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        mLayout = findViewById(R.id.activity_soundboard);

        toolbar = (Toolbar) findViewById(R.id.soundboard_toolbar);

        setSupportActionBar(toolbar);

        List<String> nameList = Arrays.asList(getResources().getStringArray(R.array.soundNames));

        SoundObject[] soundItems = {new SoundObject(nameList.get(0), R.raw.audio01),
                new SoundObject(nameList.get(1), R.raw.audio02),
                new SoundObject(nameList.get(2), R.raw.audio03),
                new SoundObject(nameList.get(3), R.raw.audio04),
                new SoundObject(nameList.get(4), R.raw.audio05),
                new SoundObject(nameList.get(5), R.raw.audio06),
                new SoundObject(nameList.get(6), R.raw.audio07),
                new SoundObject(nameList.get(7), R.raw.audio08),
                new SoundObject(nameList.get(8), R.raw.audio09),
                new SoundObject(nameList.get(9), R.raw.audio10),
                new SoundObject(nameList.get(10), R.raw.audio11),
                new SoundObject(nameList.get(11), R.raw.audio12)};

        soundList.addAll(Arrays.asList(soundItems));

        SoundView = (RecyclerView) findViewById(R.id.soundboardRecyclerView);

        SoundLayoutManager = new GridLayoutManager(this, 3);

        SoundView.setLayoutManager(SoundLayoutManager);

        SoundView.setAdapter(SoundAdapter);

        requestPermissions();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventHandlerClass.releaseMediaPlayer();
    }

    private void requestPermissions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }

            if (!Settings.System.canWrite(this)){


            }
        }
    }

    private boolean appUpdate(){



        final String PREFS_NAME = "VersionPref";
        final String PREF_VERSION_CODE_KEY = "version_code";

        final int DOESNT_EXIST = -1;


        int currentVersionCode = 0;
        try{

            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

        } catch (PackageManager.NameNotFoundException e){

            Log.e(LOG_TAG, e.getMessage());
        }


        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);


        SharedPreferences.Editor edit = prefs.edit();


        if (savedVersionCode == DOESNT_EXIST){


            edit.putInt(PREF_VERSION_CODE_KEY, currentVersionCode);
            edit.commit();
            return true;
        }
        else if (currentVersionCode > savedVersionCode){


            edit.putInt(PREF_VERSION_CODE_KEY, currentVersionCode);
            edit.commit();
            return true;
        }

        return false;
    }

}
