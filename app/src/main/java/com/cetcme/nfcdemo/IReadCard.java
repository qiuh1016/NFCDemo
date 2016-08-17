package com.cetcme.nfcdemo;

import android.nfc.Tag;
import android.os.Handler;

public abstract interface IReadCard
{
    public static final int GETALLDATA = 8;
    public static final int GETDATADEFEATED = 7;
    public static final int GETDATASUCCESS = 6;
    public static final int STATE_READ_AUTH_ERROR = 9;
    public static final int STATE_UPDATE_READCHECK = 121;
    public static final int STATE_UPDATE_WRITE_PROGRESS = 10;
    public static final int STATE_WRITE_SUCCESS = 11;

    public abstract void ReadCard(Handler paramHandler);

    public abstract void getMifareClassicInstance(Tag paramTag);
}