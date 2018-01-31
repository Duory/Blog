package home.mva.blog.postdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import home.mva.blog.R;
import home.mva.blog.data.model.Comment;


public class PostDetailFragment extends Fragment implements PostDetailContract.View {

    private boolean isCurrentUserCreator;

    @NonNull
    private static final String ARGUMENT_POST_ID = "POST_ID";

    @NonNull
    private static final String ARGUMENT_IS_CURRENT_USER_CREATOR = "IS_CURRENT_USER_CREATOR";

    private PostDetailContract.Presenter mPresenter;

    private TextView mDetailTitle;
    private TextView mDetailBody;

    public static PostDetailFragment newInstance(Integer postId, boolean isCreatedByCurrentUser) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_POST_ID, postId);
        arguments.putBoolean(ARGUMENT_IS_CURRENT_USER_CREATOR, isCreatedByCurrentUser);
        PostDetailFragment fragment = new PostDetailFragment();
        fragment.setArguments(arguments);
        fragment.isCurrentUserCreator = isCreatedByCurrentUser;
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.postdetail_fragment, container, false);

        mDetailTitle = root.findViewById(R.id.post_detail_title);
        mDetailBody = root.findViewById(R.id.post_detail_body);

        // Set up floating action button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_post);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editPost();
            }
        });

        // Hide fab if have not edit permission
        if (!isCurrentUserCreator) {
            fab.setVisibility(View.GONE);
        }
        return root;
    }

    @Override
    public void setPresenter(PostDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mDetailTitle.setText("");
            mDetailBody.setText(getString(R.string.loading));
        }
    }

    @Override
    public void showTitle(String title) {
        mDetailTitle.setText(title);
    }

    @Override
    public void showBody(String body) {
        mDetailBody.setText(body);
    }

    @Override
    public void showComments(List<Comment> comments) {

    }

    @Override
    public void showMissingPost() {
        mDetailTitle.setText("");
        mDetailBody.setText(getString(R.string.no_data));
    }

    @Override
    public void showEditPost(Integer postId) {

    }

    @Override
    public void showPostDeleted() {
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
