package com.icaynia.soundki.Fragment;

import android.accounts.Account;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.icaynia.soundki.Activity.MainActivity;
import com.icaynia.soundki.Data.FileManager;
import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Data.PlayListManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.MusicList;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.Card;
import com.icaynia.soundki.View.MenuListAdapter;
import com.icaynia.soundki.View.MusicListAdapter;
import com.icaynia.soundki.View.PlayListAdapter;
import com.icaynia.soundki.View.PlayListSelecter;

import java.lang.reflect.Array;
import java.util.ArrayList;



/**
 * Created by icaynia on 2017. 2. 8..
 */

public class MyMusicListFragment extends Fragment
{
    private String TAG = "MyMusicListFragment";
    private View v;
    private Global global;

    private ListView listView;
    private MusicFileManager mMusicManager;
    private MusicList list;

    private MusicListAdapter musicListAdapter;

    private NewFragmentEvent listener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        global = (Global) getContext().getApplicationContext();
        mMusicManager = global.mMusicManager;
        list = mMusicManager.getMusicList();
        musicListAdapter = new MusicListAdapter(getContext(), list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mymusic, container, false);

        Card today_recommand_20 = (Card) v.findViewById(R.id.today_recommand_20);
        today_recommand_20.setButtonImageDrawable(getResources().getDrawable(R.drawable.ic_grade));
        today_recommand_20.setButtonTitleText("오늘의 추천 20곡");
        today_recommand_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction()
                        .add(R.id.framelayout, new MyTop20Fragment())
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .commit();
            }
        });

        Card top_20 = (Card) v.findViewById(R.id.top_20);
        top_20.setButtonImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up));
        top_20.setButtonTitleText("가장 많이 들은 곡 20곡");
        top_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction()
                        .add(R.id.framelayout, new MyTop20Fragment())
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .commit();
            }
        });

        Card playlist = (Card) v.findViewById(R.id.playlist);
        playlist.setButtonImageDrawable(getResources().getDrawable(R.drawable.ic_playlist_play_black));
        playlist.setButtonTitleText("내 플레이리스트");
        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction()
                        .add(R.id.framelayout, new PlayListsFragment())
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .commit();
            }
        });

        Card album = (Card) v.findViewById(R.id.album);
        album.setButtonImageDrawable(getResources().getDrawable(R.drawable.ic_album));
        album.setButtonTitleText("내 앨범");
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.framelayout, new MyAlbumFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        Card artist = (Card) v.findViewById(R.id.artist);
        artist.setButtonImageDrawable(getResources().getDrawable(R.drawable.ic_recent_actors));
        artist.setButtonTitleText("내 아티스트");
        artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction()
                        .add(R.id.framelayout, new MyArtistFragment())
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .commit();
            }
        });

        listView = (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(musicListAdapter);

        listView.setOnItemClickListener(defaultClick);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                openChooseMode(position);
                return false;
            }
        });

        Spinner spinner = (Spinner) v.findViewById(R.id.spin1);
        String[] platforms = getResources().
                getStringArray(R.array.mymusicfragment_sort);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        platforms);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                    list = mMusicManager.sort(list, global.SORT_NAME);
                else if (position == 1)
                    list = mMusicManager.sort(list, global.SORT_ALBUM);
                else if (position == 2)
                    list = mMusicManager.sort(list, global.SORT_ARTIST);
                else if (position == 3)
                    list = mMusicManager.sort(list, global.SORT_LENGTH);
                else if (position == 4)
                    list = mMusicManager.sort(list, global.SORT_LENGTH);

                listView.setAdapter(new MusicListAdapter(getContext(), list));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        return v;
    }
    public AdapterView.OnItemClickListener defaultClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            String songId = view.getTag().toString();
            MusicDto song = mMusicManager.getMusicDto(songId);
            Log.e("MyMusicListFragment", "Song : " + song.getTitle() + " Artist : " + song.getArtist());
            global.playMusic(Integer.parseInt(songId));
            PlayList nowPlayingList = new PlayList();

            for (int pos = 0; pos < listView.getAdapter().getCount(); pos++)
            {
                MusicDto dto = musicListAdapter.getItem(pos);
                nowPlayingList.addItem(dto);
            }

            nowPlayingList.setPosition(position);
            global.nowPlayingList = nowPlayingList;
        }
    };

    public void setAddNewFragmentEventListener(NewFragmentEvent listener)
    {
        this.listener = listener;
    }


    public void setBackbutton()
    {
        ((MainActivity)getContext()).setOnBackPressedListener(new MainActivity.OnBackPressedListener() {
            @Override
            public void onBackPressed()
            {
                hideChooseMode();
            }
        });
    }

    public void openChooseMode(int clickedPosition)
    {
        listView.setOnItemClickListener(null);
        MusicListAdapter adapter = (MusicListAdapter)listView.getAdapter();
        adapter.setChoiceMode(true);
        adapter.notifyDataSetChanged();
        adapter.setCheckState(clickedPosition, true);
        setActionBarState(true);
        setBackbutton();
        ((MainActivity)getActivity()).openActionBarButton();
    }

    public void hideChooseMode()
    {
        MusicListAdapter adapter = (MusicListAdapter)listView.getAdapter();
        adapter.setChoiceMode(false);
        adapter.notifyDataSetChanged();
        setActionBarState(false);
        listView.setOnItemClickListener(defaultClick);

        ((MainActivity)getActivity()).setOnBackPressedListener(null);
        ((MainActivity)getActivity()).hideActionBarButton();
    }

    public void setActionBarState(boolean state)
    {
        if (state)
        {
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("선택");
            ((MainActivity)getActivity()).setActionBarPositiveButton("추가", new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    MusicListAdapter adapter = (MusicListAdapter)listView.getAdapter();
                    showDialog(adapter.getState());
                    hideChooseMode();
                }
            });
            ((MainActivity)getActivity()).setActionBarNegativeButton("취소", new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    hideChooseMode();
                }
            });
        }
        else
        {
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("SoundKi");

        }
    }


    public void showDialog(ArrayList<Boolean> state) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        //String inputText = inputTextField.getText().toString();

        DialogFragment newFragment = MyDialogFragment.newInstance(state, list);
        newFragment.show(ft, "dialog");

    }

    public static class MyDialogFragment extends DialogFragment
    {

        static MyDialogFragment newInstance(ArrayList<Boolean> state, MusicList list) {
            MyDialogFragment f = new MyDialogFragment();

            int count = 0;
            for (int i = 0; i < state.size(); i++)
            {
                if (state.get(i) == true)
                {
                    count++;
                }
            }
            Bundle bundle = new Bundle();
            bundle.putInt("count", count);
            bundle.putSerializable("statelist", state);
            bundle.putSerializable("musiclist", list);
            f.setArguments(bundle);

            if (count == 0) return  null;
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Global global = (Global) getActivity().getApplicationContext();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_checkmenu, null, false);
            int count = getArguments().getInt("count");
            final MusicList musiclist = (MusicList) getArguments().getSerializable("musiclist");
            final ArrayList<Boolean> state = (ArrayList<Boolean>) getArguments().getSerializable("statelist");

            for (int i = 0; i < state.size(); i++)
            {
                Log.e(i+"f", state.get(i)+"");
            }
            ArrayList<String> str = new ArrayList<>();
            str.add("다음 재생");
            str.add("재생목록에 추가");

            MenuListAdapter mla = new MenuListAdapter(getContext(), str);
            ListView listViewt = (ListView) view.findViewById(R.id.listview);
            listViewt.setAdapter(mla);

            final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity())
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(count+"개 선택한 항목을 ..")
                    .setView(view);

            final AlertDialog dialog = builder.create();

            listViewt.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    switch (position)
                    {
                        // TODO add to next play
                        case 0:
                            for (int i = 0; i < state.size(); i++)
                            {
                                if (state.get(i) == true)
                                {
                                    String songId = musiclist.getItem(i).getUid_local();
                                    global.nowPlayingList.addItem(Integer.parseInt(songId), global.nowPlayingList.getPosition()+1);
                                }
                            }
                            break;

                        // TODO add to playlist
                        case 1:
                            final ArrayList<String> adduid = new ArrayList<String>();
                            for (int i = 0; i < state.size(); i++)
                            {
                                if (state.get(i) == true)
                                {
                                    String songId = musiclist.getItem(i).getUid_local();
                                    adduid.add(songId);
                                }
                            }

                            PlayListSelecter selecter = new PlayListSelecter(getContext());
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList("adduid", adduid);
                            selecter.setBundle(bundle);
                            selecter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {
                                    String playlistname = (String) view.getTag();
                                    PlayListManager plm = global.playListManager;
                                    PlayList playList = plm.getPlayList(playlistname);
                                    for (int i = 0; i < adduid.size(); i++)
                                    {
                                        playList.addItem(adduid.get(i));
                                    }
                                    plm.savePlayList(playList);
                                }
                            });
                            selecter.show();
                            break;


                    }
                    Log.e("tag", position + " ");
                    dialog.dismiss();
                }
            });



            return dialog;
        }

    }

    public interface NewFragmentEvent
    {
        void changeNewFragment(Fragment fragment);
    }


}

