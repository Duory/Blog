package home.mva.blog.addcomment;

import android.support.annotation.NonNull;

import home.mva.blog.data.model.Comment;
import home.mva.blog.data.source.CommentsDataSource;
import home.mva.blog.data.source.CommentsRepository;

public class AddCommentPresenter implements AddCommentContract.Presenter {

    private static final String CURRENT_USER_EMAIL = "blog@test.com";
    private static final String CURRENT_USER_NAME = "current name";

    private final CommentsRepository mCommentsRepository;

    private final AddCommentContract.View mAddCommentView;

    private Integer mPostId;

    public AddCommentPresenter(@NonNull Integer postId,
                                @NonNull CommentsRepository commentsRepository,
                                @NonNull AddCommentContract.View addCommentView) {
        mPostId = postId;
        mCommentsRepository = commentsRepository;
        mAddCommentView = addCommentView;

        mAddCommentView.setPresenter(this);
    }

    @Override
    public void start() {
        //Nothing
    }

    @Override
    public void addComment(String body) {

        if (body.isEmpty()) {
            mAddCommentView.showEmptyCommentError();
        } else {
            Comment comment = new Comment();
            comment.setEmail(CURRENT_USER_EMAIL);
            comment.setPostId(mPostId);
            comment.setBody(body);
            comment.setName(CURRENT_USER_NAME);
            mCommentsRepository.addComment(comment, new CommentsDataSource.AddCommentCallback() {
                @Override
                public void onCommentAddedToRemote(Comment comment) {
                    //never get this
                }

                @Override
                public void onSuccess() {
                    mAddCommentView.showPostDetail();
                }

                @Override
                public void onError() {
                    mAddCommentView.showCantAddError();
                }
            });
        }

    }
}
