package edu.cczu.table.ui.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

import edu.cczu.table.BasicApp;

import static android.content.Context.MODE_PRIVATE;

public class SharedViewModel extends AndroidViewModel {
    private static String NAME = "setting";
    private static int MODE = MODE_PRIVATE;
    private SharedPreferences sharedPreferences;

    private int getCurWeek() {
        int shiftWeek = sharedPreferences.getInt("shiftWeek", 1);
        String standardWeek = sharedPreferences.getString("standardWeek", LocalDate.now().toString());

        // 开学日期
        LocalDate start = LocalDate.parse(standardWeek);
//        WeekFields week = WeekFields.of(Locale.getDefault());
//        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        WeekFields week = WeekFields.ISO;
        int startWeek = start.get(week.weekOfWeekBasedYear());

        LocalDate today = LocalDate.now();
        int todayWeek = today.get(week.weekOfWeekBasedYear());

        return todayWeek - startWeek + shiftWeek;
    }

    private BasicApp app;
    public SharedViewModel(@NonNull Application application) {
        super(application);
        this.app = (BasicApp) application;
    }

    public void restoreCurWeek() {
        sharedPreferences = this.getApplication().getApplicationContext().getSharedPreferences(NAME, MODE);
        this.setCurWeek(getCurWeek());
    }

    public void clearCourse() {
        app.getAppExecutors().diskIO().execute(() -> {
            app.getRepository().clearCourse();
        });
    }

    public LiveData<Integer> getLiveCurWeek() {
        return app.getCurWeek();
    }

    public void initStartWeek(int curWeek) {
        LocalDate now = LocalDate.now();
        String standardWeek = now.toString();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("shiftWeek", curWeek);
        edit.putString("standardWeek", standardWeek);
        edit.apply();
    }

    public void setCurWeek(int current) {
        app.getCurWeek().setValue(current);
    }
}
