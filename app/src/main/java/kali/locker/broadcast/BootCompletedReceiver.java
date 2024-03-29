package kali.locker.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kali.locker.services.AppUsageMonitorService;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // Start your service here
            Intent serviceIntent = new Intent(context, AppUsageMonitorService.class);
            context.startService(serviceIntent);
        }
    }
}
