package home.mva.blog.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Global executor pools for the whole application.
 */
public class AppExecutors {

    private static final int THREAD_COUNT = 3;

    private final Executor diskIO;
    //private final Executor networkIO;
    private final Executor mainThread;

    AppExecutors(Executor diskIO/*, Executor networkIO*/, Executor mainThread) {
        this.diskIO = diskIO;
        //this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public AppExecutors() {
        this(new DiscIOThreadExecutor(), new MainThreadExecutor());
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHamdler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHamdler.post(command);
        }
    }
}
