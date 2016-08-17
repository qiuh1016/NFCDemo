package com.cetcme.nfcdemo;

import android.nfc.Tag;
import android.os.Handler;


public abstract class Card
{
    private Handler handler;
    protected IReadCard mIReadCard;
    private Tag mTag;

    public Card(Handler paramHandler, Tag paramTag)
    {
        this.handler = paramHandler;
        this.mTag = paramTag;
    }


    public void getDataFromCard()
    {
        this.mIReadCard.getMifareClassicInstance(this.mTag);
        this.mIReadCard.ReadCard(this.handler);
    }
}