package home.mva.blog.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import home.mva.blog.data.model.Comment;

@Dao
public interface CommentsDao {

    @Query("SELECT * FROM comments WHERE postId=:postId")
    List<Comment> getCommentsForPost(Integer postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(Comment comment);

    @Delete
    void deleteComment(Comment comment);

    @Query("DELETE FROM comments WHERE postId=:postId")
    void deleteCommentsByPostId(Integer postId);
}
