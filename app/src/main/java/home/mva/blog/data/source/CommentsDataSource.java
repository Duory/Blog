package home.mva.blog.data.source;

import java.util.List;

import home.mva.blog.data.model.Comment;

public interface CommentsDataSource {

    interface GetCommentsCallback {

        void onCommentsLoaded(List<Comment> comments);

        void onDataNotAvailable();
    }

    interface GetCommentCallback {

        void onCommentLoaded(Comment comment);

        void onDataNotAvailable();
    }

    void getComments(GetCommentsCallback callback);

    void getCommentsByPostId(Integer postId, GetCommentsCallback callback);

    void getComment(Integer commentId, GetCommentCallback callback);

    void addComment(Comment comment);

    void deleteComment(Comment comment);

    void refreshComments();
}
