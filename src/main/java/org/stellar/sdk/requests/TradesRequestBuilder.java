package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.responses.TradeResponse;

import java.io.IOException;
import java.net.URI;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Builds requests connected to trades.
 */
public class TradesRequestBuilder extends RequestBuilder {
    public TradesRequestBuilder(URI serverURI, OkHttpClient client) {
        super(serverURI, "order_book/trades", client);
    }

    public TradesRequestBuilder buyingAsset(Asset asset) {
        uriBuilder.addQueryParameter("buying_asset_type", asset.getType());
        if (asset instanceof AssetTypeCreditAlphaNum) {
            AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
            uriBuilder.addQueryParameter("buying_asset_code", creditAlphaNumAsset.getCode());
            uriBuilder.addQueryParameter("buying_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
        }
        return this;
    }

    public TradesRequestBuilder sellingAsset(Asset asset) {
        uriBuilder.addQueryParameter("selling_asset_type", asset.getType());
        if (asset instanceof AssetTypeCreditAlphaNum) {
            AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
            uriBuilder.addQueryParameter("selling_asset_code", creditAlphaNumAsset.getCode());
            uriBuilder.addQueryParameter("selling_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
        }
        return this;
    }

    public static TradeResponse execute(HttpUrl url) throws IOException, TooManyRequestsException {
        TypeToken type = new TypeToken<TradeResponse>() {
        };
        ResponseHandler<TradeResponse> responseHandler = new ResponseHandler<TradeResponse>(type);
        return execute(url, responseHandler);
    }

    public TradeResponse execute() throws IOException, TooManyRequestsException {
        return this.execute(this.buildUri());
    }
}
