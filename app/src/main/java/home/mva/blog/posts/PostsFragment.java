package home.mva.blog.posts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import home.mva.blog.R;
import home.mva.blog.data.model.Post;

public class PostsFragment extends Fragment implements PostsContract.View {

    private PostsContract.Presenter mPresenter;

    private PostsAdapter mListAdapter;

    public static PostsFragment newInstance()
    {
        return new PostsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new PostsAdapter(new ArrayList<Post>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(PostsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.posts_fragment, container, false);

        // Set up posts view
        ListView listView = root.findViewById(R.id.posts_list);
        listView.setAdapter(mListAdapter);

        // Set up floating action button
        FloatingActionButton fab = getActivity().findViewById(R.id.fabAddTask);

        fab.setImageResource(R.drawable.ic_plus_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewPost();
            }
        });

        // Set up progress indicator
        final ScrollSwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadPosts(false);
            }
        });

        return root;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.refresh_layout);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showPosts(List<Post> posts) {

        mListAdapter.replaceData(posts);
    }

    @Override
    public void showPostDetailsUi(Integer postId) {

    }

    @Override
    public void showAddPost() {

    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_post_message));
    }

    @Override
    public void showNoPostsMessage() {
        showMessage(getString(R.string.no_posts_message));
    }

    @Override
    public void showLoadingPostsError() {
        showMessage(getString(R.string.loading_posts_error));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private static class PostsAdapter extends BaseAdapter {

        private List<Post> mPosts;
        private PostItemListener mItemListener;

        public PostsAdapter(List<Post> posts, PostItemListener itemListener) {
            setList(posts);
            mItemListener = itemListener;
        }

        public void replaceData(List<Post> posts) {
            setList(posts);
            notifyDataSetChanged();
        }

        private void setList(List<Post> posts) {
            mPosts = posts;
        }

        @Override
        public int getCount() {
            return mPosts.size();
        }

        @Override
        public Post getItem(int position) {
            return mPosts.get(position);
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
                rowView = inflater.inflate(R.layout.post_item, parent, false);
            }

            final Post post = getItem(position);

            TextView titleTextView = rowView.findViewById(R.id.title);
            titleTextView.setText(post.getTitle());
            TextView authorTextView = rowView.findViewById(R.id.author);
            authorTextView.setText(String.valueOf(post.getUserId()));

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onPostClick(post);
                }
            });

            return rowView;
        }
    }

    PostItemListener mItemListener = new PostItemListener() {

        @Override
        public void onPostClick(Post clickedPost) {
            mPresenter.openPost(clickedPost);
        }
    };

    public interface PostItemListener {

        void onPostClick(Post clickedPost);
    }

}
