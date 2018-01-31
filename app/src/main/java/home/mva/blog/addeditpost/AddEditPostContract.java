package home.mva.blog.addeditpost;

import home.mva.blog.base.BasePresenter;
import home.mva.blog.base.BaseView;

public interface AddEditPostContract {

    interface View extends BaseView<Presenter> {
        void showEmptyPostError();

        void setupForEditing(String title, String body);

        void showPostsList();

        boolean isActive();

        void showCantAddError();
    }

    interface Presenter extends BasePresenter {
        void savePost(String title, String body);

        void populatePost();

        boolean isDataMissing();
    }
}
