package com.example.oleg.weatherapp_demo.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class BitmapTransforamationHelper {
    public static Bitmap transformWithSavedProportions(Bitmap originalImage, int width, int height){

        Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        float originalWidth = originalImage.getWidth();
        float originalHeight = originalImage.getHeight();

        Canvas canvas = new Canvas(background);

        float scale = 0.0f;
        float xTranslation = 0.0f;
        float yTranslation = 0.0f;

        if (height > width) {
            // port
            scale = height / originalHeight;
            xTranslation = (width - originalWidth * scale) / 2.0f;
            yTranslation = 0.0f;
        } else {
            // land
            scale = width / originalWidth;
            xTranslation = 0.0f;
            yTranslation = (height - originalHeight * scale) / 2.0f;
        }

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(originalImage, transformation, paint);
        return background;
    }
}
