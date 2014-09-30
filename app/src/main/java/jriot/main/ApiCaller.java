package jriot.main;

import android.util.LruCache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ApiCaller {

    private LruCache<String, String> apiCache;

    public ApiCaller() {
        apiCache = new LruCache<String, String>(10000);
    }

    public String request(String requestURL) throws JRiotException {
        String response = apiCache.get(requestURL);
        if (response != null) {
            return response;
        }
        try {
            URL url = new URL(requestURL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(false);

            if (connection.getResponseCode() != 200) {
                throw new JRiotException(connection.getResponseCode());
            }

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = rd.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\r');
            }

            connection.disconnect();
            response = stringBuilder.toString();
            apiCache.put(requestURL, response);
            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
