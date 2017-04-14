package com.frank.novoti.confesiones;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post  {
    private String text;
    private String category;
    private int numComent;
    private int likes;
    private int dislikes;
    private Date sentDate;
    private List<String> tags;
    private boolean approved;
    private int moderationLikes;
    private int moderationDislikes;

    public Post() {
    }

    public List<String> getTags() {
        return tags;
    }

    public int getModerationLikes() {
        return moderationLikes;
    }

    public void setModerationLikes(int moderationLikes) {
        this.moderationLikes = moderationLikes;
    }

    public int getModerationDislikes() {
        return moderationDislikes;
    }

    public void setModerationDislikes(int moderationDislikes) {
        this.moderationDislikes = moderationDislikes;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Post(String text, Date sentDate, List<String> tags, String category) {
        this.text = text;
        this.category = category;
        this.sentDate = sentDate;
        this.tags = tags;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getNumComent() {
        return numComent;
    }

    public void setNumComent(int numComent) {
        this.numComent = numComent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("category", category);
        result.put("numComent", numComent);
        result.put("likes", likes);
        result.put("dislikes", dislikes);
        result.put("sentDate", sentDate);
        result.put("tags", tags);
        result.put("approved", approved);
        result.put("moderationLikes", moderationLikes);
        result.put("moderationDislikes", moderationDislikes);

        return result;
    }

}
