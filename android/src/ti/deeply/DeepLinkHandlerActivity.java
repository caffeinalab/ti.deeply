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
		Log.d(LCAT, "started");
		super.onCreate(savedInstanceState);

		processIntent(getIntent());
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.d(LCAT, "new intent");
		processIntent(intent);
	}

	@Override
	protected void onResume() {
		Log.d(LCAT, "resumed");
		super.onResume();
	}

	private void processIntent(Intent intent) {
		try {
			TiDeeplyModule module = TiDeeplyModule.getModule();
			Context context = getApplicationContext();
			String data = intent.getDataString();
			String action = intent.getAction();
			Bundle extras = intent.getExtras();

			Intent launcherIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
			launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			launcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			if (module.hasCallback() && KrollRuntime.getInstance().getRuntimeState() != KrollRuntime.State.DISPOSED) {
				module.sendDeepLink(data, action, extras);
			} else {
				launcherIntent.putExtra(TiDeeplyModule.INTENT_DATA, data);
				launcherIntent.putExtra(TiDeeplyModule.INTENT_ACTION, action);
				launcherIntent.putExtra(TiDeeplyModule.INTENT_EXTRAS, extras);
			}

			startActivity(launcherIntent);

		} catch (Exception e) {
			Log.e(LCAT, "onCreate " + e);
		}
	}

}