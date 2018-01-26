package home.mva.blog.util;

import java.util.concurrent.Executor;

/**
 * Global executor pools for the whole application.
 */
public class AppExecutors {

    private static final int THREAD_COUNT = 3;

    private final Executor diskIO;
    //private final Executor networkIO;
    //private final Executor mainThread;

    AppExecutors(Executor diskIO/*, Executor networkIO, Executor mainThread*/) {
        this.diskIO = diskIO;
        //this.networkIO = networkIO;
        //this.mainThread = mainThread;
    }

    public AppExecutors() {
        this(new DiscIOThreadExecutor());
    }
}
