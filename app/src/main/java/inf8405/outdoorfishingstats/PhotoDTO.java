package inf8405.outdoorfishingstats;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Sly on 4/1/2017.
 */



@IgnoreExtraProperties
public class PhotoDTO {

    @Exclude
    final static String PROPERTY_FILE = "file";
    @Exclude
    final static String PROPERTY_PICTURE = "pictureName";

    @PropertyName(PROPERTY_FILE)
    public String file;
    @PropertyName(PROPERTY_PICTURE)
    public String pictureName;

    @Exclude
    Bitmap bitmap;


    public PhotoDTO(){
        this.file = "Default";
        this.pictureName = "Default";

    }
}
