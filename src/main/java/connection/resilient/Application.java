package connection.resilient;

import connection.resilient.config.ResilienceConfiguration;
import connection.resilient.model.UrlState;
import connection.resilient.repository.TextFileUrlRepository;
import connection.resilient.repository.UrlRepository;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Application {

    public static void main(String[] args) {
        ResilienceConfiguration resilienceConfig = new ResilienceConfiguration();
        HttpClientWithRetry httpClient = new HttpClientWithRetry(resilienceConfig);
        final String filepath = "input/urls.txt";
        UrlRepository urlRepository = new TextFileUrlRepository(filepath);
        List<UrlState> urlStates = urlRepository.fetchAll();

        CountDownLatch latch = new CountDownLatch(urlStates.size());
        ExecutorService executor = Executors.newFixedThreadPool(urlStates.size());

        for(UrlState urlState: urlStates) {
            executor.submit(() -> httpClient.retryGetHttpResponseCode(urlState, latch));
        }

        try {
            latch.await();
            executor.shutdown();

            //for(UrlState urlState: urlStates) {
            //    System.out.println(urlState.toString());
            //}

            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
    }
}
