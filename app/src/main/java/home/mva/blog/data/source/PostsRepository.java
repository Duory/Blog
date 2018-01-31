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

    /**
     * Tells that offline mod enabled
     */
    private boolean mAppIsOffline = false;

    // Prevent direct instantiation.
    private PostsRepository(@NonNull PostsDataSource postsRemoteDataSource,
                            @NonNull PostsDataSource postsLocalDataSource) {
        mPostsRemoteDataSource = postsRemoteDataSource;
        mPostsLocalDataSource = postsLocalDataSource;
    }

    public static PostsRepository getInstance(PostsDataSource postsRemoteDataSource,
                                              PostsDataSource postsLocalDataSource) {
        if (instance == null) {
            instance = new PostsRepository(postsRemoteDataSource, postsLocalDataSource);
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

        if (mCacheIsDirty && !mAppIsOffline) {
            // If the cache is dirty and online we need to fetch new data from the remote.
            getPostsFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network if online.
            mPostsLocalDataSource.getPosts(new GetPostsCallback() {
                @Override
                public void onPostsLoaded(List<Post> posts) {
                    callback.onPostsLoaded(posts);
                }

                @Override
                public void onDataNotAvailable() {
                    if (!mAppIsOffline) {
                        getPostsFromRemoteDataSource(callback);
                    } else {
                        callback.onDataNotAvailable();
                    }
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

        if (mCacheIsDirty && !mAppIsOffline) {
            // If the cache is dirty and online we need to fetch new data from the remote.
            getPostFromRemoteDataSource(postId, callback);
        }

        // Is the post in the local data source? If not, query the network if online.
        mPostsLocalDataSource.getPost(postId, new GetPostCallback() {
            @Override
            public void onPostLoaded(Post post) {
                callback.onPostLoaded(post);
            }

            @Override
            public void onDataNotAvailable() {
                if (!mAppIsOffline) {
                    getPostFromRemoteDataSource(postId, callback);
                } else {
                    callback.onDataNotAvailable();
                }
            }
        });
    }

    @Override
    public void addPost(@NonNull Post post, @NonNull final AddPostCallback callback) {

            mPostsRemoteDataSource.addPost(post, new AddPostCallback() {
                @Override
                public void onPostAddedToRemote(Post post) {
                    mPostsLocalDataSource.addPost(post, callback);
                }

                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onError() {
                    callback.onError();
                }
            });
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
                mAppIsOffline = true;
                callback.onDataNotAvailable();
            }
        });
    }

    private void getPostFromRemoteDataSource(final Integer postId, final GetPostCallback callback) {

        mPostsRemoteDataSource.getPost(postId, new GetPostCallback() {
            @Override
            public void onPostLoaded(Post post) {
                mPostsLocalDataSource.addPost(post, new AddPostCallback() {
                    @Override
                    public void onPostAddedToRemote(Post post) {
                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
                callback.onPostLoaded(post);
            }

            @Override
            public void onDataNotAvailable() {
                mAppIsOffline = true;
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(List<Post> posts) {

        mPostsLocalDataSource.deleteAllPosts();

        for (Post post : posts) {
            mPostsLocalDataSource.addPost(post, new AddPostCallback() {
                @Override
                public void onPostAddedToRemote(Post post) {
                }

                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                }
            });
        }

        mCacheIsDirty = false;
    }
}
