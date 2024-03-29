package kali.locker.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.Nullable;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import kali.locker.R;

public class AppUsageMonitorService extends Service {

    private WindowManager windowManager;
    private View overlayView;
    private TextView messageTextView;
    private HashSet<String> excludedApps;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        showOverlayView();
        System.out.println("ujjjjjjjjjjjjjjjjjjj###########################################################################################################################");
        excludedApps = new HashSet<>(Arrays.asList("com.example.app1", "com.example.app2")); // Add package names of excluded apps here
        monitorAppUsage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayView != null) {
            windowManager.removeView(overlayView);
        }
    }

    private void showOverlayView() {
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP;
//         windowManager.addView(overlayView, params);
//        messageTextView = overlayView.findViewById(R.id.messageTextView);
        System.out.println("######################################################################");
    }

    @SuppressLint("SetTextI18n")
    private void monitorAppUsage() {
        UsageStatsManager usageStatsManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        }
        long currentTime = System.currentTimeMillis();
        List<UsageStats> usageStatsList = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 10, currentTime);
        }

        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                UsageEvents.Event event = new UsageEvents.Event();
            }
            for (UsageStats usageStats : usageStatsList) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (usageStats.getLastTimeUsed() >= currentTime - 1000 * 10) {
                        String packageName = usageStats.getPackageName();
                        if (!excludedApps.contains(packageName)) {
                            System.out.println("##################### "+packageName);
//                            messageTextView.setText("App opened: " + packageName);
                        }
                        break;
                    }
                }
            }
        }
    }
}
