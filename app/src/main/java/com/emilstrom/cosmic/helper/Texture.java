package com.emilstrom.cosmic.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.emilstrom.cosmic.MainActivity;
import com.emilstrom.cosmic.R;

/**
 * Created by Emil on 2014-07-31.
 */
public class Texture {
	public static final Texture blankTexture = new Texture(R.drawable.blank);

	//HANDLES

	int handle_texture;

	//
	public Bitmap bitmapBuffer = null;

	public int srcWidth, srcHeight, texWidth, texHeight;
	public float scaleWidth=1f, scaleHeight=1f;

	public Texture(int resourceID) {
		Bitmap b = loadBitmapFromResource(resourceID);
		b = setScaledBitmap(b);

		bitmapBuffer = b;
	}

	public static Bitmap loadBitmapFromResource(int id) {
		Bitmap b = null;

		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;

			b = BitmapFactory.decodeResource(MainActivity.context.getResources(), id, options);
		} catch(Exception e) {
			Log.v(MainActivity.TAG, "Couldn't load resource...");
			Log.v(MainActivity.TAG, e.toString());
		}

		return b;
	}

	public synchronized Bitmap setScaledBitmap(Bitmap b) {
		Bitmap sb = null;

		try {
			srcWidth = b.getWidth();
			srcHeight = b.getHeight();

			float exp = (float) (Math.log(Math.max(srcWidth, srcHeight)) / Math.log(2));
			exp = (float) Math.ceil(exp);

			sb = Bitmap.createScaledBitmap(b, (int) Math.pow(2, exp), (int) Math.pow(2, exp), false);

			texWidth = sb.getWidth();
			texHeight = sb.getHeight();

			generateScaleValues();

			if (sb != b) {
				b.recycle();
				b = null;
			}

			Thread.sleep(5);
		} catch(Exception e) {
			Log.v(MainActivity.TAG, "Couldnt scale picture");
			Log.v(MainActivity.TAG, e.toString());
		}

		return sb;
	}

	public void textureLoaded(Bitmap bmp) {
		final int[] tex = new int[1];

		GLES20.glGenTextures(1, tex, 0);

		try {
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]);

			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

			bmp.recycle();
			bmp = null;
		} catch(Exception e) {
			Log.v(MainActivity.TAG, "Couldnt upload bitmap data to GL");
			Log.v(MainActivity.TAG, e.toString());
		}

		handle_texture = tex[0];
	}

	public void generateScaleValues() {
		float w, h;

		if (srcWidth > srcHeight) {
			w = 1f;
			h = (float) srcHeight / srcWidth;
		} else {
			h = 1f;
			w = (float) srcWidth / srcHeight;
		}

		scaleWidth = w;
		scaleHeight = h;
	}

	public void uploadToGL() {
		if (bitmapBuffer != null) {
			textureLoaded(bitmapBuffer);
			bitmapBuffer = null;
		}

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handle_texture);
	}
}
