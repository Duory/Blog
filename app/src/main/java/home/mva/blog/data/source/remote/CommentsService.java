package home.mva.blog.data.source.remote;

import java.util.List;

import home.mva.blog.data.model.Comment;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentsService {

    @GET("/posts/{id}/comments")
    Call<List<Comment>> getCommentsByPostId(@Path("id") int id);

    @GET("/comments/{id}")
    Call<Comment> getCommentById(@Path("id") int id);

    @POST("/comments")
    Call<Comment> addComment(@Body Comment comment);

    @DELETE("comments/{id}")
    void deleteCommentById(@Path("id") int id);
}
