package com.xdandroid.kill;

import android.*;
import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.widget.*;

import com.xdandroid.lib.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

/**
 * uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class GenOpsActivity extends Activity implements Utils {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "GenOps", Toast.LENGTH_SHORT).show();
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 233);
            finish();
            return;
        }
        PackageManager pm = getPackageManager();
        PowerManager pwm = getSystemService(PowerManager.class);
        assert pwm != null;
        List<String> revokeOps = new ArrayList<>();
        pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
          .stream()
          .filter(i -> (i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
          .filter(i -> (i.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0)
          .forEach(i -> {
              String n = i.applicationInfo.packageName;
              boolean whiteListApp = WHITE_LIST_APPS.contains(n) || WHITE_LIST_APP_NAME_SLICES.stream().anyMatch(n::contains);
              revokeOps.addAll(Arrays
                      .stream(BLACK_LIST_OPS)
                      .map(op -> "adb shell cmd appops set " + n + ' ' + op + ' '
                              + (whiteListApp && WHITE_LIST_OPS_FOR_WHITE_LIST_APPS.contains(op) ? "allow" : "ignore")
                              + "\n\n")
                      .collect(Collectors.toList()));
              boolean ignoringBatteryOptimizations = pwm.isIgnoringBatteryOptimizations(n);
              if (whiteListApp && !ignoringBatteryOptimizations) {
                  revokeOps.add("adb shell \"cmd deviceidle whitelist +" + n + "\"\n\n");
              } else if (!whiteListApp && ignoringBatteryOptimizations) {
                  revokeOps.add("adb shell \"cmd deviceidle whitelist -" + n + "\"\n\n");
              }
          });
        revokeOps.add("adb shell settings put global hidden_api_policy_pre_p_apps 2\n\n");
        revokeOps.add("adb shell settings put global hidden_api_policy_p_apps 2\n\n");
        try (FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "ops.sh"))) {
            revokeOps.forEach(op -> {
                try { fos.write(op.getBytes(StandardCharsets.UTF_8)); } catch (IOException e) { throw Utils.asUnchecked(e); }
            });
        } catch (IOException e) { throw Utils.asUnchecked(e); }
        finish();
    }
}
