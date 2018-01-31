package home.mva.blog.addcomment;

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

public class AddCommentFragment extends Fragment implements AddCommentContract.View {

    public static final String ARGUMENT_POST_ID = "POST_ID";

    private AddCommentContract.Presenter mPresenter;

    private TextView mBody;

    public static AddCommentFragment newInstance() {

        return new AddCommentFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(AddCommentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add);
        fab.setImageResource(R.drawable.ic_check_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addComment(mBody.getText().toString());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addcomment_fragment, container, false);

        mBody = root.findViewById(R.id.add_comment_body);

        return root;
    }

    @Override
    public void showCantAddError() {
        Snackbar.make(mBody, getString(R.string.cant_add_error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyCommentError() {
        Snackbar.make(mBody, getString(R.string.empty_comment_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showPostDetail() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
