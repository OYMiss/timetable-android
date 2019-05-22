package edu.cczu.table.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.Toast;

import edu.cczu.table.R;
import edu.cczu.table.ui.NumberPickerDialog;
import edu.cczu.table.ui.add.AddFragment;
import edu.cczu.table.ui.fetch.FetchFragment;
import edu.cczu.table.ui.table.TableFragment;
import edu.cczu.table.ui.viewmodel.SharedViewModel;

public class MainActivity extends AppCompatActivity {
    private Fragment currentFragment;
    private SharedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        viewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        viewModel.restoreCurWeek();
        showTable(false);
    }

    @Override
    protected void onResume() {
        viewModel.restoreCurWeek();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void showTable(boolean needLoading) {
        TableFragment fragment = TableFragment.newInstance();
        fragment.setLoading(needLoading);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
        currentFragment = fragment;
    }

    public void showAddFragment() {
        AddFragment fragment = AddFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
        currentFragment = fragment;
    }

    public void showFetchFragment() {
        FetchFragment fragment = FetchFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
        currentFragment = fragment;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LiveData<Integer> curWeek = viewModel.getLiveCurWeek();
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        curWeek.observe(this, (v) -> {
            MenuItem item = menu.findItem(R.id.actionChangeWeek);
            Toast.makeText(this, "第 " + v + " 周", Toast.LENGTH_SHORT).show();
            item.setTitle("第 " + v + " 周");
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionChangeWeek:
                showNumberPicker();
                break;
            case R.id.addCourseMenuItem:
                showAddFragment();
                break;
            case R.id.batchTableMenuItem:
                showFetchFragment();
                break;
            case R.id.clearCourseMenuItem:
                clearCourse();
                break;
            case R.id.setStandardWeekMenuItem:
                Integer value = viewModel.getLiveCurWeek().getValue();
                if (value != null) {
                    viewModel.initStartWeek(value);
                    Toast.makeText(this, "当前周设置为第" + value + "周", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearCourse() {
        viewModel.clearCourse();
    }

    public int getScreenPixelWidth(){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof TableFragment) {
            super.onBackPressed();
        } else {
            showTable(false);
        }
    }

    public void showNumberPicker() {
        NumberPickerDialog newFragment = new NumberPickerDialog(this);
        newFragment.setValueChangeListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                viewModel.setCurWeek(newVal + 1);
            }
        });
        newFragment.show(getSupportFragmentManager(), "week picker");
    }
}
