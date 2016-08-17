package com.cetcme.nfcdemo;

/**
 * Created by qiuhong on 8/11/16.
 */

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
public class SecretUtils {
    public static final String Algorithm3DES = "DESede";
    public static final String AlgorithmDES = "DES";
    private static final byte[] FIX_KEY = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

    public static byte[] build3DesKey(byte[] paramArrayOfByte)
            throws UnsupportedEncodingException
    {
        byte[] arrayOfByte = new byte[24];
        System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
        for (int i = 0;; i++)
        {
            if (i >= 8) {
                return arrayOfByte;
            }
            arrayOfByte[(i + 16)] = paramArrayOfByte[i];
        }
    }
//
//    public static String byte2HexStr(byte[] paramArrayOfByte)
//    {
//        String str1 = "";
//        int i = 0;
//        if (i >= paramArrayOfByte.length) {
//            return str1.toUpperCase();
//        }
//        String str2 = Integer.toHexString(0xFF & paramArrayOfByte[i]);
//        if (str2.length() == 1) {}
//        for (str1 = str1 + "0" + str2;; str1 = str1 + str2)
//        {
//            i++;
//            break;
//        }
//    }
//
//    public static ArrayList<String> byte2HexStrArray(byte[] paramArrayOfByte)
//    {
//        ArrayList localArrayList = new ArrayList();
//        for (int i = 0;; i++)
//        {
//            if (i >= paramArrayOfByte.length) {
//                return localArrayList;
//            }
//            String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
//            if (str.length() == 1) {
//                str = "0" + str;
//            }
//            str.toUpperCase();
//            localArrayList.add(str);
//        }
//    }

    public static int byteToInt2(byte[] paramArrayOfByte)
    {
        int i = 0;
        for (int j = 0;; j++)
        {
            if (j >= 4) {
                return i;
            }
            i = i << 8 | 0xFF & paramArrayOfByte[j];
        }
    }

    private static byte charToByte(char paramChar)
    {
        return (byte)"0123456789ABCDEF".indexOf(paramChar);
    }

    public static byte[] decryptMode(byte[] paramArrayOfByte, String paramString)
    {
        try
        {
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(build3DesKey(FIX_KEY), paramString);
            Cipher localCipher = Cipher.getInstance(paramString);
            localCipher.init(2, localSecretKeySpec);
            byte[] arrayOfByte = localCipher.doFinal(paramArrayOfByte);
            return arrayOfByte;
        }
        catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
        {
            localNoSuchAlgorithmException.printStackTrace();
            return null;
        }
        catch (NoSuchPaddingException localNoSuchPaddingException)
        {
            for (;;)
            {
                localNoSuchPaddingException.printStackTrace();
            }
        }
        catch (Exception localException)
        {
            for (;;)
            {
                localException.printStackTrace();
            }
        }
    }


//    public static byte[] hexStringToBytes(String paramString)
//    {
//        byte[] arrayOfByte;
//        if ((paramString == null) || (paramString.equals(""))) {
//            arrayOfByte = null;
//        }
//        for (;;)
//        {
//            return arrayOfByte;
//            String str = paramString.toUpperCase();
//            int i = str.length() / 2;
//            char[] arrayOfChar = str.toCharArray();
//            arrayOfByte = new byte[i];
//            for (int j = 0; j < i; j++)
//            {
//                int k = j * 2;
//                arrayOfByte[j] = ((byte)(charToByte(arrayOfChar[k]) << 4 | charToByte(arrayOfChar[(k + 1)])));
//            }
//        }
//    }

    public static byte[] withoutAutofill(byte[] paramArrayOfByte)
    {
        byte[] arrayOfByte = new byte[-8 + paramArrayOfByte.length];
        for (int i = 0;; i++)
        {
            if (i >= arrayOfByte.length) {
                return arrayOfByte;
            }
            arrayOfByte[i] = paramArrayOfByte[i];
        }
    }
}
