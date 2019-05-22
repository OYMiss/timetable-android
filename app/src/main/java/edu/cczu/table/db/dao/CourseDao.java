package edu.cczu.table.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.cczu.table.db.entity.CourseEntity;

@Dao
public interface CourseDao {
    @Query("select * from courses")
    LiveData<List<CourseEntity>> loadAllCourses();

    @Query("select * from courses where name != :query")
    LiveData<List<CourseEntity>> searchAllCourses(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CourseEntity> courses);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CourseEntity course);

    @Query("select * from courses where id = :id")
    LiveData<CourseEntity> loadCourse(int id);

    @Query("select * from courses where id = :id")
    CourseEntity loadCourseSync(int id);

    @Query("delete from courses")
    int deleteAll();
}
