package com.icaynia.pracler.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.icaynia.pracler.Data.PlayListManager;
import com.icaynia.pracler.Data.UserManager;
import com.icaynia.pracler.Fragment.HomeFragment;
import com.icaynia.pracler.Fragment.ProfileMenuFragment;
import com.icaynia.pracler.Fragment.RootFragmentPos1;
import com.icaynia.pracler.Global;
import com.icaynia.pracler.models.MusicDto;
import com.icaynia.pracler.models.PlayList;
import com.icaynia.pracler.models.User;
import com.icaynia.pracler.R;
import com.icaynia.pracler.View.InputPopup;
import com.icaynia.pracler.View.MusicController;

public class MainActivity extends AppCompatActivity
{
    private Global global;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public Snackbar playRemoteController;


    private boolean snackbarState = false;

    private OnBackPressedListener backPressedListener = null;

    private Snackbar.SnackbarLayout layout;

    private UserManager userManager;

    private Fragment mFragmentAtPos1;


    private MusicController musicController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        global = (Global) getApplication();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        musicController = (MusicController) findViewById(R.id.controller);
        musicController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onPlayerActivity();
            }
        });
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // TODO 처음 로그인 시 initialize
        userManager = new UserManager();
        userManager.getUser(global.loginUser.getUid(), new UserManager.OnCompleteGetUserListener() {
            @Override
            public void onComplete(User user)
            {
                if (user == null)
                {
                    userManager.addNewUser();
                }
            }
        });

        global.setOnChangeListener(new Global.OnChangeListener() {
            @Override
            public void onChange()
            {
                updateController();
            }
        });

        updateController();

    }

    @Override
    public void onResume()
    {
        global.setOnChangeListener(new Global.OnChangeListener() {
            @Override
            public void onChange()
            {
                updateController();
            }
        });
        super.onResume();
    }

    public void updateController()
    {
        int songid = global.musicService.getPlayingMusic();
        if (songid != 0)
        {
            MusicDto musicDto = global.mMusicManager.getMusicDto(songid + "");
            musicController.setSongTitleTextView(musicDto.getTitle());
            musicController.setSongInformationTextView(musicDto.getArtist() + " - " + musicDto.getAlbum());
            musicController.setSongAlbumImageView(global.mMusicManager.getAlbumImage(getApplicationContext(), Integer.parseInt(musicDto.getAlbumId()), 100));
        }
    }

    public void onPlayerActivity()
    {
        if (global.musicService.getPlayingMusic() != 0)
        {
            Intent intent = new Intent(this, PlayerActivity.class);
            startActivity(intent);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {

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
        }
        else if (id == R.id.action_nowplaying)
        {
            onPlayerActivity();
            return true;
        }
        else if (id == R.id.action_newplaylist)
        {
            InputPopup inputPopup = new InputPopup(this);
            inputPopup.setListener(new InputPopup.OnCompleteInputValue() {
                @Override
                public void onComplete(String str)
                {
                    PlayList playList = new PlayList();
                    playList.setName(str);
                    PlayListManager playListManager = new PlayListManager(getBaseContext());
                    playListManager.savePlayList(playList);
                }
            });
            inputPopup.show();
            return true;
        }
        Log.e("tes", "test");

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter
    {
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            // Causes adapter to reload all Fragments when
            // notifyDataSetChanged is called
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new RootFragmentPos1();
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
