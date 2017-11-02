package cooktopper.cooktopperapp;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.w3c.dom.Text;

import java.io.IOException;

import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.presenter.StovePresenter;
import cooktopper.cooktopperapp.requests.GetRequest;
import cooktopper.cooktopperapp.requests.PostRequest;
import cooktopper.cooktopperapp.util.WifiUtil;

public class QrCodeActivity extends AppCompatActivity {

    private SurfaceView cameraPreview;
    private TextView cameraDescription;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private final int REQUESTCAMERAPERMISSIONID = 1001;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUESTCAMERAPERMISSIONID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        cameraDescription = (TextView) findViewById(R.id.camera_title);
        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1024, 768)
                .build();
        createCameraEvent();
        createBarcodeDetector();
    }

    private void createCameraEvent() {
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(QrCodeActivity.this,
                            new String[]{Manifest.permission.CAMERA},REQUESTCAMERAPERMISSIONID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });
    }

    private void createBarcodeDetector() {
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0)
                {
                    cameraDescription.post(new Runnable() {
                        @Override
                        public void run() {
                            //Create vibrate
                            Vibrator vibrator = (Vibrator)getApplicationContext()
                                    .getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(10);
                            cameraDescription.setText(qrcodes.valueAt(0).displayValue);
                            Toast.makeText(getApplicationContext(),qrcodes.valueAt(0).displayValue, Toast.LENGTH_SHORT).show();

                            WifiUtil deviceMacAddress = new WifiUtil();
                            deviceMacAddress.macAddress = deviceMacAddress.getMacAddress(getApplicationContext());

                            Log.d("MAC_ADDRESS: ", String.valueOf(deviceMacAddress.macAddress));

                            StovePresenter stovePresenter = new StovePresenter(getApplicationContext());
                            Stove stove = stovePresenter.getStoveByToken(qrcodes.valueAt(0).displayValue);

                            if (stove == null) {
                                Log.d("STOVE is >>>", " NULL");
                                Toast.makeText(getApplicationContext(), "Invalid QRCode", Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }

                            Log.d("WILL SET MAC: ", deviceMacAddress.macAddress);

                            stove.setMobileMacAddress(deviceMacAddress.macAddress);
                            stovePresenter.updateStoveFields(stove);

                            finish();
                            return;
                        }
                    });
                }
            }
        });
    }
}
