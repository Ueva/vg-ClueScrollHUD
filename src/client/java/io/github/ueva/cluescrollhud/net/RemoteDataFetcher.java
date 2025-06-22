package io.github.ueva.cluescrollhud.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RemoteDataFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);
    private static final String BASE_URL = "https://ueva.dev/api/vgcluescroll";
    private static final Gson gson = new Gson();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static long extendedExpiryTime = -1L;

    public static void fetchAll() {
        CompletableFuture.runAsync(() -> {
            fetchExtendedExpiryTime();
            LOGGER.info("- Fetched extended expiry time: {}", extendedExpiryTime);
        });
    }

    public static void startAutoRefresh() {
        scheduler.scheduleAtFixedRate(RemoteDataFetcher::fetchAll, 0, 5, TimeUnit.MINUTES);
    }

    public static void stopAutoRefresh() {
        scheduler.shutdownNow();
    }

    private static void fetchExtendedExpiryTime() {

        URI uri = URI.create(BASE_URL + "/extended/expiry");

        try {
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", VgClueScrollHUD.MOD_ID);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                if (json.has("expiry") && json.get("expiry").isJsonPrimitive()) {
                    extendedExpiryTime = json.get("expiry").getAsLong();
                }
                else {
                    LOGGER.warn("Unexpected response format from {}. Expected Long, but got: {}", uri, json);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to fetch extended expiry time from {}", uri);
            LOGGER.error("Error: {}", e.toString());
        }
    }

    public static long getExtendedExpiryTime() {
        return extendedExpiryTime;
    }

}
