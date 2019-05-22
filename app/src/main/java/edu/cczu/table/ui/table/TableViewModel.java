package edu.cczu.table.ui.table;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import edu.cczu.table.BasicApp;
import edu.cczu.table.DataRepository;
import edu.cczu.table.db.entity.CourseEntity;

public class TableViewModel extends AndroidViewModel{
    private final DataRepository mRepository;

    public TableViewModel(@NonNull Application application) {
        super(application);
        mObservableCourses = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableCourses.setValue(null);

        mRepository = ((BasicApp) application).getRepository();
//        LiveData<List<CourseEntity>> courses = mRepository.searchCourses("wu");
        LiveData<List<CourseEntity>> courses = mRepository.getCourses();
//        mObservableCourses = courses;

        // observe the changes of the courses from the database and forward them
        mObservableCourses.addSource(courses, mObservableCourses::setValue);
    }

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<CourseEntity>> mObservableCourses;

    /**
     * Expose the LiveData Courses query so the UI can observe it.
     */
    public LiveData<List<CourseEntity>> getCourses() {
//        return mRepository.getCourses();
        return mObservableCourses;
    }

    public LiveData<List<CourseEntity>> searchCourses(String query) {
        return mRepository.searchCourses(query);
    }
}
