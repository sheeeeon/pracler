package com.icaynia.pracler.models;

import io.realm.RealmObject;

/**
 * Created by icaynia on 21/03/2017.
 */

public class LocalPlayHistory extends RealmObject
{
    public int uid;
    public String Regdate; /** 20170213140302, 시작 시간 기준으로 */
}
