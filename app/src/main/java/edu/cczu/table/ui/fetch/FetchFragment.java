package edu.cczu.table.ui.fetch;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import android.os.AsyncTask;
import java.io.IOException;

import edu.cczu.table.lib.Crawler;
import edu.cczu.table.R;
import edu.cczu.table.databinding.FetchFragmentBinding;
import edu.cczu.table.ui.main.MainActivity;

public class FetchFragment extends Fragment {

    private FetchViewModel mViewModel;
    private FetchFragmentBinding binding;

    public static FetchFragment newInstance() {
        return new FetchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fetch_fragment, container, false);
        return binding.getRoot();
    }

    private void closeMe(boolean needLoading) {
        ((MainActivity) Objects.requireNonNull(getActivity())).showTable(needLoading);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FetchViewModel.class);
        binding.cancleFetchButton.setOnClickListener(v -> closeMe(false));
        binding.fetchButton.setOnClickListener(v -> {
            String studentId = binding.studentIdEditText.getText().toString();
            String password = binding.studentPasswordEditText.getText().toString();
            FetchTask fetchTask = new FetchTask(this);
            fetchTask.execute(studentId, password);
            closeMe(true);
        });
    }

    private static class FetchTask extends AsyncTask<String, Void, List<List<String>>> {
        private final FetchFragment fetchFragment;

        private FetchTask(FetchFragment fetchFragment) {
            this.fetchFragment = fetchFragment;
        }


        @Override
        protected List<List<String>> doInBackground(String... strings) {
            try {
                if (strings.length == 2) {
                    return Crawler.getInstance().getTimeTable(strings[0], strings[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<List<String>> lists) {
            fetchFragment.mViewModel.handleRawString(lists);
            super.onPostExecute(lists);
        }

    }


}
