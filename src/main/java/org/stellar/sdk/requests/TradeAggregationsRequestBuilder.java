package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Request;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.TradeAggregationResponse;

import java.io.IOException;
import java.net.URI;

/**
 * Builds requests connected to trades.
 */
public class TradeAggregationsRequestBuilder extends RequestBuilder {
    public TradeAggregationsRequestBuilder(URI serverURI, Asset baseAsset, Asset counterAsset, long startTime, long endTime, long resolution) {
        super(serverURI, "trade_aggregations");

        this.baseAsset(baseAsset);
        this.counterAsset(counterAsset);
        uriBuilder.addParameter("start_time", String.valueOf(startTime));
        uriBuilder.addParameter("end_time", String.valueOf(endTime));
        uriBuilder.addParameter("resolution", String.valueOf(resolution));
    }

    private void baseAsset(Asset asset) {
        uriBuilder.addParameter("base_asset_type", asset.getType());
        if (asset instanceof AssetTypeCreditAlphaNum) {
            AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
            uriBuilder.addParameter("base_asset_code", creditAlphaNumAsset.getCode());
            uriBuilder.addParameter("base_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
        }
    }

    private void counterAsset(Asset asset) {
        uriBuilder.addParameter("counter_asset_type", asset.getType());
        if (asset instanceof AssetTypeCreditAlphaNum) {
            AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
            uriBuilder.addParameter("counter_asset_code", creditAlphaNumAsset.getCode());
            uriBuilder.addParameter("counter_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
        }
    }

    public static Page<TradeAggregationResponse> execute(URI uri) throws IOException, TooManyRequestsException {
        TypeToken type = new TypeToken<Page<TradeAggregationResponse>>() {};
        ResponseHandler<Page<TradeAggregationResponse>> responseHandler = new ResponseHandler<Page<TradeAggregationResponse>>(type);
        return (Page<TradeAggregationResponse>) Request.Get(uri).execute().handleResponse(responseHandler);
    }

    public Page<TradeAggregationResponse> execute() throws IOException, TooManyRequestsException {
        return this.execute(this.buildUri());
    }
}
