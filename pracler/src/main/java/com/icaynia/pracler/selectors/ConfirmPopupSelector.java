package com.icaynia.pracler.selectors;

import android.app.Dialog;
import android.content.Context;

import com.icaynia.pracler.selectors.callbacks.OnConfirmSelectListener;

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
