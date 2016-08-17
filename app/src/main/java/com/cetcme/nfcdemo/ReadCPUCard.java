package com.cetcme.nfcdemo;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;
import java.util.List;

public class ReadCPUCard

{

    /*
    private Handler mHandler;
    private IsoDep mIsoDep;
    private List<String> mList = null;
    private Message mMessage;

    private void getUniqueEmptyListInstance()
    {
        if (this.mList == null) {
            this.mList = new ArrayList();
        }
        this.mList.clear();
    }

    public void ReadCard(Handler paramHandler)
    {
        this.mHandler = paramHandler;
        this.mMessage = Message.obtain(this.mHandler);
        getUniqueEmptyListInstance();
        byte[] arrayOfByte1 = new byte[2];
        arrayOfByte1[1] = 5;
        YzCard localYzCard = new YzCard(this.mIsoDep);
        if (localYzCard.OpenCard()) {
            localYzCard.SelectADF();
        }
        try
        {
            StringBuilder localStringBuilder = new StringBuilder();
            byte[] arrayOfByte2 = localYzCard.ReadEF(arrayOfByte1, 0, 99);
            String str1 = Common.getStringBySpecifiedByteLength(arrayOfByte2, 0, 16);
            String str2 = Common.getStringBySpecifiedByteLength(arrayOfByte2, 16, 16);
            String str3 = "";
            boolean bool = localYzCard.SelectADF(new byte[] { -33, 1 });
            byte[] arrayOfByte3 = null;
            if (bool)
            {
                arrayOfByte3 = localYzCard.ReadEF(arrayOfByte1, 0, 48);
                str3 = Common.getStringBySpecifiedByteLength(arrayOfByte3, 32, 16);
            }
            this.mList.add(" ");
            this.mList.add(str1);
            this.mList.add(str2);
            this.mList.add(str3);
            this.mMessage.obj = this.mList;
            this.mMessage.what = 6;
            this.mMessage.sendToTarget();
            this.mMessage = Message.obtain(this.mHandler);
            this.mMessage.what = 8;
            if (arrayOfByte3 != null) {
                localStringBuilder.append("������:").append(str3).append("\n");
            }
            localStringBuilder.append("������������:").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 0, 16)).append("\n");
            localStringBuilder.append("������������:").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 16, 16)).append("\n");
            localStringBuilder.append("������:").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 32, 6)).append("\n");
            localStringBuilder.append("���������������:").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 38, 6)).append("\n");
            localStringBuilder.append("������:").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 44, 5)).append("\n");
            localStringBuilder.append("������:").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 49, 5)).append("\n");
            localStringBuilder.append("���������:").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 54, 5)).append("\n");
            localStringBuilder.append("������������:").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 59, 10)).append("\n");
            localStringBuilder.append("������������:").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 69, 8)).append("\n");
            localStringBuilder.append("������������ :").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 77, 10)).append("\n");
            localStringBuilder.append("������������:").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 87, 4)).append("\n");
            localStringBuilder.append("������������:").append(Common.getStringBySpecifiedByteLength(arrayOfByte2, 91, 8)).append("\n");
            this.mMessage.obj = localStringBuilder.toString();
            this.mMessage.sendToTarget();
        }
        catch (Exception localException)
        {
            for (;;)
            {
                this.mMessage.what = 7;
                this.mMessage.sendToTarget();
                localException.printStackTrace();
            }
        }
        localYzCard.CloseCard();
    }

    public void getMifareClassicInstance(Tag paramTag)
    {
        this.mIsoDep = IsoDep.get(paramTag);
    }

    */
}
