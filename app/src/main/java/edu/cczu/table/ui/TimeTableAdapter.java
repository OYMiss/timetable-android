package edu.cczu.table.ui;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cczu.table.R;
import edu.cczu.table.model.Course;
import edu.cczu.table.databinding.CourseItemBinding;
import edu.cczu.table.lib.TimeTableLayout;
import edu.cczu.table.ui.main.MainActivity;

public class TimeTableAdapter extends TimeTableLayout.Adapter<TimeTableAdapter.CourseViewHolder> {
    private Map<Pair<Integer, Integer>, Course> courseMap;

    // make it static to keep when the layout is disappeared
    private List<? extends Course> courseList;

    private int pixelLength;
    private static final int ROW_CNT = 12;
    private static final int COL_CNT = 8;


    private void updateCourseList(List<? extends Course> newCourseList) {
        initCourseMap(newCourseList);
        notifyDataSetChanged();
    }

    public void setCourseList(List<? extends Course> newCourseList) {
        if (courseList == null || newCourseList.size() != courseList.size()) {
            updateCourseList(newCourseList);
        } else {
            for (int i = 0; i < courseList.size(); i++) {
                if (courseList.get(i).getId() != newCourseList.get(i).getId()) {
                    updateCourseList(newCourseList);
                }
            }
            for (int i = 0; i < courseList.size(); i++) {
                if (!courseList.get(i).equals(newCourseList.get(i))) {
                    updateCourseList(newCourseList);
                }
            }
        }
    }


    private void initCourseMap(List<? extends Course> courseList) {
        courseMap.clear();
        for (Course course : courseList) {
            courseMap.put(new Pair<>(course.getBeginBlock(), course.getDay() + 1), course);
        }
    }

    public TimeTableAdapter(Context context) {
        courseMap = new HashMap<>();
        pixelLength = ((MainActivity)context).getScreenPixelWidth() / 15;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CourseItemBinding inflate = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.course_item, parent, false);
        inflate.courseItem.setBackgroundResource(R.drawable.bg_item);
        return new CourseViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int row, int col) {
        if (col == 0) {
            holder.setSpanSize(1, 1);
            holder.binding.setDay(String.valueOf(row + 1));
            holder.itemView.setBackgroundResource(R.drawable.bg_white_gray_stroke);
        } else {
            Pair<Integer, Integer> position = new Pair<>(row, col);
            if (courseMap.containsKey(position)) {
                Course course = courseMap.get(position);
                holder.binding.setCourse(course);
                holder.setSpanSize(course.getLength(), 1);
                if ((row + col) % 2 == 0) {
                    holder.itemView.setBackgroundResource(R.drawable.bg_room_red_with_stroke);
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.bg_room_blue_with_stroke);
                }
            } else {
                // Don't Change
                holder.setSpanSize(-1, -1);
            }
        }
    }

    @Override
    public int getRowCount() {
        return ROW_CNT;
    }

    @Override
    public int getColCount() {
        return COL_CNT;
    }

    @Override
    public int getItemWidth(int row, int col) {
        if (col == 0) {
            return pixelLength;
        }
        return pixelLength * 2;
    }

    @Override
    public int getItemHeight(int row, int col) {
        return pixelLength * 2;
    }

    class CourseViewHolder extends TimeTableLayout.ViewHolder {
        final CourseItemBinding binding;
        CourseViewHolder(CourseItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
