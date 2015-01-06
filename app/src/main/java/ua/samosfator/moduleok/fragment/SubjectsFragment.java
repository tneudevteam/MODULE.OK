package ua.samosfator.moduleok.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ua.samosfator.moduleok.Auth;
import ua.samosfator.moduleok.LoadPageAsyncTask;
import ua.samosfator.moduleok.Preferences;
import ua.samosfator.moduleok.R;
import ua.samosfator.moduleok.Student;
import ua.samosfator.moduleok.parser.Subject;
import ua.samosfator.moduleok.recyclerview.adapter.SubjectItemAdapter;

public class SubjectsFragment extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    public static List<Subject> mSubjects;

    private RecyclerView mRecyclerView;
    private SubjectItemAdapter mSectionAdapter;

    public SubjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subjects, container, false);
        mSubjects = Auth.getCurrentStudent().getSemesters().getFirst().getSubjects();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.subjects_recycler_view);
        mSectionAdapter = new SubjectItemAdapter(getActivity(), mSubjects);
        mRecyclerView.setAdapter(mSectionAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }
}
