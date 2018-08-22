/**
 * Ti.Deeply
 * Copyright (c) 2018-present by Andrea Jonus.
 * Licensed under the terms of the MIT License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.deeply;

import java.util.HashMap;
import java.util.Iterator;

import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

@Kroll.module(name = "TiDeeply", id = "ti.deeply")
public class TiDeeplyModule extends KrollModule
{
	private static final String LCAT = "ti.deeply.TiDeeplyModule";
	private static final boolean DBG = TiConfig.LOGD;

	public static final String EXTRA_DEEPLY_FLAG = "tideeply.isDeepLink";

	private static TiDeeplyModule module = null;

	private KrollFunction callback = null;

	public TiDeeplyModule()
	{
		super();
		module = this;
	}

	public static TiDeeplyModule getModule()
	{
		if (module != null)
			return module;
		else
			return new TiDeeplyModule();
	}

	public void parseBootIntent()
	{
		try {
			Intent intent = TiApplication.getAppRootOrCurrentActivity().getIntent();
			Boolean deeplyFlag = intent.getBooleanExtra(EXTRA_DEEPLY_FLAG, false);

			if (deeplyFlag == true) {
				intent.removeExtra(EXTRA_DEEPLY_FLAG);
				sendDeepLink(intent.getData(), intent.getAction(), intent.getExtras());
			} else {
				Log.d(LCAT, "Not started by a deep link.");
			}
		} catch (Exception ex) {
			Log.e(LCAT, "parseBootIntent" + ex);
		}
	}

	public void sendDeepLink(Uri data, String action, Bundle extras)
	{
		if (callback == null) {
			Log.e(LCAT, "sendDeepLink invoked but no callback defined");
			return;
		}

		HashMap<String, Object> e = new HashMap<String, Object>();
		e.put("data", data.toString()); // to parse on reverse on JS side
		e.put("action", action);
		e.put("extras", convertBundleToMap(extras));

		callback.callAsync(getKrollObject(), e);
	}

	private static HashMap<String, Object> convertBundleToMap(Bundle bundle)
	{
		if (bundle == null)
			return null;

		HashMap<String, Object> map = new HashMap<String, Object>();
		Iterator<String> keys = bundle.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			map.put(key, bundle.get(key));
		}
		return map;
	}

	public boolean hasCallback()
	{
		return callback != null;
	}

	// clang-format off
	@Kroll.method
	@Kroll.setProperty
	public void setCallback(KrollFunction _callback)
	// clang-format on
	{
		callback = _callback;
		parseBootIntent();
	}
}
