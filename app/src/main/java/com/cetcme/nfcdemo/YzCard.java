package com.cetcme.nfcdemo;

/**
 * Created by qiuhong on 8/11/16.
 */
import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.IOException;

public class YzCard {



    public static final String Algorithm3DES = "DESede";
    public String ramdstr;
    public String secretstr;
//    public CpuCards yzCard;
    IsoDep dep;
    byte[] Response = new byte[300];
    public int ResponseLength = 300;

    public YzCard(IsoDep paramIsoDep)
    {
        this.dep = paramIsoDep;
//        this.yzCard = new CpuCards(paramIsoDep);
    }

//    public void CloseCard()
//    {
//        this.yzCard.SmartCard_Close();
//    }
//
//    public byte[] GetAtr()
//    {
//        return this.yzCard.GetAtr();
//    }
//
//    public boolean GetRank(byte paramByte, byte[] paramArrayOfByte)
//    {
//        byte[] arrayOfByte1 = new byte[8];
//        byte[] arrayOfByte2 = new byte[24];
//        byte b;
//        if (paramByte == 0) {
//            b = 0;
//        }
//        byte[] arrayOfByte3;
//        byte[] arrayOfByte4;
//        for (;;)
//        {
//            arrayOfByte3 = new byte[8];
//            arrayOfByte4 = this.yzCard.GetRamd((byte)4);
//            if (!arrayOfByte4.equals(null)) {
//                break;
//            }
//            do
//            {
//                return false;
//                if (paramByte == 1)
//                {
//                    b = 1;
//                    break;
//                }
//                if (paramByte == 2)
//                {
//                    b = 2;
//                    break;
//                }
//                if (paramByte == 3)
//                {
//                    b = 3;
//                    break;
//                }
//            } while (paramByte != 4);
//            b = 4;
//        }
//        int i = 0;
//        int j;
//        byte[] arrayOfByte5;
//        if (i >= 8)
//        {
//            j = 0;
//            if (j < 24) {
//                break label172;
//            }
//            arrayOfByte5 = SecretUtils.encryptMode(arrayOfByte3, arrayOfByte2, "DESede");
//        }
//        for (int k = 0;; k++)
//        {
//            if (k >= 8)
//            {
//                return this.yzCard.ExternalAuth(b, arrayOfByte1);
//                if (i < 4) {
//                    arrayOfByte3[i] = arrayOfByte4[i];
//                }
//                for (;;)
//                {
//                    i++;
//                    break;
//                    arrayOfByte3[i] = 0;
//                }
//                label172:
//                if (j < 16) {
//                    arrayOfByte2[j] = paramArrayOfByte[j];
//                }
//                for (;;)
//                {
//                    j++;
//                    break;
//                    arrayOfByte2[j] = paramArrayOfByte[(j - 16)];
//                }
//            }
//            arrayOfByte1[k] = arrayOfByte5[k];
//        }
//    }
//
//    public byte[] GetResponse()
//    {
//        return this.yzCard.GetResponse();
//    }
//
//    public byte GetSW1()
//    {
//        return this.yzCard.GetSW1();
//    }
//
//    public byte GetSW2()
//    {
//        return this.yzCard.GetSW2();
//    }

    public boolean OpenCard()
    {
//        return this.yzCard.SmartCard_Open();
        return this.SmartCard_Open();
    }



