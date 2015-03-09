/*
 * WBI Indicator Explorer
 *
 * Copyright 2015 Sebastian Nogara <snogaraleal@gmail.com>
 *
 * This file is part of WBI.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
