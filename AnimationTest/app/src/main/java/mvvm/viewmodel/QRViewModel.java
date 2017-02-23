package mvvm.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityQrBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * Created by lucas on 2017/2/21.
 */

public class QRViewModel {
    private ActivityQrBinding binding;
    private Context mContext;

    public QRViewModel(Context mContext, ActivityQrBinding binding) {
        this.mContext = mContext;
        this.binding = binding;
    }

    public void onCreate() {
        initListener();
    }

    private void initListener() {
        binding.ivQr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                analysisQRCode(((BitmapDrawable) binding.ivQr.getDrawable()).getBitmap());
                return true;
            }
        });
    }

    private void analysisQRCode(Bitmap bitmap) {

        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];

        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels);
        BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(source));
        asyncAnalysis(bb, hints);
    }

    /**
     * 识别二维码
     *
     * @param bb
     * @param hints
     */
    private void asyncAnalysis(final BinaryBitmap bb, final Hashtable<DecodeHintType, Object> hints) {
        AsyncTask.execute(new Runnable() {
                              @Override
                              public void run() {
                                  Result result = null;
                                  QRCodeReader reader = new QRCodeReader();
                                  try {
                                      result = reader.decode(bb, hints);
                                  } catch (NotFoundException | ChecksumException | FormatException e) {
                                      e.printStackTrace();
                                  }
                                  if (null != result) {
                                      Snackbar.make(binding.rlContent, result.getText(), Snackbar.LENGTH_SHORT).show();
                                  }
                              }
                          }

        );
    }

    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                if (ContextCompat.checkSelfPermission(mContext, "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{"android.permission.CAMERA"}, 1);
                    break;
                }
                intentToCaptureActivity();
                break;
            case R.id.btn_make:
                makeQR(400, 400);
                break;
        }
    }

    private void intentToCaptureActivity() {
        IntentIntegrator integrator = new IntentIntegrator((Activity) mContext);
        integrator.setCaptureLayout(R.layout.zxing_capture);
        integrator.setPrompt("place the qr code for more detail");
        integrator.initiateScan();
    }

    /**
     * 创建二维码
     *
     * @param width
     * @param height
     */
    private void makeQR(int width, int height) {
        try {
            String content = binding.etQrContent.getText().toString();
            if (content == null || content.trim().equals("")) {
                return;
            }

            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * height + x] = 0xff000000;
                    } else {
                        pixels[y * height + x] = 0xffffffff;
                    }
                }
            }
            Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            binding.ivQr.setImageBitmap(mBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionResult(@NonNull int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(mContext, "权限被禁止", Toast.LENGTH_SHORT);
                return;
            }
        }
        intentToCaptureActivity();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            Snackbar.make(binding.rlContent, intentResult.getContents(), Snackbar.LENGTH_SHORT).show();
        }
    }
}
