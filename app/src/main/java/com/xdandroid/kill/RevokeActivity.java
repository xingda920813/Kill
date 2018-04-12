package com.xdandroid.kill;

import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.text.*;

import java.io.*;
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
        Utils.setPermissive();
        Application app = ActivityThread.currentApplication();
        PackageManager pm = app.getPackageManager();
        AppOpsManager aom = app.getSystemService(AppOpsManager.class);
        List<String> revokeOps = new ArrayList<>();
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
              if (OP_BOOT_COMPLETED > 0) aom.setMode(OP_BOOT_COMPLETED, uid, n, whiteListApp ? AppOpsManager.MODE_ALLOWED : AppOpsManager.MODE_IGNORED);
              if (i.requestedPermissions == null) return;
              Arrays.stream(i.requestedPermissions)
                    .map(p -> {
                        try { return pm.getPermissionInfo(p, 0); } catch (Exception e) { return null; }
                    })
                    .filter(Objects::nonNull)
                    .filter(pi -> (pi.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE) == PermissionInfo.PROTECTION_DANGEROUS)
                    .map(pi -> pi.name)
                    .filter(pn -> pn.startsWith("android"))
                    .map(pn -> targetSdk >= Build.VERSION_CODES.M ? pn : AppOpsManager.permissionToOp(pn))
                    .filter(op -> !TextUtils.isEmpty(op))
                    .forEach(op -> {
                        boolean whiteListPermission = WHITE_LIST_PERMISSIONS.contains(op);
                        if (targetSdk >= Build.VERSION_CODES.M) {
                            if (pm.checkPermission(op, n) != (whiteListPermission ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED))
                                revokeOps.add("pm " + (whiteListPermission ? "grant" : "revoke") + ' ' + n + ' ' + op);
                        } else aom.setUidMode(op, uid, whiteListPermission ? AppOpsManager.MODE_ALLOWED : AppOpsManager.MODE_IGNORED);
                    });
          });
        revokeOps.stream().map(op -> "su -c " + op).forEach(cmd -> {
            try { Runtime.getRuntime().exec(cmd); } catch (IOException e) { e.printStackTrace(); }
        });
    }
}
