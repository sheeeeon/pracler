package com.icaynia.pracleme.View;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
;
import com.icaynia.pracleme.Global;
import com.icaynia.pracleme.R;

/**
 * Created by icaynia on 14/03/2017.
 */

public class InputPopup
{
    private Context context;
    private Bundle bundle;
    private OnCompleteInputValue listener;

    public InputPopup(Context context)
    {
        this.context = context;
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

        MyDialogFragment newFragment = MyDialogFragment.newInstance(bundle);
        newFragment.setContext(context);
        newFragment.setListener(listener);
        newFragment.show(ft, "dialog2");

    }

    public void setListener(OnCompleteInputValue listener)
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
        private OnCompleteInputValue listener;
        public void setContext(Context context)
        {
            mContext = context;
        }

        public void setListener(OnCompleteInputValue listener)
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
            View view = inflater.inflate(R.layout.view_popup_input_text, null, false);

            final EditText editText = (EditText) view.findViewById(R.id.view_editText);

            final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("새로운 이름");
            builder.setView(view);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (listener != null)
                        listener.onComplete(editText.getText().toString());
                }
            });

            final AlertDialog dialog = builder.create();
            return dialog;
        }

    }

    public interface OnCompleteInputValue
    {
        void onComplete(String str);
    }

}
