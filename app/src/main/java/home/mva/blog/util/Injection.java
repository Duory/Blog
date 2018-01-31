package home.mva.blog.util;

import android.content.Context;
import android.support.annotation.NonNull;

import home.mva.blog.data.source.CommentsRepository;
import home.mva.blog.data.source.PostsRepository;
import home.mva.blog.data.source.local.BlogDatabase;
import home.mva.blog.data.source.local.CommentsLocalDataSource;
import home.mva.blog.data.source.local.PostsLocalDataSource;
import home.mva.blog.data.source.remote.BlogRemoteDataSource;

/**
 * Enables injection of production implementations for
 * PostsDataSource and CommentsDataSource at compile time.
 */

public class Injection {

    public static PostsRepository providePostsRepository(@NonNull Context context) {
        BlogDatabase database = BlogDatabase.getInstance(context);

        return PostsRepository.getInstance(BlogRemoteDataSource.getInstance(),
                PostsLocalDataSource.getInstance(new AppExecutors(), database.getPostDao()));
    }

    public static CommentsRepository provideCommentsRepository(@NonNull Context context) {
        BlogDatabase database = BlogDatabase.getInstance(context);

        return CommentsRepository.getInstance(BlogRemoteDataSource.getInstance(),
                CommentsLocalDataSource.getInstance(new AppExecutors(), database.getCommentsDao()));
    }
}
