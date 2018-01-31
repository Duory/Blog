package home.mva.blog.data.source;

import java.util.List;

import home.mva.blog.data.model.Comment;

public interface CommentsDataSource {

    interface GetCommentsCallback {

        void onCommentsLoaded(List<Comment> comments);

        void onDataNotAvailable();
    }

    interface AddCommentCallback {

        void onCommentAddedToRemote(Comment comment);

        void onSuccess();

        void onError();
    }

    void getCommentsByPostId(Integer postId, GetCommentsCallback callback);

    void addComment(Comment comment, AddCommentCallback callback);

    void deleteCommentsByPostId(Integer postId);

    void deleteComment(Comment comment);

    void refreshComments();
}
