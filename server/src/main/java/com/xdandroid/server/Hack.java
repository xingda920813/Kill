package com.xdandroid.server;

import android.content.pm.*;
import android.os.*;
import android.util.*;

import com.android.server.pm.*;

import java.lang.reflect.*;
import java.util.*;

public class Hack {

    public static void hack(Object token) throws Throwable {
        switch (token.toString()) {
            case "Target":
                hackTarget();
                break;
            case "Debug":
                hackDebug();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    static void hackTarget() throws Throwable {
        HashMap<String, Integer> whiteListForTarget = new HashMap<>();
        whiteListForTarget.put("com.tencent.mm", 24);
        PackageManagerService pms = (PackageManagerService) ServiceManager.getService("package");
        Field packagesField = PackageManagerService.class.getDeclaredField("mPackages");
        packagesField.setAccessible(true);
        ArrayMap<String, PackageParser.Package> packages = (ArrayMap<String, PackageParser.Package>) packagesField.get(pms);
        packages.values()
                .stream()
                .filter(Objects::nonNull)
                .map(pkg -> pkg.applicationInfo)
                .forEach(ai -> {
                    if (ai.targetSdkVersion >= Build.VERSION_CODES.M) ai.targetSdkVersion = Build.VERSION_CODES.CUR_DEVELOPMENT - 1;
                    if (ai.targetSdkVersion <= Build.VERSION_CODES.LOLLIPOP_MR1) ai.targetSdkVersion = Build.VERSION_CODES.LOLLIPOP_MR1;
                    ai.targetSdkVersion = whiteListForTarget.getOrDefault(ai.packageName, ai.targetSdkVersion);
                });
        packages.values()
                .stream()
                .filter(Objects::nonNull)
                .flatMap(pkg -> pkg.services.stream().map(srv -> srv.info))
                .filter(si -> (si.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                .filter(si -> (si.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0)
                .filter(si -> !Utils.WHITE_LIST_APPS.contains(si.packageName))
                .forEach(si -> si.flags |= ServiceInfo.FLAG_STOP_WITH_TASK);
    }

    @SuppressWarnings("unchecked")
    static void hackDebug() throws Throwable {
        PackageManagerService pms = (PackageManagerService) ServiceManager.getService("package");
        Field packagesField = PackageManagerService.class.getDeclaredField("mPackages");
        packagesField.setAccessible(true);
        ((ArrayMap<String, PackageParser.Package>) packagesField
                .get(pms))
                .values()
                .stream()
                .filter(Objects::nonNull)
                .map(pkg -> pkg.applicationInfo)
                .forEach(ai -> ai.flags |= ApplicationInfo.FLAG_DEBUGGABLE);
    }
}
