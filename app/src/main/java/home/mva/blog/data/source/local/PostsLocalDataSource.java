package home.mva.blog.data.source.local;

import android.support.annotation.NonNull;

import java.util.List;

import home.mva.blog.data.model.Post;
import home.mva.blog.data.source.PostsDataSource;
import home.mva.blog.util.AppExecutors;

public class PostsLocalDataSource implements PostsDataSource {

    private static volatile PostsLocalDataSource instance;

    private PostsDao mPostsDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private PostsLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull PostsDao postsDao){
        mAppExecutors = appExecutors;
        mPostsDao = postsDao;
    }

    public static PostsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull PostsDao postsDao) {
        if (instance == null) {
            synchronized (PostsLocalDataSource.class) {
                if (instance == null) {
                    instance = new PostsLocalDataSource(appExecutors, postsDao);
                }
            }
        }
        return instance;
    }

    @Override
    public void getPosts(@NonNull final GetPostsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Post> posts = mPostsDao.getPosts();
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (posts.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onPostsLoaded(posts);
                        }
                    }
                });
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void getPost(@NonNull final Integer postId, @NonNull final GetPostCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Post post = mPostsDao.getPostById(postId);
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (post == null) {
                            // This will be called if requested element does not exist.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onPostLoaded(post);
                        }
                    }
                });
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void addPost(@NonNull final Post post) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mPostsDao.insertPost(post);
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void updatePost(@NonNull final Post post) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mPostsDao.updatePost(post);
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void deleteAllPosts() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mPostsDao.deletePosts();
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void deletePost(@NonNull final Post post) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mPostsDao.deletePost(post);
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void refreshPosts() {
        // Not required because the Repository handles the logic of refreshing the
        // posts from all the available data sources.
    }
}
