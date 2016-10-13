package com.schinizer.hackernews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Item {

    @SerializedName("by")
    @Expose
    private String by;
    @SerializedName("descendants")
    @Expose
    private Integer descendants;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("deleted")
    @Expose
    private Boolean deleted;
    @SerializedName("dead")
    @Expose
    private Boolean dead;
    @SerializedName("parent")
    @Expose
    private Integer parent;
    @SerializedName("kids")
    @Expose
    private List<Integer> kids = new ArrayList<Integer>();
    @SerializedName("parts")
    @Expose
    private List<Integer> parts = new ArrayList<Integer>();
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("time")
    @Expose
    private Date time;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * @return The by
     */
    public String getBy() {
        return by;
    }

    /**
     * @param by The by
     */
    public void setBy(String by) {
        this.by = by;
    }

    /**
     * @return The descendants
     */
    public Integer getDescendants() {
        return descendants;
    }

    /**
     * @param descendants The descendants
     */
    public void setDescendants(Integer descendants) {
        this.descendants = descendants;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The deleted
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * @param deleted The deleted
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return The dead
     */
    public Boolean getDead() {
        return dead;
    }

    /**
     * @param dead The dead
     */
    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    /**
     * @return The parent
     */
    public Integer getParent() {
        return parent;
    }

    /**
     * @param parent The parent
     */
    public void setParent(Integer parent) {
        this.parent = parent;
    }

    /**
     * @return The kids
     */
    public List<Integer> getKids() {
        return kids;
    }

    /**
     * @param kids The kids
     */
    public void setKids(List<Integer> kids) {
        this.kids = kids;
    }

    /**
     * @return The parts
     */
    public List<Integer> getParts() {
        return parts;
    }

    /**
     * @param parts The parts
     */
    public void setParts(List<Integer> parts) {
        this.parts = parts;
    }

    /**
     * @return The score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * @param score The score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * @return The text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return The time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Item{" +
                "by='" + by + '\'' +
                ", descendants=" + descendants +
                ", id=" + id +
                ", deleted=" + deleted +
                ", dead=" + dead +
                ", parent=" + parent +
                ", kids=" + kids +
                ", parts=" + parts +
                ", score=" + score +
                ", text='" + text + '\'' +
                ", time=" + time +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}