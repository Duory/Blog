package home.mva.blog.data.source;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import home.mva.blog.data.model.Comment;

/**
 * Concrete implementation to load comments from the data sources into a cache.
 */
public class CommentsRepository implements CommentsDataSource {

    private static CommentsRepository instance = null;

    private final CommentsDataSource mCommentsLocalDataSource;

    private final CommentsDataSource mCommentsRemoteDataSource;

    /**
     * Marks the local data as dirty, to force an update the next time data is requested.
     */
    private boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private CommentsRepository(@NonNull CommentsDataSource commentsRemoteDataSource,
                               @NonNull CommentsDataSource commentsLocalDataSource) {
        mCommentsRemoteDataSource = commentsRemoteDataSource;
        mCommentsLocalDataSource = commentsLocalDataSource;
    }

    public static CommentsRepository getInstance(CommentsDataSource commentsRemoteDataSource,
                                                 CommentsDataSource commentsLocalDataSource) {
        if (instance == null) {
            instance = new CommentsRepository(commentsRemoteDataSource, commentsLocalDataSource);
        }
        return instance;
    }

    public static void destroyInstance(){
        instance = null;
    }

    /**
     * Gets comments by post id from local data source or remote data source, whichever is
     * available first.
     */
    @Override
    public void getCommentsByPostId(@NonNull final Integer postId,
                                    @NonNull final GetCommentsCallback callback) {


        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the remote.
            getCommentsForPostFromRemoteDataSource(postId, callback);
        }

        // Is comments in the local data source? If not, query the network.
        mCommentsLocalDataSource.getCommentsByPostId(postId, new GetCommentsCallback() {
            @Override
            public void onCommentsLoaded(List<Comment> comments) {
                callback.onCommentsLoaded(comments);
            }

            @Override
            public void onDataNotAvailable() {
                getCommentsForPostFromRemoteDataSource(postId, callback);
            }
        });

    }

    @Override
    public void addComment(@NonNull Comment comment) {

        mCommentsLocalDataSource.addComment(comment);
        mCommentsRemoteDataSource.addComment(comment);
    }

    @Override
    public void deleteCommentsByPostId(@NonNull final Integer postId) {

        mCommentsLocalDataSource.deleteCommentsByPostId(postId);
        mCommentsRemoteDataSource.deleteCommentsByPostId(postId);
    }

    @Override
    public void deleteComment(@NonNull final Comment comment) {

        mCommentsLocalDataSource.deleteComment(comment);
        mCommentsRemoteDataSource.deleteComment(comment);
    }

    @Override
    public void refreshComments() {
        mCacheIsDirty = true;
    }


    private void getCommentsForPostFromRemoteDataSource(final Integer postId, final GetCommentsCallback callback) {

        mCommentsRemoteDataSource.getCommentsByPostId(postId, new GetCommentsCallback() {
            @Override
            public void onCommentsLoaded(List<Comment> comments) {
                updateLocalDataSource(postId, comments);
                callback.onCommentsLoaded(comments);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void updateLocalDataSource(Integer postId, List<Comment> comments) {

        mCommentsLocalDataSource.deleteCommentsByPostId(postId);

        for (Comment comment : comments){
            mCommentsLocalDataSource.addComment(comment);
        }
    }

}
