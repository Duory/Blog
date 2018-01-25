package home.mva.blog.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import home.mva.blog.data.model.Comment;
import home.mva.blog.data.model.Post;

@Database(entities = {Post.class, Comment.class}, version = 1)
public abstract class BlogDatabase extends RoomDatabase {

    public abstract PostsDao getPostDao();
    public abstract CommentsDao getCommentsDao();

    private static final String DB_NAME = "blogDatabase.db";
    private static volatile BlogDatabase instance;

    static synchronized BlogDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static BlogDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                BlogDatabase.class,
                DB_NAME).build();
    }
}
