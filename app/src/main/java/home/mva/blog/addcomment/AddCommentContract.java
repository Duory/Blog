package home.mva.blog.addcomment;

import home.mva.blog.base.BasePresenter;
import home.mva.blog.base.BaseView;

public interface AddCommentContract {

    interface View extends BaseView<Presenter> {
        void showCantAddError();

        void showEmptyCommentError();

        void showPostDetail();
    }

    interface Presenter extends BasePresenter {
        void addComment(String body);
    }
}
