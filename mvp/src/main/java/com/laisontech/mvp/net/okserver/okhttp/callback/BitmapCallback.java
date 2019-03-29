package com.laisontech.mvp.net.okserver.okhttp.callback;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.laisontech.mvp.net.okserver.okhttp.convert.BitmapConvert;

import okhttp3.Response;

/**
 * 描    述：返回图片的Bitmap，这里没有进行图片的缩放，可能会发生 OOM
 */
public abstract class BitmapCallback extends AbsCallback<Bitmap> {

    private BitmapConvert convert;

    public BitmapCallback() {
        convert = new BitmapConvert();
    }

    public BitmapCallback(int maxWidth, int maxHeight) {
        convert = new BitmapConvert(maxWidth, maxHeight);
    }

    public BitmapCallback(int maxWidth, int maxHeight, Bitmap.Config decodeConfig, ImageView.ScaleType scaleType) {
        convert = new BitmapConvert(maxWidth, maxHeight, decodeConfig, scaleType);
    }

    @Override
    public Bitmap convertResponse(Response response) throws Throwable {
        Bitmap bitmap = convert.convertResponse(response);
        response.close();
        return bitmap;
    }
}
