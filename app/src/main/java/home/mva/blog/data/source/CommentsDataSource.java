package home.mva.blog.data.source;

import java.util.List;

import home.mva.blog.data.model.Comment;

public interface CommentsDataSource {

    interface GetCommentsCallback {

        void onCommentsLoaded(List<Comment> comments);

        void onDataNotAvailable();
    }

    void getCommentsByPostId(Integer postId, GetCommentsCallback callback);

    void addComment(Comment comment);

    void deleteCommentsByPostId(Integer postId);

    void deleteComment(Comment comment);

    void refreshComments();
}
