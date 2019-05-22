package edu.cczu.table.ui.fetch;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.cczu.table.BasicApp;
import edu.cczu.table.DataRepository;
import edu.cczu.table.db.AppDatabase;
import edu.cczu.table.db.DataGenerator;
import edu.cczu.table.db.entity.CourseEntity;

public class FetchViewModel extends AndroidViewModel {

    private DataRepository repository;
    private Application application;
    public FetchViewModel(Application application) {
        super(application);
        this.application = application;
        repository = ((BasicApp) application).getRepository();
    }

//    public LiveData<List<CourseEntity>> searchCourses(String query) {
//        return mRepository.searchCourses(query);
//    }

    void handleRawString(List<List<String>> raw) {
        List<CourseEntity> courseEntityList = new ArrayList<>();
        for (int day = 0; day < raw.size(); day++) {
            List<String> dayCourses = raw.get(day);
            int i = 0;
            while (i < dayCourses.size()) {
                int beginBlock = i;
                if (dayCourses.get(i).isEmpty()) {
                    i++;
                    continue;
                }
                String[] split = dayCourses.get(i).split(",/");
                for (int c = 0; c < split.length; c++) {
                    String s = split[c];
                    int length = 1;
                    int cnt = 0;
                    while (i + cnt + 1 < dayCourses.size() && dayCourses.get(i + cnt + 1).contains(s)) {
                        length++;
                        cnt++;
                    }
                    if (c == split.length - 1) i += cnt;
                    CourseEntity entity = parse(s, day, beginBlock, length);
                    if (entity != null) courseEntityList.add(entity);
                }
                i++;
            }
        }
        ((BasicApp) application).getAppExecutors().diskIO().execute(() -> {
            repository.insertCourse(courseEntityList);
        });

    }

    private CourseEntity parse(String s, int day, int beginBlock, int length) {
        if (s.trim().isEmpty()) return null;

        int ALL_WEEK = 0;
        int ODD_WEEK = 1;
        int EVEN_WEEK = 2;
        String[] arr = s.split(" ");
        int i = 0;
        String courseName = arr[i++];
        String courseClassroom = arr[i++];

        int weekType = ALL_WEEK;
        if (arr[i].equals("单") || arr[i].equals("双")) {
            weekType = arr[i++].equals("单") ? ODD_WEEK : EVEN_WEEK;
        }
        List<Integer> allWeek = new ArrayList<>();
        String[] weeks = arr[i].split(",");
        for (String week : weeks) {
            String[] split = week.split("-");
            int beginWeek = Integer.parseInt(split[0]);
            int endWeek = Integer.parseInt(split[0]);
            if (split.length == 2) endWeek = Integer.parseInt(split[1]);

            for (int weekNum = beginWeek; weekNum <= endWeek; weekNum++) {
                if (weekType == ALL_WEEK) {
                    allWeek.add(weekNum);
                } else if (weekType == ODD_WEEK && weekNum % 2 == 1) {
                    allWeek.add(weekNum);
                } else if (weekType == EVEN_WEEK && weekNum % 2 == 0) {
                    allWeek.add(weekNum);
                }
            }
        }

        CourseEntity course = new CourseEntity();
        course.setDay(day);
        course.setBeginBlock(beginBlock);
        course.setLength(length);
        course.setName(courseName);
        course.setLocation(courseClassroom);
        course.setWeeks(allWeek);
        return course;
    }
}
