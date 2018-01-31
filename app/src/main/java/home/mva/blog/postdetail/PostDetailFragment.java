package home.mva.blog.postdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import home.mva.blog.R;
import home.mva.blog.addcomment.AddCommentActivity;
import home.mva.blog.addcomment.AddCommentFragment;
import home.mva.blog.addeditpost.AddEditPostActivity;
import home.mva.blog.addeditpost.AddEditPostFragment;
import home.mva.blog.data.model.Comment;


public class PostDetailFragment extends Fragment implements PostDetailContract.View {

    private boolean isCurrentUserCreator;

    private CommentsAdapter mListAdapter;

    @NonNull
    private static final String ARGUMENT_POST_ID = "POST_ID";

    @NonNull
    private static final int REQUEST_EDIT_POST = 1;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new CommentsAdapter(new ArrayList<Comment>(0));
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

        setHasOptionsMenu(isCurrentUserCreator);

        mDetailTitle = root.findViewById(R.id.post_detail_title);
        mDetailBody = root.findViewById(R.id.post_detail_body);

        // Set up comments list view
        ListView listView = root.findViewById(R.id.comments_list);
        listView.setAdapter(mListAdapter);

        // Set up floating action button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_post);

        fab.setImageResource(R.drawable.ic_pencil_white_24dp);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addComment();
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_menu_item:
                mPresenter.editPost();
                return true;
        }
        return false;
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
        mListAdapter.replaceData(comments);
    }

    @Override
    public void showMissingPost() {
        mDetailTitle.setText("");
        mDetailBody.setText(getString(R.string.no_data));
    }

    @Override
    public void showEditPost(Integer postId) {
        Intent intent = new Intent(getContext(), AddEditPostActivity.class);
        intent.putExtra(AddEditPostFragment.ARGUMENT_EDIT_POST_ID, postId);
        startActivityForResult(intent, REQUEST_EDIT_POST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_POST) {
            // If the task was edited successfully, go back to the list.
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
            }
        }
    }

    @Override
    public void showPostDeleted() {
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMissingComments() {
        Snackbar.make(getView(), getString(R.string.no_comments_found), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAddCommentForPost(Integer postId) {
        Intent intent = new Intent(getContext(), AddCommentActivity.class);
        intent.putExtra(AddCommentFragment.ARGUMENT_POST_ID, postId);
        startActivity(intent);
    }

    private static class CommentsAdapter extends BaseAdapter {

        private List<Comment> mComments;

        public CommentsAdapter(List<Comment> comments) {
            mComments = comments;
        }

        public void replaceData(List<Comment> comments) {
            mComments = comments;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mComments.size();
        }

        @Override
        public Comment getItem(int position) {
            return mComments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                rowView = inflater.inflate(R.layout.comment_item, parent, false);
            }

            final Comment comment = getItem(position);

            TextView authorTextView = rowView.findViewById(R.id.comment_author);
            authorTextView.setText(comment.getName());

            TextView emailTextView = rowView.findViewById(R.id.author_email);
            emailTextView.setText(comment.getEmail());

            TextView bodyTextView = rowView.findViewById(R.id.comment_body);
            bodyTextView.setText(comment.getBody());

            return rowView;
        }
    }
}
