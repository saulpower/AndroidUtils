package com.moki.touch.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;

public class ImageTool {

	public static StateListDrawable getStates(BitmapDrawable normal, BitmapDrawable selected) {
		
		StateListDrawable states = new StateListDrawable();
		states.addState(new int[] {android.R.attr.state_pressed}, selected);
		states.addState(new int[] {android.R.attr.state_focused}, selected);
		states.addState(new int[] {android.R.attr.state_selected}, selected);
		states.addState(new int[] {android.R.attr.state_activated}, selected);
		states.addState(new int[] { }, normal);
		
		return states;
	}
	
	public static Bitmap setTint(Bitmap sourceBitmap, int color) {

		try {
			
		    Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);            
	
		    Canvas canvas = new Canvas(resultBitmap);
		    
		    float r = ((color >> 16) & 0xFF) / 255.0f;
		    float g = ((color >> 8) & 0xFF) / 255.0f;
		    float b = ((color >> 0) & 0xFF) / 255.0f;
		    
		    float[] colorTransform = {
		            0, r, 0, 0, 0, 
		            0, 0, g, 0, 0,
		            0, 0, 0, b, 0, 
		            0, 0, 0, 1f, 0};
	
		    ColorMatrix colorMatrix = new ColorMatrix();
		    colorMatrix.setSaturation(0f);
		    colorMatrix.set(colorTransform);
	
		    ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
		    Paint paint = new Paint();
		    paint.setColorFilter(colorFilter);
			canvas.drawBitmap(sourceBitmap, 0, 0, paint);
		    
		    return resultBitmap;
		    
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
}