    public boolean SmartCard_Open()
    {
        try
        {
            this.dep.connect();
            return this.dep.isConnected();
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
        return false;
    }

    public boolean SelectEF(byte[] paramArrayOfByte)
    {
        byte[] arrayOfByte = new byte[7];
        arrayOfByte[0] = 0;
        arrayOfByte[1] = -92;
        arrayOfByte[2] = 2;
        arrayOfByte[3] = 0;
        arrayOfByte[4] = 2;
        arrayOfByte[5] = paramArrayOfByte[0];
        arrayOfByte[6] = paramArrayOfByte[1];
        return SmartCard_Exchange(arrayOfByte);
    }

    public byte[] ReadBinary(byte paramByte1, byte paramByte2, byte paramByte3)
    {
        boolean bool = SmartCard_Exchange(new byte[] { 0, -80, paramByte1, paramByte2, paramByte3 });
        byte[] arrayOfByte = null;
        if (bool) {
            arrayOfByte = new byte[this.ResponseLength];
        }
        for (int i = 0;; i++)
        {
            if (i >= this.ResponseLength) {
                return arrayOfByte;
            }
            arrayOfByte[i] = this.Response[i];
        }
    }

    public byte[] ReadEF(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
        int i = paramInt1;
        byte[] arrayOfByte1 = new byte[paramInt2];
        boolean bool = this.SelectEF(paramArrayOfByte);
        int j = 0;
        if (!bool) {
            return null;
        }
        if (paramInt2 - j > 255) {}
        byte[] arrayOfByte2;
        for (int k = 255;; k = paramInt2 - j)
        {

            arrayOfByte2 = this.ReadBinary((byte)(i / 256), (byte)(i % 256), (byte)k);
            if (arrayOfByte2 != null) {
                break;
            }

            for (int m = 0;; m++)
            {

                if (m >= arrayOfByte2.length)
                {
                    i += k;
                    j += k;
                    if (j < paramInt2) {
                        break;
                    }
                    return arrayOfByte1;
                }
                arrayOfByte1[(j + m)] = arrayOfByte2[m];
            }

        }
        return null;
    }

    public boolean SelectADF()
    {
        byte[] arrayOfByte = { 78, 84, 65, 66, 67, 46, 89, 89, 67, 66, 46, 68, 68, 70, 48, 49 };
        return this.SelectDDF(arrayOfByte);
    }


    public boolean SelectDDF(byte[] paramArrayOfByte)
    {
        byte[] arrayOfByte = new byte[5 + paramArrayOfByte.length];
        arrayOfByte[0] = 0;
        arrayOfByte[1] = -92;
        arrayOfByte[2] = 4;
        arrayOfByte[3] = 0;
        arrayOfByte[4] = 0;
        for (int i = 0;; i++)
        {
            if (i >= paramArrayOfByte.length) {
                return SmartCard_Exchange(arrayOfByte);
            }
            arrayOfByte[(i + 5)] = paramArrayOfByte[i];
            arrayOfByte[4] = ((byte)(1 + arrayOfByte[4]));
        }
    }

    public boolean SmartCard_Exchange(byte[] paramArrayOfByte)
    {
        int i = paramArrayOfByte.length;
        boolean bool = true;
        if (i > this.dep.getMaxTransceiveLength()) {
            return false;
        }
        int j = 0;

        if (j >= 300) {}
        for (;;) {
            Object localObject;
            int k;
            int i2;
            try {
                byte[] arrayOfByte1 = this.dep.transceive(paramArrayOfByte);
                Log.i("Main", "****array0: " + arrayOfByte1[0] + ",1: " + arrayOfByte1[1]);

//                localObject = arrayOfByte1;
//                if (localObject.equals(null)) {
//                    break;
//                }
//                k = arrayOfByte1.length;
//                if (k < 2) {
//                    break;
//                }
//                if (arrayOfByte1[(k - 2)] != 97) {
//                    break;
//                }
//                byte[] arrayOfByte4 = new byte[5];
//                arrayOfByte4[0] = 0;
//                arrayOfByte4[1] = -64;
//                arrayOfByte4[2] = 0;
//                arrayOfByte4[3] = 0;
//                arrayOfByte4[4] = arrayOfByte1[(k - 1)];
            } catch (IOException localIOException1) {
                byte[] arrayOfByte4;
                byte[] arrayOfByte5;
//                Log.e(this.TAG, localIOException1.getLocalizedMessage());
                return false;
            }
        }

//     return false;
//            try
//            {
//                arrayOfByte5 = this.dep.transceive(arrayOfByte4);
//                localObject = arrayOfByte5;
//                k = localObject.length;
//                if ((localObject[(k - 2)] != -112) || (localObject[(k - 1)] != 0)) {
//                    break label264;
//                }
//                i2 = 0;
//                if (i2 < k - 2) {
//                    break label246;
//                }
//                this.ResponseLength = (k - 2);
//                this.SW1 = localObject[(k - 2)];
//                this.SW2 = localObject[(k - 1)];
//                return bool;
//            }
//            catch (IOException localIOException3)
//            {
//                Log.e(this.TAG, localIOException3.getLocalizedMessage());
//                return false;
//            }
//            this.Response[j] = 0;
//            j++;
//            break label21;
//            label246:
//            this.Response[i2] = localObject[i2];
//            i2++;
//            continue;
//            label264:
//            bool = false;
//            continue;
//            label269:
//            if (localObject[(k - 2)] == 108)
//            {
//                byte[] arrayOfByte2 = new byte[i];
//                int n = 0;
//                label289:
//                if (n >= i) {
//                    paramArrayOfByte[(i - 1)] = localObject[(k - 1)];
//                }
//                for (;;)
//                {
//                    int i1;
//                    try
//                    {
//                        byte[] arrayOfByte3 = this.dep.transceive(arrayOfByte2);
//                        localObject = arrayOfByte3;
//                        k = localObject.length;
//                        if ((localObject[(k - 2)] != -112) || (localObject[(k - 1)] != 0)) {
//                            break label422;
//                        }
//                        i1 = 0;
//                        if (i1 < k - 2) {
//                            break label404;
//                        }
//                        this.ResponseLength = (k - 2);
//                    }
//                    catch (IOException localIOException2)
//                    {
//                        Log.e(this.TAG, localIOException2.getLocalizedMessage());
//                        return false;
//                    }
//                    arrayOfByte2[n] = paramArrayOfByte[n];
//                    n++;
//                    break label289;
//                    label404:
//                    this.Response[i1] = localObject[i1];
//                    i1++;
//                }
//                label422:
//                bool = false;
//            }
//            else
//            {
//                if ((localObject[(k - 2)] == -112) && (localObject[(k - 1)] == 0)) {
//                    for (int m = 0;; m++)
//                    {
//                        if (m >= k - 2)
//                        {
//                            this.ResponseLength = (k - 2);
//                            break;
//                        }
//                        this.Response[m] = localObject[m];
//                    }
//                }
//                bool = false;
//            }
//        }
    }
//
//    public boolean SelectADF(byte[] paramArrayOfByte)
//    {
//        return this.yzCard.SelectADF(paramArrayOfByte);
//    }
//
//    public boolean UpdateEF(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2)
//    {
//        int i = paramInt1;
//        boolean bool1 = this.yzCard.SelectEF(paramArrayOfByte1);
//        int j = 0;
//        if (!bool1) {
//            return bool1;
//        }
//        boolean bool2;
//        label119:
//        do
//        {
//            int k;
//            byte[] arrayOfByte;
//            if (paramInt2 - j > 255)
//            {
//                k = 255;
//                arrayOfByte = new byte[k];
//            }
//            for (int m = 0;; m++)
//            {
//                if (m >= k)
//                {
//                    bool2 = this.yzCard.UpdateBinary((byte)(i / 256), (byte)(i % 256), (byte)k, arrayOfByte);
//                    if (bool2) {
//                        break label119;
//                    }
//                    return bool2;
//                    k = paramInt2 - j;
//                    break;
//                }
//                arrayOfByte[m] = paramArrayOfByte2[(i + m)];
//            }
//            i += k;
//            j += k;
//        } while (j < paramInt2);
//        return bool2;
//    }


}
