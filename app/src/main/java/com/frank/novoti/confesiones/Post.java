package com.frank.novoti.confesiones;

/**
 * Created by Claudina on 11/04/17.
 */

public class Post  { private String text;
    private int postId;
    private int numComent;

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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
