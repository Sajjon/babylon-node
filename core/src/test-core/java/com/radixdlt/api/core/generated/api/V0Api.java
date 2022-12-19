/*
 * Babylon Core API
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node. It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Heavy load may impact the node's function.  If you require queries against historical ledger state, you may also wish to consider using the [Gateway API](https://betanet-gateway.redoc.ly/). 
 *
 * The version of the OpenAPI document: 0.1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package com.radixdlt.api.core.generated.api;

import com.radixdlt.api.core.generated.client.ApiClient;
import com.radixdlt.api.core.generated.client.ApiException;
import com.radixdlt.api.core.generated.client.ApiResponse;
import com.radixdlt.api.core.generated.client.Pair;

import com.radixdlt.api.core.generated.models.ErrorResponse;
import com.radixdlt.api.core.generated.models.NetworkConfigurationResponse;
import com.radixdlt.api.core.generated.models.V0CommittedTransactionRequest;
import com.radixdlt.api.core.generated.models.V0CommittedTransactionResponse;
import com.radixdlt.api.core.generated.models.V0StateClockResponse;
import com.radixdlt.api.core.generated.models.V0StateComponentRequest;
import com.radixdlt.api.core.generated.models.V0StateComponentResponse;
import com.radixdlt.api.core.generated.models.V0StateEpochResponse;
import com.radixdlt.api.core.generated.models.V0StateNonFungibleRequest;
import com.radixdlt.api.core.generated.models.V0StateNonFungibleResponse;
import com.radixdlt.api.core.generated.models.V0StatePackageRequest;
import com.radixdlt.api.core.generated.models.V0StatePackageResponse;
import com.radixdlt.api.core.generated.models.V0StateResourceRequest;
import com.radixdlt.api.core.generated.models.V0StateResourceResponse;
import com.radixdlt.api.core.generated.models.V0TransactionStatusRequest;
import com.radixdlt.api.core.generated.models.V0TransactionStatusResponse;
import com.radixdlt.api.core.generated.models.V0TransactionSubmitRequest;
import com.radixdlt.api.core.generated.models.V0TransactionSubmitResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class V0Api {
  private final HttpClient memberVarHttpClient;
  private final ObjectMapper memberVarObjectMapper;
  private final String memberVarBaseUri;
  private final Consumer<HttpRequest.Builder> memberVarInterceptor;
  private final Duration memberVarReadTimeout;
  private final Consumer<HttpResponse<InputStream>> memberVarResponseInterceptor;
  private final Consumer<HttpResponse<String>> memberVarAsyncResponseInterceptor;

  public V0Api() {
    this(new ApiClient());
  }

  public V0Api(ApiClient apiClient) {
    memberVarHttpClient = apiClient.getHttpClient();
    memberVarObjectMapper = apiClient.getObjectMapper();
    memberVarBaseUri = apiClient.getBaseUri();
    memberVarInterceptor = apiClient.getRequestInterceptor();
    memberVarReadTimeout = apiClient.getReadTimeout();
    memberVarResponseInterceptor = apiClient.getResponseInterceptor();
    memberVarAsyncResponseInterceptor = apiClient.getAsyncResponseInterceptor();
  }

  protected ApiException getApiException(String operationId, HttpResponse<InputStream> response) throws IOException {
    String body = response.body() == null ? null : new String(response.body().readAllBytes());
    String message = formatExceptionMessage(operationId, response.statusCode(), body);
    return new ApiException(response.statusCode(), message, response.headers(), body);
  }

  private String formatExceptionMessage(String operationId, int statusCode, String body) {
    if (body == null || body.isEmpty()) {
      body = "[no body]";
    }
    return operationId + " call failed with: " + statusCode + " - " + body;
  }

  /**
   * Get Clock Details
   * Reads the clock&#39;s substate/s from the top of the current ledger. 
   * @return V0StateClockResponse
   * @throws ApiException if fails to make API call
   */
  public V0StateClockResponse v0StateClockPost() throws ApiException {
    ApiResponse<V0StateClockResponse> localVarResponse = v0StateClockPostWithHttpInfo();
    return localVarResponse.getData();
  }

  /**
   * Get Clock Details
   * Reads the clock&#39;s substate/s from the top of the current ledger. 
   * @return ApiResponse&lt;V0StateClockResponse&gt;
   * @throws ApiException if fails to make API call
   */
  public ApiResponse<V0StateClockResponse> v0StateClockPostWithHttpInfo() throws ApiException {
    HttpRequest.Builder localVarRequestBuilder = v0StateClockPostRequestBuilder();
    try {
      HttpResponse<InputStream> localVarResponse = memberVarHttpClient.send(
          localVarRequestBuilder.build(),
          HttpResponse.BodyHandlers.ofInputStream());
      if (memberVarResponseInterceptor != null) {
        memberVarResponseInterceptor.accept(localVarResponse);
      }
      try {
        if (localVarResponse.statusCode()/ 100 != 2) {
          throw getApiException("v0StateClockPost", localVarResponse);
        }
        return new ApiResponse<V0StateClockResponse>(
          localVarResponse.statusCode(),
          localVarResponse.headers().map(),
          memberVarObjectMapper.readValue(localVarResponse.body(), new TypeReference<V0StateClockResponse>() {}) // closes the InputStream
          
        );
      } finally {
      }
    } catch (IOException e) {
      throw new ApiException(e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiException(e);
    }
  }

  private HttpRequest.Builder v0StateClockPostRequestBuilder() throws ApiException {

    HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

    String localVarPath = "/v0/state/clock";

    localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

    localVarRequestBuilder.header("Accept", "application/json");

    localVarRequestBuilder.method("POST", HttpRequest.BodyPublishers.noBody());
    if (memberVarReadTimeout != null) {
      localVarRequestBuilder.timeout(memberVarReadTimeout);
    }
    if (memberVarInterceptor != null) {
      memberVarInterceptor.accept(localVarRequestBuilder);
    }
    return localVarRequestBuilder;
  }
  /**
   * Get Component Details
   * Reads the component&#39;s substate/s from the top of the current ledger. Also recursively extracts vault balance totals from the component&#39;s entity subtree. 
   * @param v0StateComponentRequest  (required)
   * @return V0StateComponentResponse
   * @throws ApiException if fails to make API call
   */
  public V0StateComponentResponse v0StateComponentPost(V0StateComponentRequest v0StateComponentRequest) throws ApiException {
    ApiResponse<V0StateComponentResponse> localVarResponse = v0StateComponentPostWithHttpInfo(v0StateComponentRequest);
    return localVarResponse.getData();
  }

  /**
   * Get Component Details
   * Reads the component&#39;s substate/s from the top of the current ledger. Also recursively extracts vault balance totals from the component&#39;s entity subtree. 
   * @param v0StateComponentRequest  (required)
   * @return ApiResponse&lt;V0StateComponentResponse&gt;
   * @throws ApiException if fails to make API call
   */
  public ApiResponse<V0StateComponentResponse> v0StateComponentPostWithHttpInfo(V0StateComponentRequest v0StateComponentRequest) throws ApiException {
    HttpRequest.Builder localVarRequestBuilder = v0StateComponentPostRequestBuilder(v0StateComponentRequest);
    try {
      HttpResponse<InputStream> localVarResponse = memberVarHttpClient.send(
          localVarRequestBuilder.build(),
          HttpResponse.BodyHandlers.ofInputStream());
      if (memberVarResponseInterceptor != null) {
        memberVarResponseInterceptor.accept(localVarResponse);
      }
      try {
        if (localVarResponse.statusCode()/ 100 != 2) {
          throw getApiException("v0StateComponentPost", localVarResponse);
        }
        return new ApiResponse<V0StateComponentResponse>(
          localVarResponse.statusCode(),
          localVarResponse.headers().map(),
          memberVarObjectMapper.readValue(localVarResponse.body(), new TypeReference<V0StateComponentResponse>() {}) // closes the InputStream
          
        );
      } finally {
      }
    } catch (IOException e) {
      throw new ApiException(e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiException(e);
    }
  }

  private HttpRequest.Builder v0StateComponentPostRequestBuilder(V0StateComponentRequest v0StateComponentRequest) throws ApiException {
    // verify the required parameter 'v0StateComponentRequest' is set
    if (v0StateComponentRequest == null) {
      throw new ApiException(400, "Missing the required parameter 'v0StateComponentRequest' when calling v0StateComponentPost");
    }

    HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

    String localVarPath = "/v0/state/component";

    localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

    localVarRequestBuilder.header("Content-Type", "application/json");
    localVarRequestBuilder.header("Accept", "application/json");

    try {
      byte[] localVarPostBody = memberVarObjectMapper.writeValueAsBytes(v0StateComponentRequest);
      localVarRequestBuilder.method("POST", HttpRequest.BodyPublishers.ofByteArray(localVarPostBody));
    } catch (IOException e) {
      throw new ApiException(e);
    }
    if (memberVarReadTimeout != null) {
      localVarRequestBuilder.timeout(memberVarReadTimeout);
    }
    if (memberVarInterceptor != null) {
      memberVarInterceptor.accept(localVarRequestBuilder);
    }
    return localVarRequestBuilder;
  }
  /**
   * Get Epoch Details
   * Reads the epoch manager&#39;s substate/s from the top of the current ledger. 
   * @return V0StateEpochResponse
   * @throws ApiException if fails to make API call
   */
  public V0StateEpochResponse v0StateEpochPost() throws ApiException {
    ApiResponse<V0StateEpochResponse> localVarResponse = v0StateEpochPostWithHttpInfo();
    return localVarResponse.getData();
  }

  /**
   * Get Epoch Details
   * Reads the epoch manager&#39;s substate/s from the top of the current ledger. 
   * @return ApiResponse&lt;V0StateEpochResponse&gt;
   * @throws ApiException if fails to make API call
   */
  public ApiResponse<V0StateEpochResponse> v0StateEpochPostWithHttpInfo() throws ApiException {
    HttpRequest.Builder localVarRequestBuilder = v0StateEpochPostRequestBuilder();
    try {
      HttpResponse<InputStream> localVarResponse = memberVarHttpClient.send(
          localVarRequestBuilder.build(),
          HttpResponse.BodyHandlers.ofInputStream());
      if (memberVarResponseInterceptor != null) {
        memberVarResponseInterceptor.accept(localVarResponse);
      }
      try {
        if (localVarResponse.statusCode()/ 100 != 2) {
          throw getApiException("v0StateEpochPost", localVarResponse);
        }
        return new ApiResponse<V0StateEpochResponse>(
          localVarResponse.statusCode(),
          localVarResponse.headers().map(),
          memberVarObjectMapper.readValue(localVarResponse.body(), new TypeReference<V0StateEpochResponse>() {}) // closes the InputStream
          
        );
      } finally {
      }
    } catch (IOException e) {
      throw new ApiException(e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiException(e);
    }
  }

  private HttpRequest.Builder v0StateEpochPostRequestBuilder() throws ApiException {

    HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

    String localVarPath = "/v0/state/epoch";

    localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

    localVarRequestBuilder.header("Accept", "application/json");

    localVarRequestBuilder.method("POST", HttpRequest.BodyPublishers.noBody());
    if (memberVarReadTimeout != null) {
      localVarRequestBuilder.timeout(memberVarReadTimeout);
    }
    if (memberVarInterceptor != null) {
      memberVarInterceptor.accept(localVarRequestBuilder);
    }
    return localVarRequestBuilder;
  }
  /**
   * Get Non-Fungible Details
   * Reads the data associated with a single Non-Fungible Unit under a Non-Fungible Resource. 
   * @param v0StateNonFungibleRequest  (required)
   * @return V0StateNonFungibleResponse
   * @throws ApiException if fails to make API call
   */
  public V0StateNonFungibleResponse v0StateNonFungiblePost(V0StateNonFungibleRequest v0StateNonFungibleRequest) throws ApiException {
    ApiResponse<V0StateNonFungibleResponse> localVarResponse = v0StateNonFungiblePostWithHttpInfo(v0StateNonFungibleRequest);
    return localVarResponse.getData();
  }

  /**
   * Get Non-Fungible Details
   * Reads the data associated with a single Non-Fungible Unit under a Non-Fungible Resource. 
   * @param v0StateNonFungibleRequest  (required)
   * @return ApiResponse&lt;V0StateNonFungibleResponse&gt;
   * @throws ApiException if fails to make API call
   */
  public ApiResponse<V0StateNonFungibleResponse> v0StateNonFungiblePostWithHttpInfo(V0StateNonFungibleRequest v0StateNonFungibleRequest) throws ApiException {
    HttpRequest.Builder localVarRequestBuilder = v0StateNonFungiblePostRequestBuilder(v0StateNonFungibleRequest);
    try {
      HttpResponse<InputStream> localVarResponse = memberVarHttpClient.send(
          localVarRequestBuilder.build(),
          HttpResponse.BodyHandlers.ofInputStream());
      if (memberVarResponseInterceptor != null) {
        memberVarResponseInterceptor.accept(localVarResponse);
      }
      try {
        if (localVarResponse.statusCode()/ 100 != 2) {
          throw getApiException("v0StateNonFungiblePost", localVarResponse);
        }
        return new ApiResponse<V0StateNonFungibleResponse>(
          localVarResponse.statusCode(),
          localVarResponse.headers().map(),
          memberVarObjectMapper.readValue(localVarResponse.body(), new TypeReference<V0StateNonFungibleResponse>() {}) // closes the InputStream
          
        );
      } finally {
      }
    } catch (IOException e) {
      throw new ApiException(e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiException(e);
    }
  }

  private HttpRequest.Builder v0StateNonFungiblePostRequestBuilder(V0StateNonFungibleRequest v0StateNonFungibleRequest) throws ApiException {
    // verify the required parameter 'v0StateNonFungibleRequest' is set
    if (v0StateNonFungibleRequest == null) {
      throw new ApiException(400, "Missing the required parameter 'v0StateNonFungibleRequest' when calling v0StateNonFungiblePost");
    }

    HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

    String localVarPath = "/v0/state/non-fungible";

    localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

    localVarRequestBuilder.header("Content-Type", "application/json");
    localVarRequestBuilder.header("Accept", "application/json");

    try {
      byte[] localVarPostBody = memberVarObjectMapper.writeValueAsBytes(v0StateNonFungibleRequest);
      localVarRequestBuilder.method("POST", HttpRequest.BodyPublishers.ofByteArray(localVarPostBody));
    } catch (IOException e) {
      throw new ApiException(e);
    }
    if (memberVarReadTimeout != null) {
      localVarRequestBuilder.timeout(memberVarReadTimeout);
    }
    if (memberVarInterceptor != null) {
      memberVarInterceptor.accept(localVarRequestBuilder);
    }
    return localVarRequestBuilder;
  }
  /**
   * Get Package Details
   * Reads the package&#39;s substate/s from the top of the current ledger. 
   * @param v0StatePackageRequest  (required)
   * @return V0StatePackageResponse
   * @throws ApiException if fails to make API call
   */
  public V0StatePackageResponse v0StatePackagePost(V0StatePackageRequest v0StatePackageRequest) throws ApiException {
    ApiResponse<V0StatePackageResponse> localVarResponse = v0StatePackagePostWithHttpInfo(v0StatePackageRequest);
    return localVarResponse.getData();
  }

  /**
   * Get Package Details
   * Reads the package&#39;s substate/s from the top of the current ledger. 
   * @param v0StatePackageRequest  (required)
   * @return ApiResponse&lt;V0StatePackageResponse&gt;
   * @throws ApiException if fails to make API call
   */
  public ApiResponse<V0StatePackageResponse> v0StatePackagePostWithHttpInfo(V0StatePackageRequest v0StatePackageRequest) throws ApiException {
    HttpRequest.Builder localVarRequestBuilder = v0StatePackagePostRequestBuilder(v0StatePackageRequest);
    try {
      HttpResponse<InputStream> localVarResponse = memberVarHttpClient.send(
          localVarRequestBuilder.build(),
          HttpResponse.BodyHandlers.ofInputStream());
      if (memberVarResponseInterceptor != null) {
        memberVarResponseInterceptor.accept(localVarResponse);
      }
      try {
        if (localVarResponse.statusCode()/ 100 != 2) {
          throw getApiException("v0StatePackagePost", localVarResponse);
        }
        return new ApiResponse<V0StatePackageResponse>(
          localVarResponse.statusCode(),
          localVarResponse.headers().map(),
          memberVarObjectMapper.readValue(localVarResponse.body(), new TypeReference<V0StatePackageResponse>() {}) // closes the InputStream
          
        );
      } finally {
      }
    } catch (IOException e) {
      throw new ApiException(e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiException(e);
    }
  }

  private HttpRequest.Builder v0StatePackagePostRequestBuilder(V0StatePackageRequest v0StatePackageRequest) throws ApiException {
    // verify the required parameter 'v0StatePackageRequest' is set
    if (v0StatePackageRequest == null) {
      throw new ApiException(400, "Missing the required parameter 'v0StatePackageRequest' when calling v0StatePackagePost");
    }

    HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

    String localVarPath = "/v0/state/package";

    localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

    localVarRequestBuilder.header("Content-Type", "application/json");
    localVarRequestBuilder.header("Accept", "application/json");

    try {
      byte[] localVarPostBody = memberVarObjectMapper.writeValueAsBytes(v0StatePackageRequest);
      localVarRequestBuilder.method("POST", HttpRequest.BodyPublishers.ofByteArray(localVarPostBody));
    } catch (IOException e) {
      throw new ApiException(e);
    }
    if (memberVarReadTimeout != null) {
      localVarRequestBuilder.timeout(memberVarReadTimeout);
    }
    if (memberVarInterceptor != null) {
      memberVarInterceptor.accept(localVarRequestBuilder);
    }
    return localVarRequestBuilder;
  }
  /**
   * Get Resource Details
   * Reads the resource manager&#39;s substate/s from the top of the current ledger. 
   * @param v0StateResourceRequest  (required)
   * @return V0StateResourceResponse
   * @throws ApiException if fails to make API call
   */
  public V0StateResourceResponse v0StateResourcePost(V0StateResourceRequest v0StateResourceRequest) throws ApiException {
    ApiResponse<V0StateResourceResponse> localVarResponse = v0StateResourcePostWithHttpInfo(v0StateResourceRequest);
    return localVarResponse.getData();
  }

  /**
   * Get Resource Details
   * Reads the resource manager&#39;s substate/s from the top of the current ledger. 
   * @param v0StateResourceRequest  (required)
   * @return ApiResponse&lt;V0StateResourceResponse&gt;
   * @throws ApiException if fails to make API call
   */
  public ApiResponse<V0StateResourceResponse> v0StateResourcePostWithHttpInfo(V0StateResourceRequest v0StateResourceRequest) throws ApiException {
    HttpRequest.Builder localVarRequestBuilder = v0StateResourcePostRequestBuilder(v0StateResourceRequest);
    try {
      HttpResponse<InputStream> localVarResponse = memberVarHttpClient.send(
          localVarRequestBuilder.build(),
          HttpResponse.BodyHandlers.ofInputStream());
      if (memberVarResponseInterceptor != null) {
        memberVarResponseInterceptor.accept(localVarResponse);
      }
      try {
        if (localVarResponse.statusCode()/ 100 != 2) {
          throw getApiException("v0StateResourcePost", localVarResponse);
        }
        return new ApiResponse<V0StateResourceResponse>(
          localVarResponse.statusCode(),
          localVarResponse.headers().map(),
          memberVarObjectMapper.readValue(localVarResponse.body(), new TypeReference<V0StateResourceResponse>() {}) // closes the InputStream
          
        );
      } finally {
      }
    } catch (IOException e) {
      throw new ApiException(e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiException(e);
    }
  }

  private HttpRequest.Builder v0StateResourcePostRequestBuilder(V0StateResourceRequest v0StateResourceRequest) throws ApiException {
    // verify the required parameter 'v0StateResourceRequest' is set
    if (v0StateResourceRequest == null) {
      throw new ApiException(400, "Missing the required parameter 'v0StateResourceRequest' when calling v0StateResourcePost");
    }

    HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

    String localVarPath = "/v0/state/resource";

    localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

    localVarRequestBuilder.header("Content-Type", "application/json");
    localVarRequestBuilder.header("Accept", "application/json");

    try {
      byte[] localVarPostBody = memberVarObjectMapper.writeValueAsBytes(v0StateResourceRequest);
      localVarRequestBuilder.method("POST", HttpRequest.BodyPublishers.ofByteArray(localVarPostBody));
    } catch (IOException e) {
      throw new ApiException(e);
    }
    if (memberVarReadTimeout != null) {
      localVarRequestBuilder.timeout(memberVarReadTimeout);
    }
    if (memberVarInterceptor != null) {
      memberVarInterceptor.accept(localVarRequestBuilder);
    }
    return localVarRequestBuilder;
  }
  /**
   * Get Network Configuration (V0)
   * Returns the network configuration.
   * @return NetworkConfigurationResponse
   * @throws ApiException if fails to make API call
   */
  public NetworkConfigurationResponse v0StatusNetworkConfigurationPost() throws ApiException {
    ApiResponse<NetworkConfigurationResponse> localVarResponse = v0StatusNetworkConfigurationPostWithHttpInfo();
    return localVarResponse.getData();
  }

  /**
   * Get Network Configuration (V0)
   * Returns the network configuration.
   * @return ApiResponse&lt;NetworkConfigurationResponse&gt;
   * @throws ApiException if fails to make API call
   */
  public ApiResponse<NetworkConfigurationResponse> v0StatusNetworkConfigurationPostWithHttpInfo() throws ApiException {
    HttpRequest.Builder localVarRequestBuilder = v0StatusNetworkConfigurationPostRequestBuilder();
    try {
      HttpResponse<InputStream> localVarResponse = memberVarHttpClient.send(
          localVarRequestBuilder.build(),
          HttpResponse.BodyHandlers.ofInputStream());
      if (memberVarResponseInterceptor != null) {
        memberVarResponseInterceptor.accept(localVarResponse);
      }
      try {
        if (localVarResponse.statusCode()/ 100 != 2) {
          throw getApiException("v0StatusNetworkConfigurationPost", localVarResponse);
        }
        return new ApiResponse<NetworkConfigurationResponse>(
          localVarResponse.statusCode(),
          localVarResponse.headers().map(),
          memberVarObjectMapper.readValue(localVarResponse.body(), new TypeReference<NetworkConfigurationResponse>() {}) // closes the InputStream
          
        );
      } finally {
      }
    } catch (IOException e) {
      throw new ApiException(e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiException(e);
    }
  }

  private HttpRequest.Builder v0StatusNetworkConfigurationPostRequestBuilder() throws ApiException {

    HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

    String localVarPath = "/v0/status/network-configuration";

    localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

    localVarRequestBuilder.header("Accept", "application/json");

    localVarRequestBuilder.method("POST", HttpRequest.BodyPublishers.noBody());
    if (memberVarReadTimeout != null) {
      localVarRequestBuilder.timeout(memberVarReadTimeout);
    }
    if (memberVarInterceptor != null) {
      memberVarInterceptor.accept(localVarRequestBuilder);
    }
    return localVarRequestBuilder;
  }
  /**
   * Get Transaction Receipt
   * Gets the transaction receipt for a committed transaction. 
   * @param v0CommittedTransactionRequest  (required)
   * @return V0CommittedTransactionResponse
   * @throws ApiException if fails to make API call
   */
  public V0CommittedTransactionResponse v0TransactionReceiptPost(V0CommittedTransactionRequest v0CommittedTransactionRequest) throws ApiException {
    ApiResponse<V0CommittedTransactionResponse> localVarResponse = v0TransactionReceiptPostWithHttpInfo(v0CommittedTransactionRequest);
    return localVarResponse.getData();
  }

  /**
   * Get Transaction Receipt
   * Gets the transaction receipt for a committed transaction. 
   * @param v0CommittedTransactionRequest  (required)
   * @return ApiResponse&lt;V0CommittedTransactionResponse&gt;
   * @throws ApiException if fails to make API call
   */
  public ApiResponse<V0CommittedTransactionResponse> v0TransactionReceiptPostWithHttpInfo(V0CommittedTransactionRequest v0CommittedTransactionRequest) throws ApiException {
    HttpRequest.Builder localVarRequestBuilder = v0TransactionReceiptPostRequestBuilder(v0CommittedTransactionRequest);
    try {
      HttpResponse<InputStream> localVarResponse = memberVarHttpClient.send(
          localVarRequestBuilder.build(),
          HttpResponse.BodyHandlers.ofInputStream());
      if (memberVarResponseInterceptor != null) {
        memberVarResponseInterceptor.accept(localVarResponse);
      }
      try {
        if (localVarResponse.statusCode()/ 100 != 2) {
          throw getApiException("v0TransactionReceiptPost", localVarResponse);
        }
        return new ApiResponse<V0CommittedTransactionResponse>(
          localVarResponse.statusCode(),
          localVarResponse.headers().map(),
          memberVarObjectMapper.readValue(localVarResponse.body(), new TypeReference<V0CommittedTransactionResponse>() {}) // closes the InputStream
          
        );
      } finally {
      }
    } catch (IOException e) {
      throw new ApiException(e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiException(e);
    }
  }

  private HttpRequest.Builder v0TransactionReceiptPostRequestBuilder(V0CommittedTransactionRequest v0CommittedTransactionRequest) throws ApiException {
    // verify the required parameter 'v0CommittedTransactionRequest' is set
    if (v0CommittedTransactionRequest == null) {
      throw new ApiException(400, "Missing the required parameter 'v0CommittedTransactionRequest' when calling v0TransactionReceiptPost");
    }

    HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

    String localVarPath = "/v0/transaction/receipt";

    localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

    localVarRequestBuilder.header("Content-Type", "application/json");
    localVarRequestBuilder.header("Accept", "application/json");

    try {
      byte[] localVarPostBody = memberVarObjectMapper.writeValueAsBytes(v0CommittedTransactionRequest);
      localVarRequestBuilder.method("POST", HttpRequest.BodyPublishers.ofByteArray(localVarPostBody));
    } catch (IOException e) {
      throw new ApiException(e);
    }
    if (memberVarReadTimeout != null) {
      localVarRequestBuilder.timeout(memberVarReadTimeout);
    }
    if (memberVarInterceptor != null) {
      memberVarInterceptor.accept(localVarRequestBuilder);
    }
    return localVarRequestBuilder;
  }
  /**
   * Get Transaction Status
   * Shares the node&#39;s knowledge of any payloads associated with the given intent hash. Generally there will be a single payload for a given intent, but it&#39;s theoretically possible there may be multiple. This knowledge is summarised into a status for the intent. This summarised status in the response is likely sufficient for most clients. 
   * @param v0TransactionStatusRequest  (required)
   * @return V0TransactionStatusResponse
   * @throws ApiException if fails to make API call
   */
  public V0TransactionStatusResponse v0TransactionStatusPost(V0TransactionStatusRequest v0TransactionStatusRequest) throws ApiException {
    ApiResponse<V0TransactionStatusResponse> localVarResponse = v0TransactionStatusPostWithHttpInfo(v0TransactionStatusRequest);
    return localVarResponse.getData();
  }

  /**
   * Get Transaction Status
   * Shares the node&#39;s knowledge of any payloads associated with the given intent hash. Generally there will be a single payload for a given intent, but it&#39;s theoretically possible there may be multiple. This knowledge is summarised into a status for the intent. This summarised status in the response is likely sufficient for most clients. 
   * @param v0TransactionStatusRequest  (required)
   * @return ApiResponse&lt;V0TransactionStatusResponse&gt;
   * @throws ApiException if fails to make API call
   */
  public ApiResponse<V0TransactionStatusResponse> v0TransactionStatusPostWithHttpInfo(V0TransactionStatusRequest v0TransactionStatusRequest) throws ApiException {
    HttpRequest.Builder localVarRequestBuilder = v0TransactionStatusPostRequestBuilder(v0TransactionStatusRequest);
    try {
      HttpResponse<InputStream> localVarResponse = memberVarHttpClient.send(
          localVarRequestBuilder.build(),
          HttpResponse.BodyHandlers.ofInputStream());
      if (memberVarResponseInterceptor != null) {
        memberVarResponseInterceptor.accept(localVarResponse);
      }
      try {
        if (localVarResponse.statusCode()/ 100 != 2) {
          throw getApiException("v0TransactionStatusPost", localVarResponse);
        }
        return new ApiResponse<V0TransactionStatusResponse>(
          localVarResponse.statusCode(),
          localVarResponse.headers().map(),
          memberVarObjectMapper.readValue(localVarResponse.body(), new TypeReference<V0TransactionStatusResponse>() {}) // closes the InputStream
          
        );
      } finally {
      }
    } catch (IOException e) {
      throw new ApiException(e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiException(e);
    }
  }

  private HttpRequest.Builder v0TransactionStatusPostRequestBuilder(V0TransactionStatusRequest v0TransactionStatusRequest) throws ApiException {
    // verify the required parameter 'v0TransactionStatusRequest' is set
    if (v0TransactionStatusRequest == null) {
      throw new ApiException(400, "Missing the required parameter 'v0TransactionStatusRequest' when calling v0TransactionStatusPost");
    }

    HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

    String localVarPath = "/v0/transaction/status";

    localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

    localVarRequestBuilder.header("Content-Type", "application/json");
    localVarRequestBuilder.header("Accept", "application/json");

    try {
      byte[] localVarPostBody = memberVarObjectMapper.writeValueAsBytes(v0TransactionStatusRequest);
      localVarRequestBuilder.method("POST", HttpRequest.BodyPublishers.ofByteArray(localVarPostBody));
    } catch (IOException e) {
      throw new ApiException(e);
    }
    if (memberVarReadTimeout != null) {
      localVarRequestBuilder.timeout(memberVarReadTimeout);
    }
    if (memberVarInterceptor != null) {
      memberVarInterceptor.accept(localVarRequestBuilder);
    }
    return localVarRequestBuilder;
  }
  /**
   * Transaction Submit (V0)
   * Submits a notarized transaction to the network. Returns whether the transaction submission was already included in the node&#39;s mempool. This will be removed when V0 endpoints are merged into the main API. 
   * @param v0TransactionSubmitRequest  (required)
   * @return V0TransactionSubmitResponse
   * @throws ApiException if fails to make API call
   */
  public V0TransactionSubmitResponse v0TransactionSubmitPost(V0TransactionSubmitRequest v0TransactionSubmitRequest) throws ApiException {
    ApiResponse<V0TransactionSubmitResponse> localVarResponse = v0TransactionSubmitPostWithHttpInfo(v0TransactionSubmitRequest);
    return localVarResponse.getData();
  }

  /**
   * Transaction Submit (V0)
   * Submits a notarized transaction to the network. Returns whether the transaction submission was already included in the node&#39;s mempool. This will be removed when V0 endpoints are merged into the main API. 
   * @param v0TransactionSubmitRequest  (required)
   * @return ApiResponse&lt;V0TransactionSubmitResponse&gt;
   * @throws ApiException if fails to make API call
   */
  public ApiResponse<V0TransactionSubmitResponse> v0TransactionSubmitPostWithHttpInfo(V0TransactionSubmitRequest v0TransactionSubmitRequest) throws ApiException {
    HttpRequest.Builder localVarRequestBuilder = v0TransactionSubmitPostRequestBuilder(v0TransactionSubmitRequest);
    try {
      HttpResponse<InputStream> localVarResponse = memberVarHttpClient.send(
          localVarRequestBuilder.build(),
          HttpResponse.BodyHandlers.ofInputStream());
      if (memberVarResponseInterceptor != null) {
        memberVarResponseInterceptor.accept(localVarResponse);
      }
      try {
        if (localVarResponse.statusCode()/ 100 != 2) {
          throw getApiException("v0TransactionSubmitPost", localVarResponse);
        }
        return new ApiResponse<V0TransactionSubmitResponse>(
          localVarResponse.statusCode(),
          localVarResponse.headers().map(),
          memberVarObjectMapper.readValue(localVarResponse.body(), new TypeReference<V0TransactionSubmitResponse>() {}) // closes the InputStream
          
        );
      } finally {
      }
    } catch (IOException e) {
      throw new ApiException(e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiException(e);
    }
  }

  private HttpRequest.Builder v0TransactionSubmitPostRequestBuilder(V0TransactionSubmitRequest v0TransactionSubmitRequest) throws ApiException {
    // verify the required parameter 'v0TransactionSubmitRequest' is set
    if (v0TransactionSubmitRequest == null) {
      throw new ApiException(400, "Missing the required parameter 'v0TransactionSubmitRequest' when calling v0TransactionSubmitPost");
    }

    HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

    String localVarPath = "/v0/transaction/submit";

    localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

    localVarRequestBuilder.header("Content-Type", "application/json");
    localVarRequestBuilder.header("Accept", "application/json");

    try {
      byte[] localVarPostBody = memberVarObjectMapper.writeValueAsBytes(v0TransactionSubmitRequest);
      localVarRequestBuilder.method("POST", HttpRequest.BodyPublishers.ofByteArray(localVarPostBody));
    } catch (IOException e) {
      throw new ApiException(e);
    }
    if (memberVarReadTimeout != null) {
      localVarRequestBuilder.timeout(memberVarReadTimeout);
    }
    if (memberVarInterceptor != null) {
      memberVarInterceptor.accept(localVarRequestBuilder);
    }
    return localVarRequestBuilder;
  }
}
