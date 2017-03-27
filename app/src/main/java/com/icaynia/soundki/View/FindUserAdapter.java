package com.icaynia.soundki.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icaynia.soundki.Activity.ProfileActivity;
import com.icaynia.soundki.Data.UserManager;
import com.icaynia.soundki.Fragment.ProfileMenuFragment;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.User;
import com.icaynia.soundki.R;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by icaynia on 25/03/2017.
 */

public class FindUserAdapter extends BaseAdapter
{
    private Context context;
    public Global global;
    private ArrayList<User> followingList;
    private LayoutInflater inflater;

    private OnClickListener listener;

    public FindUserAdapter(Context context, ArrayList<User> followingList)
    {
        this.context = context;
        this.followingList = followingList;
        this.global = (Global) context.getApplicationContext();
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return followingList.size();
    }

    @Override
    public User getItem(int position)
    {
        return followingList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.view_list_finduser_row, parent, false);
            ListView.LayoutParams layoutParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.MATCH_PARENT);
            convertView.setLayoutParams(layoutParams);
        }

        RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onClick("position", position);
            }
        });

        TextView viewTitle = (TextView) convertView.findViewById(R.id.view_title);
        viewTitle.setText(followingList.get(position).name);

        Button button = (Button) convertView.findViewById(R.id.button_follow);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onClick("follow", position);
            }
        });

        return convertView;
    }

    public void setOnClickListener(OnClickListener listener)
    {
        this.listener = listener;
    }

    public class UpdateView extends AsyncTask<String, Void, Bitmap>
    {
        public ImageView imageView;

        public void setImageView(ImageView imageView)
        {
            this.imageView = imageView;
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... url)
        {
            for (String i : url)
            {
                return getImage(i);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }

        private Bitmap getImage(final String ul)
        {
            try
            {
                URL url = new URL(ul);
                InputStream is = url.openStream();
                final Bitmap bm = BitmapFactory.decodeStream(is); //비트맵 객체로 보여주기

                return bm;
            }
            catch(Exception e)
            {
                return null;
            }
        }
    }

    public interface OnClickListener
    {
        void onClick(String str, int position);
    }

}
