package home.mva.blog.data.source.remote;

import home.mva.blog.data.model.Comment;
import home.mva.blog.data.model.Post;
import home.mva.blog.data.source.CommentsDataSource;
import home.mva.blog.data.source.PostsDataSource;

public class BlogRemoteDataSource implements PostsDataSource, CommentsDataSource {

    @Override
    public void getPosts(GetPostsCallback callback) {

    }

    @Override
    public void getPost(Integer postId, GetPostCallback callback) {

    }

    @Override
    public void addPost(Post post) {

    }

    @Override
    public void updatePost(Post post) {

    }

    @Override
    public void deleteAllPosts() {
        //No needed for remote for now
    }

    @Override
    public void deletePost(Post post) {

    }

    @Override
    public void refreshPosts() {
        // Not required because the Repository handles the logic of refreshing the
        // posts from all the available data sources.
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
        //No needed for remote for now
    }

    @Override
    public void deleteComment(Comment comment) {

    }

    @Override
    public void refreshComments() {
        // Not required because the Repository handles the logic of refreshing the
        // comments from all the available data sources.
    }
}
