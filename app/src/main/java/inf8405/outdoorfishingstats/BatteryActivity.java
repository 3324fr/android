package inf8405.outdoorfishingstats;

/**
 * Created by lam_1 on 4/15/2017.
 */

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BatteryActivity extends AppCompatActivity {

    private String levelUsed;
    public static final String BATTERY_LEVEL = "inf8405.outdoorfishingstats.batteryLevel";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        //levelUsed = getIntent().getStringExtra("percent");


        //setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Calculate the battery percent level if it not in intent extra
        int percent = getIntent().getIntExtra(BatteryReceiver.BATTERY_LEVEL,0);
        Log.d("test", percent+"");
        if(percent == 0){
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            Log.d("test1", level+"");
            percent = (level*100 / scale);
            Log.d("test2", scale+"");
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        Bundle extras = getIntent().getExtras();
        Float atStartLevel = (Float)(extras.get(BATTERY_LEVEL));
        Log.d("test", percent+"");
        Float difference = atStartLevel - percent;
        myToolbar.setTitle("Utilisation depuis lancement : " + difference);
        TextView tv = (TextView) findViewById(R.id.level);
        //Log.d("test3", difference + " " + atStartLevel + " " + percent);
        tv.setText(Integer.toString(percent) + getString(R.string.percent) + " Au lancement : " +  (atStartLevel).intValue() + getString(R.string.percent));
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setProgress(percent);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        Bundle extras = data.getExtras();
        levelUsed = (String)extras.get(BATTERY_LEVEL);
        int percent = getIntent().getIntExtra(BatteryReceiver.BATTERY_LEVEL,0);
        Log.d("test", percent+"");
        if(percent == 0){
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            Log.d("test1", level+"");
            percent = (level*100 / scale);
            Log.d("test2", scale+"");
        }
        Float difference = Float.valueOf(levelUsed) - percent;
        myToolbar.setTitle("Utilisation depuis lancement" + difference);
        TextView tv = (TextView) findViewById(R.id.level);
        Log.d("test3", difference + " " + levelUsed + " " + percent);
        tv.setText(Integer.toString(percent) + getString(R.string.percent));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view){ // action to pass in battery saver mode
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.location_updateInterval_key),"60");
        editor.commit();
        finish();
    }
}
