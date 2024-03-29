package kali.locker;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class AppLockManager {

    private static final String LOCKED_APPS_PREFS_KEY = "LOCKED_APPS_PREFS_KEY";

    public static void lockApp(Context context, String packageName) {
        Set<String> lockedApps = getLockedApps(context);
        lockedApps.add(packageName);
        saveLockedApps(context, lockedApps);
    }

    public static void unlockApp(Context context, String packageName) {
        Set<String> lockedApps = getLockedApps(context);
        lockedApps.remove(packageName);
        saveLockedApps(context, lockedApps);
    }

    public static boolean isAppLocked(Context context, String packageName) {
        Set<String> lockedApps = getLockedApps(context);
        return lockedApps.contains(packageName);
    }

    private static Set<String> getLockedApps(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("APP_LOCK_PREFS", Context.MODE_PRIVATE);
        return preferences.getStringSet(LOCKED_APPS_PREFS_KEY, new HashSet<>());
    }

    private static void saveLockedApps(Context context, Set<String> lockedApps) {
        SharedPreferences preferences = context.getSharedPreferences("APP_LOCK_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(LOCKED_APPS_PREFS_KEY, lockedApps);
        editor.apply();
    }
}
