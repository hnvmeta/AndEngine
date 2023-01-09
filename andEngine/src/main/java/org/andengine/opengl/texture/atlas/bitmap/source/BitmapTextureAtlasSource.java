package org.andengine.opengl.texture.atlas.bitmap.source;


import android.graphics.Bitmap;

import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;

public class BitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource{
    private final Bitmap mBitmap;

    public BitmapTextureAtlasSource(Bitmap pBitmap){
        super(0,0, pBitmap.getWidth(), pBitmap.getHeight());
        mBitmap = pBitmap;
    }

    @Override
    public IBitmapTextureAtlasSource deepCopy() {
        return new BitmapTextureAtlasSource(mBitmap.copy(mBitmap.getConfig(), false));
    }

    @Override
    public Bitmap onLoadBitmap(Bitmap.Config pBitmapConfig) {
        return mBitmap.copy(pBitmapConfig, false);
    }

    @Override
    public Bitmap onLoadBitmap(Bitmap.Config pBitmapConfig, boolean pMutable) {
        return mBitmap.copy(pBitmapConfig, pMutable);
    }
}
