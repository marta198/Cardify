package com.application.cardify;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Beam extends AppCompatActivity {
    public static final String Error_Detected = "No NFC Tag Detected";
    public static final String Write_Success = "Text Broadcasted Successfully";
    public static final String Write_Error = "Error during broadcast, Try Again!";

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] intentFiltersArray;
    String[][] techListsArray;
    Context context;
    TextView nfc_contents;
    Button BroadcastButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam);
        nfc_contents = findViewById(R.id.nfc_contents);
        BroadcastButton = findViewById(R.id.ActivateButton);
        context = this;

        BroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (nfcAdapter == null) {
                        Toast.makeText(context, Error_Detected, Toast.LENGTH_LONG).show();
                    } else {
                        // Create an NDEF message and set it as the NFC message to be broadcasted
                        NdefMessage message = createNdefMessage("PlainText|" + "Your Message Here");
                        nfcAdapter.setNdefPushMessage(message, Beam.this);
                        Toast.makeText(context, Write_Success, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, Write_Error, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }

        // Define NFC intent filters and tech lists
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Failed to add MIME type.");
        }
        intentFiltersArray = new IntentFilter[]{ndef};
        techListsArray = new String[][]{new String[]{Ndef.class.getName()}};
    }

    NfcAdapter.CreateNdefMessageCallback ndefCallback = new NfcAdapter.CreateNdefMessageCallback() {
        @Override
        public NdefMessage createNdefMessage(NfcEvent event) {
            // Create and return the NDEF message here
            String text = "Your message here";
            NdefRecord record = NdefRecord.createTextRecord(null, text);
            return new NdefMessage(new NdefRecord[]{record});
        }
    };


    // Create an NDEF message
    private NdefMessage createNdefMessage(String text) {
        NdefRecord record = NdefRecord.createMime("application/com.application.cardify", text.getBytes());
        return new NdefMessage(new NdefRecord[]{record});
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleNfcIntent(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }

    private void handleNfcIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                nfc_contents.setText("");
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    nfc_contents.append(new String(msgs[i].getRecords()[0].getPayload()));
                }
            }
        }
    }
}
