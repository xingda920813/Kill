package com.xdandroid.kill;

import android.app.*;
import android.content.pm.*;
import android.os.*;

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
        PackageManager pm = getPackageManager();
        List<String> revokeOps = new ArrayList<>();
        pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
          .stream()
          .filter(i -> (i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
          .filter(i -> (i.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0)
          .forEach(i -> {
              String n = i.applicationInfo.packageName;
              revokeOps.addAll(Arrays
                      .stream(BLACK_LIST_OPS)
                      .map(op -> "adb shell cmd appops set " + n + ' ' + op + ' '
                              + (WHITE_LIST_APPS.contains(n) && WHITE_LIST_OPS_FOR_WHITE_LIST_APPS.contains(op) ? "allow" : "ignore") + "\n\n")
                      .collect(Collectors.toList()));
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
