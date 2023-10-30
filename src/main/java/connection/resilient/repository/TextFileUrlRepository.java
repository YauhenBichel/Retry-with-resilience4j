package connection.resilient.repository;

import connection.resilient.model.UrlState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class TextFileUrlRepository implements UrlRepository {
    private static Logger logger = LogManager.getLogger(TextFileUrlRepository.class);
    private final String pathname;

    public TextFileUrlRepository(String pathname) {
        this.pathname = pathname;
    }

    @Override
    public List<UrlState> fetchAll() {
        List<UrlState> urls = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(pathname));
            while (scanner.hasNextLine()) {
                String url = scanner.nextLine();
                UrlState urlState = new UrlState(UUID.randomUUID(), url);
                urls.add(urlState);
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            logger.error(ex);
        }

        return urls;
    }
}
