package home.mva.blog.posts;

import android.support.annotation.NonNull;

import java.util.List;

import home.mva.blog.base.BasePresenter;
import home.mva.blog.base.BaseView;
import home.mva.blog.data.model.Post;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface PostsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showPosts(List<Post> posts);

        void showPostDetailsUi(Integer postId, boolean isCreatedByCurrentUser);

        void showAddPost();

        void showSuccessfullySavedMessage();

        void showNoPostsMessage();

        void showLoadingPostsError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadPosts(boolean forceUpdate);

        void openPost(@NonNull Post requestedPost);

        void addNewPost();
    }
}
