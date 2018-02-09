package org.stellar.sdk.requests;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Abstract class for request builders.
 */
public abstract class RequestBuilder {
    protected HttpUrl.Builder uriBuilder;
    private ArrayList<String> segments;
    private boolean segmentsAdded;

    private OkHttpClient sHttpClient;

    RequestBuilder(URI serverURI, String defaultSegment, OkHttpClient client) {
        uriBuilder = HttpUrl.get(serverURI).newBuilder();

        sHttpClient = client;

        segments = new ArrayList<String>();
        if (defaultSegment != null) {
            this.setSegments(defaultSegment);
        }
        segmentsAdded = false; // Allow overwriting segments
    }

    protected RequestBuilder setSegments(String... segments) {
        if (segmentsAdded) {
            throw new RuntimeException("URL segments have been already added.");
        }

        segmentsAdded = true;
        // Remove default segments
        this.segments.clear();
        for (String segment : segments) {
            this.segments.add(segment);
        }

        return this;
    }

    /**
     * Sets <code>cursor</code> parameter on the request.
     * A cursor is a value that points to a specific location in a collection of resources.
     * The cursor attribute itself is an opaque value meaning that users should not try to parse it.
     *
     * @param cursor
     * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/page.html">Page documentation</a>
     */
    public RequestBuilder cursor(String cursor) {
        uriBuilder.addQueryParameter("cursor", cursor);
        return this;
    }

    /**
     * Sets <code>limit</code> parameter on the request.
     * It defines maximum number of records to return.
     * For range and default values check documentation of the endpoint requested.
     *
     * @param number maxium number of records to return
     */
    public RequestBuilder limit(int number) {
        uriBuilder.addQueryParameter("limit", String.valueOf(number));
        return this;
    }

    /**
     * Sets <code>order</code> parameter on the request.
     *
     * @param direction {@link org.stellar.sdk.requests.RequestBuilder.Order}
     */
    public RequestBuilder order(Order direction) {
        uriBuilder.addQueryParameter("order", direction.getValue());
        return this;
    }

    HttpUrl buildUri() {
        if (segments.size() > 0) {
            for (String segment : segments) {
                uriBuilder.addPathSegment(segment);
            }
        }
        return uriBuilder.build();
    }

    String buildTarget() {
        return buildUri().toString().replaceAll("%", "%%");
    }

    public <T> T execute(HttpUrl url, ResponseHandler<T> handler) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return handler.handleResponse(sHttpClient.newCall(request).execute());
    }

    /**
     * Represents possible <code>order</code> parameter values.
     */
    public enum Order {
        ASC("asc"),
        DESC("desc");
        private final String value;

        Order(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
