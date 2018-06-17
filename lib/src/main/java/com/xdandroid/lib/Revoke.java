package com.xdandroid.lib;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.provider.*;

import java.util.*;

public interface Revoke extends Utils {

    static void invokeHack(Context c) {
        revokePermissions(c);
        disallowHiddenAPIs(c);
    }

    static void revokePermissions(Context c) {
        PackageManager pm = c.getPackageManager();
        AppOpsManager aom = c.getSystemService(AppOpsManager.class);
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
              //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) aom.setMode(AppOpsManager.OP_RUN_ANY_IN_BACKGROUND, uid, n, whiteListApp
              //        ? AppOpsManager.MODE_ALLOWED : AppOpsManager.MODE_IGNORED);
              if (i.requestedPermissions == null || targetSdk >= Build.VERSION_CODES.M) return;
              Arrays.stream(i.requestedPermissions)
                    .map(p -> { try { return pm.getPermissionInfo(p, 0); } catch (Throwable e) { return null; } })
                    .filter(Objects::nonNull)
                    .filter(pi -> (pi.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE) == PermissionInfo.PROTECTION_DANGEROUS)
                    .map(pi -> pi.name)
                    .filter(pn -> pn.startsWith("android"))
                    .map(AppOpsManager::permissionToOpCode)
                    .filter(op -> op != AppOpsManager.OP_NONE)
                    .forEach(op -> aom.setMode(op, uid, n, WHITE_LIST_PERMISSIONS.contains(op) ? AppOpsManager.MODE_ALLOWED : AppOpsManager.MODE_IGNORED));
          });
    }

    static void disallowHiddenAPIs(Context c) {
        ContentResolver cr = c.getContentResolver();
        Settings.Global.putInt(cr, "hidden_api_policy_pre_p_apps", 2);
        Settings.Global.putInt(cr, "hidden_api_policy_p_apps", 2);
    }
}
