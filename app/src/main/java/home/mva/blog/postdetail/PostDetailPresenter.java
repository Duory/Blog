package home.mva.blog.postdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import home.mva.blog.data.model.Comment;
import home.mva.blog.data.model.Post;
import home.mva.blog.data.source.CommentsDataSource;
import home.mva.blog.data.source.CommentsRepository;
import home.mva.blog.data.source.PostsDataSource;
import home.mva.blog.data.source.PostsRepository;

public class PostDetailPresenter implements PostDetailContract.Presenter {

    private final PostsRepository mPostsRepository;

    private final CommentsRepository mCommentsRepository;

    private final PostDetailContract.View mPostDetailView;

    @Nullable
    private Integer mPostId;

    public PostDetailPresenter(@Nullable Integer postId,
                               @NonNull PostsRepository postsRepository,
                               @NonNull CommentsRepository commentsRepository,
                               @NonNull PostDetailContract.View postDetailView) {
        mPostId = postId;
        mPostsRepository = postsRepository;
        mCommentsRepository = commentsRepository;
        mPostDetailView = postDetailView;

        mPostDetailView.setPresenter(this);
    }


    @Override
    public void start() {
        openPost();
        getComments();
    }

    private void openPost() {
        if (mPostId == null) {
            mPostDetailView.showMissingPost();
            return;
        }
        mPostDetailView.setLoadingIndicator(true);
        mPostsRepository.getPost(mPostId, new PostsDataSource.GetPostCallback() {
            @Override
            public void onPostLoaded(Post post) {
                // The view may not be able to handle UI updates anymore
                if (!mPostDetailView.isActive()) {
                    return;
                }
                mPostDetailView.setLoadingIndicator(false);
                if (post == null) {
                    mPostDetailView.showMissingPost();
                } else {
                    showPost(post);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mPostDetailView.isActive()) {
                    return;
                }
                mPostDetailView.showMissingPost();
            }
        });

    }

    private void showPost(@NonNull Post post) {
        String title = post.getTitle();
        String body = post.getBody();

        mPostDetailView.showTitle(title);
        mPostDetailView.showBody(body);
    }

    @Override
    public void editPost() {
        mPostDetailView.showEditPost(mPostId);
    }

    @Override
    public void deletePost() {

    }

    @Override
    public void getComments() {
        if (mPostId == null) {
            return;
        }
        mCommentsRepository.getCommentsByPostId(mPostId, new CommentsDataSource.GetCommentsCallback() {
            @Override
            public void onCommentsLoaded(List<Comment> comments) {
                // The view may not be able to handle UI updates anymore
                if (!mPostDetailView.isActive()) {
                    return;
                }
                if (comments == null) {
                    mPostDetailView.showMissingComments();
                } else {
                    mPostDetailView.showComments(comments);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mPostDetailView.isActive()) {
                    return;
                }
                mPostDetailView.showMissingComments();
            }
        });
    }

    @Override
    public void addComment() {
        mPostDetailView.showAddCommentForPost(mPostId);
    }
}
