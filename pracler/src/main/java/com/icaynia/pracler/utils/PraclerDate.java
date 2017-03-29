package com.icaynia.pracler.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by icaynia on 29/03/2017.
 */

public class PraclerDate
{
    public static String getNowDate()
    {
        String format = new String("yyyyMMddHHmmss");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
        String Regdate = sdf.format(new Date());

        return Regdate;
    }
}
