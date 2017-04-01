package inf8405.outdoorfishingstats;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

/**
 * Created by Sly on 4/1/2017.
 */

public class FishActivity extends AppCompatActivity{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public FishDTO m_fishDTO;
    public Location m_lastLocation;
    //public LocationManager m_locationManager;

    //private SensorManager m_SensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish);

        //m_locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //m_SensorManager =  (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    public void addFish(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //todo do something with imageBitmap (FB + SQLite)

            m_fishDTO = new FishDTO();

            Date currentDate  = new Date(System.currentTimeMillis());

            final TextView fishTime = (TextView) findViewById(R.id.fish_time);
            fishTime.setText(currentDate.toString());

            final TextView fishLocation = (TextView) findViewById(R.id.fish_location);

//            String locationProvider = LocationManager.GPS_PROVIDER;
//
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                // Acquire a reference to the system Location Manager
//                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//                // todo check GPS provider
//                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//            }
            //SensorData sensorData = new SensorData(m_SensorManager ,this);

            picture(imageBitmap);
        }
    }//onActivityResult

    private void picture(Bitmap picture) {//Display picture
        ImageView mImageView = (ImageView) findViewById(R.id.fish_picture);
        mImageView.setImageBitmap(picture);
    }




}
