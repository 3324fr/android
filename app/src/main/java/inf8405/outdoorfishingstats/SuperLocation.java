package inf8405.outdoorfishingstats;

import android.location.Location;
import android.location.LocationManager;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Sly on 4/1/2017.
 */


@IgnoreExtraProperties
public class SuperLocation extends Location {
    public SuperLocation(){
        super(LocationManager.GPS_PROVIDER);
    }
    public SuperLocation(Location loc){
        super(loc);
    }
}