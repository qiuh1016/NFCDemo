package com.cetcme.nfcdemo;

import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.justimesoftware.cpucardlib.YzCard;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private TextView showText;
    private Button button;

    private IntentFilter[] mIntentFiltersArray;
    private String[][] mTechListsArray;
    private PendingIntent mPendingIntent;
    private NfcAdapter mNFCAdapter;
    private YzCard mYzCard;

    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitNfc();

        showText = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(showText.getText());
                Toast.makeText(getApplicationContext(), "复制成功!", Toast.LENGTH_LONG).show();
            }
        });
        button.setVisibility(View.INVISIBLE);
    }

    private void readBoatBaseInfo(final Tag tagFromIntent) {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                // 卡的MF文件,文件标识为 3F00,打开卡的时候默认选中.
                mYzCard = new YzCard(IsoDep.get(tagFromIntent));
                byte[] efid = { 0x00, 0x05 }; // 读取的EF的文件标识
                // 根据卡结构文档,船舶法定主体数据在MF目录中,文件efid为05
                mYzCard.OpenCard();   //打开卡.
                final byte[] data = mYzCard.ReadEF(efid, 0, 105);  //读取EF

                final String boatInfo = getStringBySpecifiedByteLength(data, 0, 105);
                Log.i("Main", "boatInfo: " + boatInfo);

                new Thread() {
                    public void run() {
                        //耗时操作，完成之后更新UI；
                        runOnUiThread(new Runnable(){

                            @Override
                            public void run() {
                                //更新UI
                                info = "卡标识符：" + getStringBySpecifiedByteLength(data, 0, 16) + "\n";
                                info += "渔船编码：" + getStringBySpecifiedByteLength(data, 16,16) + "\n";
                                info += "船长：" + getStringBySpecifiedByteLength(data, 32,6) + "\n";
                                info += "上甲板长度：" + getStringBySpecifiedByteLength(data, 38,6) + "\n";
                                info += "船宽：" + getStringBySpecifiedByteLength(data, 44,5) + "\n";
                                info += "船深：" + getStringBySpecifiedByteLength(data, 49,5) + "\n";
                                info += "总吨位：" + getStringBySpecifiedByteLength(data, 54,5) + "\n";
                                info += "船体材质：" + getStringBySpecifiedByteLength(data, 59,10) + "\n";
                                info += "建成日期：" + getStringBySpecifiedByteLength(data, 69,8) + "\n";
                                info += "船舶类型：" + getStringBySpecifiedByteLength(data, 77,10) + "\n";
                                info += "核定航区：" + getStringBySpecifiedByteLength(data, 87,4) + "\n";
                                info += "制卡日期：" + getStringBySpecifiedByteLength(data, 91,8) + "\n";
                                info += "制卡单位：" + getStringBySpecifiedByteLength(data, 99,6);
                                showText.setText(info);
                            }

                        });
                    }
                }.start();

                return boatInfo;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
//                showText.setText(result);
                button.setVisibility(View.VISIBLE);
                if(TextUtils.isEmpty(result)){
                    Toast.makeText(MainActivity.this, "获取失败!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "获取成功!", Toast.LENGTH_SHORT).show();
                }
            }


        }.execute();

    }

    /**
     * 转换对应字节为字符串
     * @param bytes  源数据
     * @param offset 偏移
     * @param length 长度
     * @return
     * @author HH
     * @date 2015年11月12日 上午10:09:22
     */
    public static String getStringBySpecifiedByteLength(byte[] bytes, int offset, int length) {
        String str = null;
        byte[] mbyte = new byte[length];
        int j = 0;
        for (int i = offset; i < offset + length; i++) {
            mbyte[j] = bytes[i];
            j++;
        }
        str = ByteToString(mbyte);
        return str;

    }

    public static final String ByteToString(byte[] data) {
        String dataStr = null;
        try {
            dataStr = new String(data, "GB2312");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        if (dataStr.indexOf('\0') > -1)
            dataStr = dataStr.substring(0, dataStr.indexOf('\0')).trim();
        return dataStr;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (checkCardIsCpuCard(mTag)) {
            readBoatBaseInfo (mTag);
        } else {
            Toast.makeText(MainActivity.this, "非CPU卡!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检查卡是否为CPU卡
     * @param tagFromIntent
     * @return
     * @author HH
     * @date 2015年11月12日 上午9:44:28
     */
    private boolean checkCardIsCpuCard(Tag tagFromIntent) {

        String[] tList = tagFromIntent.getTechList();
        ArrayList<String> AyList = new ArrayList<>();
        Collections.addAll(AyList, tList);

        return AyList.contains("android.nfc.tech.IsoDep");

    }

    private void InitNfc() {

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

        mIntentFiltersArray = new IntentFilter[] { ndef, };
        mTechListsArray = new String[][] { new String[] { IsoDep.class.getName() },
                new String[] { NfcA.class.getName() }, new String[] { MifareClassic.class.getName() } };
        // 获取默认的NFC控制器
        mNFCAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNFCAdapter == null) {
            Toast.makeText(MainActivity.this, "设备不支持NFC功能!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 添加前台发布
     * */
    @Override
    protected void onResume() {
        super.onResume();
        if (mNFCAdapter != null)
            mNFCAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFiltersArray, mTechListsArray);
    }

    /**
     * 取消前台发布
     * */
    @Override
    public void onPause() {
        super.onPause();
        if (mNFCAdapter != null)
            mNFCAdapter.disableForegroundDispatch(this);
    }

}
