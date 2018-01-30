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
    private Integer id;

    @ColumnInfo(name = "userid")
    private Integer userId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "body")
    private String body;


    public Integer getId() {
        return id;
    }

    public void setId(Integer mId) {
        this.id = mId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer mUserId) {
        this.userId = mUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String mBody) {
        this.body = mBody;
    }
}
