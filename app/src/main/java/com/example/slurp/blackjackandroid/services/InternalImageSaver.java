package com.example.slurp.blackjackandroid.services;

import android.content.Context;
import android.media.Image;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class InternalImageSaver implements Runnable {

    /**
     * The JPEG image
     */

    public interface InternalImageSaverListener{
        void onFinishedSaving(boolean wasSavedSuccessfully);
    }

    private InternalImageSaverListener mInternalImageSaverListener;
    private final String TAG = "InternalImageSaver";
    private Image mImage;

    /**
     * The file we save the image into.
     */
    private File mFile;

    private Context mContext;

    public InternalImageSaver(Context context, Image image, File file,
                              InternalImageSaverListener internalImageSaverListener) {
        this.mImage = image;
        this.mFile = file;
        this.mContext = context;
        this.mInternalImageSaverListener = internalImageSaverListener;
    }

    @Override
    public void run() {
        // write bytes of image to file
        FileOutputStream outputStream = null;

        ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        Log.d(TAG, "attempting to save image to path: " + mFile.getAbsolutePath());

        try {
            outputStream = this.mContext.openFileOutput(mFile.getName(), Context.MODE_PRIVATE);
            outputStream.write(bytes);
            outputStream.close();
            this.mInternalImageSaverListener.onFinishedSaving(true);

        } catch (Exception e) {
            e.printStackTrace();
            this.mInternalImageSaverListener.onFinishedSaving(false);
        }finally {
            mImage.close();
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

