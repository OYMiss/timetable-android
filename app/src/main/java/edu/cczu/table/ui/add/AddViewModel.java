package edu.cczu.table.ui.add;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cczu.table.BasicApp;
import edu.cczu.table.db.entity.CourseEntity;

import static edu.cczu.table.db.entity.CourseEntity.ALL_WEEK;
import static edu.cczu.table.db.entity.CourseEntity.EVEN_WEEK;
import static edu.cczu.table.db.entity.CourseEntity.ODD_WEEK;

public class AddViewModel extends AndroidViewModel {
    private Map<String, Integer> dayToIndex;
    private Map<String, Integer> typeToIndex;
    private BasicApp app;
    public AddViewModel(@NonNull Application application) {
        super(application);
        this.app = (BasicApp) application;
        dayToIndex = new HashMap<>();
        dayToIndex.put("Mon", 0);
        dayToIndex.put("Tue", 1);
        dayToIndex.put("Wed", 2);
        dayToIndex.put("Thu", 3);
        dayToIndex.put("Fri", 4);
        dayToIndex.put("Sat", 5);
        dayToIndex.put("Sun", 6);
        typeToIndex = new HashMap<>();
        typeToIndex.put("All", ALL_WEEK);
        typeToIndex.put("Odd", ODD_WEEK);
        typeToIndex.put("Even", EVEN_WEEK);
    }

    List<String> getDayTypes() {
        ArrayList<String> types = new ArrayList<>();
        types.add("Mon");
        types.add("Tue");
        types.add("Wed");
        types.add("Thu");
        types.add("Fri");
        types.add("Sat");
        types.add("Sun");
        return types;
    }

    List<String> getWeekTypes() {
        ArrayList<String> types = new ArrayList<>();
        types.add("All");
        types.add("Odd");
        types.add("Even");
        return types;
    }

    public void addCourse(String name, String location, String dayStr, int beginBlock, int length, String weeksStr, String weekTypeStr) {
        Integer weekType = typeToIndex.get(weekTypeStr);
        Integer day = dayToIndex.get(dayStr);
        List<Integer> weeks = new ArrayList<>();
        String[] split = weeksStr.split(",");
        for (String w : split) {
            String trim = w.trim();
            String[] cur = trim.split("-");
            int beginWeek = Integer.parseInt(cur[0]);
            int endWeek = Integer.parseInt(cur[0]);
            if (split.length == 2) endWeek = Integer.parseInt(split[1]);

            for (int weekNum = beginWeek; weekNum <= endWeek; weekNum++) {
                weeks.add(weekNum);
                if (weekType == ALL_WEEK) {
                    weeks.add(weekNum);
                } else if (weekType == ODD_WEEK && weekNum % 2 == 1) {
                    weeks.add(weekNum);
                } else if (weekType == EVEN_WEEK && weekNum % 2 == 0) {
                    weeks.add(weekNum);
                }
            }
        }

        CourseEntity course = new CourseEntity(day, beginBlock, length, name, location, weeks);
        app.getAppExecutors().diskIO().execute(() -> {
            app.getRepository().insertCourse(course);
        });
    }
}
