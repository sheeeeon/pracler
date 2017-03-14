package com.icaynia.soundki.View;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
;
import com.icaynia.soundki.Activity.MainActivity;
import com.icaynia.soundki.Data.PlayListManager;
import com.icaynia.soundki.Fragment.MyMusicListFragment;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicList;
import com.icaynia.soundki.R;

import java.util.ArrayList;

/**
 * Created by icaynia on 14/03/2017.
 */

public class PlayListSelecter
{
    private Context context;
    private AdapterView.OnItemClickListener listener;
    private Bundle bundle;

    public PlayListSelecter(Context context)
    {
        this.context = context;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public void show()
    {
        showDialog(bundle);
    }

    public void showDialog(Bundle bundle) {
        final Activity activity = (Activity) context;
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag("dialog2");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        ArrayList<String> list = (ArrayList<String>) bundle.get("adduid");
        MyDialogFragment newFragment = MyDialogFragment.newInstance(bundle);
        newFragment.setContext(context);
        newFragment.setListener(listener);
        newFragment.show(ft, "dialog2");

    }

    public void setBundle(Bundle bundle)
    {
        this.bundle = bundle;
    }

    public static class MyDialogFragment extends DialogFragment
    {
        private Context mContext;
        private AdapterView.OnItemClickListener listener;
        public void setContext(Context context)
        {
            mContext = context;
        }

        public void setListener(AdapterView.OnItemClickListener listener)
        {
            this.listener = listener;
        }

        static MyDialogFragment newInstance(Bundle bundle) {
            MyDialogFragment f = new MyDialogFragment();
            f.setArguments(bundle);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Global global = (Global) getActivity().getApplicationContext();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_checkmenu, null, false);
            final ArrayList<String> adduid = (ArrayList<String>) getArguments().getStringArrayList("adduid");

            ArrayList<String> str = new ArrayList<>();

            PlayListManager plm = new PlayListManager(mContext);
            ArrayList<String> playlists = plm.getPlayListList();

            //str.add("새로운 플레이리스트에 저장 ...");
            for (int i = 0; i < playlists.size(); i++)
            {
                str.add(playlists.get(i));
            }


            MenuListAdapter mla = new MenuListAdapter(mContext, str);
            ListView listViewt = (ListView) view.findViewById(R.id.listview);
            listViewt.setAdapter(mla);

            final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("선택한 항목을 ..");
            builder.setView(view);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();
                }
            });

            final AlertDialog dialog = builder.create();

            listViewt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    // for customizing.
                    listener.onItemClick(parent, view, position, id);
                }
            });

            return dialog;
        }

    }


}
