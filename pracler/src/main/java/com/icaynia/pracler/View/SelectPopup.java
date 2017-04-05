package com.icaynia.pracler.View;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
;
import com.icaynia.pracler.Global;
import com.icaynia.pracler.R;

import java.util.ArrayList;

/**
 * Created by icaynia on 14/03/2017.
 *
 *
 */

public class SelectPopup
{
    public Context context;
    private Bundle bundle;
    private OnCompleteSelect listener;
    private ArrayList<String> list;
    private MyDialogFragment newFragment;

    private String fragmentTag;

    public SelectPopup(Context context)
    {
        this.context = context;
    }

    public void show()
    {
        showDialog(bundle);
    }

    public void setList(ArrayList<String> strings)
    {
        this.list = strings;
    }

    public void dismiss()
    {
        newFragment.dismiss();
    }

    public void setTitleText(String titleText)
    {

    }

    public void setTag(String tag)
    {
        this.fragmentTag = tag;
    }

    public void showDialog(Bundle bundle) {
        final Activity activity = (Activity) context;
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(fragmentTag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        newFragment = MyDialogFragment.newInstance(bundle);
        newFragment.setList(list);
        newFragment.setContext(context);
        newFragment.setListener(listener);
        newFragment.show(ft, fragmentTag);

    }

    public void setListener(OnCompleteSelect listener)
    {
        this.listener = listener;
    }


    public void setBundle(Bundle bundle)
    {
        this.bundle = bundle;
    }

    public static class MyDialogFragment extends DialogFragment
    {
        private Context mContext;
        private OnCompleteSelect listener;
        private ArrayList<String> list;
        private String titleText;

        public void setContext(Context context)
        {
            mContext = context;
        }

        public void setListener(OnCompleteSelect listener)
        {
            this.listener = listener;
        }

        public void setList(ArrayList<String> string)
        {
            this.list = string;
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
            View view = inflater.inflate(R.layout.view_popup_select, null, false);

            final ListView listView = (ListView) view.findViewById(R.id.listview);
            ArrayAdapter Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list) ;

            listView.setAdapter(Adapter);
            final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("이 재생목록을...");
            builder.setView(view);


            final AlertDialog dialog = builder.create();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    listener.onComplete(i);
                }
            });

            return dialog;
        }

        public void setTitleText(String str)
        {
            titleText = str;
        }

        @Override
        public void dismiss()
        {
            super.dismiss();
        }

    }

    public interface OnCompleteSelect
    {
        void onComplete(int position);
    }

}
