package com.icaynia.pracler.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.icaynia.pracler.View.CardHeader;
import com.icaynia.pracler.data.PlayListManager;
import com.icaynia.pracler.Global;
import com.icaynia.pracler.R;
import com.icaynia.pracler.View.InputPopup;
import com.icaynia.pracler.View.PlayListSelectPopup;
import com.icaynia.pracler.View.SelectPopup;
import com.icaynia.pracler.adapters.PlayListsAdapter;
import com.icaynia.pracler.models.PlayList;

import java.util.ArrayList;

/**
 * Created by icaynia on 16/03/2017.
 */

public class PlayListsFragment extends Fragment
{
    private Global global;
    private View v;

    private ListView listView;
    private TextView noPlayListTextView;

    private ArrayList<String> playLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_myplaylists, container, false);
        setHasOptionsMenu(true);
        viewInitialize();
        prepare();
        return v;
    }

    public void viewInitialize()
    {
        listView = (ListView) v.findViewById(R.id.listview);
        noPlayListTextView = (TextView) v.findViewById(R.id.no_playlist);
    }

    public void prepare()
    {
        global = (Global) getContext().getApplicationContext();

        this.update();

        if (playLists.size() == 0)
        {
            noPlayListTextView.setVisibility(View.VISIBLE);
            noPlayListTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    InputPopup inputPopup = new InputPopup(getContext());
                    inputPopup.setListener(new InputPopup.OnCompleteInputValue() {
                        @Override
                        public void onComplete(String str)
                        {
                            PlayList playList = new PlayList();
                            playList.setName(str);
                            PlayListManager playListManager = new PlayListManager(getContext());
                            playListManager.savePlayList(playList);

                            prepare();
                        }
                    });

                    inputPopup.setTitleText("새로운 재생목록 만들기");
                    inputPopup.show();
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int realPosition = i-1;

                PlayListFragment plf = new PlayListFragment();
                plf.setPlayList(playLists.get(realPosition));
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.framelayout, plf)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .commit();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                final int realPosition = i-1;
                final SelectPopup selectPopup = new SelectPopup(getContext());
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("재생");
                arrayList.add("재생목록 이름 변경");
                arrayList.add("재생목록 삭제");
                selectPopup.setList(arrayList);
                selectPopup.setTitleText("이 재생목록을...");
                selectPopup.setListener(new SelectPopup.OnCompleteSelect()
                {
                    @Override
                    public void onComplete(int position)
                    {

                        switch(position)
                        {
                            case 0:
                                try
                                {
                                    global.nowPlayingList = global.playListManager.getPlayList(playLists.get(realPosition));
                                    global.playMusic(Integer.parseInt(global.nowPlayingList.get(0)));
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), getString(R.string.no_item_playlist), Toast.LENGTH_SHORT).show();
                                }
                                selectPopup.dismiss();
                                break;
                            case 1:
                                InputPopup inputPopup = new InputPopup(getContext());
                                inputPopup.setTitleText(getString(R.string.edit_playlist));
                                inputPopup.setListener(new InputPopup.OnCompleteInputValue() {
                                    @Override
                                    public void onComplete(String str)
                                    {
                                        PlayListManager playListManager = new PlayListManager(getContext());
                                        PlayList playList = playListManager.getPlayList(playLists.get(realPosition));
                                        playList.setName(str);
                                        playListManager.deletePlayList(playLists.get(realPosition));


                                        playListManager.savePlayList(playList);
                                        selectPopup.dismiss();

                                        update();
                                    }
                                });
                                inputPopup.show();
                                break;
                            case 2:
                                checkDelete(playLists.get(realPosition));
                                selectPopup.dismiss();
                                break;
                        }


                    }
                });
                selectPopup.show();
                return false;
            }
        });
    }

    public void update()
    {
        listView.removeHeaderView(listView.getChildAt(0));
        playLists = global.playListManager.getPlayListList();

        PlayListsAdapter playListsAdapter = new PlayListsAdapter(getContext(), playLists);
        listView.setAdapter(playListsAdapter);

        CardHeader cardHeader = new CardHeader(getContext());
        cardHeader.setTitleIcon(getResources().getDrawable(R.drawable.ic_playlist_play_black));
        cardHeader.setTitleText("내 재생목록");
        listView.addHeaderView(cardHeader);
    }

    public void checkDelete(final String playlistName)
    {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());

        alert_confirm.setMessage("정말로 삭제할까요? ").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        global.playListManager.deletePlayList(playlistName);
                        update();
                        // 'YES'
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // 'No'
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }



}
