package org.daum.library.android.sitac.view;

import java.io.InputStream;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class DrawableFactory {
	
	private static final String TAG = "DrawableFactory";

	public static final String PICTO_RED_UP = "/images/picto_red_up.png";
	public static final String PICTO_BLUE_UP = "/images/picto_blue_up.png";
	public static final String PICTO_ORANGE_UP = "/images/picto_orange_up.png";
	
	public static final String PICTO_RED_DOWN = "/images/picto_red_down.png";
	public static final String PICTO_BLUE_DOWN = "/images/picto_blue_down.png";
	public static final String PICTO_GREEN_DOWN = "/images/picto_green_down.png";
	public static final String PICTO_ORANGE_DOWN = "/images/picto_orange_down.png";
	
	public static final String PICTO_RED_AGRES = "/images/picto_red_agres.png";
	public static final String PICTO_BLUE_AGRES = "/images/picto_blue_agres.png";
	public static final String PICTO_BLACK_AGRES = "/images/picto_black_agres.png";
	public static final String PICTO_VIOLET_AGRES = "/images/picto_violet_agres.png";
	public static final String PICTO_ORANGE_AGRES = "/images/picto_orange_agres.png";
	public static final String PICTO_GREEN_AGRES = "/images/picto_green_agres.png";
	
	public static final String PICTO_RED_DOTTED_AGRES = "/images/picto_red_dotted_agres.png";
	public static final String PICTO_BLUE_DOTTED_AGRES = "/images/picto_blue_dotted_agres.png";
	public static final String PICTO_BLACK_DOTTED_AGRES = "/images/picto_black_dotted_agres.png";
	public static final String PICTO_VIOLET_DOTTED_AGRES = "/images/picto_violet_dotted_agres.png";
	public static final String PICTO_ORANGE_DOTTED_AGRES = "/images/picto_orange_dotted_agres.png";
	public static final String PICTO_GREEN_DOTTED_AGRES = "/images/picto_green_dotted_agres.png";
	
	public static final String PICTO_LINE_BLUE = "/images/picto_line_blue.png";
	public static final String PICTO_LINE_RED = "/images/picto_line_red.png";
	public static final String PICTO_LINE_ORANGE = "/images/picto_line_orange.png";
	public static final String PICTO_LINE_GREEN = "/images/picto_line_green.png";
	
	public static final String PICTO_ZONE = "/images/picto_zone.png";
	
	public static Drawable buildDrawable(Context ctx, String imgPath) {
		try {
			InputStream is = DrawableFactory.class.getResourceAsStream(imgPath);
			return new BitmapDrawable(ctx.getResources(), is);
		} catch (Exception e) {
			Log.w(TAG, "Error while creating drawable for image \""+imgPath+"\"");
			return null;
		}
	}
}
