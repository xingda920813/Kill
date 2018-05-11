package com.xdandroid.kill;

import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.text.*;

import java.util.*;

/**
 * uses-permission android:name="android.permission.UPDATE_APP_OPS_STATS"
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class RevokeActivity extends Activity implements Utils {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invokeHack();
        finish();
    }

    static void invokeHackNoThrow() {
        try {
            invokeHack();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static void invokeHack() {
        Application app = ActivityThread.currentApplication();
        PackageManager pm = app.getPackageManager();
        AppOpsManager aom = app.getSystemService(AppOpsManager.class);
        pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
          .stream()
          .filter(i -> (i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
          .filter(i -> (i.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0)
          .forEach(i -> {
              int uid = i.applicationInfo.uid;
              String n = i.applicationInfo.packageName;
              int targetSdk = i.applicationInfo.targetSdkVersion;
              aom.setMode(AppOpsManager.OP_WIFI_SCAN, uid, n, AppOpsManager.MODE_IGNORED);
              aom.setMode(AppOpsManager.OP_WRITE_SETTINGS, uid, n, AppOpsManager.MODE_IGNORED);
              aom.setMode(AppOpsManager.OP_SYSTEM_ALERT_WINDOW, uid, n, AppOpsManager.MODE_IGNORED);
              aom.setMode(AppOpsManager.OP_WAKE_LOCK, uid, n, AppOpsManager.MODE_IGNORED);
              boolean whiteListApp = WHITE_LIST_APPS.contains(n);
              aom.setMode(AppOpsManager.OP_RUN_IN_BACKGROUND, uid, n, whiteListApp ? AppOpsManager.MODE_ALLOWED : AppOpsManager.MODE_IGNORED);
              if (i.requestedPermissions == null || targetSdk >= Build.VERSION_CODES.M) return;
              Arrays.stream(i.requestedPermissions)
                    .map(p -> { try { return pm.getPermissionInfo(p, 0); } catch (Throwable e) { return null; } })
                    .filter(Objects::nonNull)
                    .filter(pi -> (pi.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE) == PermissionInfo.PROTECTION_DANGEROUS)
                    .map(pi -> pi.name)
                    .filter(pn -> pn.startsWith("android"))
                    .map(AppOpsManager::permissionToOp)
                    .filter(op -> !TextUtils.isEmpty(op))
                    .forEach(op -> aom.setUidMode(op, uid, WHITE_LIST_PERMISSIONS.contains(op) ? AppOpsManager.MODE_ALLOWED : AppOpsManager.MODE_IGNORED));
          });
    }
}
