package com.icaynia.soundki.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icaynia on 13/03/2017.
 */

public class PlayLogListAdapter extends RecyclerView.Adapter<PlayLogListAdapter.PlayLogListItemViewHolder>
{
    private ArrayList<MusicDto> list;

    public PlayLogListAdapter(ArrayList<MusicDto> modelData)
    {
        if (modelData == null)
        {
            throw new IllegalArgumentException("ModelData must not be null");
        }
        this.list = modelData;
    }

    @Override
    public PlayLogListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_list_playlog, null);
        return new PlayLogListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayLogListItemViewHolder viewHolder, int position)
    {
        //viewHolder.albumView.setImageBitmap(null);
        viewHolder.songName.setText(list.get(position).getTitle());
        //viewHolder.regdate.setText("0000-00-00");
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class PlayLogListItemViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView albumView;
        public TextView regdate;
        public TextView songName;

        public PlayLogListItemViewHolder(View itemView)
        {
            super(itemView);
            this.songName = (TextView) itemView.findViewById(R.id.songText);
        }
    }

}
