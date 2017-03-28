package com.icaynia.pracleme.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.icaynia.pracleme.Global;
import com.icaynia.pracleme.models.User;
import com.icaynia.pracleme.View.ProfileRow;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by icaynia on 25/03/2017.
 */

public class ProfileMenuAdapter extends BaseAdapter
{
    private Context context;
    public Global global;
    private ArrayList<User> followingList;
    private LayoutInflater inflater;

    public ProfileMenuAdapter(Context context, ArrayList<User> followingList)
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
            convertView = new ProfileRow(context);
            ListView.LayoutParams layoutParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.MATCH_PARENT);
            convertView.setLayoutParams(layoutParams);
        }

        final ProfileRow v = (ProfileRow) convertView;

        v.setText(followingList.get(position).name);
        UpdateView updateView = new UpdateView();
        updateView.setImageView(v.imageView);
        updateView.execute(followingList.get(position).picture);

        convertView = v;

        return convertView;
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
}
