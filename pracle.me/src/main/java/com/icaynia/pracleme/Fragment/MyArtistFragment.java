package com.icaynia.pracleme.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.icaynia.pracleme.Global;
import com.icaynia.pracleme.models.MusicDto;
import com.icaynia.pracleme.models.MusicList;
import com.icaynia.pracleme.models.PlayList;
import com.icaynia.pracleme.R;
import com.icaynia.pracleme.View.CardHeader;
import com.icaynia.pracleme.adapters.MusicListAdapter;
import com.icaynia.pracleme.View.PlayListSelectPopup;
import com.icaynia.pracleme.View.SelectPopup;

import java.util.ArrayList;

/**
 * Created by icaynia on 16/03/2017.
 */

public class MyArtistFragment extends Fragment
{
    private Global global;
    private View v;

    // TODO VIEW
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_myartist, container, false);
        setHasOptionsMenu(true);
        viewInitialize();
        prepare();
        return v;
    }

    public void viewInitialize()
    {
        listView = (ListView) v.findViewById(R.id.listview);

    }

    public void prepare()
    {
        global = (Global) getContext().getApplicationContext();

        final MusicList musicList = global.mMusicManager.getMusicList();

        MusicListAdapter adapter = new MusicListAdapter(getContext(), musicList);

        listView.setAdapter(adapter);

        CardHeader cardHeader = new CardHeader(getContext());
        cardHeader.setTitleIcon(getResources().getDrawable(R.drawable.ic_recent_actors));
        cardHeader.setTitleText("아티스트 정렬");
        listView.addHeaderView(cardHeader);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i -= 1;
                global.playMusic(Integer.parseInt(musicList.getItem(i).getUid_local()));

                PlayList newNowPlayingList = new PlayList();
                for (int t = 0; t < musicList.size(); t++)
                {
                    newNowPlayingList.addItem(musicList.getItem(t).getUid_local());
                }

                newNowPlayingList.setPosition(i);
                global.nowPlayingList = newNowPlayingList;
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                i += 1;
                final MusicDto musicDto = musicList.getItem(i);

                SelectPopup selectPopup = new SelectPopup(getContext());
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("재생");
                arrayList.add("다음 재생");
                arrayList.add("재생목록에 추가");
                selectPopup.setList(arrayList);
                selectPopup.setListener(new SelectPopup.OnCompleteSelect()
                {
                    @Override
                    public void onComplete(int position)
                    {
                        switch(position)
                        {
                            case 0:
                                global.playMusic(Integer.parseInt(musicDto.getUid_local()));
                                break;
                            case 1:
                                global.nowPlayingList.addItem(Integer.parseInt(musicDto.getUid_local()), global.nowPlayingList.getPosition() + 1);
                                break;
                            case 2:
                                PlayListSelectPopup popup = new PlayListSelectPopup(getContext());

                                final ArrayList<String> arrayList = global.playListManager.getPlayListList();
                                popup.setList(arrayList);

                                popup.setListener(new SelectPopup.OnCompleteSelect()
                                {
                                    @Override
                                    public void onComplete(int position)
                                    {
                                        PlayList tmpPlayList = global.playListManager.getPlayList(arrayList.get(position));
                                        tmpPlayList.addItem(musicDto);
                                        global.playListManager.savePlayList(tmpPlayList);
                                        Toast.makeText(getContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                popup.show();
                                break;
                        }
                    }
                });
                selectPopup.show();
                return false;
            }
        });
    }


}
