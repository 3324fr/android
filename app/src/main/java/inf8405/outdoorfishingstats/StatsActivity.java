package inf8405.outdoorfishingstats;

import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class StatsActivity extends AppCompatActivity {

    private Singleton ourInstance;
    private HashMap<String, Stats> m_HashStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ourInstance = Singleton.getInstance(this);
        updateStats();
    }

    private void updateStats(){
        m_HashStats = new HashMap<>();
        try{

            for(FishEntry fish: ourInstance.getFish()){
                if(!m_HashStats.containsKey(fish.fishType)){
                    Stats stats = m_HashStats.get(fish.fishType);
                    stats.m_nbElement += 1;
                    stats.m_temperature =  (stats.m_temperature + fish.temperature)/stats.m_nbElement;
                    stats.m_fieldStrength = (stats.m_fieldStrength + fish.fieldStrength)/stats.m_nbElement;
                    stats.m_latitude = (stats.m_latitude + fish.latitude)/stats.m_nbElement;
                    stats.m_longitude = (stats.m_longitude + fish.longitude)/stats.m_nbElement;
                    stats.m_pressure = (stats.m_pressure + fish.pressure)/stats.m_nbElement;
                    m_HashStats.put(fish.fishType, stats);
                } else {
                    Stats stats = new Stats(fish.temperature, fish.fieldStrength, fish.latitude, fish.longitude, fish.pressure, 1);
                    m_HashStats.put(fish.fishType, stats);
                }
            }
            updateTextViewInfos();
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    private void updateTextViewInfos() {
        Spinner spinner = (Spinner)findViewById(R.id.fish_category);
        TextView tvTemp = (TextView)findViewById(R.id.stats_temperature);
        TextView tvPress = (TextView)findViewById(R.id.stats_pressure);
        TextView tvLat = (TextView)findViewById(R.id.stats_lat);
        TextView tvLong = (TextView)findViewById(R.id.stats_long);
        TextView tvField = (TextView)findViewById(R.id.stats_field);
        String name = spinner.getSelectedItem().toString();
        Stats stats = m_HashStats.get(name);
        tvTemp.setText(stats.m_temperature);
        tvPress.setText(stats.m_pressure);
        tvLat.setText(String.valueOf(stats.m_latitude));
        tvLong.setText(String.valueOf(stats.m_longitude));
        tvField.setText(String.valueOf(stats.m_fieldStrength));
    }

    class Stats
    {
        public String m_fishType;
        public int m_temperature;
        public float m_fieldStrength;
        public String m_comment;
        public float m_latitude;
        public float m_longitude;
        public int m_pressure;
        public int m_nbElement;

        Stats(){
            m_fishType = null;
            m_temperature = 0;
            m_fieldStrength = 0;
            m_comment = null;
            m_latitude = 0;
            m_longitude = 0;
            m_pressure = 0;
            m_nbElement = 0;
        }
        Stats(int temp, float field, float lat, float longi, int press, int size){
            m_temperature = temp;
            m_fieldStrength = field;
            m_latitude = lat;
            m_longitude = longi;
            m_pressure = press;
            m_nbElement = size;
        }
    }
}
