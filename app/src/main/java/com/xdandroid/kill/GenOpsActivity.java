package com.xdandroid.kill;

import android.app.*;
import android.content.*;
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

    static List<String> getRevokeOps(Context c, boolean adbShell) {
        String prefix = adbShell ? "adb shell \"" : "";
        String suffix = adbShell ? "\"\n\n" : "\n\n";
        PackageManager pm = c.getPackageManager();
        PowerManager pwm = c.getSystemService(PowerManager.class);
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
                      .map(op -> prefix + "cmd appops set " + n + ' ' + op + ' '
                              + (whiteListApp && WHITE_LIST_OPS_FOR_WHITE_LIST_APPS.contains(op) ? "allow" : "ignore")
                              + suffix)
                      .collect(Collectors.toList()));
              revokeOps.add(prefix + "cmd deviceidle whitelist " + (whiteListApp ? '+' : '-') + n + suffix);
          });
        revokeOps.add(prefix + "settings put global hidden_api_policy 2" + suffix);
        revokeOps.add(prefix + "settings put global hidden_api_policy_pre_p_apps 2" + suffix);
        revokeOps.add(prefix + "settings put global hidden_api_policy_p_apps 2" + suffix);
        return revokeOps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "GenOps", Toast.LENGTH_SHORT).show();
        List<String> revokeOps = getRevokeOps(this, true);
        try (FileOutputStream fos = new FileOutputStream(new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "ops.sh"))) {
            revokeOps.forEach(op -> {
                try { fos.write(op.getBytes(StandardCharsets.UTF_8)); } catch (IOException e) { throw Utils.asUnchecked(e); }
            });
        } catch (IOException e) { throw Utils.asUnchecked(e); }
        finish();
    }
}
