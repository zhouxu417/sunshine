package com.zhouxu417.xu.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import static android.app.PendingIntent.getActivity;

public class Sunshine_MainActivity extends AppCompatActivity {

    private final String LOG_TAG = Sunshine_MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DF_TAG";
    private String mLocation;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);
        setContentView(R.layout.activity_sunshine__main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.weather_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();
            updateWeather();


            }
        });
    }
    //更新天气数据
    private void updateWeather() {

        FetchWeatherTask weatherTask = new FetchWeatherTask(getApplication());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String location = prefs.getString(getString(R.string.pref_key_location),getString(R.string.pref_default_location));
        weatherTask.execute(location);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_map){
            openPreferredLocationInMap();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        updateWeather();
    }

    public void openPreferredLocationInMap(){
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String location = sharedPreferences.getString();
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q","210046").build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if(intent.resolveActivity(getPackageManager()) != null ){
            startActivity(intent);
        } else {
            Log.d(LOG_TAG,"Couldn't call " + "210046" );

        }
    }

}
