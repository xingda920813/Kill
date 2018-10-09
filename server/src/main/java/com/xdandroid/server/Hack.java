package com.xdandroid.server;

import android.content.pm.*;
import android.os.*;
import android.util.*;

import com.android.server.pm.*;
import com.xdandroid.lib.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

public class Hack implements Utils {

    public static void hack(Object token) throws Throwable {
        switch (token.toString()) {
            case "Target":
                hackTarget(0);
                break;
            case "TargetAll":
                hackTarget(SystemProperties.getInt("persist.server.target", Build.VERSION_CODES.CUR_DEVELOPMENT - 1));
                break;
            case "Debug":
                hackDebug();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    static void hackTarget(int targetSdk) throws Throwable {
        HashMap<String, Integer> whiteListForTarget = new HashMap<>();
        whiteListForTarget.put("com.tencent.mm", Build.VERSION_CODES.M);
        PackageManagerService pms = (PackageManagerService) ServiceManager.getService("package");
        Field packagesField = PackageManagerService.class.getDeclaredField("mPackages");
        packagesField.setAccessible(true);
        ArrayMap<String, PackageParser.Package> packages = (ArrayMap<String, PackageParser.Package>) packagesField.get(pms);
        Stream<ApplicationInfo> aiStream = packages
                .values()
                .stream()
                .filter(Objects::nonNull)
                .map(pkg -> pkg.applicationInfo)
                .filter(ai -> (ai.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                .filter(ai -> (ai.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0);
        if (targetSdk == 0) aiStream.forEach(ai -> {
            if (ai.targetSdkVersion >= Build.VERSION_CODES.M) ai.targetSdkVersion = Build.VERSION.SDK_INT == Build.VERSION_CODES.O
                    ? Build.VERSION_CODES.O : Build.VERSION_CODES.CUR_DEVELOPMENT - 1;
            if (ai.targetSdkVersion <= Build.VERSION_CODES.LOLLIPOP_MR1) ai.targetSdkVersion = Build.VERSION_CODES.LOLLIPOP_MR1;
            ai.targetSdkVersion = whiteListForTarget.getOrDefault(ai.packageName, ai.targetSdkVersion);
        });
        else aiStream.forEach(ai -> ai.targetSdkVersion = targetSdk);
        packages.values()
                .stream()
                .filter(Objects::nonNull)
                .flatMap(pkg -> pkg.services.stream().map(srv -> srv.info))
                .filter(si -> (si.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                .filter(si -> (si.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0)
                .filter(si -> !WHITE_LIST_APPS.contains(si.packageName))
                .filter(si -> WHITE_LIST_APP_NAME_SLICES.stream().noneMatch(slice -> si.packageName.contains(slice)))
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
                .filter(ai -> (ai.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                .filter(ai -> (ai.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0)
                .filter(ai -> WHITE_LIST_APP_NAME_SLICES.stream().noneMatch(slice -> ai.packageName.contains(slice)))
                .forEach(ai -> ai.flags |= ApplicationInfo.FLAG_DEBUGGABLE);
    }
}
