package com.icaynia.soundki.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.MusicList;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.CardHeader;
import com.icaynia.soundki.View.MusicListAdapter;
import com.icaynia.soundki.View.PlayListAdapter;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by icaynia on 16/03/2017.
 */

public class Today20Fragment extends Fragment
{
    private Global global;
    private View v;
    private ListView mainListView;

    private MusicList musicList;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        global = (Global) getContext().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_today20, container, false);
        setHasOptionsMenu(true);
        this.viewInitialize();
        this.prepare();
        return v;
    }

    public void viewInitialize()
    {
        mainListView = (ListView) v.findViewById(R.id.listview);
    }

    public void prepare()
    {
        musicList = arrayShuffle(global.mMusicManager.getMusicList());
        MusicListAdapter adapter = new MusicListAdapter(getContext(), musicList);

        mainListView.setAdapter(adapter);
        CardHeader cardHeader = new CardHeader(getContext());
        cardHeader.setTitleIcon(getResources().getDrawable(R.drawable.ic_grade));
        cardHeader.setTitleText("지금 SoundKi가 추천하는 10곡");
        mainListView.addHeaderView(cardHeader);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    }

    /** 배열을 섞는 메소드 **/
    private MusicList arrayShuffle(MusicList originalArray){
        Random rand = new Random(System.currentTimeMillis())
                ;
        // ArrayList 타입으로 생성하고 내용 복사
        ArrayList<MusicDto> tempArray = new ArrayList<MusicDto>();
        for(MusicDto item : originalArray.getList()) { tempArray.add(item); }

        MusicList musicList = new MusicList();

        // 랜덤으로 하나씩 뽑아서 새로 대입
        int newIndex = 0;
        while( tempArray.size() > 0 ){
            int selectRandomIndex = rand.nextInt(tempArray.size());
            musicList.getList().add(newIndex, tempArray.remove(selectRandomIndex));
            newIndex++;
            if (newIndex == 10)
            {
                break;
            }
        }
        return musicList;

        // NOTE : originalArray는 reference가 넘어올테니 리턴하지 않아도 될듯?
    }


}
