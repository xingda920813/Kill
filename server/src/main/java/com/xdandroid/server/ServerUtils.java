package com.xdandroid.server;

import com.xdandroid.lib.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import dalvik.system.*;

interface ServerUtils extends Utils {

    @SuppressWarnings("unchecked")
    static Object recreateDexPathList(Object originalDexPathList, ClassLoader newDefiningContext) throws Exception {
        Field dexElementsField = Utils.findField(originalDexPathList, "dexElements");
        Object[] dexElements = (Object[]) dexElementsField.get(originalDexPathList);
        Field nativeLibraryDirectoriesField = Utils.findField(originalDexPathList, "nativeLibraryDirectories");
        List<File> nativeLibraryDirectories = (List<File>) nativeLibraryDirectoriesField.get(originalDexPathList);
        StringBuilder dexPathBuilder = new StringBuilder();
        Field dexFileField = Utils.findField(dexElements.getClass().getComponentType(), "dexFile");
        boolean isFirstItem = true;
        for (Object dexElement : dexElements) {
            DexFile dexFile = (DexFile) dexFileField.get(dexElement);
            if (dexFile == null) continue;
            if (isFirstItem) isFirstItem = false; else dexPathBuilder.append(File.pathSeparator);
            dexPathBuilder.append(dexFile.getName());
        }
        String dexPath = dexPathBuilder.toString();
        StringBuilder libraryPathBuilder = new StringBuilder();
        isFirstItem = true;
        for (File libDir : nativeLibraryDirectories) {
            if (libDir == null) continue;
            if (isFirstItem) isFirstItem = false; else libraryPathBuilder.append(File.pathSeparator);
            libraryPathBuilder.append(libDir.getAbsolutePath());
        }
        String libraryPath = libraryPathBuilder.toString();
        Constructor<?> dexPathListConstructor = Utils.findConstructor(originalDexPathList, ClassLoader.class, String.class, String.class, File.class);
        return dexPathListConstructor.newInstance(newDefiningContext, dexPath, libraryPath, null);
    }
}
