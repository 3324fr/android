package inf8405.outdoorfishingstats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private Singleton ourInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ourInstance = Singleton.getInstance(this);
    }

    private void updateStats(){
        Stats test;
        ArrayList<FishEntry> list = new ArrayList<>(ourInstance.getFish());
    }

    class Stats
    {
        public String FirstName;
        public String LastName;
        public int    BirthYear;
    };
}
