package pe.edu.usat.laboratorio.appcomercial.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Gallery {

    public static Bitmap rotateImage(Context context, Uri uri, int orientation) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }

    public static int getOrientation(Context context, Uri uri) {

        int rotate = 0;

        try {

            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");

            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            FileInputStream input = new FileInputStream(fileDescriptor);

            File tempFile = File.createTempFile("exif", "tmp");

            String tempFilename = tempFile.getPath();

            FileOutputStream output = new FileOutputStream(tempFile.getPath());

            int read;

            byte[] bytes = new byte[4096];

            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }

            input.close();
            output.close();

            ExifInterface exif = new ExifInterface(tempFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

        } catch (Exception e) {
            Log.i("TAG orientación", e.getLocalizedMessage());
        }

        return rotate;
    }

    public static Bitmap compress(Bitmap yourBitmap){
        //converted into webp into lowest quality
        Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, (int)(yourBitmap.getWidth()*0.8), (int)(yourBitmap.getHeight()*0.8), true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG,10,stream);//0=lowest, 100=highest quality
        byte[] byteArray = stream.toByteArray();


        //convert your byteArray into bitmap
        Bitmap yourCompressBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        return yourCompressBitmap;
    }
}
