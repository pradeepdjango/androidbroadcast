package kali.locker.broadcast;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import kali.locker.AppLockManager;
import kali.locker.PinAuthenticationActivity;
import kali.locker.R;

public class PackageChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && (action.equals(Intent.ACTION_PACKAGE_ADDED) || action.equals(Intent.ACTION_PACKAGE_REMOVED))) {
                String packageName = intent.getData().getEncodedSchemeSpecificPart();
                if (AppLockManager.isAppLocked(context, packageName)) {
                    // App is locked, prompt the user for PIN authentication
                    Toast.makeText(context, "An app was installed or removed. Please authenticate to continue.", Toast.LENGTH_SHORT).show();
                    startAuthenticationActivity(context);
                } else {
                    // App is not locked, no need for authentication
                    if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                        // App installed, check if it should be locked
                        lockNewlyInstalledApp(context, packageName);
                    } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                        // App removed, unlock if it was locked
                        unlockRemovedApp(context, packageName);
                    }
                }
            } else if (action != null && (action.equals(Intent.ACTION_PACKAGE_REPLACED) || action.equals(Intent.ACTION_PACKAGE_FIRST_LAUNCH))) {
                // App is opened or replaced, show notification
                showAppOpenNotification(context);
            }
        }
    }

    private void startAuthenticationActivity(Context context) {
        Intent authIntent = new Intent(context, PinAuthenticationActivity.class);
        authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(authIntent);
    }

    private void lockNewlyInstalledApp(Context context, String packageName) {
        // Lock the newly installed app
        AppLockManager.lockApp(context, packageName);
    }

    private void unlockRemovedApp(Context context, String packageName) {
        // Unlock the removed app if it was locked
        AppLockManager.unlockApp(context, packageName);
    }

    private void showAppOpenNotification(Context context) {
        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default_channel_id")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("App Opened")
                .setContentText("An app has been opened.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
