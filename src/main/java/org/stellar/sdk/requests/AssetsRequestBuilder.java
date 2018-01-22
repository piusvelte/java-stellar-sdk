package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Request;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.AssetResponse;

import java.io.IOException;
import java.net.URI;

public class AssetsRequestBuilder extends RequestBuilder {
    public AssetsRequestBuilder(URI serverURI) {
        super(serverURI, "assets");
    }

    public AssetsRequestBuilder assetCode(String assetCode) {
        uriBuilder.addParameter("asset_code", assetCode);
        return this;
    }

    public AssetsRequestBuilder assetIssuer(String assetIssuer) {
        uriBuilder.addParameter("asset_issuer", assetIssuer);
        return this;
    }

    public static Page<AssetResponse> execute(URI uri) throws IOException, TooManyRequestsException {
        TypeToken type = new TypeToken<Page<AssetResponse>>() {};
        ResponseHandler<Page<AssetResponse>> responseHandler = new ResponseHandler<Page<AssetResponse>>(type);
        return (Page<AssetResponse>) Request.Get(uri).execute().handleResponse(responseHandler);
    }

    public Page<AssetResponse> execute() throws IOException, TooManyRequestsException {
        return this.execute(this.buildUri());
    }
}
