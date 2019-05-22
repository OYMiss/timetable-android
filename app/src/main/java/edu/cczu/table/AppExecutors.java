package edu.cczu.table;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private final Executor mDiskIO;
    private final Executor mMainThread;

    private AppExecutors(Executor mDiskIO, Executor mMainThread) {
        this.mDiskIO = mDiskIO;
        this.mMainThread = mMainThread;
    }

    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
