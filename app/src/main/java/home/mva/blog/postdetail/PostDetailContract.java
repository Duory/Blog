package home.mva.blog.postdetail;

import java.util.List;

import home.mva.blog.base.BasePresenter;
import home.mva.blog.base.BaseView;
import home.mva.blog.data.model.Comment;

public interface PostDetailContract {

    interface  View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTitle(String title);

        void showBody(String body);

        void showComments(List<Comment> comments);

        void showMissingPost();

        void showEditPost(Integer postId);

        void showPostDeleted();

        boolean isActive();

        void showMissingComments();
    }

    interface Presenter extends BasePresenter {

        void editPost();

        void deletePost();

        void getComments();
    }
}
