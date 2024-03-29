package kali.locker.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kali.locker.services.AppUsageMonitorService;

public class AppInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_PACKAGE_ADDED)) {
            // Get the package name of the installed app
            String packageName = intent.getData().getEncodedSchemeSpecificPart();

            // Start your service here
            Intent serviceIntent = new Intent(context, AppUsageMonitorService.class);
            context.startService(serviceIntent);
        }
    }
}
