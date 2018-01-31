package home.mva.blog.posts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import home.mva.blog.R;
import home.mva.blog.util.ActivityUtils;
import home.mva.blog.util.Injection;

public class PostsActivity extends AppCompatActivity {

    private PostsPresenter mPostsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_activity);

        PostsFragment postsFragment = (PostsFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.postContentFrame);
        if (postsFragment == null) {
            postsFragment = PostsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), postsFragment, R.id.postContentFrame);
        }

        // Create the presenter
        mPostsPresenter = new PostsPresenter(
                Injection.providePostsRepository(getApplicationContext()), postsFragment);
    }
}
