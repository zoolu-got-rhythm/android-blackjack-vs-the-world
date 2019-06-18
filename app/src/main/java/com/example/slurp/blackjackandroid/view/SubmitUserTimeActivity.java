package com.example.slurp.blackjackandroid.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.services.CameraHelper;

import java.io.File;

public class SubmitUserTimeActivity extends AppCompatActivity {

    private CameraHelper mCameraHelper;
    private TextureView mTextureView;
    private Button mTakePhotoButton;

    private final static int MY_PERMISSIONS_REQUEST_ACCESS_CAMERA = 1;

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_ACCESS_CAMERA);
    }

    private TextureView.SurfaceTextureListener mSurfaceTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {

//                    openCamera(0, 0);
                    // see if permission already obtained if not, request it
                    //open camera
                    //else{ request permission };
                    requestCameraPermission();

                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_user_time);


        final LinearLayout hintsLayoutParent = findViewById(R.id.title_container);
        final MenuHintView menuHintView = new MenuHintView(getApplicationContext(), "give us a smile");
        hintsLayoutParent.addView(menuHintView);

        this.mTextureView = findViewById(R.id.textureView);
        this.mTextureView.setSurfaceTextureListener(this.mSurfaceTextureListener);

        this.mTakePhotoButton = findViewById(R.id.takePictureButton);
        this.mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }


        File file = new File(this.getExternalFilesDir(null), "timestamp");
        CameraManager cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);


        this.mCameraHelper = CameraHelper.initInstance(cameraManager);

    }

    public void takePhoto(){
        this.mCameraHelper.takePicture(new CameraHelper.ImageHandler() {
            @Override
            public Runnable onImageReady(Image image) {
                return null;
                //proccess image and store it
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    this.openCamera(0, 0);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        try {
//            this.mCameraHelper.open();
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
    }

    private void openCamera(int width, int height){
        try {
            this.mCameraHelper.open();
            SurfaceTexture surfaceTexture = this.mTextureView.getSurfaceTexture();
            this.mCameraHelper.start(new Surface(surfaceTexture));
        }catch (CameraAccessException e){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mCameraHelper.close();
    }
}