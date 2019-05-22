package edu.cczu.table;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import edu.cczu.table.db.AppDatabase;
import edu.cczu.table.db.entity.CourseEntity;

/**
 * Repository handling the work with courses and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<CourseEntity>> mObservableCourses;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableCourses = new MediatorLiveData<>();

        mObservableCourses.addSource(mDatabase.courseDao().loadAllCourses(),
                courseEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableCourses.postValue(courseEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of courses from the database and get notified when the data changes.
     */
    public LiveData<List<CourseEntity>> getCourses() {
//        return mDatabase.courseDao().loadAllCourses();
        return mObservableCourses;
    }

    public LiveData<CourseEntity> loadCourse(final int courseId) {
        return mDatabase.courseDao().loadCourse(courseId);
    }

    public void insertCourse(List<CourseEntity> courses) {
        mDatabase.courseDao().insertAll(courses);
    }

    public void insertCourse(CourseEntity course) {
        mDatabase.courseDao().insert(course);
    }

    public void clearCourse() {
        mDatabase.courseDao().deleteAll();
    }

    public LiveData<List<CourseEntity>> searchCourses(String query) {
        return mDatabase.courseDao().searchAllCourses(query);
    }
}
