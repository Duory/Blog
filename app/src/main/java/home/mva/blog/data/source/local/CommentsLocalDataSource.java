package home.mva.blog.data.source.local;

import android.support.annotation.NonNull;

import java.util.List;

import home.mva.blog.data.model.Comment;
import home.mva.blog.data.source.CommentsDataSource;
import home.mva.blog.util.AppExecutors;


public class CommentsLocalDataSource implements CommentsDataSource {

    private static volatile CommentsLocalDataSource instance;

    private CommentsDao mCommentsDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private CommentsLocalDataSource(@NonNull AppExecutors appExecutors,
                                    @NonNull CommentsDao commentsDao){
        mAppExecutors = appExecutors;
        mCommentsDao = commentsDao;
    }

    public static CommentsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                      @NonNull CommentsDao commentsDao) {
        if (instance == null) {
            synchronized (CommentsLocalDataSource.class) {
                if (instance == null) {
                    instance = new CommentsLocalDataSource(appExecutors, commentsDao);
                }
            }
        }
        return instance;
    }

    @Override
    public void getCommentsByPostId(@NonNull final Integer postId, @NonNull final GetCommentsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Comment> comments = mCommentsDao.getCommentsForPost(postId);
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (comments.isEmpty()) {
                            // This will be called if post does not have comments.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onCommentsLoaded(comments);
                        }
                    }
                });
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void addComment(@NonNull final Comment comment, final AddCommentCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mCommentsDao.insertComment(comment);
                callback.onSuccess();
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void deleteCommentsByPostId(@NonNull final Integer postId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mCommentsDao.deleteCommentsByPostId(postId);
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void deleteComment(@NonNull final Comment comment) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mCommentsDao.deleteComment(comment);
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void refreshComments() {
        // Not required because the Repository handles the logic of refreshing the
        // comments from all the available data sources.
    }
}
