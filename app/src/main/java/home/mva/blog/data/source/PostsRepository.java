package home.mva.blog.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import home.mva.blog.data.model.Post;


/**
 * Concrete implementation to load posts from the data sources into a cache.
 */
public class PostsRepository implements PostsDataSource {

    private static PostsRepository instance = null;

    private final PostsDataSource mPostsLocalDataSource;

    private final PostsDataSource mPostsRemoteDataSource;

    private Map<Integer, Post> mCachedPosts;

    /**
     * Marks the cache as dirty, to force an update the next time data is requested.
     */
    private boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private PostsRepository(@NonNull PostsDataSource postsLocalDataSource,
                            @NonNull PostsDataSource postsRemoteDataSource) {
        mPostsLocalDataSource = postsLocalDataSource;
        mPostsRemoteDataSource = postsRemoteDataSource;
    }

    public static PostsRepository getInstance(PostsDataSource postsLocalDataSource,
                                              PostsDataSource postsRemoteDataSource) {
        if (instance == null) {
            instance = new PostsRepository(postsLocalDataSource, postsRemoteDataSource);
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    /**
     * Gets posts from cache, local data source or remote data source, whichever is
     * available first.
     */
    @Override
    public void getPosts(@NonNull final GetPostsCallback callback) {

        // Respond immediately with cache if available and not dirty
        if (mCachedPosts != null && !mCacheIsDirty) {
            callback.onPostsLoaded(new ArrayList<Post>(mCachedPosts.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the remote.
            getPostsFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mPostsLocalDataSource.getPosts(new GetPostsCallback() {
                @Override
                public void onPostsLoaded(List<Post> posts) {
                    refreshCache(posts);
                    callback.onPostsLoaded(new ArrayList<Post>(mCachedPosts.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getPostsFromRemoteDataSource(callback);
                }
            });
        }
    }

    /**
     * Gets post from cache, local data source or remote data source, whichever is
     * available first.
     */
    @Override
    public void getPost(@NonNull final Integer postId, @NonNull final GetPostCallback callback) {

        Post cachedPost = getPostWithId(postId);

        // Respond immediately with cache if available
        if (cachedPost != null) {
            callback.onPostLoaded(cachedPost);
            return;
        }

        // Is the post in the local data source? If not, query the network.
        mPostsLocalDataSource.getPost(postId, new GetPostCallback() {
            @Override
            public void onPostLoaded(Post post) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedPosts == null) {
                    mCachedPosts = new LinkedHashMap<>();
                }
                mCachedPosts.put(post.getId(), post);
                callback.onPostLoaded(post);
            }

            @Override
            public void onDataNotAvailable() {
                mPostsRemoteDataSource.getPost(postId, new GetPostCallback() {
                    @Override
                    public void onPostLoaded(Post post) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedPosts == null) {
                            mCachedPosts = new LinkedHashMap<>();
                        }
                        mCachedPosts.put(post.getId(), post);
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

        mPostsLocalDataSource.addPost(post);
        mPostsRemoteDataSource.addPost(post);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedPosts == null) {
            mCachedPosts = new LinkedHashMap<>();
        }
        mCachedPosts.put(post.getId(), post);
    }

    @Override
    public void updatePost(@NonNull Post post) {

        mPostsLocalDataSource.updatePost(post);
        mPostsRemoteDataSource.updatePost(post);

        mCachedPosts.put(post.getId(), post);

    }

    @Override
    public void deleteAllPosts() {

        mPostsLocalDataSource.deleteAllPosts();
        mPostsRemoteDataSource.deleteAllPosts();

        if (mCachedPosts == null) {
            mCachedPosts = new LinkedHashMap<>();
        }
        mCachedPosts.clear();
    }

    @Override
    public void deletePost(@NonNull Post post) {

        mPostsLocalDataSource.deletePost(post);
        mPostsRemoteDataSource.deletePost(post);

        mCachedPosts.remove(post.getId());
    }

    @Override
    public void refreshPosts() {
        mCacheIsDirty = true;
    }

    private void getPostsFromRemoteDataSource(final GetPostsCallback callback) {

        mPostsRemoteDataSource.getPosts(new GetPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                refreshCache(posts);
                refreshLocalDataSource(posts);
                callback.onPostsLoaded(new ArrayList<Post>(mCachedPosts.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Nullable
    private Post getPostWithId(@NonNull Integer postId) {

        if (mCachedPosts == null || mCachedPosts.isEmpty()) {
            return null;
        } else {
            return mCachedPosts.get(postId);
        }
    }

    private void refreshCache(List<Post> posts) {
        if (mCachedPosts == null) {
            mCachedPosts = new LinkedHashMap<>();
        }
        mCachedPosts.clear();
        for (Post post : posts) {
            mCachedPosts.put(post.getId(), post);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Post> posts) {

        mPostsLocalDataSource.deleteAllPosts();

        for (Post post : posts) {
            mPostsLocalDataSource.addPost(post);
        }
    }
}
