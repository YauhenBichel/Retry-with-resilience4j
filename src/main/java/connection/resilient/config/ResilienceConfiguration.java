package connection.resilient.config;

import connection.resilient.Constants;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.HttpURLConnection;
import java.time.Duration;

public class ResilienceConfiguration {
    private static Logger logger = LogManager.getLogger(ResilienceConfiguration.class);

    public Retry getRetry(String retryName) {
        RetryConfig config = RetryConfig.<Integer>custom()
                .maxAttempts(Constants.MAX_ATTEMPTS)
                .waitDuration(Duration.ofSeconds(Constants.WAIT_SECONDS))
                .retryOnResult(httpCode -> httpCode != HttpURLConnection.HTTP_OK)
                .build();
        RetryRegistry registry = RetryRegistry.of(config);
        logger.info("Retry settings is configured");

        Retry retry = registry.retry(retryName);
        retry.getEventPublisher().onRetry(e -> {
            logger.info("Retrying: " + e.getCreationTime() + "; " + e.getNumberOfRetryAttempts() +
                    "; " + e.getWaitInterval());
        });

        return retry;
    }
}
