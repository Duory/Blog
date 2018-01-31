package home.mva.blog.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Model class for a Comment.
 */
@Entity(tableName = "comments",
        foreignKeys = @ForeignKey(entity = Post.class,
                                  parentColumns = "id",
                                  childColumns = "postid",
                                  onDelete = CASCADE))
public class Comment {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer id;

    @ColumnInfo(name = "postid")
    private Integer postId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "body")
    private String body;

    public Integer getId() {
        return id;
    }

    public void setId(Integer mId) {
        this.id = mId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer mPostId) {
        this.postId = mPostId;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mEmail) {
        this.email = mEmail;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String mBody) {
        this.body = mBody;
    }
}
