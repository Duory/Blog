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
    private CommentsRepository(@NonNull CommentsDataSource commentsLocalDataSource,
                               @NonNull CommentsDataSource commentsRemoteDataSource) {
        mCommentsLocalDataSource = commentsLocalDataSource;
        mCommentsRemoteDataSource = commentsRemoteDataSource;
    }

    public static CommentsRepository getInstance(CommentsDataSource commentsLocalDataSource,
                                                 CommentsDataSource commentsRemoteDataSource) {
        if (instance == null) {
            instance = new CommentsRepository(commentsLocalDataSource, commentsRemoteDataSource);
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
    public void getComment(@NonNull final Integer commentId,
                           @NonNull final GetCommentCallback callback) {

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the remote.
            getCommentFromRemoteDataSource(commentId, callback);
        }
        // Is the comment in the local data source? If not, query the network.
        mCommentsLocalDataSource.getComment(commentId, new GetCommentCallback() {
            @Override
            public void onCommentLoaded(Comment comment) {
                callback.onCommentLoaded(comment);
            }

            @Override
            public void onDataNotAvailable() {
                getCommentFromRemoteDataSource(commentId, callback);
            }
        });
    }

    @Override
    public void addComment(@NonNull Comment comment) {

        mCommentsLocalDataSource.addComment(comment);
        mCommentsRemoteDataSource.addComment(comment);
    }

    @Override
    public void deleteAllComments() {

        mCommentsLocalDataSource.deleteAllComments();
        mCommentsRemoteDataSource.deleteAllComments();
    }

    @Override
    public void deleteComment(Comment comment) {

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
                updateLocalDataSource(comments);
                callback.onCommentsLoaded(comments);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void updateLocalDataSource(List<Comment> comments) {

        for (Comment comment : comments){
            mCommentsLocalDataSource.addComment(comment);
        }
    }

    private void getCommentFromRemoteDataSource(final Integer commentId, final GetCommentCallback callback) {

        mCommentsRemoteDataSource.getComment(commentId, new GetCommentCallback() {
            @Override
            public void onCommentLoaded(Comment comment) {
                updateLocalDataSource(Collections.singletonList(comment));
                callback.onCommentLoaded(comment);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

}
