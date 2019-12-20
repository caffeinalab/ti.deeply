# Deeply

### Simple deep link handling in your Titanium Android app

## Install

### Automatically:
// COMING SOON

### Manually:
- Get the module from the [releases page](https://github.com/caffeinalab/ti.deeply/releases);
- Add the content to the `modules` folder of your project;
- Add `<module platform="android">ti.deeply</module>` under the `<modules>` tag of your `tiapp.xml` file.

**Note**: version 1.0.0 is for Titanium SDK >= 7.1.0, version 0.9.0 is for Titanium SDK >= 6.3.0

## Usage

Add the following activity to your `tiapp.xml` file, in the Android manifest part:

```xml
<activity android:name="ti.deeply.DeepLinkHandlerActivity" android:noHistory="true" android:excludeFromRecents="true" android:theme="@android:style/Theme.NoDisplay" android:launchMode="singleTask">
	<intent-filter>
		<action android:name="android.intent.action.VIEW"/>
		<category android:name="android.intent.category.DEFAULT"/>
		<category android:name="android.intent.category.BROWSABLE"/>
		<data android:scheme="<Your URL scheme>"/>
	</intent-filter>
	<!-- and all the other intent filters you want to declare -->
</activity>
```

Require the module wherever you see fit (generally, in the `alloy.js` or `index.js` of your app) and set the callback function:

```js
var Deeply = require('ti.deeply');

Deeply.setCallback(function(e) {
	Ti.API.debug('Deep link called with:');
	Ti.API.debug('  data:', e.data);
	Ti.API.debug('  action:', e.action);
	Ti.API.debug('  extras', e.extras);
});
```

Test it from a terminal:

```bash
adb shell am start -a android.intent.action.VIEW -d "examplescheme://foo" your.app.id
```

## Notes

### `android:launchMode`

The tag `android:launchMode="singleTask"` is necessary for the module to work correctly when the app is opened from a closed state by a deep link.
If you want more informations on why this is the case, see:
- https://developer.android.com/guide/topics/manifest/activity-element#lmode
- https://stackoverflow.com/questions/35931028/android-deep-link-does-not-work-if-the-app-is-opened-by-deep-link-already
- https://medium.com/@ankit.sinhal/understand-activity-launch-mode-with-examples-721e85b6421e

Until Titanium 7.3.0, the tag `android:launchMode` was automatically removed at build time. This means that, if you're using a Titanium version lower than 7.3.0, your app will not open the same deep link again if it was used to start the app.

## License

MIT
