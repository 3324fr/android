package inf8405.outdoorfishingstats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        startActivity(intentBattery);
    }
}
