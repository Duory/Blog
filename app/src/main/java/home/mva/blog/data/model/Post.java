package home.mva.blog.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Model class for a Post.
 */
@Entity(tableName = "posts")
public final class Post {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer mId;

    @ColumnInfo(name = "userid")
    private Integer mUserId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "body")
    private String mBody;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer mId) {
        this.mId = mId;
    }

    public Integer getUserId() {
        return mUserId;
    }

    public void setUserId(Integer mUserId) {
        this.mUserId = mUserId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String mBody) {
        this.mBody = mBody;
    }
}
