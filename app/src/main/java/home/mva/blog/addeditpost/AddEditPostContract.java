package home.mva.blog.addeditpost;

import home.mva.blog.base.BasePresenter;
import home.mva.blog.base.BaseView;
import home.mva.blog.data.model.Post;

public interface AddEditPostContract {

    interface View extends BaseView<Presenter> {

        void showEmptyPostError();

        void setupForEditing(Post post);

        boolean isActive();

    }

    interface Presenter extends BasePresenter {



    }
}
