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
			finish();

			TiDeeplyModule module = TiDeeplyModule.getModule();
			Context context = getApplicationContext();
			Intent intent = getIntent();
			String data = intent.getStringExtra(TiDeeplyModule.INTENT_DATA);
			String action = intent.getStringExtra(TiDeeplyModule.INTENT_ACTION);
			Bundle extras = intent.getBundleExtra(TiDeeplyModule.INTENT_EXTRAS);

			Intent launcherIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
			launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			launcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			if (module.hasDeepLinkCallback() && KrollRuntime.getInstance().getRuntimeState() != KrollRuntime.State.DISPOSED) {
				module.sendDeepLink(data, action, extras, true);
			} else {
				launcherIntent.putExtra(TiDeeplyModule.INTENT_DATA, data);
				launcherIntent.putExtra(TiDeeplyModule.INTENT_ACTION, action);
				launcherIntent.putExtra(TiDeeplyModule.INTENT_EXTRAS, extras);
			}

			startActivity(launcherIntent);

		} catch (Exception e) {
			// noop
		} finally {
			finish();
		}
	}

	@Override protected void onResume() {
		Log.d(LCAT, "resumed");
		super.onResume();
	}

}