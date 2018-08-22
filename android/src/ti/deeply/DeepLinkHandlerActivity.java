/**
 * Ti.Deeply
 * Copyright (c) 2018-present by Andrea Jonus.
 * Licensed under the terms of the MIT License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.deeply;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

import org.appcelerator.kroll.KrollRuntime;

public class DeepLinkHandlerActivity extends Activity
{

	private static String LCAT = "ti.deeply.DeepLinkHandlerActivity";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d(LCAT, "started");
		super.onCreate(savedInstanceState);

		processIntent(getIntent());
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		Log.d(LCAT, "new intent");
		processIntent(intent);
	}

	@Override
	protected void onResume()
	{
		Log.d(LCAT, "resumed");
		super.onResume();
	}

	private void processIntent(Intent intent)
	{
		try {
			TiDeeplyModule module = TiDeeplyModule.getModule();
			Context context = getApplicationContext();
			Uri data = intent.getData();
			String action = intent.getAction();
			Bundle extras = intent.getExtras();

			Intent launcherIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());

			if (module.hasCallback() && KrollRuntime.getInstance().getRuntimeState() != KrollRuntime.State.DISPOSED) {
				module.sendDeepLink(data, action, extras);
			} else {
				launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				launcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				launcherIntent.setData(data);
				launcherIntent.setAction(action);
				launcherIntent.putExtras(intent);
				launcherIntent.putExtra(TiDeeplyModule.EXTRA_DEEPLY_FLAG, true);
			}

			startActivity(launcherIntent);

		} catch (Exception e) {
			Log.e(LCAT, "onCreate " + e);
		}
	}
}