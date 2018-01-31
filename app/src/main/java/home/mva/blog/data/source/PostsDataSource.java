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

    interface AddPostCallback {

        void onPostAddedToRemote(Post post);

        void onSuccess();

        void onError();

    }

    void getPosts(GetPostsCallback callback);

    void getPost(Integer postId, GetPostCallback callback);

    void addPost(Post post, AddPostCallback callback);

    void updatePost(Post post);

    void deleteAllPosts();

    void deletePost(Post post);

    void refreshPosts();
}
