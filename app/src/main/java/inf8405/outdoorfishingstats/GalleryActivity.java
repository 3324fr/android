package inf8405.outdoorfishingstats;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {


    private static FirebaseDatabase m_FirebaseDatabase;
    private static FirebaseStorage m_FirebaseStorage;
    private static StorageReference m_UserPictureRef;
    private static DatabaseReference m_groupRef;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static ArrayList<PhotoDTO> m_listEntry;
    static PhotoDTO entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        m_FirebaseDatabase = FirebaseDatabase.getInstance();
        m_groupRef = m_FirebaseDatabase.getReference("FishList").child(FishActivity.PHOTO_REF);
        m_FirebaseStorage = FirebaseStorage.getInstance();
        FirebaseAuth.getInstance().signInAnonymously();
        m_UserPictureRef = m_FirebaseStorage.getReference(pref.getString(FishActivity.PREFS_KEY, getResources().getString(R.string.pref_default_display_name)));
        m_listEntry = new ArrayList<>();
        m_groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    try{
                        final PhotoDTO dto = postSnapshot.getValue(PhotoDTO.class);
                        if(dto == null){
                            Log.d("dto Null", "null dto photo");
                        }
                        entry = dto;
                        m_UserPictureRef.child(entry.pictureName).getBytes(Long.MAX_VALUE)
                                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        // Use the bytes to display the image
                                        entry.bitmap = BitmapFactory
                                                .decodeByteArray(bytes, 0, bytes.length);


                                    }
                                });
                        if(entry.bitmap != null){
                            m_listEntry.add(entry);
                            Toast.makeText(getApplicationContext(), "FOUND", Toast.LENGTH_SHORT).show();
                            //textView.setText("yo");
                        } else {
                            Toast.makeText(getApplicationContext(), "ERREUR BITMAP", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "DANS DE BOUCLE", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "FIN DE BOUCLE", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                System.out.println("The getAllUsername read failed: " + databaseError.getCode());
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //TextView textView = (TextView) findViewById(R.id.section_label);
        //textView.setText( "TEST BOFRE");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            //picture(m_listEntry.get(getArguments().getInt(ARG_SECTION_NUMBER)),rootView);
            textView.setText( m_listEntry.get(getArguments().getInt(ARG_SECTION_NUMBER)).pictureName);

            return rootView;
        }
    }
//     Bitmap m_bitmap;
//    private   void picture(FishEntry fish, View rootView) {//Display picture
//
//        m_UserPictureRef.child(fish.name).getBytes(Long.MAX_VALUE)
//                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
//                        // Use the bytes to display the image
//                        m_bitmap = BitmapFactory
//                                .decodeByteArray(bytes, 0, bytes.length);
//
//
//                    }
//                });
//        ImageView mImageView = (ImageView) rootView.findViewById(R.id.imageGallery);
//        mImageView.setImageBitmap(m_bitmap);
//
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

            return m_listEntry.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "SECTION 1";
            //return null;
        }

    }
}
