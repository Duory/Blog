package home.mva.blog.posts;

import android.support.annotation.NonNull;

import java.util.List;

import home.mva.blog.data.model.Post;
import home.mva.blog.data.source.PostsDataSource;
import home.mva.blog.data.source.PostsRepository;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */
public class PostsPresenter implements PostsContract.Presenter {

    private final PostsRepository mPostsRepository;

    private final PostsContract.View mPostsView;

    private boolean mFirstLoad = true;

    public PostsPresenter(@NonNull PostsRepository postsRepository,
                          @NonNull PostsContract.View postsView) {
        mPostsRepository = postsRepository;
        mPostsView = postsView;

        mPostsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadPosts(false);
    }

    @Override
    public void loadPosts(boolean forceUpdate) {
        loadPosts(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadPosts(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mPostsView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mPostsRepository.refreshPosts();
        }

        mPostsRepository.getPosts(new PostsDataSource.GetPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                if (!mPostsView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mPostsView.setLoadingIndicator(false);
                }
                if (posts.isEmpty()) {
                    mPostsView.showNoPostsMessage();
                } else {
                    mPostsView.showPosts(posts);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!mPostsView.isActive()) {
                    return;
                }
                mPostsView.showLoadingPostsError();
            }
        });
    }

    @Override
    public void openPost(@NonNull Post requestedPost) {
        mPostsView.showPostDetailsUi(requestedPost.getId());
    }

    @Override
    public void addNewPost() {
        mPostsView.showAddPost();
    }
}
