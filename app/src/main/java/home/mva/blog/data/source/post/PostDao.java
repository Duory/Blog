package home.mva.blog.data.source.post;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import home.mva.blog.data.model.Post;

public interface PostDao {

    @Query("SELECT * FROM posts")
    List<Post> getPosts();

    @Query("SELECT * FROM posts WHERE postid =:postId")
    Post getPostById(String postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(Post post);

    @Update
    void updatePost(Post post);

    @Query("DELETE FROM posts WHERE entryid =:postId")
    void deletePost(Post post);
}
