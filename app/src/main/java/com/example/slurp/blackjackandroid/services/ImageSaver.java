package com.example.slurp.blackjackandroid.services;

import android.media.Image;

import java.io.File;

public class ImageSaver implements Runnable {

    /**
     * The JPEG image
     */

    private Image mImage;

    /**
     * The file we save the image into.
     */
    private File mFile;

    @Override
    public void run() {

//        val buffer = image.planes[0].buffer
//        val bytes = ByteArray(buffer.remaining())
//        buffer.get(bytes)
//        var output: FileOutputStream? = null
//        try {
//            output = FileOutputStream(file).apply {
//                write(bytes)
//            }
//        } catch (e: Throwable) {
//            Log.e(TAG, e.toString())
//        } finally {
//            image.close()
//            output?.let {
//                try {
//                    it.close()
//                } catch (e: IOException) {
//                    Log.e(TAG, e.toString())
//                }
//            }
//        }

    }
}
