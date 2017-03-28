package com.icaynia.pracleme.CardLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icaynia.pracleme.models.MusicDto;
import com.icaynia.pracleme.R;

/**
 * Created by icaynia on 16/03/2017.
 *
 * 메인 화면에서 사용하는 레이아웃 뷰.
 */

public class RecommandSongView extends LinearLayout
{
    private View mainView;

    private LayoutInflater inflater;
    private ImageView imageView;
    private TextView Artist;
    private TextView Title;

    public RecommandSongView(Context context)
    {
        super(context);
        viewInitialize();
    }

    public RecommandSongView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        viewInitialize();
    }

    private void viewInitialize()
    {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_main_recommand_song, null);

        imageView = (ImageView) mainView.findViewById(R.id.view_album);
        Artist = (TextView) mainView.findViewById(R.id.view_artist);
        Title = (TextView) mainView.findViewById(R.id.view_title);

        addView(mainView);
    }

    public void setImage(Bitmap bitmap)
    {
           imageView.setImageBitmap(bitmap);
    }

    public void setRecommandSong(MusicDto musicDto)
    {

        Title.setText(musicDto.getTitle());
        Artist.setText(musicDto.getArtist());
    }

}
