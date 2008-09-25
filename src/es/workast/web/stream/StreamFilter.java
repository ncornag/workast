package es.workast.web.stream;


/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class StreamFilter {

    private Long firstId;

    private Integer maxResults;

    private Boolean backwards;

    private String type = "";

    private String[] tags;

    private boolean globalTags;

    private String text = "";

    public Long getFirstId() {
        return firstId == null ? 0 : firstId;
    }

    public void setFirstId(Long firstId) {
        this.firstId = firstId;
    }

    public Integer getMaxResults() {
        return maxResults == null ? 20 : maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Boolean getBackwards() {
        return backwards == null ? false : backwards;
    }

    public void setBackwards(Boolean backwards) {
        this.backwards = backwards;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getText() {
        return text == null ? "" : text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isGlobalTags() {
        return globalTags;
    }

    public void setGlobalTags(boolean globalTags) {
        this.globalTags = globalTags;
    }

}
