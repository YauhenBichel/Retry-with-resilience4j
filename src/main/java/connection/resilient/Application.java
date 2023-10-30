package connection.resilient;

import connection.resilient.config.ResilienceConfiguration;
import connection.resilient.model.UrlState;
import connection.resilient.repository.TextFileUrlRepository;
import connection.resilient.repository.UrlRepository;

import java.util.List;

public class Application {

    public static void main(String[] args) {
        ResilienceConfiguration resilienceConfig = new ResilienceConfiguration();
        HttpClientWithRetry httpClient = new HttpClientWithRetry(resilienceConfig);

        UrlRepository urlRepository = new TextFileUrlRepository("input/urls.txt");
        List<UrlState> urls = urlRepository.fetchAll();

        httpClient.retryGetHttpResponseCode(urls);

        for(UrlState url: urls) {
            System.out.println(url.toString());
        }
    }
}
