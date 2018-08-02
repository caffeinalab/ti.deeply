package ti.deeply;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

import org.appcelerator.kroll.KrollRuntime;

public class DeepLinkHandlerActivity extends Activity {

	private static String LCAT = "ti.deeply.DeepLinkHandlerActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			Log.d(LCAT, "started");
			super.onCreate(savedInstanceState);

			TiDeeplyModule module = TiDeeplyModule.getModule();
			Context context = getApplicationContext();
			Intent intent = getIntent();
			String data = intent.getDataString();
			String action = intent.getAction();
			Bundle extras = intent.getExtras();

			Intent launcherIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
			launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			launcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			Log.d(LCAT, "has callback? " + module.hasDeepLinkCallback());
			Log.d(LCAT, "is available? " + (KrollRuntime.getInstance().getRuntimeState() != KrollRuntime.State.DISPOSED));
			if (module.hasDeepLinkCallback() && KrollRuntime.getInstance().getRuntimeState() != KrollRuntime.State.DISPOSED) {
				Log.d(LCAT, "sending " + data);
				Log.d(LCAT, "sending " + action);
				Log.d(LCAT, "sending " + extras);
				module.sendDeepLink(data, action, extras, true);
			} else {
				Log.d(LCAT, "putting " + data);
				Log.d(LCAT, "putting " + action);
				Log.d(LCAT, "putting " + extras);
				launcherIntent.putExtra(TiDeeplyModule.INTENT_DATA, data);
				launcherIntent.putExtra(TiDeeplyModule.INTENT_ACTION, action);
				launcherIntent.putExtra(TiDeeplyModule.INTENT_EXTRAS, extras);
			}

			Log.d(LCAT, "launching intent");
			startActivity(launcherIntent);

		} catch (Exception e) {
			Log.e(LCAT, "onCreate " + e);
		} finally {
			finish();
		}
	}

	@Override protected void onResume() {
		Log.d(LCAT, "resumed");
		super.onResume();
	}

}