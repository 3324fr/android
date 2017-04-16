package inf8405.outdoorfishingstats;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.nio.charset.Charset;

public class BeamActivity  extends AppCompatActivity  implements CreateNdefMessageCallback{

    NfcAdapter mNfcAdapter;
    TextView textView;
    public static final String PREFS_KEY = "displayName";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam);
        TextView textView = (TextView) findViewById(R.id.textnfc);
        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback
        mNfcAdapter.setNdefPushMessageCallback(this, this);
        textView = (TextView) findViewById(R.id.textnfc);
        textView.setText(" TV before");
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        //String text = ("Beam me up, Android!\n\n" + "Beam Time: " + System.currentTimeMillis());

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String text = (pref.getString(FishActivity.PREFS_KEY, getResources().getString(R.string.pref_default_display_name)));
        //Toast.makeText(this,"Username " + getDisplayNamePreference(getApplicationContext()) , Toast.LENGTH_SHORT).show();
        String userName = FishActivity.m_displayName == null ? getDisplayNamePreference(getApplicationContext()) : FishActivity.m_displayName;
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { NdefRecord.createMime(
                        userName, text.getBytes())
                        /**
                         * The Android Application Record (AAR) is commented out. When a device
                         * receives a push with an AAR in it, the application specified in the AAR
                         * is guaranteed to run. The AAR overrides the tag dispatch system.
                         * You can add it back in to guarantee that this
                         * activity starts when receiving a beamed message. For now, this code
                         * uses the tag dispatch system.
                        */
                        //,NdefRecord.createApplicationRecord("com.example.android.beam")
                });
        return msg;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        } else {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {

        textView = (TextView) findViewById(R.id.textnfc);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        String msgBuilder = "";
        if(rawMsgs != null){
            NdefMessage[] messages = new NdefMessage[rawMsgs.length];

            for (int i = 0; i < rawMsgs.length; i++) {
                messages[i] = (NdefMessage) rawMsgs[i];
                msgBuilder+= new String(messages[i].getRecords()[0].getPayload());

            }
        }
        else {
            msgBuilder = "erreur";
        }
        // record 0 contains the MIME type, record 1 is the AAR, if present
        MainActivity.listeAmis.add(msgBuilder);

        textView.setText(msgBuilder);
    }


    private String getDisplayNamePreference(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(FishActivity.PREFS_KEY, getResources().getString(R.string.pref_default_display_name));
    }
}