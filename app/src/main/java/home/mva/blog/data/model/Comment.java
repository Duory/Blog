package home.mva.blog.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Model class for a Comment.
 */
@Entity(foreignKeys = @ForeignKey(entity = Post.class,
                                  parentColumns = "id",
                                  childColumns = "postid",
                                  onDelete = CASCADE))
public class Comment {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer mId;

    @ColumnInfo(name = "postid")
    private Integer mPostId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "email")
    private String mEmail;

    @ColumnInfo(name = "body")
    private String mBody;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer mId) {
        this.mId = mId;
    }

    public Integer getPostId() {
        return mPostId;
    }

    public void setPostId(Integer mPostId) {
        this.mPostId = mPostId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String mBody) {
        this.mBody = mBody;
    }
}
