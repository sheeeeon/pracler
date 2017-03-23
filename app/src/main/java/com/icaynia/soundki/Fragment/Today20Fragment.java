package com.icaynia.soundki.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.icaynia.soundki.Global;
import com.icaynia.soundki.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_today20, container, false);
        setHasOptionsMenu(true);
        viewInitialize();
        return v;
    }

    public void viewInitialize()
    {
        mainListView = (ListView) v.findViewById(R.id.listview);
    }

    public void prepare()
    {

    }

    /** 배열을 섞는 메소드 **/
    private void arrayShuffle(String[] originalArray){
        Random rand = new Random(System.currentTimeMillis());
        // ArrayList 타입으로 생성하고 내용 복사
        ArrayList<String> tempArray = new ArrayList<String>();
        for(String item : originalArray){ tempArray.add(item); }
        // 랜덤으로 하나씩 뽑아서 새로 대입
        int newIndex = 0;
        while( tempArray.size() > 0 ){
            int selectRandomIndex = rand.nextInt(tempArray.size());
            originalArray[newIndex] = tempArray.remove(selectRandomIndex);
            newIndex++;
        }
        // NOTE : originalArray는 reference가 넘어올테니 리턴하지 않아도 될듯?
    }


}
