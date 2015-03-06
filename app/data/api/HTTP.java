package data.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HTTP {
    public static class URL {
        public static URI get(URI uri, String[][] params)
            throws URISyntaxException {

            URIBuilder uriBuilder = new URIBuilder(uri);

            for (String[] param : params) {
                uriBuilder.setParameter(param[0], param[1]);
            }

            return uriBuilder.build();
        }

        public static URI get(String baseURL, String path, String[][] params) {
            try {
                return get(new URI(baseURL).resolve(path), params);
            } catch (URISyntaxException exception) {
                exception.printStackTrace();
                return null;
            }
        }

        public static URI get(String url, String[][] params) {
            try {
                return get(new URI(url), params);
            } catch (URISyntaxException exception) {
                exception.printStackTrace();
                return null;
            }
        }
    } 

    public static String get(String url) {
        String response = null;

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpEntity entity = client.execute(new HttpGet(url)).getEntity();

            if (entity != null) {
                response = EntityUtils.toString(entity);
            }

            client.close();

        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }

        return response;
    }
}
