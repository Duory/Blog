package home.mva.blog.postdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import home.mva.blog.R;
import home.mva.blog.util.ActivityUtils;
import home.mva.blog.util.Injection;

public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST_ID = "POST_ID";
    public static final String EXTRA_IS_CURRENT_USER_CREATOR = "IS_CURRENT_USER_CREATOR";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.postdetail_activity);

        // Get the requested post id
        Integer postId = getIntent().getIntExtra(EXTRA_POST_ID, 0);
        boolean isCreatedByCurrentUser = getIntent().getBooleanExtra(EXTRA_IS_CURRENT_USER_CREATOR, false);

        PostDetailFragment postDetailFragment = (PostDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (postDetailFragment == null) {
            postDetailFragment = PostDetailFragment.newInstance(postId, isCreatedByCurrentUser);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    postDetailFragment, R.id.contentFrame);
        }

        // Create the presenter
        new PostDetailPresenter(
                postId,
                Injection.providePostsRepository(getApplicationContext()),
                Injection.provideCommentsRepository(getApplicationContext()),
                postDetailFragment);
    }
}
