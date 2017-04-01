package inf8405.outdoorfishingstats;

import android.hardware.Sensor;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

/**
 * Created by Sly on 4/1/2017.
 */



@IgnoreExtraProperties
public class FishDTO {

    @Exclude
    final static String PROPERTY_LONGITUDE = "longitude";
    final static String PROPERTY_LATITUDE = "latitude";
    final static String PROPERTY_NAME = "name";
    final static String PROPERTY_CONTACT = "contact";
    final static String PROPERTY_TIME = "time";

    @PropertyName(PROPERTY_NAME)
    public String name;
    @PropertyName(PROPERTY_CONTACT)
    public String contact;
    @PropertyName(PROPERTY_TIME)
    public String time;
    @PropertyName(PROPERTY_LONGITUDE)
    public double longitude;
    @PropertyName(PROPERTY_LATITUDE)
    public double latitude;

    public FishDTO(){
        this.name = "test";
    }
}
