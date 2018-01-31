package home.mva.blog.addeditpost;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import home.mva.blog.data.model.Post;
import home.mva.blog.data.source.PostsDataSource;

public class AddEditPostPresenter implements AddEditPostContract.Presenter, PostsDataSource.GetPostCallback {

    private static final Integer CURRENT_USER = 3;

    @NonNull
    private final PostsDataSource mPostsRepository;

    @NonNull
    private final AddEditPostContract.View mAddEditPostView;

    @Nullable
    private Integer mPostId;

    private boolean mIsDataMissing;

    public AddEditPostPresenter(@Nullable Integer postId, @NonNull PostsDataSource postsRepository,
                                @NonNull AddEditPostContract.View addEditPostView,
                                boolean shouldLoadDataFromRepo) {
        mPostId = postId;
        mPostsRepository = postsRepository;
        mAddEditPostView = addEditPostView;
        mIsDataMissing = shouldLoadDataFromRepo;
        mAddEditPostView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isNewTask() && mIsDataMissing) {
            populatePost();
        }
    }

    @Override
    public void savePost(String title, String body) {
        if (isNewTask()) {
            addPost(title, body);
        } else {
            updatePost(title, body);
        }
    }

    @Override
    public void populatePost() {
        mPostsRepository.getPost(mPostId, this);
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    @Override
    public void onPostLoaded(Post post) {
        // The view may not be able to handle UI updates anymore
        if (mAddEditPostView.isActive()) {
            mAddEditPostView.setupForEditing(post.getTitle(), post.getBody());
        }
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        if (mAddEditPostView.isActive()) {
            mAddEditPostView.showEmptyPostError();
        }
    }

    private boolean isNewTask() {
        return mPostId == 0;
    }

    private void addPost(String title, String body) {
        if (title.isEmpty() || body.isEmpty()) {
            mAddEditPostView.showEmptyPostError();
        } else {
            Post newPost = new Post();
            newPost.setUserId(CURRENT_USER);
            newPost.setTitle(title);
            newPost.setBody(body);

            mPostsRepository.addPost(newPost, new PostsDataSource.AddPostCallback() {
                @Override
                public void onPostAddedToRemote(Post post) {
                    //never get this
                }

                @Override
                public void onSuccess() {
                    mAddEditPostView.showPostsList();
                }

                @Override
                public void onError() {
                    mAddEditPostView.showCantAddError();
                }
            });
        }


    }

    private void updatePost(String title, String body) {
        Post post = new Post();
        post.setUserId(CURRENT_USER);
        post.setId(mPostId);
        post.setTitle(title);
        post.setBody(body);

        mPostsRepository.updatePost(post);
        mAddEditPostView.showPostsList();
    }
}
