package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.ExecutionException;


public  class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private static final String TAG = "MAINACT";


    private String CUR_USERNAME;
    private String CUR_SERVICETAG;
    UserData currentUser;
    public static Context mContext;

    ISetTextInFragment setProfile;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    int count = 0;
    int selected = 0;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public void updateCurrentUser() throws ExecutionException, InterruptedException {
        Object params[] = new Object[3];
        params[0] = "getUserInfo";
        params[1] = CUR_USERNAME;
        params[2] = this;
        currentUser = new RetrieveMotto().execute(params).get();


    }
    public void addUser() throws ExecutionException, InterruptedException {
        Object params[] = new Object[3];
        params[0] = "addUser";
        params[1] = CUR_USERNAME;
        params[2] = this;
        currentUser = new RetrieveMotto().execute(params).get();
    }



    public UserData getCurrentUser() {
        return currentUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String themeName = pref.getString("themeSettings", "Purdue Black and Gold");
        Log.d("FUCKINGBULLSHIT", "themeName: " + themeName);

        if (themeName.equals("Default")) {
            this.setTheme(R.style.BlackTheme);
            Log.d("FUCKINGBULLSHIT", "themeName: " + themeName);

        } else if (themeName.equals("Purdue Black and Gold")) {
            this.setTheme(R.style.AppTheme);
            Log.d("FUCKINGBULLSHIT", "themeName: " + themeName);

        }

        mContext = this;

        Bundle extras = getIntent().getExtras();



        if (extras != null) {
            CUR_USERNAME = extras.getString("Username");
            CUR_SERVICETAG = extras.getString("ServiceTag");
        }
        try {
            addUser();
            updateCurrentUser();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), CUR_USERNAME);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }


    @Override
    public void onBackPressed() {
        if(count == 0){
            LayoutInflater inflater = getLayoutInflater();

            View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.custom_toast_layout));
            TextView text = (TextView) layout.findViewById(R.id.textToShow);
            text.setText("Press back again to logout");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
        count++;
        if(count == 2){
            finish();
            Intent auth = new Intent(MainActivity.this, CASClient.class);
            startActivity(auth);
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                count = 0;
            }
        }, 2000);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selected = position;
        try {
            updateCurrentUser();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                Fragment profile = ProfileFrag.newInstance(position+1);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, profile).addToBackStack(null)
                        .commit();

                setProfile = (ISetTextInFragment) profile;
                if (setProfile == null) {
                    Log.d(TAG, "Null Profile~~~~");
                }else {
                    Log.d(TAG, "getBio(): " + currentUser.getBio());
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            setProfile.enableProgess();
                            setProfile.showBioText(currentUser.getBio());
                            setProfile.showNameText(currentUser.getUsername());
                            setProfile.showInterestText(currentUser.getInterests());
                            setProfile.showCourseText(currentUser.getCourses());
                        }
                    }, 2000);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            setProfile.disableProgess();
                        }
                    }, 3000);
                }
                break;
            case 1:
                Intent classes = new Intent(MainActivity.this, Courses.class);
                classes.putExtra("Username", CUR_USERNAME);
                startActivity(classes);
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Chat.newInstance(position + 1, CUR_USERNAME))
                        .commit();
                break;
            case 3:
                Intent settings = new Intent(MainActivity.this, Settings.class);
                settings.putExtra("username", CUR_USERNAME);
                startActivity(settings);
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, About.newInstance(position + 1))
                        .commit();
                break;
            case 5:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Feedback.newInstance(position + 1))
                        .commit();
                break;
            case 6:
                finish();
                Intent auth = new Intent(MainActivity.this, CASClient.class);
                startActivity(auth);
                break;
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                break;

        }

    }
    public void updateBio(View v) {
        EditText edit = (EditText) findViewById(R.id.profileBio);
        String updatedBio = edit.getText().toString();
        edit.setText(updatedBio);
        Object params[] = new Object[3];
        params[0] = "editUser";
        params[1] = CUR_USERNAME;
        params[2] = updatedBio;

        try {
            final Boolean bioUpdated = new UpdateBioMotto().execute(params).get();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(bioUpdated) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView text = (TextView) layout.findViewById(R.id.textToShow);
                        text.setText("Updated your bio!");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                    }
                }
            }, 1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void onInterestsClick(View view) {
        Intent interests = new Intent(MainActivity.this, Interests.class);
        interests.putExtra("Username", CUR_USERNAME);
        startActivity(interests);
    }


    public void switchToEditPicture(View view) {
        Fragment editFrag = new EditPictureFrag();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.container, EditPictureFrag.newInstance(0)).commit();
    }

    public void switchToEditProfile(View view) {
        Fragment editFrag = new EditProfileFrag();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.container, EditProfileFrag.newInstance(0)).commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String themeName = pref.getString("themeSettings", "Purdue Black and Gold");
        if (themeName.equals("Default")) {
            Log.d("FUCKINGBULLSHIT", "themeName: " + themeName);

            this.setTheme(R.style.BlackTheme);
        } else if (themeName.equals("Purdue Black and Gold")) {
            Log.d("FUCKINGBULLSHIT", "themeName: " + themeName);
            this.setTheme(R.style.AppTheme);
        }
        if(selected == 1 || selected == 3 || selected == 6){
            onNavigationDrawerItemSelected(0);
        }else {
            onNavigationDrawerItemSelected(selected);
        }

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
            View rootView;


            rootView = inflater.inflate(R.layout.fragment_main, container, false);


            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
