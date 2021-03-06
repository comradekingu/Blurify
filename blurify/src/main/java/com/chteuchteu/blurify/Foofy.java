package com.chteuchteu.blurify;

import android.content.Context;
import android.support.v8.renderscript.RSInvalidStateException;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

public class Foofy {
	private Context context;
	private RenderScript renderScriptContext;

	private static Foofy instance;

	public static synchronized Foofy getInstance() {
		if (instance.context == null)
			throw new RuntimeException("Calling getInstance() on a context-less Foofy instance");
		return instance;
	}
	public static synchronized Foofy getInstance(Context context) {
		if (instance == null)
			instance = new Foofy(context);

		return instance;
	}
	private Foofy(Context context) {
		this.context = context;
		this.renderScriptContext = null;
	}


	public synchronized RenderScript getRenderScriptContext() {
		if (renderScriptContext == null) {
			Foofy.log("Creating RenderScript context");
			renderScriptContext = RenderScript.create(context);
		}

		return renderScriptContext;
	}
	public void destroyRenderScriptContext() {
		if (renderScriptContext != null) {
            try {
                renderScriptContext.destroy();
            } catch (RSInvalidStateException | NullPointerException ignoredException) {
                // Ignored exception
            }
        }
	}


	public static void log(String txt) {
		log(txt, Log.DEBUG);
	}
	public static void log(String txt, int logLevel) {
		if (BuildConfig.DEBUG) {
			switch (logLevel) {
				case Log.DEBUG: Log.d("Blurify", txt); break;
				case Log.ERROR: Log.e("Blufiry", txt); break;
				case Log.INFO: Log.i("Blurify", txt); break;
				case Log.VERBOSE: Log.v("Blurify", txt); break;
				case Log.WARN: Log.w("Blurify", txt); break;
				default: log(txt, Log.INFO); break;
			}
		}
	}
}
