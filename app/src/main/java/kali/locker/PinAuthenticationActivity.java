package kali.locker;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PinAuthenticationActivity extends AppCompatActivity {

    private EditText editTextPinAuth;
    private Button btnAuthenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_authentication);

        editTextPinAuth = findViewById(R.id.editTextPinAuth);
        btnAuthenticate = findViewById(R.id.btnAuthenticate);

        btnAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = editTextPinAuth.getText().toString().trim();
                if (!pin.isEmpty() && authenticatePin(pin)) {
                    Toast.makeText(PinAuthenticationActivity.this, "PIN authenticated!", Toast.LENGTH_SHORT).show();
                    // Proceed to next screen or unlock functionality
                } else {
                    Toast.makeText(PinAuthenticationActivity.this, "Invalid PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean authenticatePin(String pin) {
        SharedPreferences preferences = getSharedPreferences("PIN_PREFS", MODE_PRIVATE);
        String savedPin = preferences.getString("pin", "");
        return savedPin.equals(pin);
    }


    private void authenticateAndProceed(String pin) {
        if (!pin.isEmpty() && authenticatePin(pin)) {
            Toast.makeText(this, "PIN authenticated!", Toast.LENGTH_SHORT).show();
            // Proceed to unlock functionality or next screen
            unlockLockedApps();
        } else {
            Toast.makeText(this, "Invalid PIN", Toast.LENGTH_SHORT).show();
        }
    }

    private void unlockLockedApps() {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo appInfo : installedApps) {
            String packageName = appInfo.packageName;
            if (AppLockManager.isAppLocked(this, packageName)) {
                // Implement your logic to unlock the app here
                // For example, you can show a notification to the user that the app is locked and cannot be accessed
            }
        }
    }

}
