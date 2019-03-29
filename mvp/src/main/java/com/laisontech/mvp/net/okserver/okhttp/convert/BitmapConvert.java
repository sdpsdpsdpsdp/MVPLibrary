package com.laisontech.mvp.net.okserver.okhttp.convert;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 描    述：字符串的转换器
 */
public class BitmapConvert implements Converter<Bitmap> {

    private int maxWidth;
    private int maxHeight;
    private Bitmap.Config decodeConfig;
    private ImageView.ScaleType scaleType;

    public BitmapConvert() {
        this(1000, 1000, Bitmap.Config.ARGB_8888, ImageView.ScaleType.CENTER_INSIDE);
    }

    public BitmapConvert(int maxWidth, int maxHeight) {
        this(maxWidth, maxHeight, Bitmap.Config.ARGB_8888, ImageView.ScaleType.CENTER_INSIDE);
    }

    public BitmapConvert(int maxWidth, int maxHeight, Bitmap.Config decodeConfig, ImageView.ScaleType scaleType) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.decodeConfig = decodeConfig;
        this.scaleType = scaleType;
    }

    @Override
    public Bitmap convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return null;
        return parse(body.bytes());
    }

    private Bitmap parse(byte[] byteArray) throws OutOfMemoryError {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap;
        if (maxWidth == 0 && maxHeight == 0) {
            decodeOptions.inPreferredConfig = decodeConfig;
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);
        } else {
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;

            int desiredWidth = getResizedDimension(maxWidth, maxHeight, actualWidth, actualHeight, scaleType);
            int desiredHeight = getResizedDimension(maxHeight, maxWidth, actualHeight, actualWidth, scaleType);

            decodeOptions.inJustDecodeBounds = false;
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);

            if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }
        }
        return bitmap;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary, ImageView.ScaleType scaleType) {

        // If no dominant value at all, just return the actual.
        if ((maxPrimary == 0) && (maxSecondary == 0)) {
            return actualPrimary;
        }

        // If ScaleType.FIT_XY fill the whole rectangle, ignore ratio.
        if (scaleType == ImageView.ScaleType.FIT_XY) {
            if (maxPrimary == 0) {
                return actualPrimary;
            } else {
                return maxPrimary;
            }
        }

        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;

        // If ScaleType.CENTER_CROP fill the whole rectangle, preserve aspect ratio.
        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            if ((resized * ratio) < maxSecondary) {
                resized = (int) (maxSecondary / ratio);
            }
            return resized;
        }

        if ((resized * ratio) > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    private static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }
        return (int) n;
    }
}
