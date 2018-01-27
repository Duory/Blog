package home.mva.blog.data.source.remote;

import java.util.List;

import home.mva.blog.data.model.Comment;
import home.mva.blog.data.model.Post;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BlogService {

    @GET("/posts")
    Call<List<Post>> getAllPosts();

    @GET("/posts/{id}")
    Call<Post> getPostById(@Path("id") int id);

    @POST("/posts")
    Call<Post> addPost(@Body Post post);

    @PUT("posts/{id}")
    Call<Post> updatePost(@Body Post post);

    //@DELETE("/posts")
    //void deleteAllPosts();

    @DELETE("/posts/{id}")
    void deletePostById(@Path("id") int id);

    @GET("/posts/{id}/comments")
    Call<List<Comment>> getCommentsByPostId(@Path("id") int id);

    @POST("/comments")
    Call<Comment> addComment(@Body Comment comment);

    @DELETE("comments/{id}")
    void deleteCommentById(@Path("id") int id);
}
