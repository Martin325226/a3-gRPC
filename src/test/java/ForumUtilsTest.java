import Client.ForumClient;
import forum.Forum;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;


public class ForumUtilsTest {

    @Test
    public void testGetMostUpvotedReply() {
        ForumClient mockClient = mock(ForumClient.class);
        String postId = "post1";

        // Mock the responses
        mockGetPostResponse(mockClient, postId);
        mockGetTopCommentsResponse(mockClient, postId);
        mockExpandCommentBranchResponse(mockClient, "comment1");

        // Call the function
        Forum.Comment result = ForumUtils.getMostUpvotedReply(mockClient, postId);

        // Assert the expected result
        assertNotNull(result);
        assertEquals("Top Reply", result.getText());
    }

    private void mockGetPostResponse(ForumClient client, String postId) {
        Forum.PostResponse postResponse = Forum.PostResponse.newBuilder()
                .setPost(Forum.Post.newBuilder().setTitle("Test Post").build())
                .build();
        when(client.getPost(postId)).thenReturn(postResponse);
    }

    private void mockGetTopCommentsResponse(ForumClient client, String postId) {
        Forum.Comment topComment = Forum.Comment.newBuilder()
                .setCommentId("comment1") // Assuming 'setCommentId' method exists
                .setScore(10)
                .setText("Top Comment")
                .build();
        Forum.TopCommentsResponse topCommentsResponse = Forum.TopCommentsResponse.newBuilder()
                .addAllComments(Collections.singletonList(topComment))
                .build();
        when(client.getTopComments(postId, Integer.MAX_VALUE)).thenReturn(topCommentsResponse);
    }

    private void mockExpandCommentBranchResponse(ForumClient client, String commentId) {
        Forum.Comment topReply = Forum.Comment.newBuilder()
                .setCommentId("reply1") // Assuming 'setCommentId' method exists
                .setScore(15)
                .setText("Top Reply")
                .build();
        Forum.CommentBranchResponse commentBranchResponse = Forum.CommentBranchResponse.newBuilder()
                .addAllComments(Collections.singletonList(topReply))
                .build();
        when(client.expandCommentBranch(Forum.ExpandCommentBranchRequest.newBuilder()
                .setCommentId(commentId)
                .build())).thenReturn(commentBranchResponse);
    }
}
