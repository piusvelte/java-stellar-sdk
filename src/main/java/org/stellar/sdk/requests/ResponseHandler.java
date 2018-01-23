package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.Response;

import java.io.IOException;

import okhttp3.ResponseBody;

public class ResponseHandler<T> {

    private TypeToken<T> type;

    /**
     * "Generics on a type are typically erased at runtime, except when the type is compiled with the
     * generic parameter bound. In that case, the compiler inserts the generic type information into
     * the compiled class. In other cases, that is not possible."
     * More info: http://stackoverflow.com/a/14506181
     *
     * @param type
     */
    public ResponseHandler(TypeToken<T> type) {
        this.type = type;
    }

    public T handleResponse(final okhttp3.Response response) throws IOException, TooManyRequestsException {
        if (response.code() == 429) {
            int retryAfter = Integer.parseInt(response.header("Retry-After"));
            throw new TooManyRequestsException(retryAfter);
        }

        if (response.code() >= 300) {
            throw new IOException(String.valueOf(response.code()));
        }

        ResponseBody body = response.body();

        // No content
        if (body == null) {
            throw new IOException("Response contains no content");
        }

        T object = GsonSingleton.getInstance().fromJson(body.string(), type.getType());
        if (object instanceof Response) {
            ((Response) object).setHeaders(
                    response.header("X-Ratelimit-Limit"),
                    response.header("X-Ratelimit-Remaining"),
                    response.header("X-Ratelimit-Reset")
            );
        }
        return object;
    }
}
