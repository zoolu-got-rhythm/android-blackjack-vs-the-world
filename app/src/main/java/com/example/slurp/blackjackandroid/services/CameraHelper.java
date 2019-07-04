package com.example.slurp.blackjackandroid.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

// singleton
public class CameraHelper {

    final private String TAG = "cameraHelper";

    private HandlerThread mBackgroundHandlerThread; // thread: put threads onto handler (message que?) via threads looper ref?
    private Handler mBackgroundHandler; // handler

    private ImageReader mImageReader;
    private CameraCharacteristics mCameraCharacteristics;
    private Semaphore mSemaphoreLock = new Semaphore(1);
    private CameraDevice mCameraDevice;
    private Surface mSurface;
    private String mCameraId;
    private CameraManager mCameraManager;
    private CameraCaptureSession mCameraCaptureSession;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray(4);
    private Context mContext;
    private int mDeviceRotation;

    private Boolean mIsClosed = true;
    private static volatile CameraHelper instance;

    private State mtakePicturePhaseState = State.PREVIEW;

    private enum State {
        PREVIEW,
        WAITING_LOCK,
        TAKEN;
    }

    public interface ImageHandler{
        public Runnable onImageReady(Image image);
    }

    private CameraHelper(CameraManager cameraManager, int deviceRotation) {
        this.mCameraManager = cameraManager;
        this.mCameraId = this.setUpCameraId(cameraManager);
        try {
            this.mCameraCharacteristics = this.mCameraManager.getCameraCharacteristics(this.mCameraId);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        this.mDeviceRotation = deviceRotation;

        this.ORIENTATIONS.append(Surface.ROTATION_0, 90);
        this.ORIENTATIONS.append(Surface.ROTATION_90, 0);
        this.ORIENTATIONS.append(Surface.ROTATION_180, 270);
        this.ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    // singleton pattern
    public synchronized static CameraHelper initInstance(CameraManager cameraManager, int deviceRotation) {
        if (instance != null)
            return instance;
        instance = new CameraHelper(cameraManager, deviceRotation);
        return instance;
    }

    private int getJpegOrientation(){
        int sensorOrientation = this.mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
//        this.mContext.getAc
        int surfaceRotation = this.ORIENTATIONS.get(this.mDeviceRotation);
        return (surfaceRotation + sensorOrientation + 270) % 360;
    };

    private @Nullable String setUpCameraId(CameraManager cameraManager){

        String foundCameraId = null;

        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                Integer cameraDirection = null; // not sure if this null check is needed and just a kotlin thing
                cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cameraDirection != null && cameraDirection == CameraCharacteristics.LENS_FACING_BACK) {
                    continue;
                }
                foundCameraId = cameraId;
                break;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return foundCameraId;
    };

    final private CameraCaptureSession.StateCallback mCameraCaptureSessionStateCallback =
            new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    // assign object to member ih class
                    // then call start preview
                    mSemaphoreLock.release();
                    if(mIsClosed)
                     return;
                    mCameraCaptureSession = cameraCaptureSession;
                    startPreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }
            };

    final private CameraCaptureSession.CaptureCallback mCameraCaptureSessionCaptureCallback =
            new CameraCaptureSession.CaptureCallback() {
                private void process(CaptureResult result){

//                    Log.i("af state int", result.get(CaptureResult.CONTROL_AF_STATE).toString());

                    // extra feature like autoFocus and AutoExosure can be checked and ran from here.
                    // beforeCaptureStillPicture is execute
                    // you must first check if device supports autoFocus and autoExposure though

                    switch (mtakePicturePhaseState){
                        case WAITING_LOCK: {
                            captureStillPicture();
                        }
                    }
                }

                @Override
                public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                    super.onCaptureProgressed(session, request, partialResult);
                    this.process(partialResult);
                }

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
//                    result.get(CaptureResult.CONTROL_AF_STATE);
                    this.process(result);

                }
            };

    final private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            mSemaphoreLock.release();
            Log.d(TAG, "camera device has been assigned");
            mIsClosed = false;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mSemaphoreLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            mIsClosed = true;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            mSemaphoreLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            mIsClosed = true;
        }
    };

    private void captureStillPicture(){
        mtakePicturePhaseState = State.TAKEN;
        try {
            // This is the CaptureRequest.Builder that we use to take a picture.
            CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
//            enableDefaultModes(builder)

            builder.set(
                    CaptureRequest.JPEG_ORIENTATION,
                    this.getJpegOrientation());

            builder.addTarget(mImageReader.getSurface());
            builder.addTarget(mSurface);
            mCameraCaptureSession.stopRepeating();
            mCameraCaptureSession.capture(
                    builder.build(),
                    new CameraCaptureSession.CaptureCallback() {
                        @Override
                        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                            super.onCaptureCompleted(session, request, result);
                            //my custom imageHandler ready callback will get called
                        }
                    },
                    mBackgroundHandler);

        } catch (CameraAccessException e) {
            Log.e(TAG, "captureStillPicture " + e);
        }
    }

    private void startPreview(){
        try {
            if(!mSemaphoreLock.tryAcquire(1L, TimeUnit.SECONDS)){
                return;
            }

////                    if(isClosed) return
            mtakePicturePhaseState = State.PREVIEW;

            CaptureRequest.Builder builder = createPreviewRequestBuilder();

            this.mCameraCaptureSession.setRepeatingRequest(
                    builder.build(), this.mCameraCaptureSessionCaptureCallback, this.mBackgroundHandler
            );


        } catch (IllegalStateException e1) {

        } catch (CameraAccessException e2) {

        } catch (InterruptedException e3) {

        } finally {
            this.mSemaphoreLock.release();
        }
    }

    private CaptureRequest.Builder createPreviewRequestBuilder() throws CameraAccessException {

        CaptureRequest.Builder builder = this.mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        builder.addTarget(this.mSurface);
        return builder;
    }

    public void open() throws CameraAccessException {
        try{
            try {
                if(!mSemaphoreLock.tryAcquire(3l, TimeUnit.SECONDS)){
                    throw new IllegalStateException("camera launch failed");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // if cameraDevice already opened no need to re-open
            if(mCameraDevice != null){
                mSemaphoreLock.release();
                return;
            }

            startBackgroundHandler();
            this.mCameraManager.openCamera(this.mCameraId, this.mCameraDeviceStateCallback, this.mBackgroundHandler);
        }catch (SecurityException e){
            Log.d(TAG, "open camera failed");
        }

        // cameraManager : openCamera();
    }

    /**
     * Start camera. Should be called after open() is successful
     */
    public void start(Surface surface) {
        this.mSurface = surface;

        try {
            if(!mSemaphoreLock.tryAcquire(3l, TimeUnit.SECONDS)){
                throw new IllegalStateException("attempting to create capture session on assigned cameraDevice reference");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // setup camera session

        StreamConfigurationMap map = this.mCameraCharacteristics.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//        if (map == null) {
////            continue;
//        }

        // For still image captures, we use the largest available size.
        Size largestSize = Collections.max(
                Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
                    @Override
                    public int compare(Size lhs, Size rhs) {
                        return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                                (long) rhs.getWidth() * rhs.getHeight());
                    }
                });

        this.mImageReader = ImageReader.newInstance(
                largestSize.getWidth(), largestSize.getHeight(), ImageFormat.JPEG, 1
        );

        try {
            Log.d(Boolean.toString(this.mCameraDevice == null), TAG);
            this.mCameraDevice.createCaptureSession(
                    Arrays.asList(surface, this.mImageReader.getSurface()),
                    this.mCameraCaptureSessionStateCallback,
                    this.mBackgroundHandler
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // by the time this is called by user from button event: takePicture, background handler should be setup -
    // and ready to recieve messages to run on it's looper
    // start sequence to run all steps required to take picture: focus, exposure, capture
    public void takePicture(final ImageHandler imageHandler){
        this.mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {

                // return image to activity through callback

                Image image = imageReader.acquireNextImage();

                mBackgroundHandler.post(imageHandler.onImageReady(image));
            }
        }, this.mBackgroundHandler);
        this.lockFocus();
    }

    private void setImageOutputOrientation(){

    }

    private void lockFocus(){

        this.mtakePicturePhaseState = State.WAITING_LOCK;

        CaptureRequest.Builder builder = null;
        try {
            builder = createPreviewRequestBuilder();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // maybe get rid of this if statement here, not sure if i need it
        if(!mCameraCharacteristics.getAvailableCaptureRequestKeys().contains(CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)) {
            // If continuous AF is not supported , start AF here
            builder.set(
                    CaptureRequest.CONTROL_AF_TRIGGER,
                    CaptureRequest.CONTROL_AF_TRIGGER_START
            );
        }

        try {
            mCameraCaptureSession.capture(builder.build(), mCameraCaptureSessionCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }



    private void startBackgroundHandler(){

        if(mBackgroundHandlerThread != null)
            return;

        mBackgroundHandlerThread = new HandlerThread("camera" + mCameraId + "-tasks");
        mBackgroundHandlerThread.start();
        Looper looperRef = mBackgroundHandlerThread.getLooper();
        mBackgroundHandler = new Handler(looperRef);
    }

    private void stopBackgroundHandler() {
        if(mBackgroundHandlerThread == null)
            return;

        mBackgroundHandlerThread.quitSafely();
        try {
            // TODO: investigate why thread does not end when join is called
            mBackgroundHandlerThread.join();
            mBackgroundHandlerThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            Log.e(TAG, "===== stop background error" + e);
        }
    }



    public void close(){
        // release all camera related resources, this should be called from onDestroy?
        // but definitely called from onPause();
        this.stopBackgroundHandler();
    }
}
