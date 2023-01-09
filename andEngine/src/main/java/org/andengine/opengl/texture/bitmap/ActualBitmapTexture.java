package org.andengine.opengl.texture.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.util.GLState;
import org.andengine.util.exception.NullBitmapException;
import org.andengine.util.math.MathUtils;

import java.io.IOException;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:16:25 - 30.07.2011
 */
public class ActualBitmapTexture extends Texture {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private final int mWidth;
    private final int mHeight;
    private final BitmapTextureFormat mBitmapTextureFormat;
    private Bitmap mBitmap;
    // ===========================================================
    // Constructors
    // ===========================================================

    public ActualBitmapTexture(final TextureManager pTextureManager, final Bitmap bitmap) {
        super(pTextureManager, BitmapTextureFormat.RGBA_8888.getPixelFormat(), TextureOptions.DEFAULT, null);
        this.mBitmap = bitmap;
        this.mBitmapTextureFormat = BitmapTextureFormat.RGBA_8888;

        this.mWidth = bitmap.getWidth();
        this.mHeight = bitmap.getHeight();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public int getWidth() {
        return this.mWidth;
    }

    @Override
    public int getHeight() {
        return this.mHeight;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void writeTextureToHardware(final GLState pGLState) throws IOException {
        final Config bitmapConfig = this.mBitmapTextureFormat.getBitmapConfig();
        final Bitmap bitmap = this.onGetBitmap(bitmapConfig);
        try {
            if (bitmap == null) {
                throw new NullBitmapException("Caused by: '" + this.toString() + "'.");
            }

            if (bitmap.isRecycled()) {
                throw new IOException("Bitmap is recycled!!!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        final boolean useDefaultAlignment = MathUtils.isPowerOfTwo(bitmap.getWidth()) && MathUtils.isPowerOfTwo(bitmap.getHeight()) && (this.mPixelFormat == PixelFormat.RGBA_8888);
        if (!useDefaultAlignment) {
            /* Adjust unpack alignment. */
            GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
        }

        final boolean preMultipyAlpha = this.mTextureOptions.mPreMultiplyAlpha;
        if (preMultipyAlpha) {
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        } else {
            pGLState.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0, this.mPixelFormat);
        }

        if (!useDefaultAlignment) {
            /* Restore default unpack alignment. */
            GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, GLState.GL_UNPACK_ALIGNMENT_DEFAULT);
        }

        bitmap.recycle();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    protected Bitmap onGetBitmap(final Config pBitmapConfig) {
        return mBitmap;
    }

    @Override
    public void unload() {
        super.unload();
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }

        mBitmap = null;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
