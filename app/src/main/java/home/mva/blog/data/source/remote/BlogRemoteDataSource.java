package home.mva.blog.data.source.remote;

import android.support.annotation.NonNull;

import java.util.List;

import home.mva.blog.data.model.Comment;
import home.mva.blog.data.model.Post;
import home.mva.blog.data.source.CommentsDataSource;
import home.mva.blog.data.source.PostsDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogRemoteDataSource implements PostsDataSource, CommentsDataSource {

    private static final String BASE_API_URL = "https://jsonplaceholder.typicode.com/";

    private static volatile BlogRemoteDataSource instance;

    private BlogApi mBlogApi;

    private Retrofit mRetrofit;

    // Prevent direct instantiation.
    private BlogRemoteDataSource() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mBlogApi = mRetrofit.create(BlogApi.class);
    }

    public static BlogRemoteDataSource getInstance () {
        if (instance == null) {
            synchronized (BlogRemoteDataSource.class) {
                if (instance == null) {
                    instance = new BlogRemoteDataSource();
                }
            }
        }
        return instance;
    }

    @Override
    public void getPosts(@NonNull final GetPostsCallback callback) {

        Call<List<Post>> getAllPosts = mBlogApi.getAllPosts();

        getAllPosts.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onPostsLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void getPost(@NonNull final Integer postId,
                        @NonNull final GetPostCallback callback) {

        Call<Post> getPostById = mBlogApi.getPostById(postId);

        getPostById.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onPostLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void addPost(@NonNull final Post post,@NonNull final AddPostCallback callback) {

        Call<Post> addPost = mBlogApi.addPost(post);

        addPost.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onPostAddedToRemote(response.body());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void updatePost(Post post) {

        Call<Post> editPost = mBlogApi.updatePost(post);

        editPost.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                //No needed
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                //No needed
            }
        });
    }

    @Override
    public void deleteAllPosts() {
        //No needed for remote for now
    }

    @Override
    public void deletePost(Post post) {

    }

    @Override
    public void refreshPosts() {
        // Not required because the Repository handles the logic of refreshing the
        // posts from all the available data sources.
    }

    @Override
    public void getCommentsByPostId(@NonNull final Integer postId, @NonNull final GetCommentsCallback callback) {

        Call<List<Comment>> getCommentsByPost = mBlogApi.getCommentsByPostId(postId);

        getCommentsByPost.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onCommentsLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void addComment(@NonNull final Comment comment,@NonNull final AddCommentCallback callback) {

        Call<Comment> addComment = mBlogApi.addComment(comment);

        addComment.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onCommentAddedToRemote(response.body());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void deleteCommentsByPostId(Integer postId) {
        //no needed for remote for now
    }

    @Override
    public void deleteComment(Comment comment) {

    }

    @Override
    public void refreshComments() {
        // Not required because the Repository handles the logic of refreshing the
        // comments from all the available data sources.
    }
}
