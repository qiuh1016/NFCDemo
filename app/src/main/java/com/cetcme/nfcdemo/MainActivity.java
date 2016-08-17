package com.cetcme.nfcdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private TextView textView;

    private String[][] techListArray;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilersArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);


        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);

        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            tagIntentFilter.addDataType("text/plain");
        } catch (Throwable t) {
            t.printStackTrace();
        }

        /*
        nfcCheck();

        //创建一个PendingIntent对象，以便Android系统能够在扫描到NFC标签时用它来封装NFC标签的详细信息
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        //声明想要截获处理的Intent对象的Intent过滤器
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        //建立一个应用程序希望处理的NFC标签技术数组
        techListArray = new String[][] {new String[] {NfcF.class.getName()}};
        */

        nfcCheck();

        // 78, 84, 65, 66, 67, 46, 89, 89, 67, 66, 46, 68, 68, 70, 48, 49   NTABC.YYCB.DDF01

//        byte[] b = { 78, 84, 65, 66, 67, 46, 89, 89, 67, 66, 46, 68, 68, 70, 48, 49 };  // NTABC.YYCB.DDF01
//        try {
//            String s = new String(b, "UTF-8");
//            log("S: " + s);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            log("e");
//        }
    }

    private void textByte() {
        try {
            byte[] midbytes= "15845F".getBytes("UTF8");

            for (int i = 0; i < midbytes.length; i++) {
                log("******midbytes: " + midbytes[i]);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] bytes = {
//                (byte) 0x31, (byte) 0x35,(byte) 0x38,(byte) 0x34,(byte) 0x35,(byte) 0x46
                49,53,56,52,53,70
        };
        try {
            String srt2= new String(bytes,"UTF-8");
            log("******srt2: " + srt2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void nfcCheck() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            textView.setText("设备不支持NFC！");
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            textView.setText("请在系统设置中先启功NFC功能！");
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilersArray, null);

    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.i("Main", "New Intent");
        textView.setText("new Intent");

        isoDep(intent);

//        new Thread(new ReadThread(intent)).start();
    }

    private void isoDep(Intent intent) {


        /**
         *  isoDep
         */

        Tag tagFromIntent = intent.getParcelableExtra("android.nfc.extra.TAG");

        byte[] aa = tagFromIntent.getId();
        Log.i("Main", "***UID: " + bytesToHexString(aa));

        IsoDep isoDep = IsoDep.get(tagFromIntent);
        try {
            isoDep.connect();
            if (isoDep.isConnected()) {
                Log.i("Main", "***isoDep is connected!");

                Log.i("Main", "***tag: " + isoDep.getTag().toString());
                Log.i("Main", "***getHistoricalBytes: " + bytesToHexString(isoDep.getHistoricalBytes()));
                Log.i("Main", "***getHiLayerResponse: " + isoDep.getHiLayerResponse());
                Log.i("Main", "***max len: " + isoDep.getMaxTransceiveLength());
                Log.i("Main", "***timeout: " + isoDep.getTimeout());
            }

//            byte[] TEST = {
//                    (byte) 0xAA, (byte) 0x00, (byte) 0x02, (byte) 0x80, (byte) 0x02, (byte) 0x80, (byte) 0xBB,
//            };
//
//            byte[] b = "MF.EF05".getBytes("UTF-8");
//
//            for (int i = 0; i < b.length; i++) {
//                log("***b: " + b[i]);
//            }

            byte[] SELECT = {
                    (byte) 0x00, // CLA Class
                    (byte) 0xA4, // INS Instruction
                    (byte) 0x04, // P1  Parameter 1
                    (byte) 0x00, // P2  Parameter 2
                    (byte) 0x01, // Length
//                    -24,-70,-85,-28,-69,-67,-24,-81,-127  // 身份证
                    77,70,46,69,70,48,53 // MF.EF05
            };

            byte[] result = isoDep.transceive(SELECT);
            log("result: " + result[0]);
            log("result: " + result[1]);

//            for (int i = 0; ; i++) {
//                byte[] result = isoDep.transceive(SELECT);
//                if ((result[0] == 103) && result[1] == 0) {
//                    log("break: \n select[4] = " + SELECT[4]);
//                    log("67 00");
//                    log("i: " + i);
//                    SELECT[4] += 1;
//                } else {
//                    log("break: \n select[4] = " + SELECT[4]);
//                    log("result: " + result[0]);
//                    log("result: " + result[1]);
//                    break;
//                }
//            }



//            byte[] READ_BINARY = {
//                    (byte) 0x00, // CLA Class
//                    (byte) 0xB0, // INS Instruction
//                    (byte) 0x80, // P1  (indicate use of SFI)
//                    (byte) 0x01, // P2  (SFI = 0x01)
//                    (byte) 0x04  // LE  maximal number of bytes expected in result
//            };

//            byte[] NATIVE_SELECT_APP_COMMAND = new byte[] {
//                            (byte) 0x90, (byte) 0x5A, (byte) 0x00, (byte) 0x00, 3,  // SELECT
//                            (byte) 0x5F, (byte) 0x84, (byte) 0x15, (byte) 0x00      // APPLICATION ID
//            };
//
//            byte[] NATIVE_SELECT_FILE_COMMAND = new byte[] {
//                            (byte) 0x90, (byte) 0xBD, (byte) 0x00, (byte) 0x00, 7,  // READ
//                            (byte) 0x01,                                            // FILE ID
//                            (byte) 0x00, (byte) 0x00, (byte) 0x00,                  // OFFSET
//                            (byte) 0x00, (byte) 0x00, (byte) 0x00,                  // LENGTH
//                            (byte) 0x00
//            };
//            byte[] result = isoDep.transceive(SELECT);
//
//            for (int i = 0; i < result.length; i++) {
//                log("******result: " + result[i]);
//            }

//            byte[] SELECT = {
//                    (byte) 0x00, // CLA = 00 (first interindustry command set)
//                    (byte) 0xA4, // INS = A4 (SELECT)
//                    (byte) 0x04, // P1  = 04 (select file by DF name)
//                    (byte) 0x0C, // P2  = 0C (first or only file; no FCI)
//                    (byte) 0x06, // Lc  = 6  (data/AID has 6 bytes)
//                    (byte) 0x31, (byte) 0x35,(byte) 0x38,(byte) 0x34,(byte) 0x35,(byte) 0x46 // AID = 15845F
//            };
//
//
//            byte[] READ_BINARY = {
//                    (byte) 0x00, // CLA Class
//                    (byte) 0xB0, // INS Instruction
//                    (byte) 0x80, // P1  (indicate use of SFI)
//                    (byte) 0x01, // P2  (SFI = 0x01)
//                    (byte) 0x04  // LE  maximal number of bytes expected in result
//            };
//
//            Log.i("Main", "***read: " + isoDep.transceive(READ_BINARY));
//
//            byte[] result = isoDep.transceive(SELECT);
//            Log.i("Main", "SELECT: " + result);

//            if (!(result[0] == (byte) 0x90 && result[1] == (byte) 0x00))
//                throw new IOException("could not select application");

//            byte[] GET_STRING = {
//                    (byte) 0x00, // CLA Class
//                    (byte) 0xB0, // INS Instruction
//                    (byte) 0x00, // P1  Parameter 1
//                    (byte) 0x00, // P2  Parameter 2
//                    (byte) 0x04  // LE  maximal number of bytes expected in result
//            };
//
//            result = isoDep.transceive(GET_STRING);
//            Log.i("Main", "GET_STRING: " + bytesToHexString(result));

        } catch (IOException e) {
            e.printStackTrace();
        }
//
    }


    public class ReadThread implements Runnable {
        private Intent intent;

        public ReadThread(Intent paramIntent) {
            this.intent = paramIntent;
        }

        public void run() {
            Looper.prepare();
            MainActivity.this.processIntent(this.intent);
        }
    }

    private void processIntent(Intent paramIntent) {
        Tag localTag = paramIntent.getParcelableExtra("android.nfc.extra.TAG");

        /**
         *  techList中如果有isoDep则进行下一步，删掉了判断，读其它卡可能报错
         */
        dealWithCpuCard(localTag);

    }

    public void dealWithCpuCard(Tag paramTag) {

        IsoDep localIsoDep = IsoDep.get(paramTag);
        byte[] arrayOfByte1 = new byte[2];
        arrayOfByte1[1] = 5;
        YzCard localYzCard = new YzCard(localIsoDep);

        /**
         *  open card 省略 isConnected 的判断
         */
        try {
            localIsoDep.connect();  //open card
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectADF(localIsoDep, arrayOfByte1);

//        readEF();





//        try {
//            byte[] arrayOfByte2 = localYzCard.ReadEF(arrayOfByte1, 0, 105);
//            if (arrayOfByte2 == null)
//            {
//                Message localMessage2 = Message.obtain();
//                localMessage2.what = 22;
//                localYzCard.CloseCard();
//                return;
//            }
//            boolean bool = localYzCard.SelectADF(new byte[] { -33, 1 });
//            byte[] arrayOfByte3 = null;
//            if (bool) {
//                arrayOfByte3 = localYzCard.ReadEF(arrayOfByte1, 0, 48);
//            }
//            String str = "";
//            if (arrayOfByte3 != null) {
//                str = Common.getStringBySpecifiedByteLength(arrayOfByte3, 32, 16);
//            }
//            localYzCard.CloseCard();
//            processCpuInfo(arrayOfByte2, str);
//        }
//        catch (Exception localException) {
//
//        }
//        localYzCard.CloseCard();



    }

    private void readEF(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {

    }

    private void selectADF(IsoDep localIsoDep,byte[] arrayOfByte1 ) {
        /**
         *  SelectADF & SelectDDF
         */
        if (localIsoDep.isConnected()) {

            log("localIsoDep.isConnected");
            log("***tag: " + localIsoDep.getTag().toString());
            log("***getHistoricalBytes: " + bytesToHexString(localIsoDep.getHistoricalBytes()));
            log("***getHiLayerResponse: " + localIsoDep.getHiLayerResponse());
            log("***max len: " + localIsoDep.getMaxTransceiveLength());
            log("***timeout: " + localIsoDep.getTimeout());

            byte[] paramArrayOfByte = {78, 84, 65, 66, 67, 46, 89, 89, 67, 66, 46, 68, 68, 70, 48, 49};

            byte[] arrayOfByte = new byte[5 + paramArrayOfByte.length];
            arrayOfByte[0] = 0;
            arrayOfByte[1] = -92;
            arrayOfByte[2] = 4;
            arrayOfByte[3] = 0;
            arrayOfByte[4] = 0;
            byte[] arrayOfByteToTransceive;

            for (int i = 0; ; i++) {
                if (i >= paramArrayOfByte.length) {
                    arrayOfByteToTransceive = arrayOfByte;

                    /**
                     *  Smart_card_exchange
                     */

                    byte i1;

                    label257:
                    for (;;) {
                        byte[] arrayOfByte1_1;
                        int k;
                        try {
                            arrayOfByte1 = localIsoDep.transceive(paramArrayOfByte);
                            if (arrayOfByte1.equals(null)) {
                                break;
                            }
                            k = arrayOfByte1.length;
                            if (k < 2) {
                                break;
                            }
                            if (arrayOfByte1[(k - 2)] != 97) {
                                break label257;
                            }
                            i1 = arrayOfByte1[(k - 1)];
                            byte[] aramArrayOfByte = localIsoDep.transceive(new byte[] { 0, -64, 0, 0, i1 });
                            log("*****" + aramArrayOfByte + "");


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }


//                    break;
                }
                arrayOfByte[(i + 5)] = paramArrayOfByte[i];
                arrayOfByte[4] = ((byte) (1 + arrayOfByte[4]));
            }

//            log(arrayOfByteToTransceive + " ");

        }
    }

    private void log(String str) {
        Log.i("Main", str);
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }


    private void processMifareClassicIntent(Intent intent) {
        //取出封装在intent中的TAG
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        for (String tech : tagFromIntent.getTechList()) {
            System.out.println(tech);
        }
        boolean auth = false;

        MifareClassic mfc = MifareClassic.get(tagFromIntent);
        try {
            String metaInfo = "";
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            if (mfc.isConnected()) {
                Log.i("Main",  "****mfc connected! ");
            }

            int type = mfc.getType();//获取TAG的类型
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }
            metaInfo += "卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
                    + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";
            for (int j = 0; j < sectorCount; j++) {
                //Authenticate a sector with key A.
                auth = mfc.authenticateSectorWithKeyA(j,
                        MifareClassic.KEY_DEFAULT);
                int bCount;
                int bIndex;
                if (auth) {
                    metaInfo += "Sector " + j + ":验证成功\n";
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        metaInfo += "Block " + bIndex + " : "
                                + bytesToHexString(data) + "\n";
                        bIndex++;
                    }
                } else {
                    metaInfo += "Sector " + j + ":验证失败\n";
                }
            }
            textView.setText(metaInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}




//public class MainActivity extends AppCompatActivity {
//
//    private NfcAdapter nfcAdapter;
//    private String readResult;
//    private TextView textView;
//
//    private String[][] techListArray;
//    private PendingIntent pendingIntent;
//    private IntentFilter[] intentFilersArray;
//
//    private NfcA nfcaTag;
//
//    private NdefMessage mNdefMessage;
//
//    String TAG = "Main";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        textView = (TextView) findViewById(R.id.textView);
//
//
//
//        Intent nfcIntent = new Intent(this, getClass());
//        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
//
//        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        try {
//            tagIntentFilter.addDataType("text/plain");
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//
//        /*
//        nfcCheck();
//
//        //创建一个PendingIntent对象，以便Android系统能够在扫描到NFC标签时用它来封装NFC标签的详细信息
//        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//        //声明想要截获处理的Intent对象的Intent过滤器
//        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        try {
//            ndef.addDataType("");
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//            throw new RuntimeException("fail", e);
//        }
//        //建立一个应用程序希望处理的NFC标签技术数组
//        techListArray = new String[][] {new String[] {NfcF.class.getName()}};
//        */
//
//        nfcCheck();
//    }
//
//    private void nfcCheck() {
//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if (nfcAdapter == null) {
//            textView.setText("设备不支持NFC！");
//            return;
//        }
//        if (!nfcAdapter.isEnabled()) {
//            textView.setText("请在系统设置中先启功NFC功能！");
//            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
//            return;
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        nfcAdapter.disableForegroundDispatch(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
////        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
//////            readFromTag(getIntent());
////            processIntent(getIntent());
////        }
//        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilersArray, null);
//
//    }
//
//    @Override
//    public void onNewIntent(Intent intent) {
//        Log.i("Main", "New Intent");
//        textView.setText("new Intent");
//        getTag(intent);
//    }
//
//    private void getTag(Intent i) {
//        if (i == null) {
//            return;
//        }
//
//        String action = i.getAction();
//
//        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
//            Log.i("Main", "Action Tag Found");
//            resolveIntentNfcA(i);
////            processIntent1(i);
//        }
//    }
//
//
//    private boolean readFromTag(Intent intent){
//        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//        NdefMessage mNdefMsg = (NdefMessage)rawArray[0];
//        NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
//        try {
//            if(mNdefRecord != null){
//                readResult = new String(mNdefRecord.getPayload(),"UTF-8");
//                textView.setText("读取到内容：\n" + readResult);
//                return true;
//            }
//        }
//        catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    private String bytesToHexString(byte[] src) {
//        StringBuilder stringBuilder = new StringBuilder("0x");
//        if (src == null || src.length <= 0) {
//            return null;
//        }
//        char[] buffer = new char[2];
//        for (int i = 0; i < src.length; i++) {
//            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
//            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
////            System.out.println(buffer);
//            stringBuilder.append(buffer);
//        }
//        return stringBuilder.toString();
//    }
//
//
//    void processIntent1(Intent intent) {
//        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
//                NfcAdapter.EXTRA_NDEF_MESSAGES);
//        // only one message sent during the beam
//        NdefMessage msg = (NdefMessage) rawMsgs[0];
//        // record 0 contains the MIME type, record 1 is the AAR, if present
//        textView.setText(new String(msg.getRecords()[0].getPayload()));
//    }
//    /**
//     * Parses the NDEF Message from the intent and prints to the TextView
//     */
//    private void processIntent(Intent intent) {
//        //取出封装在intent中的TAG
//        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        for (String tech : tagFromIntent.getTechList()) {
//            System.out.println(tech);
//        }
//        boolean auth = false;
//
//        MifareClassic mfc = MifareClassic.get(tagFromIntent);
//        try {
//            String metaInfo = "";
//            //Enable I/O operations to the tag from this TagTechnology object.
//            mfc.connect();
//            if (mfc.isConnected()) {
//                Log.i("Main",  "****mfc connected! ");
//            }
//
//            int type = mfc.getType();//获取TAG的类型
//            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
//            String typeS = "";
//            switch (type) {
//                case MifareClassic.TYPE_CLASSIC:
//                    typeS = "TYPE_CLASSIC";
//                    break;
//                case MifareClassic.TYPE_PLUS:
//                    typeS = "TYPE_PLUS";
//                    break;
//                case MifareClassic.TYPE_PRO:
//                    typeS = "TYPE_PRO";
//                    break;
//                case MifareClassic.TYPE_UNKNOWN:
//                    typeS = "TYPE_UNKNOWN";
//                    break;
//            }
//            metaInfo += "卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
//                    + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";
//            for (int j = 0; j < sectorCount; j++) {
//                //Authenticate a sector with key A.
//                auth = mfc.authenticateSectorWithKeyA(j,
//                        MifareClassic.KEY_DEFAULT);
//                int bCount;
//                int bIndex;
//                if (auth) {
//                    metaInfo += "Sector " + j + ":验证成功\n";
//                    // 读取扇区中的块
//                    bCount = mfc.getBlockCountInSector(j);
//                    bIndex = mfc.sectorToBlock(j);
//                    for (int i = 0; i < bCount; i++) {
//                        byte[] data = mfc.readBlock(bIndex);
//                        metaInfo += "Block " + bIndex + " : "
//                                + bytesToHexString(data) + "\n";
//                        bIndex++;
//                    }
//                } else {
//                    metaInfo += "Sector " + j + ":验证失败\n";
//                }
//            }
//            textView.setText(metaInfo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    class CommandAsyncTask extends AsyncTask<Integer, Integer, String> {
//
//        @Override
//        protected String doInBackground(Integer... params) {
//            // TODO Auto-generated method stub
//            byte[] search = new byte[]{0x05, 0x00, 0x00};
//            search = new byte[]{0x00, (byte) 0xA4, 0x00, 0x00, 0x02, 0x60,
//                    0x02};
//            search = new byte[]{0x1D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
//                    0x01, 0x08};
//            byte[] result = new byte[]{};
//            StringBuffer sb = new StringBuffer();
//            try {
//                byte[] cmd = new byte[]{0x05, 0x00, 0x00};
//
//                result = nfcaTag.transceive(cmd);
//                sb.append("寻卡指令:" + ByteArrayToHexString(cmd) + "\n");
//                sb.append("收:" + ByteArrayToHexString(result) + "\n");
//                cmd = new byte[]{0x1D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
//                        0x01, 0x08};
//                result = nfcaTag.transceive(cmd);
//                sb.append("选卡指令:" + ByteArrayToHexString(cmd) + "\n");
//                sb.append("收:" + ByteArrayToHexString(result) + "\n");
//                sb.append("读固定信息指令\n");
//
//                cmd = new byte[]{0x00, (byte) 0xA4, 0x00, 0x00, 0x02, 0x60,
//                        0x02};
//                result = nfcaTag.transceive(cmd);
//                sb.append("发:" + ByteArrayToHexString(cmd) + "\n");
//                sb.append("收:" + ByteArrayToHexString(result) + "\n");
//                cmd = new byte[]{(byte) 0x80, (byte) 0xB0, 0x00, 0x00, 0x20};
//                result = nfcaTag.transceive(cmd);
//                sb.append("发:" + ByteArrayToHexString(cmd) + "\n");
//                sb.append("收:" + ByteArrayToHexString(result) + "\n");
//                cmd = new byte[]{0x00, (byte) 0x88, 0x00, 0x52, 0x0A,
//                        (byte) 0xF0, 0x00, 0x0E, 0x0C, (byte) 0x89, 0x53,
//                        (byte) 0xC3, 0x09, (byte) 0xD7, 0x3D};
//                result = nfcaTag.transceive(cmd);
//                sb.append("发:" + ByteArrayToHexString(cmd) + "\n");
//                sb.append("收:" + ByteArrayToHexString(result) + "\n");
//                cmd = new byte[]{0x00, (byte) 0x88, 0x00, 0x52, 0x0A,
//                        (byte) 0xF0, 0x00,};
//                result = nfcaTag.transceive(cmd);
//                sb.append("发:" + ByteArrayToHexString(cmd) + "\n");
//                sb.append("收:" + ByteArrayToHexString(result) + "\n");
//                cmd = new byte[]{0x00, (byte) 0x84, 0x00, 0x00, 0x08};
//                result = nfcaTag.transceive(cmd);
//                sb.append("发:" + ByteArrayToHexString(cmd) + "\n");
//                sb.append("收:" + ByteArrayToHexString(result) + "\n");
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            Log.i("Main", "*******sb: " + sb.toString());
//            return sb.toString();
//        }
//    }
//
//    private String ByteArrayToHexString(byte[] inarray) {
//        // converts byte
//        // arrays to string
//        int i, j, in;
//        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
//                "B", "C", "D", "E", "F"};
//        String out = "";
//
//        for (j = 0; j < inarray.length; ++j) {
//            in = inarray[j] & 0xff;
//            i = (in >> 4) & 0x0f;
//            out += hex[i];
//            i = in & 0x0f;
//            out += hex[i];
//        }
//        return out;
//    }
//
//
//    void resolveIntentNfcA(Intent intent){
//        Log.i("Main", "***resolveIntentNfcA");
//
//        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//
////        Log.i("Main", intent.getAction());
////        if ("android.nfc.action.TAG_DISCOVERED".equals(intent.getAction()))
////        {
////            Tag localTag = (Tag) intent.getParcelableExtra("android.nfc.extra.TAG");
////            if (MifareClassic.get(localTag) != null)
////            {
////                MifareClassic mifareClassic = MifareClassic.get(localTag);
////                Log.i("Main", "***getSectorCount: " + mifareClassic.getSectorCount());
////                Log.i("Main", "***getBlockCount: " + mifareClassic.getBlockCount());
////                Log.i("Main", "***getType: " + mifareClassic.getType());
////
////
////            } else {
////                Log.i("Main", "***null");
////            }
////        } else {
////            Log.i("Main", "*** is not TECH_DISCOVERED");
////        }
//
//
//
//        /**
//         *  反编译
//         */
////        String[] arrayOfString = tagFromIntent.getTechList();
////        ArrayList localArrayList = new ArrayList();
////        int i = arrayOfString.length;
////        for (int j = 0;; j++)
////        {
////            if (j >= i)
////            {
////                if (!localArrayList.contains("android.nfc.tech.IsoDep")) {
////                    break;
////                }
////                dealWithCpuCard(tagFromIntent);
////                return;
////            }
////            localArrayList.add(arrayOfString[j]);
////        }
//
//        /**
//         *  ndef
//         */
//
///*
//
//        Ndef ndef = Ndef.get(tagFromIntent);
//        //打开连接
//        try {
//            ndef.connect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //获取NDEF消息
//        NdefMessage message = null;
//        try {
//            message = ndef.getNdefMessage();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (FormatException e) {
//            e.printStackTrace();
//        }
//        //将消息转换成字节数组
//        byte[] data = message.toByteArray();
//        //将字节数组转换成字符串
//        String str = new String(data, Charset.forName("UTF-8"));
//
//        Log.i(TAG, "******str: " + str);
//        //关闭连接
//        try {
//            ndef.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//*/
//
//
//        /**
//         *  isoDep
//         */
//
//
//        byte[] aa = tagFromIntent.getId();
//        Log.i("Main", "***UID: " + bytesToHexString(aa));
//
//        IsoDep isoDep = IsoDep.get(tagFromIntent);
//        try {
//            isoDep.connect();
//            if (isoDep.isConnected()) {
//                Log.i("Main", "***isoDep is connected!");
//
//                Log.i("Main", "***tag: " + isoDep.getTag().toString());
//                Log.i("Main", "***getHistoricalBytes: " + bytesToHexString(isoDep.getHistoricalBytes()));
//                Log.i("Main", "***getHiLayerResponse: " + isoDep.getHiLayerResponse());
//                Log.i(TAG, "***max len: " + isoDep.getMaxTransceiveLength());
//                Log.i(TAG, "***timeout: " + isoDep.getTimeout());
//            }
//
//
//            byte[] SELECT = {
//                    (byte) 0x00, // CLA = 00 (first interindustry command set)
//                    (byte) 0xA4, // INS = A4 (SELECT)
//                    (byte) 0x04, // P1  = 04 (select file by DF name)
//                    (byte) 0x0C, // P2  = 0C (first or only file; no FCI)
//                    (byte) 0x06, // Lc  = 6  (data/AID has 6 bytes)
//                    (byte) 0x31, (byte) 0x35,(byte) 0x38,(byte) 0x34,(byte) 0x35,(byte) 0x46 // AID = 15845F
//            };
//
//
//            byte[] READ_BINARY = {
//                    (byte) 0x00, // CLA Class
//                    (byte) 0xB0, // INS Instruction
//                    (byte) 0x80, // P1  (indicate use of SFI)
//                    (byte) 0x01, // P2  (SFI = 0x01)
//                    (byte) 0x04  // LE  maximal number of bytes expected in result
//            };
//
//            Log.i("Main", "***read: " + isoDep.transceive(READ_BINARY));
//
//            byte[] result = isoDep.transceive(SELECT);
//            Log.i("Main", "SELECT: " + result);
//
////            if (!(result[0] == (byte) 0x90 && result[1] == (byte) 0x00))
////                throw new IOException("could not select application");
//
//            byte[] GET_STRING = {
//                    (byte) 0x00, // CLA Class
//                    (byte) 0xB0, // INS Instruction
//                    (byte) 0x00, // P1  Parameter 1
//                    (byte) 0x00, // P2  Parameter 2
//                    (byte) 0x04  // LE  maximal number of bytes expected in result
//            };
//
//            result = isoDep.transceive(GET_STRING);
//            Log.i("Main", "GET_STRING: " + bytesToHexString(result));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//
//
//        /**
//         *  nfcA
//         */
//        /*
//        try
//        {
//            NfcA nfcA = NfcA.get(tagFromIntent);
//            nfcA.connect();
//            Log.i("Main", "***tag: " + nfcA.getTag().toString());
//            Log.i("Main", "***atqa: " + bytesToHexString(nfcA.getAtqa()));
//            Log.i("Main", "***Sak: " + nfcA.getSak());
//            Log.i(TAG, "***max len: " + nfcA.getMaxTransceiveLength());
//            Log.i(TAG, "***timeout: " + nfcA.getTimeout());
//
//
//            byte[] cmd=new byte[]{0x02,0x60,0x08,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
//
//            Log.i(TAG, "***Card Number:"+ bytesToHexString(nfcA.transceive(cmd)));
//
//
//
//            byte[] SELECT = {
//                    (byte) 0x30,
//                    (byte) 0x05,
//            };
//
//
//            byte[] result = nfcA.transceive(SELECT);
//
//            Log.i("Main", "*****result:" + result.toString());
//            int data_len = ((result[0]&0x0f)<<8)+((result[1]&0xff));
//            Log.i("Main", "是否已写入数据"+result[0]+"，写入数据长度："+data_len);
//            byte[] buf_res = new byte[data_len/2+4];
//
//
//            if (result[0]!=0 && data_len!=0){
//                int count = data_len/2/64;
//                int i = 0;
//                for (i=0; i<count; i++){
////                      //读取数据
//                    byte[] DATA_READ = {
//                            (byte) 0x3A,
//                            (byte) (0x06+i*(64/4)),
//                            (byte) (0x06+(i+1)*(64/4))
////                              (byte) (5+data_len/8)
//                    };
//                    byte[] data_res = nfcA.transceive(DATA_READ);
//                    System.arraycopy(data_res, 0, buf_res, i*64, 64);
//                    Log.i("Main", "读卡成功");
//                }
//                if (((data_len/2)%(64))!=0){
//                    byte[]DATA_READ = {
//                            (byte) 0x3A,
//                            (byte) (0x06+i*(64/4)),
//                            (byte) (((0x06+i*(64/4))+(data_len/2/4)%(64/4))-1)
////                              (byte) (5+data_len/8)
//                    };
//                    byte[] data_res = nfcA.transceive(DATA_READ);
//                    System.arraycopy(data_res, 0, buf_res, i*64, (data_len/2)%64);
//                    Log.i("Main", "读卡成功2");
//                }
//                String res = gb2312ToString(buf_res);
//                Log.i("Main", "stringBytes:"+res);
//                textView.setText(res);
//            }
//
//
//        }catch(IOException e) {
//            e.printStackTrace();
//        }
//
//
//        */
//
//
//    }
//
//    private String gb2312ToString(byte[] data) {
//        String str = null;
//        try {
//            str = new String(data, "gb2312");//"utf-8"
//        } catch (UnsupportedEncodingException e) {
//        }
//        return str;
//    }
//
//    private void read(Tag tagFromIntent) {
//        Ndef ndef = Ndef.get(tagFromIntent);
//
//        try {
//            ndef.connect();
//            mNdefMessage = ndef.getNdefMessage();
//            NdefRecord[] records = mNdefMessage.getRecords();
//            byte[] payload = records[0].getPayload();
//            String displayString = getTextFromNdefRecord(records[0]);
//            textView.setText(displayString);
//            Toast.makeText(this, "String read", Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            Log.d(TAG, e.toString());
//        } finally {
//            try {
//                ndef.close();
//            } catch (Exception e) {
//                Log.d(TAG, e.toString());
//            }
//        }
//    }
//
//    public String getTextFromNdefRecord(NdefRecord ndefRecord)
//    {
//        String tagContent = null;
//        try {
//            byte[] payload = ndefRecord.getPayload();
//            String textEncoding = "UTF-8";
//            int languageSize = payload[0] & 0063;
//            tagContent = new String(payload, languageSize + 1,
//                    payload.length - languageSize - 1, textEncoding);
//        } catch (UnsupportedEncodingException e) {
//            Log.e("getTextFromNdefRecord", e.getMessage(), e);
//        }
//        return tagContent;
//    }
//
//
//    /**
//     *  反编译
//     */
//    private boolean IsS70 = false;
//    private String boatName;
//    private String data = "";
//    private String result = "有误";
//    private int type = 0;
//
//    public void dealWithCpuCard(Tag paramTag)
//    {
//        Log.i("Main", "dealWithCpuCard");
//
//
//        this.IsS70 = false;
//        IsoDep localIsoDep = IsoDep.get(paramTag);
//        byte[] arrayOfByte1 = new byte[2];
//        arrayOfByte1[1] = 5;
//        YzCard localYzCard = new YzCard(localIsoDep);
//        if (localYzCard.OpenCard())
//        {
//            localYzCard.SelectADF();
//            if (1 == 0) {}
//        }
//
//
//        try
//        {
//            byte[] arrayOfByte2 = localYzCard.ReadEF(arrayOfByte1, 0, 105);
//
//            Log.i(TAG, "***********array:" + arrayOfByte2.toString());
////            if (arrayOfByte2 == null)
////            {
////                Message localMessage2 = Message.obtain();
////                localMessage2.what = 22;
////                this.handler.sendMessage(localMessage2);
////                localYzCard.CloseCard();
////                return;
////            }
////            boolean bool = localYzCard.SelectADF(new byte[] { -33, 1 });
////            byte[] arrayOfByte3 = null;
////            if (bool) {
////                arrayOfByte3 = localYzCard.ReadEF(arrayOfByte1, 0, 48);
////            }
////            String str = "";
////            if (arrayOfByte3 != null) {
////                str = Common.getStringBySpecifiedByteLength(arrayOfByte3, 32, 16);
////            }
////            localYzCard.CloseCard();
////            processCpuInfo(arrayOfByte2, str);
//        }
//        catch (Exception localException)
//        {
////            for (;;)
////            {
////                Message localMessage1 = Message.obtain();
////                localMessage1.what = 3232;
////                this.handler.sendMessage(localMessage1);
////                localException.printStackTrace();
////            }
//        }
////        localYzCard.CloseCard();
//
//
//    }
//
//
//    private void processCpuInfo(byte[] paramArrayOfByte, String paramString)
//    {
//       Log.i("Main", "*****param: " + paramArrayOfByte.toString());
//    }
//
//}

