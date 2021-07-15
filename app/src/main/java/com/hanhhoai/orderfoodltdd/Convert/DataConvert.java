package com.hanhhoai.orderfoodltdd.Convert;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataConvert {
    Context context;
    public DataConvert(Context context)
    {
        context = context;
    }
    public static byte [] ConvertImages (Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,0,stream);
        return stream.toByteArray();
    }

    public static byte [] ConvertImagesPNG (Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,stream);
        return stream.toByteArray();
    }

    public static Bitmap ConvertBitmap (byte [] arr){
        return BitmapFactory.decodeByteArray(arr,0,arr.length);
    }
    public  Bitmap convertStringToBitmapFromAccess(String filename) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
