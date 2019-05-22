package edu.cczu.table.ui.table;

import androidx.databinding.DataBindingUtil;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.cczu.table.R;
import edu.cczu.table.databinding.TableFragmentBinding;
import edu.cczu.table.db.entity.CourseEntity;
import edu.cczu.table.model.Course;
import edu.cczu.table.ui.main.MainActivity;
import edu.cczu.table.ui.TimeTableAdapter;
import edu.cczu.table.ui.viewmodel.SharedViewModel;

public class TableFragment extends Fragment {

    private TableViewModel mViewModel;
    private SharedViewModel sharedViewModel;
    private TableFragmentBinding binding;
    private TimeTableAdapter adapter;
    private boolean needLoading;
    private List<CourseEntity> courseEntities;

    public static TableFragment newInstance() {
        return new TableFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.table_fragment, container, false);
        mViewModel = ViewModelProviders.of(this).get(TableViewModel.class);
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        adapter = new TimeTableAdapter(getContext());
        binding.myTable.setAdapter(adapter);
        return binding.getRoot();
    }

    public void setLoading(boolean loading) {
        needLoading = loading;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDay();
        LiveData<Integer> liveCurWeek = sharedViewModel.getLiveCurWeek();
        liveCurWeek.observe(Objects.requireNonNull(getActivity()), (v) -> {
            if (this.courseEntities == null) {
                subscribeUi(mViewModel.getCourses());
            } else {
                updateCourse(this.courseEntities);
            }
        });
    }

    private void updateCourse(List<CourseEntity> courses) {
        if (courses != null) {
            Integer curWeek = sharedViewModel.getLiveCurWeek().getValue();
            System.out.println("week: " + curWeek);
            List<CourseEntity> collect = courses.stream().filter((courseEntity -> {
                List<Integer> weeks = courseEntity.getWeeks();
                if (weeks == null) return false;
                return weeks.contains(curWeek);
            })).collect(Collectors.toList());
            adapter.setCourseList(collect);
            if (needLoading) {
                binding.setIsLoading(true);
                needLoading = false;
            } else {
                binding.setIsLoading(false);
            }
        } else {
            needLoading = false;
            binding.setIsLoading(true);
        }
        // espresso does not know how to wait for data binding's loop so we execute changes
        // sync.
        binding.executePendingBindings();
    }

    private void subscribeUi(LiveData<List<CourseEntity>> liveData) {
        // Update the list when the data changes
        liveData.observe(this, this::updateCourse);
    }

    private GridLayout.LayoutParams getLayoutParams(int x, int y, int itemWidth, int itemHeight) {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                GridLayout.spec(x),
                GridLayout.spec(y)
        );
        params.setGravity(Gravity.FILL);
        params.height = itemHeight;
        params.width = itemWidth;
        return params;
    }

    private TextView getTextView() {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.bg_white_gray_stroke);
        return textView;
    }

    private void initDay() {
        binding.dayHeaderGridLayout.setRowCount(8);
        int itemWidth = ((MainActivity) Objects.requireNonNull(getActivity())).getScreenPixelWidth() / 15;
        TextView nullTextView = getTextView();
        nullTextView.setText("");
        binding.dayHeaderGridLayout.addView(nullTextView, getLayoutParams(0, 0, itemWidth, itemWidth * 2));

        String[] days = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < 7; i++) {
            GridLayout.LayoutParams params = getLayoutParams(0, i + 1, itemWidth * 2, itemWidth * 2);
            TextView textView = getTextView();
            textView.setText(days[i]);
            binding.dayHeaderGridLayout.addView(textView, params);
        }
    }


}
