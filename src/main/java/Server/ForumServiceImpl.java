package Server;

import io.grpc.stub.StreamObserver;
import forum.ForumServiceGrpc;
import forum.Forum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ForumServiceImpl extends ForumServiceGrpc.ForumServiceImplBase {

    private final Map<String, Forum.Post> posts = new HashMap<>();
    private final Map<String, Forum.Comment> comments = new HashMap<>();
    private final AtomicInteger postCounter = new AtomicInteger();
    private final AtomicInteger commentCounter = new AtomicInteger();

    @Override
    public void createPost(Forum.CreatePostRequest req, StreamObserver<Forum.PostResponse> responseObserver) {
        String postId = "post" + postCounter.incrementAndGet();
        Forum.Post post = Forum.Post.newBuilder()
                .setTitle(req.getTitle())
                .setText(req.getText())
                .setScore(0)
                .setAuthor(req.getAuthor())
                .setPublicationDate("2023-01-01") // Placeholder date
                .build();
        posts.put(postId, post);

        Forum.PostResponse response = Forum.PostResponse.newBuilder().setPost(post).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void votePost(Forum.VotePostRequest req, StreamObserver<Forum.VoteResponse> responseObserver) {
        Forum.Post post = posts.get(req.getPostId());
        if (post != null) {
            int newScore = post.getScore() + (req.getUpvote() ? 1 : -1);
            Forum.Post updatedPost = Forum.Post.newBuilder(post)
                    .setScore(newScore)
                    .build();
            posts.put(req.getPostId(), updatedPost);

            Forum.VoteResponse response = Forum.VoteResponse.newBuilder().setNewScore(newScore).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("Post not found"));
        }
    }

    @Override
    public void getPost(Forum.GetPostRequest req, StreamObserver<Forum.PostResponse> responseObserver) {
        Forum.Post post = posts.get(req.getPostId());
        if (post != null) {
            Forum.PostResponse response = Forum.PostResponse.newBuilder().setPost(post).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("Post not found"));
        }
    }

    @Override
    public void createComment(Forum.CreateCommentRequest req, StreamObserver<Forum.CommentResponse> responseObserver) {
        String commentId = "comment" + commentCounter.incrementAndGet();
        Forum.Comment comment = Forum.Comment.newBuilder()
                .setAuthor(req.getAuthor())
                .setText(req.getText())
                .setScore(0)
                .setPublicationDate("2023-01-01") // Placeholder date
                .build();
        comments.put(commentId, comment);

        Forum.CommentResponse response = Forum.CommentResponse.newBuilder().setComment(comment).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void voteComment(Forum.VoteCommentRequest req, StreamObserver<Forum.VoteResponse> responseObserver) {
        Forum.Comment comment = comments.get(req.getCommentId());
        if (comment != null) {
            int newScore = comment.getScore() + (req.getUpvote() ? 1 : -1);
            Forum.Comment updatedComment = Forum.Comment.newBuilder(comment)
                    .setScore(newScore)
                    .build();
            comments.put(req.getCommentId(), updatedComment);

            Forum.VoteResponse response = Forum.VoteResponse.newBuilder().setNewScore(newScore).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("Comment not found"));
        }
    }

    @Override
    public void getTopComments(Forum.GetTopCommentsRequest req, StreamObserver<Forum.TopCommentsResponse> responseObserver) {
        Forum.TopCommentsResponse.Builder responseBuilder = Forum.TopCommentsResponse.newBuilder();
        comments.values().stream()
                .limit(req.getN())
                .forEach(responseBuilder::addComments);

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void expandCommentBranch(Forum.ExpandCommentBranchRequest req, StreamObserver<Forum.CommentBranchResponse> responseObserver) {
        Forum.CommentBranchResponse response = Forum.CommentBranchResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
