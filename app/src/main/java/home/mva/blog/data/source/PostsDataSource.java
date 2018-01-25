package home.mva.blog.data.source;

import java.util.List;

import home.mva.blog.data.model.Post;

public interface PostsDataSource {

    interface GetPostsCallback {

        void onPostsLoaded(List<Post> posts);

        void onDataNotAvailable();
    }

    interface GetPostCallback {

        void onPostLoaded(Post post);

        void onDataNotAvailable();
    }

    void getPosts(GetPostsCallback callback);

    void getPost(String postId, GetPostCallback callback);

    void addPost(Post post);

    void updatePost(Post post);

    void deletePost(Post post);

    void refreshPosts();
}
