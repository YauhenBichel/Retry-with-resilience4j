package connection.resilient;

import connection.resilient.config.ResilienceConfiguration;
import connection.resilient.model.RetryRecord;
import connection.resilient.model.RetryStatus;
import connection.resilient.model.UrlState;
import io.github.resilience4j.retry.Retry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

/**
 * Http client with retry logic
 */
public class HttpClientWithRetry {
    private static Logger logger = LogManager.getLogger(HttpClientWithRetry.class);
    private Function<UrlState, Integer> getHttpCodeWithRetry;

    public HttpClientWithRetry(ResilienceConfiguration resilienceConfig) {
        getHttpCodeWithRetry = Retry.decorateFunction(resilienceConfig.getRetry(this.toString()), (url) -> {
            int httpStatusCode = getHttpResponseCode(url);
            System.out.println("Thread: " + Thread.currentThread() + ". URL: " + url + ". Http response code: " + httpStatusCode);
            logger.info("Thread: " + Thread.currentThread() + ". URL: " + url + ". Http response code: " + httpStatusCode);
            return httpStatusCode;
        });
    }

    /**
     * Fetches http response code
     *
     * @param urlState UrlState of resource
     * @return HTTP code
     */
    private int getHttpResponseCode(UrlState urlState) {
        int httpRespCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
        try {
            URL url = new URL(urlState.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //set false to automatically follow redirects
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            httpRespCode = connection.getResponseCode();
        } catch (IOException ex) {
            logger.error(ex);
        }

        updateUrlState(urlState, httpRespCode);

        return httpRespCode;
    }

    /**
     * Adds retry case to retry status
     * @param urlState urlState for specific url
     * @param httpCode http response code
     */
    private void updateUrlState(UrlState urlState, int httpCode) {
        List<RetryRecord> retries = urlState.getRetryRecords();
        if (retries == null) {
            retries = new ArrayList<>();
        }

        RetryStatus status = httpCode == HttpURLConnection.HTTP_OK ?
                RetryStatus.PASSED :
                RetryStatus.FAILED;

        RetryRecord record = new RetryRecord(UUID.randomUUID(), LocalDateTime.now(), httpCode, status);
        logger.debug(record);
        System.out.println("Thread is " + Thread.currentThread() + ". Retry: " + record);
        retries.add(new RetryRecord(UUID.randomUUID(), LocalDateTime.now(), httpCode, status));

        urlState.setRetryRecords(retries);
    }

    /**
     * Fetches http response code with retry
     * @param urlState url
     */
    public void retryGetHttpResponseCode(UrlState urlState, CountDownLatch latch) {
        System.out.println("Thread is " + Thread.currentThread());
        getHttpCodeWithRetry.apply(urlState);
        latch.countDown();
        System.out.println(urlState.toString());
    }
}
