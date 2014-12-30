package com.chteuchteu.blurify.ast;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.chteuchteu.blurify.R;
import com.chteuchteu.blurify.hlpr.BitmapUtil;
import com.chteuchteu.blurify.hlpr.BlurUtil;
import com.enrique.stackblur.StackBlurManager;

public class BlurBackgroundBitmap extends AsyncTask<Void, Integer, Void> {
	private Bitmap b2;
	private Bitmap tmp_original_bitmap;
	private Activity activity;
	private int dominantColor;

	public BlurBackgroundBitmap(Activity activity, Bitmap tmp_original_bitmap) {
		this.activity = activity;
		this.tmp_original_bitmap = tmp_original_bitmap;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Bitmap b = null;
		try {
			StackBlurManager stackBlurManager = new StackBlurManager(tmp_original_bitmap);
			stackBlurManager.process(10);
			b = stackBlurManager.returnBlurredImage();
		} catch (Exception ex) {
			try {
				b = BlurUtil.fastBlur(tmp_original_bitmap, 10);
			} catch (Exception ex2) { ex2.printStackTrace(); }
		}
		if (b != null) {
			b2 = Bitmap.createScaledBitmap(b, b.getWidth()/2, b.getHeight()/2, false);

			// Find dominant color
			this.dominantColor = BitmapUtil.getDominantColor(b2);

			b.recycle();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		AlphaAnimation a = new AlphaAnimation(0.0f, 1.0f);
		a.setDuration(1000);
		ImageView blurryBackground = (ImageView) activity.findViewById(R.id.blurryBackground);
		blurryBackground.setImageBitmap(b2);
		blurryBackground.startAnimation(a);
		blurryBackground.setVisibility(View.VISIBLE);

		View darkMask = activity.findViewById(R.id.blurryBackground_darkMask);
		darkMask.setBackgroundColor(dominantColor);
		darkMask.setAlpha(0.5f);
		darkMask.setVisibility(View.VISIBLE);
	}
}
