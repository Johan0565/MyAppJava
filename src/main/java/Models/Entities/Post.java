package Models.Entities;

public class Post {
    private int postId;
    private String postName;

    public Post(int postId, String postName) {
        this.postId = postId;
        this.postName = postName;
    }

    public int getPostId() { return postId; }
    public String getPostName() { return postName; }

    @Override
    public String toString() {
        return "Vacant post: id = " + postId + ", name = \"" + postName + "\".";
    }


}