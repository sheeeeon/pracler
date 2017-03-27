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
import com.icaynia.soundki.Model.State;
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
    private ArrayList<User> searchList;
    private LayoutInflater inflater;

    private ArrayList<String> followList;

    private OnClickListener listener;

    public FindUserAdapter(Context context, ArrayList<User> searchList)
    {
        this.context = context;
        this.searchList = searchList;
        this.global = (Global) context.getApplicationContext();
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    /* 팔로우 되어있는지를 확인하기 위한 리스트 */
    public void setFollowList(ArrayList<String> followList)
    {
        this.followList = followList;
    }

    @Override
    public int getCount()
    {
        return searchList.size();
    }

    @Override
    public User getItem(int position)
    {
        return searchList.get(position);
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
                listener.onClick("profile", position);
            }
        });

        TextView viewTitle = (TextView) convertView.findViewById(R.id.view_title);
        viewTitle.setText(searchList.get(position).name);

        final Button button = (Button) convertView.findViewById(R.id.button_follow);

        final State state = new State();

        for (int i = 0; i < followList.size(); i++)
        {
            /* 팔로우 하는 중인 유저인 경우 */
            if (searchList.get(position).uid.equals(followList.get(i)))
            {
                state.setState(true);
                button.setBackgroundResource(R.drawable.view_follow_enable);
                break;
            }
            /* 아닌 경우 */
            else
            {
                state.setState(false);
                button.setBackgroundResource(R.drawable.view_follow_disable);
                break;
            }
        }
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (state.getState())
                {
                    listener.onClick("follow-enable", position);
                    button.setBackgroundResource(R.drawable.view_follow_disable);
                    state.setState(false);
                }
                else if (!state.getState())
                {
                    listener.onClick("follow-disable", position);
                    button.setBackgroundResource(R.drawable.view_follow_enable);
                    state.setState(true);
                }
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
