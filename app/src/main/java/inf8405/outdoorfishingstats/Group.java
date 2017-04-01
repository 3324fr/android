package inf8405.outdoorfishingstats;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lam_1 on 4/1/2017.
 */
@IgnoreExtraProperties
public class Group {
    @Exclude
    final static String PROPERTY_M_LIST = "FishList";
    final static String PROPERTY_NAME = "name";

    @PropertyName(PROPERTY_M_LIST)
    public final HashMap<String,FishDTO> m_fishes;
    @PropertyName(PROPERTY_NAME)
    public final String m_name;

    public  Group(){
        this.m_fishes = new HashMap<>();
        this.m_name = "nothing";
    }

    @Exclude
    public void add(FishDTO fishDTO){
        this.m_fishes.put(fishDTO.name,fishDTO);
    }
}
