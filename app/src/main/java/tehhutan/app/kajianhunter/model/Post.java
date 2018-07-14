package tehhutan.app.kajianhunter.model;

import java.io.Serializable;

public class Post implements Serializable {
    private User user;
    private String postText;
    private String postId;
    private String postTitle;
    private long numLikes;
    private long numComments;

    public Post() {
    }

    public Post(User user, String postText, String postId, long numLikes, long numComments, String posttitle) {

        this.user = user;
        this.postText = postText;
        this.postId = postId;
        this.numLikes = numLikes;
        this.numComments = numComments;
        this.postTitle = posttitle;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(long numLikes) {
        this.numLikes = numLikes;
    }

    public long getNumComments() {
        return numComments;
    }

    public void setNumComments(long numComments) {
        this.numComments = numComments;
    }

}
