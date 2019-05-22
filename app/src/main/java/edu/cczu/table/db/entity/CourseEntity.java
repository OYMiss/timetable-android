package edu.cczu.table.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

import edu.cczu.table.model.Course;

@Entity(tableName = "courses")
public class CourseEntity implements Course {
    public static final int ALL_WEEK = 0;
    public static final int ODD_WEEK = 1;
    public static final int EVEN_WEEK = 2;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int day;
    private int beginBlock;
    private int length;
    private String name;
    private String location;
    private List<Integer> weeks;

    public CourseEntity() {
    }

    @Ignore
    public CourseEntity(int day, int beginBlock, int length, String name, String location, List<Integer> weeks) {
        this.day = day;
        this.beginBlock = beginBlock;
        this.length = length;
        this.name = name;
        this.location = location;
        this.weeks = weeks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public List<Integer> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Integer> weeks) {
        this.weeks = weeks;
    }

    @Override
    public int getDay() {
        return day;
    }

    @Override
    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public int getBeginBlock() {
        return beginBlock;
    }

    @Override
    public void setBeginBlock(int beginBlock) {
        this.beginBlock = beginBlock;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String courseName) {
        this.name = courseName;
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setLocation(String courseLocation) {
        this.location = courseLocation;
    }
}
