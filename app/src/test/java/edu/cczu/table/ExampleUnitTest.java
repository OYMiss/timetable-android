package edu.cczu.table;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.cczu.table.db.entity.CourseEntity;
import edu.cczu.table.lib.Crawler;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    void handleRawString(List<List<String>> raw) {
        List<CourseEntity> courseEntityList = new ArrayList<>();
        for (int day = 0; day < raw.size(); day++) {
            List<String> dayCourses = raw.get(day);
            int i = 0;
            while (i < dayCourses.size()) {
                int beginBlock = i, length = 1;
                if (dayCourses.get(i).isEmpty()) {
                    i++;
                    continue;
                }
//                String[] split = dayCourses.get(i).split(",/");
                String[] split = dayCourses.get(i).split(",/");
                for (int c = 0; c < split.length; c++) {
                    String s = split[c];
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
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        List<List<String>> timeTable = null;
        try {
            timeTable = Crawler.getInstance().getTimeTable("", "");
            handleRawString(timeTable);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Android基础及应用 W机械213机房 2-13,/
//        毛泽东思想和中国特色 W6阶 单 1-14,/
        System.out.println(timeTable);
    }
    @Test
    public void AA() {
        String s = "编译原理 W1405 双 1-16,/软件测试技术 W13阶 单 2-12,/";
        String[] split = s.split(",/");
    }

    @Test
    public void test() {
        String class1 = "Android基础及应用 W机械213机房 2-13,15,/";
        String class2 = "毛泽东思想和中国特色 W6阶 单 1-14,/";
        parse(class1);
        parse(class2);
    }

    public void parse(String s) {
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

        String[] weeks = arr[i].substring(0, arr[i].length() - 2).split(",");
        for (String week : weeks) {
            String[] split = week.split("-");
            String beginWeek = split[0];
            String endWeek = split[0];
            if (split.length == 2) endWeek = split[1];

            System.out.println(beginWeek + "-" + endWeek);
        }
    }
}