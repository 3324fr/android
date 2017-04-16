package inf8405.outdoorfishingstats;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by Sly on 4/1/2017.
 */

public class FishActivity extends AppCompatActivity implements SensorEventListener{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final static int MY_LOCATION_REQUEST_CODE = 1;
    public static final String PREFS_NAME = "pref_general";
    public static final String PREFS_KEY = "displayName";

    public static String m_displayName;

    public FishDTO m_fishDTO;
    public Location m_lastLocation;
    public Date m_currentDateTime;
    public Bitmap m_fishPicture;
    private static DatabaseReference m_groupRef;

    private static DatabaseHelper m_sqLitehelper;
    private static FirebaseDatabase m_FirebaseDatabase;
    private static FirebaseStorage m_FirebaseStorage;
    private static StorageReference m_UserPictureRef;
    private Group m_group;
    //public LocationManager m_locationManager;

    private SensorManager m_SensorManager;
    public static Sensor m_pressureEnvSensor,m_temperatureEnvSensor,m_accelerometerEnvSensor,m_gyroEnvSensor,m_proximityEnvSensor,m_compassEnvSensor;

    private static EnvSensorEntry envSensorEntry;
    private TextView temperatureTextView;
    private TextView pressureTextView;
    private TextView compassTextView;
    private TextView accelerometerTextView;
    private TextView gyroscopeTextView ;
    private TextView proximityTextView;

