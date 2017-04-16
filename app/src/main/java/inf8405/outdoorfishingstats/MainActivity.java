package inf8405.outdoorfishingstats;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final String BATTERY_LEVEL = "inf8405.outdoorfishingstats.batteryLevel";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int level;
    private float percent;
    private int scale;
    public static ArrayList<String> listeAmis = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
        level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        percent = (level*100 / scale);
    }

    public void OnClickSetting(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void OnClickMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void OnClickGallery(View view){
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    public void OnClickFish(View view) {
        Intent intent = new Intent(this, FishActivity.class);
        startActivity(intent);
    }

    public void OnClickStats(View v){
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    public void OnClickBattery(View v){
        Intent intentBattery = new Intent(this, BatteryActivity.class);
        intentBattery.putExtra(BATTERY_LEVEL,percent);
        startActivity(intentBattery);
    }

    public void OnClickBeam(View v){
        Intent intent = new Intent(this, BeamActivity.class);
        startActivity(intent);
    }

}
