package connection.resilient.model;

import java.util.List;
import java.util.UUID;

public class UrlState {
    private UUID id;
    private String url;
    private List<RetryRecord> retryRecords;

    public UrlState(UUID id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    public List<RetryRecord> getRetryRecords() {
        return retryRecords;
    }
    public void setRetryRecords(List<RetryRecord> retryRecords) {
        this.retryRecords = retryRecords;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UrlState{");
        sb.append("id=").append(id);
        sb.append(", url='").append(url).append('\'');
        sb.append(", retryRecords amount=").append(retryRecords.size());
        sb.append(", retryRecords=").append(retryRecords);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlState)) return false;

        UrlState that = (UrlState) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        return retryRecords != null ? retryRecords.equals(that.retryRecords) : that.retryRecords == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (retryRecords != null ? retryRecords.hashCode() : 0);
        return result;
    }
}
