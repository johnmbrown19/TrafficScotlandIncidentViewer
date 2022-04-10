package org.me.gcu.trafficapptest;
// John Brown S1917384

public class CurrentIncident {
    private String title;
    private String desc;
    private String link;
    private String georss;
    private String pubDate;

    public CurrentIncident(String title, String desc, String link, String georss, String pubDate) {
        this.title = title;
        this.desc = desc;
        this.link = link;
        this.georss = georss;
        this.pubDate = pubDate;
    }

    public CurrentIncident() {}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setGeorss(String georss) {
        this.georss = georss;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getLink() {
        return link;
    }

    public String getGeorss() {
        return georss;
    }

    public String getPubDate() {
        return pubDate;
    }

    @Override
    public String toString() {
        return "CurrentIncident{" +
                "title='" + title + '\'' +
                ", description='" + desc + '\'' +
                ", link='" + link + '\'' +
                ", georss='" + georss + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }
}
