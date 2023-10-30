package connection.resilient.repository;

import connection.resilient.model.UrlState;

import java.util.List;

public interface UrlRepository {
    List<UrlState> fetchAll();
}
