package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.responses.OrderBookResponse;

import java.io.IOException;
import java.net.URI;

import okhttp3.OkHttpClient;

/**
 * Builds requests connected to order book.
 */
public class OrderBookRequestBuilder extends RequestBuilder {
    public OrderBookRequestBuilder(URI serverURI, OkHttpClient client) {
        super(serverURI, "order_book", client);
    }

    public OrderBookRequestBuilder buyingAsset(Asset asset) {
        uriBuilder.addQueryParameter("buying_asset_type", asset.getType());
        if (asset instanceof AssetTypeCreditAlphaNum) {
            AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
            uriBuilder.addQueryParameter("buying_asset_code", creditAlphaNumAsset.getCode());
            uriBuilder.addQueryParameter("buying_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
        }
        return this;
    }

    public OrderBookRequestBuilder sellingAsset(Asset asset) {
        uriBuilder.addQueryParameter("selling_asset_type", asset.getType());
        if (asset instanceof AssetTypeCreditAlphaNum) {
            AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
            uriBuilder.addQueryParameter("selling_asset_code", creditAlphaNumAsset.getCode());
            uriBuilder.addQueryParameter("selling_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
        }
        return this;
    }

    public OrderBookResponse execute() throws IOException, TooManyRequestsException {
        TypeToken type = new TypeToken<OrderBookResponse>() {
        };
        ResponseHandler<OrderBookResponse> responseHandler = new ResponseHandler<OrderBookResponse>(type);
        return execute(buildUri(), responseHandler);
    }

    @Override
    public RequestBuilder cursor(String cursor) {
        throw new RuntimeException("Not implemented yet.");
    }

    @Override
    public RequestBuilder limit(int number) {
        throw new RuntimeException("Not implemented yet.");
    }

    @Override
    public RequestBuilder order(Order direction) {
        throw new RuntimeException("Not implemented yet.");
    }
}
