package edu.cczu.table.ui.add;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Objects;

import edu.cczu.table.R;
import edu.cczu.table.databinding.AddFragmentBinding;
import edu.cczu.table.ui.main.MainActivity;

public class AddFragment extends Fragment {

    private AddViewModel mViewModel;
    private AddFragmentBinding binding;

    public static AddFragment newInstance() {
        return new AddFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_fragment, container, false);
        return binding.getRoot();
    }


    private void closeMe(boolean needLoading) {
        ((MainActivity) Objects.requireNonNull(getActivity())).showTable(needLoading);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddViewModel.class);
        initButton();
        initSpinner();
    }

    private void initButton() {
        binding.addCourseButton.setOnClickListener((v) -> {
            String name = binding.courseNameEditText.getText().toString();
            String location = binding.courseLocationEditText.getText().toString();
            String day = binding.daySpinner.getSelectedItem().toString();
            int beginBlock = Integer.parseInt(binding.courseBeginBlockEditText.getText().toString());
            int length = Integer.parseInt(binding.courseEndBlockEditText.getText().toString()) - beginBlock + 1;
            String weeks = binding.courseWeeksEditText.getText().toString();
            mViewModel.addCourse(name, location, day, beginBlock - 1, length, weeks, binding.weekSpinner.getSelectedItem().toString());
            closeMe(true);
        });
        binding.cancleButton.setOnClickListener((v) -> {
            closeMe(false);
        });
    }
    private void initSpinner() {
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                R.layout.support_simple_spinner_dropdown_item, mViewModel.getDayTypes());
        binding.daySpinner.setAdapter(dayAdapter);
        ArrayAdapter<String> weekAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                R.layout.support_simple_spinner_dropdown_item, mViewModel.getWeekTypes());
        binding.weekSpinner.setAdapter(weekAdapter);
    }


}
