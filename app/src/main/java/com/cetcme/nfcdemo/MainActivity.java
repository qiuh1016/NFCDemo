package com.cetcme.nfcdemo;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private String readResult;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            textView.setText("设备不支持NFC！");
            finish();
            return;
        }
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            textView.setText("请在系统设置中先启功NFC功能！");
            finish();
            return;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            readFromTag(getIntent());
        }
    }

    private boolean readFromTag(Intent intent){
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage)rawArray[0];
        NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
        try {
            if(mNdefRecord != null){
                readResult = new String(mNdefRecord.getPayload(),"UTF-8");
                textView.setText("读取到内容：\n" + readResult);
                return true;
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
