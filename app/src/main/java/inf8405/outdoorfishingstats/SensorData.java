package inf8405.outdoorfishingstats;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

/**
 * Created by Sly on 4/1/2017.
 */

public class SensorData implements SensorEventListener {
    private SensorManager m_SensorManager;
    private Sensor m_pressureSensor;
    private Sensor m_tempSensor;
    private final FishActivity ma;

    public SensorData(SensorManager sm, FishActivity ma){
        m_SensorManager = sm;
        m_pressureSensor = m_SensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        m_tempSensor = m_SensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        this.ma = ma;
    }

    protected void onResume() {
        m_SensorManager.registerListener(this, m_pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        m_SensorManager.registerListener(this, m_tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        m_SensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        // Here we call a method in MainActivity and pass it the values from the SensorChanged event
        //ma.setTextViewValue( event.values );
    }
}
