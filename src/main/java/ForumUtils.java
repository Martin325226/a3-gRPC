import Client.ForumClient;
import forum.Forum;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ForumUtils {

    public static Forum.Comment getMostUpvotedReply(ForumClient client, String postId) {
        Forum.PostResponse postResponse = client.getPost(postId);
        if (postResponse == null || postResponse.getPost() == null) {
            return null;
        }

        Forum.TopCommentsResponse topCommentsResponse = client.getTopComments(postId, Integer.MAX_VALUE);
        if (topCommentsResponse == null || topCommentsResponse.getCommentsList().isEmpty()) {
            return null;
        }

        Forum.Comment mostUpvotedComment = topCommentsResponse.getCommentsList().stream()
                .max(Comparator.comparingInt(Forum.Comment::getScore))
                .orElse(null);

        if (mostUpvotedComment == null) {
            return null;
        }

        String commentId = mostUpvotedComment.getCommentId(); // Assuming 'getCommentId' method exists
        Forum.ExpandCommentBranchRequest request = Forum.ExpandCommentBranchRequest.newBuilder()
                .setCommentId(commentId)
                .build();
        Forum.CommentBranchResponse commentBranchResponse = client.expandCommentBranch(request);

        List<Forum.Comment> replies = commentBranchResponse.getCommentsList().stream()
                .filter(comment -> !commentId.equals(comment.getCommentId())) // Assuming 'getCommentId' method exists
                .collect(Collectors.toList());

        return replies.stream()
                .max(Comparator.comparingInt(Forum.Comment::getScore))
                .orElse(null);
    }
}
