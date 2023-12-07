package Client;

public class ClientMain {
    public static void main(String[] args) {
        ForumClient client = new ForumClient("localhost", 50051);

        // Create a new post
        String newPostTitle = "Hello gRPC";
        String newPostText = "This is a sample post";
        String author = "User123";
        client.createPost(newPostTitle, newPostText, author);

        // Assuming the new post has an ID of "post1"
        String postId = "post1";

        // Vote on the post
        client.votePost(postId, true); // Upvote the post

        // Retrieve the post
        client.getPost(postId);

        // Create a comment on the post
        String commentText = "This is a sample comment";
        client.createComment(postId, author, commentText);

        // Assuming the new comment has an ID of "comment1"
        String commentId = "comment1";

        // Vote on the comment
        client.voteComment(commentId, true); // Upvote the comment

        // Retrieve the top N comments under a post
        int topN = 5;
        client.getTopComments(postId, topN);
    }
}
