package com.icaynia.soundki.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.icaynia.soundki.Fragment.HomeFragment;
import com.icaynia.soundki.Fragment.MyMusicListFragment;
import com.icaynia.soundki.Fragment.ProfileMenuFragment;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.MusicRemoteController;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
{
    private Global global;
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
    public  Snackbar playRemoteController;

    private MusicRemoteController musicRemoteController;

    private boolean snackbarState = false;

    private OnBackPressedListener backPressedListener = null;

    private Snackbar.SnackbarLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        musicRemoteController = new MusicRemoteController(this);
        musicRemoteController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onPlayerActivity();
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (snackbarState)
                    hideSnackbarController();
                else
                    onSnackbarController();
            }
        });

        makeSnackbarController(fab);

        global = (Global) getApplication();
        global.mainActivityMusicRemoteController = musicRemoteController;
        global.updateController();


    }

    public void onPlayerActivity()
    {
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }

    public void makeSnackbarController(final View view)
    {
        playRemoteController = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
        layout = (Snackbar.SnackbarLayout) playRemoteController.getView();
        layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        View snackView = musicRemoteController;
        layout.setPadding(0, 0, 0, 0);
        layout.addView(snackView, Snackbar.SnackbarLayout.LayoutParams.MATCH_PARENT, 160);
        playRemoteController.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event)
            {
                super.onDismissed(snackbar, event);
                if (event == DISMISS_EVENT_SWIPE)
                {
                    ((Snackbar.SnackbarLayout) playRemoteController.getView()).removeAllViews();
                    makeSnackbarController(view);
                }
                hideSnackbarController();
            }
        });
    }

    public MusicRemoteController getMusicRemoteController()
    {
        return musicRemoteController;
    }

    public void onSnackbarController() {
        playRemoteController.show();
        snackbarState = true;
    }

    public void hideSnackbarController() {
        playRemoteController.dismiss();
        snackbarState = false;
    }

    public void setOnBackPressedListener(OnBackPressedListener listener)
    {
        backPressedListener = listener;
    }

    public void setActionBarPositiveButton(String str, View.OnClickListener listener)
    {
        TextView textView = (TextView) findViewById(R.id.action_positive);
        textView.setText(str);
        textView.setOnClickListener(listener);
        textView.setVisibility(View.VISIBLE);
    }

    public void setActionBarNegativeButton(String str, View.OnClickListener listener)
    {
        TextView textView = (TextView) findViewById(R.id.action_negative);
        textView.setText(str);
        textView.setOnClickListener(listener);
        textView.setVisibility(View.VISIBLE);
    }

    public void hideActionBarButton()
    {
        TextView positive = (TextView) findViewById(R.id.action_positive);
        positive.setVisibility(View.GONE);
        TextView negative = (TextView) findViewById(R.id.action_negative);
        negative.setVisibility(View.GONE);
    }

    public void openActionBarButton()
    {
        TextView positive = (TextView) findViewById(R.id.action_positive);
        positive.setVisibility(View.VISIBLE);
        TextView negative = (TextView) findViewById(R.id.action_negative);
        negative.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed()
    {
        if (backPressedListener != null)
        {
            backPressedListener.onBackPressed();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_nowplaying)
        {
            onPlayerActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new MyMusicListFragment();
                case 2:
                    return new ProfileMenuFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return "홈";
                case 1:
                    return "내 음악";
                case 2:
                    return "프로필";
            }
            return null;
        }
    }

    public interface OnBackPressedListener
    {
        void onBackPressed();
    }
}
