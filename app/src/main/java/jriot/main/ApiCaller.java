package jriot.main;

import android.util.LruCache;

import com.github.theholywaffle.lolchatapi.RateLimiter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ApiCaller {

    private LruCache<String, Response> apiCache;
    private RateLimiter shortLimiter, longLimiter;

    public ApiCaller() {
        apiCache = new LruCache<String, Response>(10000);
        shortLimiter = new RateLimiter(10, 10000);//10 request(s) every 10 seconds
        longLimiter = new RateLimiter(500, 600000);//500 requests every 10 minutes
    }

    public String request(String requestURL) throws JRiotException {
        Response response = apiCache.get(requestURL);
        if (response != null) {
            if(!response.isExpired())
                return response.getResponse();
            apiCache.remove(requestURL);
        }
        shortLimiter.acquire();
        longLimiter.acquire();
        shortLimiter.enter();
        longLimiter.enter();
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
            response = new Response(stringBuilder.toString());
            apiCache.put(requestURL, response);
            return response.getResponse();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private class Response {
        private String response;
        private long date;
        public Response(String response) {
            this.response = response;
            date = System.currentTimeMillis();
        }

        public String getResponse() {
            return response;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - date >= 600000; //10 minutes
        }
    }
}
