package com.xdandroid.kill;

import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.text.*;

import com.xdandroid.lib.*;

import java.io.*;
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
              int targetSdk = i.applicationInfo.targetSdkVersion;
              revokeOps.addAll(Arrays
                      .stream(BLACK_LIST_OPS)
                      .map(op -> "adb shell cmd appops set " + n + ' ' + op + ' ' + (WHITE_LIST_APPS.contains(n) && WHITE_LIST_OPS_FOR_WHITE_LIST_APPS.contains(op) ? "allow" : "ignore") + "\n\n")
                      .collect(Collectors.toList()));
              if (i.requestedPermissions == null) return;
              Arrays.stream(i.requestedPermissions)
                    .map(p -> { try { return pm.getPermissionInfo(p, 0); } catch (Throwable e) { return null; } })
                    .filter(Objects::nonNull)
                    .filter(pi -> (pi.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE) == PermissionInfo.PROTECTION_DANGEROUS)
                    .map(pi -> pi.name)
                    .filter(pn -> pn.startsWith("android"))
                    .map(pn -> targetSdk >= Build.VERSION_CODES.M ? pn : LocalAppOpsManager.opToName(LocalAppOpsManager.permissionToOpCode(pn)))
                    .filter(op -> !TextUtils.isEmpty(op))
                    .forEach(op -> {
                        if (targetSdk >= Build.VERSION_CODES.M) {
                            if (pm.checkPermission(op, n) != (WHITE_LIST_PERMISSIONS.contains(op) ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED))
                                revokeOps.add("adb shell su -c \\\"pm " + (WHITE_LIST_PERMISSIONS.contains(op) ? "grant" : "revoke") + ' ' + n + ' ' + op + "\\\"\n\n");
                        } else revokeOps.add("adb shell cmd appops set " + n + ' ' + op + ' ' + (WHITE_LIST_PERMISSIONS.contains(op) ? "allow" : "ignore") + "\n\n");
                    });
          });
        try (FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "ops.sh"))) {
            revokeOps.forEach(op -> {
                try { fos.write(op.getBytes("UTF-8")); } catch (IOException e) { throw Utils.asUnchecked(e); }
            });
        } catch (IOException e) { throw Utils.asUnchecked(e); }
        finish();
    }
}
