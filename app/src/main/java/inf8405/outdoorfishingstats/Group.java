package inf8405.outdoorfishingstats;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lam_1 on 4/1/2017.
 */

public class Group {
    final static String PROPERTY_M_LIST = "m_List";
    final static String PROPERTY_MEETING = "meeting";

    private final ArrayList<Object> m_fishes;
    private final String m_name;

    public  Group(){
        this.m_fishes = new ArrayList<>();
        this.m_name = "nothing";
    }
}
