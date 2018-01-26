package home.mva.blog.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import home.mva.blog.data.model.Post;

@Dao
public interface PostsDao {

    @Query("SELECT * FROM posts")
    List<Post> getPosts();

    @Query("SELECT * FROM posts WHERE id =:postId")
    Post getPostById(Integer postId);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertPost(Post post);

    @Update
    void updatePost(Post post);

    @Delete
    void deletePost(Post post);

}