    private TextView fishLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish);
        //m_locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        m_displayName = getDisplayNamePreference(getApplicationContext());
        setupSensors();
        setupFirebase();
        m_sqLitehelper = new DatabaseHelper(this);
    }

    public void OnClickTakePicture(View view) {
        takePicture();
    }

    public void setupSensors(){
        m_SensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        envSensorEntry = new EnvSensorEntry();
        m_temperatureEnvSensor = m_SensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        m_pressureEnvSensor = m_SensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        m_accelerometerEnvSensor = m_SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m_gyroEnvSensor = m_SensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        m_proximityEnvSensor = m_SensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        m_compassEnvSensor = m_SensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        temperatureTextView = (TextView) findViewById(R.id.temperature);
        pressureTextView = (TextView) findViewById(R.id.pressure);
        compassTextView = (TextView) findViewById(R.id.compass);
        accelerometerTextView = (TextView) findViewById(R.id.accelerometer);
        gyroscopeTextView = (TextView) findViewById(R.id.gyroscope);
        proximityTextView = (TextView) findViewById(R.id.proximity);

        fishLocation = (TextView) findViewById(R.id.fish_location);

        temperatureTextView.setText("Temperature: " + envSensorEntry.temperature);
        pressureTextView.setText("Pressure: " + envSensorEntry.pressure);
        accelerometerTextView.setText("Accelerometer: " + envSensorEntry.accelerometer);
        gyroscopeTextView.setText("Gyroscope: " + envSensorEntry.gyroscope);
        proximityTextView.setText("Proximity: " + envSensorEntry.proximity);
        compassTextView.setText("Compass: " + envSensorEntry.compass);

        fishLocation.setText("Lon: _"+ ", Lat: _");
    }

    public void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            m_fishPicture = (Bitmap) extras.get("data");

            m_currentDateTime  = new Date(System.currentTimeMillis());

            final TextView fishTime = (TextView) findViewById(R.id.fish_time);
            fishTime.setText(m_currentDateTime.toString());


            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Acquire a reference to the system Location Manager
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                m_lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                fishLocation.setText("Lon: "+m_lastLocation.getLongitude() + ", Lat: " + m_lastLocation.getLatitude());
            }

            //SensorData sensorData = new SensorData(m_SensorManager ,this);
            picture(m_fishPicture);
        }
    }//onActivityResult

    private void picture(Bitmap picture) {//Display picture
        ImageView mImageView = (ImageView) findViewById(R.id.fish_picture);
        mImageView.setImageBitmap(picture);
    }

    public void addFish(View view){
        EditText editText_name = ((EditText) findViewById(R.id.fish_name));
        String nameText = editText_name.getText().toString();

        Spinner spinner = ((Spinner) findViewById(R.id.fish_category));
        String spinnerName = String.valueOf(spinner.getSelectedItem());
        if(nameText.isEmpty()) {
            Toast.makeText(this, getString(R.string.name_missing), Toast.LENGTH_SHORT).show();
            return;
        }

        if(m_fishPicture == null){
            Toast.makeText(this, getString(R.string.picture_missing), Toast.LENGTH_SHORT).show();
        }

        else{
            FishDTO fishDTO = new FishDTO();
            fishDTO.name = nameText;
            fishDTO.fish = spinnerName.toString();

            EditText editText_contact = ((EditText) findViewById(R.id.fish_name));
            String contactText = editText_contact.getText().toString();

            if(!contactText.isEmpty() && !contactText.matches("^-?\\d+$")) {
                fishDTO.contact = contactText;
            }
            m_currentDateTime = m_currentDateTime == null ?  new Date(System.currentTimeMillis()) : m_currentDateTime;
            fishDTO.time = m_currentDateTime.toString();

            fishDTO.longitude = m_lastLocation == null ?  0 : m_lastLocation.getLongitude();
            fishDTO.latitude = m_lastLocation  == null ?  0 : m_lastLocation.getLatitude();

            //Add fish to firebase & Picture with m_displayName
            Log.d("DBUpdate", "FISH_NEWFISH");
            m_groupRef.child("FishList").child(fishDTO.name).setValue(fishDTO);
            m_UserPictureRef.child(fishDTO.name).putBytes(convertBitmap2Array(m_fishPicture));
            //todo SQLITE VERIFY
            SQLiteDatabase db_write = m_sqLitehelper.getWritableDatabase();
            saveToSQL(db_write, fishDTO);

            Intent i = new Intent(FishActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    private byte[] convertBitmap2Array(Bitmap img){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private String getDisplayNamePreference(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(PREFS_KEY, getResources().getString(R.string.pref_default_display_name));
    }


    private void setupFirebase(){
        m_FirebaseStorage = FirebaseStorage.getInstance();
        FirebaseAuth.getInstance().signInAnonymously();
        m_FirebaseDatabase = FirebaseDatabase.getInstance();
        m_groupRef = m_FirebaseDatabase.getReference("FishList");
        m_UserPictureRef = m_FirebaseStorage.getReference(m_displayName);
        m_group = new Group();
        setupListenerDTO();
    }

    private void setupListenerDTO() {
        m_groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // todo try catch

                Group group = null;
                try{group = dataSnapshot.getValue(Group.class);}
                catch (Exception e) {//todo
                    e.printStackTrace();
                }
                if(group == null){
                    Log.d("DBDelete", "FISH_SETUP");
                    m_groupRef.setValue(m_group);
                } else {
                    m_group = group;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Exclude
    public void saveToSQL(SQLiteDatabase db , FishDTO fish) {
        // Create insert entries
        ContentValues values = new ContentValues();
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_NAME, fish.name);

        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_LAT, fish.latitude);
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_LNG, fish.longitude);
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_TIME, fish.time);
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_COMMENT, fish.contact);

        // TODO GET TEMPERATURE AND PRESSURE AND MAG FIELD
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_TEMPERATURE, 0);
        // TODO GET TEMPERATURE AND PRESSURE AND MAG FIELD
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_PRESSURE, 0);
        // TODO GET TEMPERATURE AND PRESSURE AND MAG FIELD
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_MAG, 0);

        //values.put(SQLiteContract.FishingEntry.COLUMN_NAME_PICTURE, bytesPicture);
        db.insert(SQLiteContract.FishingEntry.TABLE_NAME, null, values);
    }

    //SOURCE: http://android-er.blogspot.ca/2016/04/requesting-permissions-of.html
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(FishActivity.this,
                            R.string.permgranted,
                            Toast.LENGTH_LONG).show();
                    getAndSetUserLocationWithPermission();

                } else {
                    Toast.makeText(FishActivity.this,
                            R.string.permdenied,
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    //SOURCE http://developer.android.com/training/permissions/requesting.html
    private void getAndSetUserLocationWithPermission(){
        try{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //------------------------------------------------------------------------------
                ActivityCompat.requestPermissions(FishActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);

                return;
            }
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float val = event.values[0];
        int type=event.sensor.getType();
        String typeOutput = "";
        switch(type){
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                envSensorEntry.temperature = val;
                typeOutput = Sensor.STRING_TYPE_AMBIENT_TEMPERATURE;
                if(temperatureTextView != null)
                    temperatureTextView.setText("Temperature: " + envSensorEntry.temperature);
                break;
            case Sensor.TYPE_PRESSURE:
                envSensorEntry.pressure = val;
                typeOutput = Sensor.STRING_TYPE_PRESSURE;
                if(pressureTextView != null)
                    pressureTextView.setText("Pressure: " + envSensorEntry.pressure);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                envSensorEntry.accelerometer = val;
                typeOutput = Sensor.STRING_TYPE_ACCELEROMETER;
                if(accelerometerTextView != null)
                    accelerometerTextView.setText("Accelerometer: " + envSensorEntry.accelerometer);
                break;
            case Sensor.TYPE_GYROSCOPE:
                envSensorEntry.gyroscope = val;
                typeOutput = Sensor.STRING_TYPE_GYROSCOPE;
                if(gyroscopeTextView != null)
                    gyroscopeTextView.setText("Gyroscope: " +  envSensorEntry.gyroscope);
                break;
            case Sensor.TYPE_PROXIMITY:
                envSensorEntry.proximity = val;
                typeOutput = Sensor.STRING_TYPE_PROXIMITY;
                if(proximityTextView != null)
                    proximityTextView.setText("Proximity: " +  envSensorEntry.proximity);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                envSensorEntry.compass = val;
                typeOutput = Sensor.STRING_TYPE_MAGNETIC_FIELD;
                if(compassTextView != null)
                    compassTextView.setText("Compass: " + envSensorEntry.compass);
                break;
            default: break;
        }
        Log.d("SENSOR", typeOutput+": "+val);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();
        registerListenerSensors();
    }

    public void  registerListenerSensors(){
        if(m_temperatureEnvSensor!=null)
            m_SensorManager.registerListener(this, m_temperatureEnvSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(m_pressureEnvSensor!=null)
            m_SensorManager.registerListener(this, m_pressureEnvSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(m_accelerometerEnvSensor!=null)
            m_SensorManager.registerListener(this, m_accelerometerEnvSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(m_gyroEnvSensor!=null)
            m_SensorManager.registerListener(this, m_gyroEnvSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(m_proximityEnvSensor!=null)
            m_SensorManager.registerListener(this, m_proximityEnvSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(m_compassEnvSensor!=null)
            m_SensorManager.registerListener(this, m_compassEnvSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }



}
