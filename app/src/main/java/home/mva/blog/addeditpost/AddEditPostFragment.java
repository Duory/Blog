package home.mva.blog.addeditpost;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import home.mva.blog.R;

public class AddEditPostFragment extends Fragment implements AddEditPostContract.View {

    public static final String ARGUMENT_EDIT_POST_ID = "EDIT_POST_ID";

    private AddEditPostContract.Presenter mPresenter;

    private TextView mTitle;

    private TextView mBody;

    public static AddEditPostFragment newInstance() {

        return new AddEditPostFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(AddEditPostContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab_apply);
        fab.setImageResource(R.drawable.ic_check_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.savePost(mTitle.getText().toString(), mBody.getText().toString());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addeditpost_fragment, container, false);

        mTitle = root.findViewById(R.id.add_edit_post_title);
        mBody = root.findViewById(R.id.add_edit_post_body);

        return root;
    }

    @Override
    public void showEmptyPostError() {
        Snackbar.make(mTitle, getString(R.string.empty_post_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setupForEditing(String title, String body) {
        mTitle.setText(title);
        mBody.setText(body);
    }

    @Override
    public void showPostsList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showCantAddError() {
        Snackbar.make(mTitle, getString(R.string.cant_add_error), Snackbar.LENGTH_LONG).show();
    }
}
