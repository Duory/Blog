package home.mva.blog.data.source.comment;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import home.mva.blog.data.model.Comment;

@Dao
public interface CommentsDao {

    @Query("SELECT * FROM comments")
    List<Comment> getComments();

    @Query("SELECT * FROM comments WHERE postid=:postId")
    List<Comment> getCommentsForPost(final int postId);

    @Query("SELECT * FROM comments WHERE id=:commentId")
    Comment getCommentById(String commentId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(Comment comment);

    @Delete
    void deleteComment(Comment comment);
}
