package com.xdandroid.kill;

import android.app.*;
import android.os.*;
import android.widget.*;

import com.xdandroid.lib.*;

import java.io.*;
import java.lang.*;
import java.lang.Process;
import java.nio.charset.*;
import java.util.*;

/**
 * uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class RootRevokeActivity extends Activity {

    static int runAsRoot(List<String> revokeOps) throws IOException, InterruptedException {
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("su");
            OutputStream out = proc.getOutputStream();
            for (String op : revokeOps) {
                out.write(op.getBytes(StandardCharsets.UTF_8));
                out.flush();
            }
            out.write("exit\n\n".getBytes(StandardCharsets.UTF_8));
            out.flush();
            return proc.waitFor();
        } finally {
            if (proc != null) proc.destroy();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "RootRevoke", Toast.LENGTH_SHORT).show();
        List<String> revokeOps = GenOpsActivity.getRevokeOps(this, false);
        try {
            runAsRoot(revokeOps);
        } catch (IOException | InterruptedException e) {
            throw Utils.asUnchecked(e);
        }
        finish();
    }
}
