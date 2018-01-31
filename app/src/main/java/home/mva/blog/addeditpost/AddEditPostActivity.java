package home.mva.blog.addeditpost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import home.mva.blog.R;
import home.mva.blog.util.ActivityUtils;
import home.mva.blog.util.Injection;

public class AddEditPostActivity extends AppCompatActivity {

    private AddEditPostPresenter mAddEditPostPresenter;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    public static final int REQUEST_ADD_POST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addeditpost_activity);

        AddEditPostFragment addEditPostFragment = (AddEditPostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addEditContentFrame);

        //Get post id
        Integer postId = getIntent().getIntExtra(AddEditPostFragment.ARGUMENT_EDIT_POST_ID, 0);

        if (addEditPostFragment == null) {
            addEditPostFragment = addEditPostFragment.newInstance();

            if (getIntent().hasExtra(AddEditPostFragment.ARGUMENT_EDIT_POST_ID)) {
                Bundle bundle = new Bundle();
                bundle.putInt(AddEditPostFragment.ARGUMENT_EDIT_POST_ID, postId);
                addEditPostFragment.setArguments(bundle);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditPostFragment, R.id.addEditContentFrame);
        }

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        mAddEditPostPresenter = new AddEditPostPresenter(
                postId,
                Injection.providePostsRepository(getApplicationContext()),
                addEditPostFragment,
                shouldLoadDataFromRepo);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddEditPostPresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }
}
