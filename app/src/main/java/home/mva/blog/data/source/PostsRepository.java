package home.mva.blog.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import home.mva.blog.data.model.Post;


/**
 * Concrete implementation to load posts from the data sources into a cache.
 */
public class PostsRepository implements PostsDataSource {

    private static PostsRepository instance = null;

    private final PostsDataSource mPostsLocalDataSource;

    private final PostsDataSource mPostsRemoteDataSource;

    /**
     * Marks the local data as dirty, to force an update the next time data is requested.
     */
    private boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private PostsRepository(@NonNull PostsDataSource postsLocalDataSource,
                            @NonNull PostsDataSource postsRemoteDataSource) {
        mPostsLocalDataSource = postsLocalDataSource;
        mPostsRemoteDataSource = postsRemoteDataSource;
    }

    public static PostsRepository getInstance(PostsDataSource postsRemoteDataSource,
                                              PostsDataSource postsLocalDataSource) {
        if (instance == null) {
            instance = new PostsRepository(postsLocalDataSource, postsRemoteDataSource);
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    /**
     * Gets posts from local data source or remote data source, whichever is
     * available first.
     */
    @Override
    public void getPosts(@NonNull final GetPostsCallback callback) {

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the remote.
            getPostsFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mPostsLocalDataSource.getPosts(new GetPostsCallback() {
                @Override
                public void onPostsLoaded(List<Post> posts) {
                    callback.onPostsLoaded(posts);
                }

                @Override
                public void onDataNotAvailable() {
                    getPostsFromRemoteDataSource(callback);
                }
            });
        }
    }

    /**
     * Gets post from local data source or remote data source, whichever is
     * available first.
     */
    @Override
    public void getPost(@NonNull final Integer postId, @NonNull final GetPostCallback callback) {

        // Is the post in the local data source? If not, query the network.
        mPostsLocalDataSource.getPost(postId, new GetPostCallback() {
            @Override
            public void onPostLoaded(Post post) {
                callback.onPostLoaded(post);
            }

            @Override
            public void onDataNotAvailable() {
                mPostsRemoteDataSource.getPost(postId, new GetPostCallback() {
                    @Override
                    public void onPostLoaded(Post post) {
                        mPostsLocalDataSource.addPost(post);
                        callback.onPostLoaded(post);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void addPost(@NonNull Post post) {

        mPostsRemoteDataSource.addPost(post);
    }

    @Override
    public void updatePost(@NonNull Post post) {

        mPostsLocalDataSource.updatePost(post);
        mPostsRemoteDataSource.updatePost(post);

    }

    @Override
    public void deleteAllPosts() {

        mPostsLocalDataSource.deleteAllPosts();
        mPostsRemoteDataSource.deleteAllPosts();
    }

    @Override
    public void deletePost(@NonNull Post post) {

        mPostsLocalDataSource.deletePost(post);
        mPostsRemoteDataSource.deletePost(post);
    }

    @Override
    public void refreshPosts() {
        mCacheIsDirty = true;
    }

    private void getPostsFromRemoteDataSource(final GetPostsCallback callback) {

        mPostsRemoteDataSource.getPosts(new GetPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                refreshLocalDataSource(posts);
                callback.onPostsLoaded(posts);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(List<Post> posts) {

        mPostsLocalDataSource.deleteAllPosts();

        for (Post post : posts) {
            mPostsLocalDataSource.addPost(post);
        }

        mCacheIsDirty = false;
    }
}
