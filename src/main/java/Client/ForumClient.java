package Client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import forum.ForumServiceGrpc;
import forum.Forum;

public class ForumClient {
    private final ForumServiceGrpc.ForumServiceBlockingStub blockingStub;

    public ForumClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = ForumServiceGrpc.newBlockingStub(channel);
    }

    public void createPost(String title, String text, String author) {
        Forum.CreatePostRequest request = Forum.CreatePostRequest.newBuilder()
                .setTitle(title)
                .setText(text)
                .setAuthor(author)
                .build();

        Forum.PostResponse response = blockingStub.createPost(request);
        System.out.println("Post created: " + response.getPost().getTitle());
    }

    public void votePost(String postId, boolean upvote) {
        Forum.VotePostRequest request = Forum.VotePostRequest.newBuilder()
                .setPostId(postId)
                .setUpvote(upvote)
                .build();

        Forum.VoteResponse response = blockingStub.votePost(request);
        System.out.println("New score for post " + postId + ": " + response.getNewScore());
    }

    public Forum.PostResponse getPost(String postId) {
        Forum.GetPostRequest request = Forum.GetPostRequest.newBuilder()
                .setPostId(postId)
                .build();

        Forum.PostResponse response = blockingStub.getPost(request);
        System.out.println("Retrieved post: " + response.getPost().getTitle());
        return response;
    }

    public void createComment(String postId, String author, String text) {
        Forum.CreateCommentRequest request = Forum.CreateCommentRequest.newBuilder()
                .setPostId(postId)
                .setAuthor(author)
                .setText(text)
                .build();

        Forum.CommentResponse response = blockingStub.createComment(request);
        System.out.println("Comment created by " + author + ": " + text);
    }

    public void voteComment(String commentId, boolean upvote) {
        Forum.VoteCommentRequest request = Forum.VoteCommentRequest.newBuilder()
                .setCommentId(commentId)
                .setUpvote(upvote)
                .build();

        Forum.VoteResponse response = blockingStub.voteComment(request);
        System.out.println("New score for comment " + commentId + ": " + response.getNewScore());
    }

    public Forum.TopCommentsResponse getTopComments(String postId, int n) {
        Forum.GetTopCommentsRequest request = Forum.GetTopCommentsRequest.newBuilder()
                .setPostId(postId)
                .setN(n)
                .build();

        Forum.TopCommentsResponse response = blockingStub.getTopComments(request);
        System.out.println("Top " + n + " comments for post " + postId + ":");
        response.getCommentsList().forEach(comment ->
                System.out.println(comment.getText() + " - Score: " + comment.getScore()));
        return response;
    }

    public Forum.CommentBranchResponse expandCommentBranch(Forum.ExpandCommentBranchRequest request) {
        return blockingStub.expandCommentBranch(request);
    }
}
