package home.mva.blog.addcomment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import home.mva.blog.R;
import home.mva.blog.util.ActivityUtils;
import home.mva.blog.util.Injection;

public class AddCommentActivity extends AppCompatActivity {

    private AddCommentPresenter mAddCommentPresenter;

    public static final String EXTRA_POST_ID = "POST_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcomment_activity);

        // Get post id
        Integer postId = getIntent().getIntExtra(EXTRA_POST_ID, 0);

        AddCommentFragment addCommentFragment = (AddCommentFragment) getSupportFragmentManager()
                .findFragmentById(R.id.commentContentFrame);

        if (addCommentFragment == null) {
            addCommentFragment = AddCommentFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), addCommentFragment, R.id.commentContentFrame);
        }

        // Create the presenter
        mAddCommentPresenter = new AddCommentPresenter(
                postId,
                Injection.provideCommentsRepository(getApplicationContext()),
                addCommentFragment);
    }
}
