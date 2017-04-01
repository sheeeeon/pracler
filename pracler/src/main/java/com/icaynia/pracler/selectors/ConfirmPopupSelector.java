package com.icaynia.pracler.selectors;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.icaynia.pracler.Data.PlayListManager;
import com.icaynia.pracler.Global;
import com.icaynia.pracler.R;
import com.icaynia.pracler.View.PlayListSelecter;
import com.icaynia.pracler.View.SelectPopup;
import com.icaynia.pracler.adapters.MenuListAdapter;
import com.icaynia.pracler.selectors.callbacks.OnConfirmSelectListener;

import java.util.ArrayList;

/**
 * Created by icaynia on 02/04/2017.
 *
 * 미작동
 */

public class ConfirmPopupSelector extends Dialog
{
    private Context mContext;
    private OnConfirmSelectListener listener;

    private CharSequence title;
    private CharSequence message;

    public ConfirmPopupSelector(Context context) {
        super(context);
    }

    public void setTitleText(CharSequence title)
    {
        this.title = title;
    }

    public void setMessageText(CharSequence message)
    {
        this.message = message;
    }

    public void setListener(OnConfirmSelectListener listener)
    {
        this.listener = listener;
    }

}
