package com.application.cardify;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.nio.charset.StandardCharsets;

public class QRCodeFullscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_fullscreen);

        ImageView qrCodeImageView = findViewById(R.id.qr_code_fullscreen);
        String qrCodeData = getIntent().getStringExtra("qrCodeData");

        if (qrCodeData != null) {
            try {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                int qrCodeSize = 800;

                BitMatrix bitMatrix = multiFormatWriter.encode(
                        new String(qrCodeData.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1),
                        BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);

                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qrCodeImageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        qrCodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
