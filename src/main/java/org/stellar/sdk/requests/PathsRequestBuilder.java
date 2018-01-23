package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.PathResponse;

import java.io.IOException;
import java.net.URI;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Builds requests connected to paths.
 */
public class PathsRequestBuilder extends RequestBuilder {
    public PathsRequestBuilder(URI serverURI, OkHttpClient client) {
        super(serverURI, "paths", client);
    }

    public PathsRequestBuilder destinationAccount(KeyPair account) {
        uriBuilder.addQueryParameter("destination_account", account.getAccountId());
        return this;
    }

    public PathsRequestBuilder sourceAccount(KeyPair account) {
        uriBuilder.addQueryParameter("source_account", account.getAccountId());
        return this;
    }

    public PathsRequestBuilder destinationAmount(String amount) {
        uriBuilder.addQueryParameter("destination_amount", amount);
        return this;
    }

    public PathsRequestBuilder destinationAsset(Asset asset) {
        uriBuilder.addQueryParameter("destination_asset_type", asset.getType());
        if (asset instanceof AssetTypeCreditAlphaNum) {
            AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
            uriBuilder.addQueryParameter("destination_asset_code", creditAlphaNumAsset.getCode());
            uriBuilder.addQueryParameter("destination_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
        }
        return this;
    }

    /**
     * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
     * @throws IOException
     */
    public static Page<PathResponse> execute(HttpUrl url) throws IOException, TooManyRequestsException {
        TypeToken type = new TypeToken<Page<PathResponse>>() {
        };
        ResponseHandler<Page<PathResponse>> responseHandler = new ResponseHandler<Page<PathResponse>>(type);
        return execute(url, responseHandler);
    }

    /**
     * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
     * @throws IOException
     */
    public Page<PathResponse> execute() throws IOException, TooManyRequestsException {
        return this.execute(this.buildUri());
    }
}
