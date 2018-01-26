package home.mva.blog.data.source;

import home.mva.blog.data.model.Comment;

/**
 * Created by makov on 26.01.2018.
 */

public class CommentsRepository implements CommentsDataSource {
    @Override
    public void getComments(GetCommentsCallback callback) {
        
    }

    @Override
    public void getCommentsByPostId(Integer postId, GetCommentsCallback callback) {

    }

    @Override
    public void getComment(Integer commentId, GetCommentCallback callback) {

    }

    @Override
    public void addComment(Comment comment) {

    }

    @Override
    public void deleteAllComments() {

    }

    @Override
    public void deleteComment(Comment comment) {

    }

    @Override
    public void refreshComments() {

    }
}
