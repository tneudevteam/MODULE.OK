package ua.samosfator.moduleok.fragment.modules;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.splunk.mint.Mint;
import com.splunk.mint.MintLogLevel;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import ua.samosfator.moduleok.utils.App;
import ua.samosfator.moduleok.utils.FragmentUtils;
import ua.samosfator.moduleok.utils.FragmentsKeeper;
import ua.samosfator.moduleok.R;
import ua.samosfator.moduleok.utils.StudentKeeper;
import ua.samosfator.moduleok.event.RefreshEndEvent;
import ua.samosfator.moduleok.student_bean.Semester;
import ua.samosfator.moduleok.student_bean.Subject;

public class ModulesFragment extends Fragment {

    private static FragmentManager fragmentManager;
    private static int maxModulesCount;
    static List<Subject> mSubjects = new ArrayList<>();
    private View rootView;
    private ModulesPagerAdapter modulesPagerAdapter;


    public ModulesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        App.registerClassForEventBus(this);
        Mint.logEvent("view ModulesFragment", MintLogLevel.Info);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_modules, container, false);
        fragmentManager = getFragmentManager();

        initSubjects();
        initTabStrip(rootView);

        return rootView;
    }

    private void initTabStrip(View rootView) {
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.modules_viewpager);
        modulesPagerAdapter = new ModulesPagerAdapter(getChildFragmentManager(), maxModulesCount);
        pager.setAdapter(modulesPagerAdapter);
        pager.setOffscreenPageLimit(maxModulesCount);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.modules_tabs);
        tabs.setViewPager(pager);
    }

    static void initSubjects() {
        try {
            mSubjects.clear();
            int semesterIndex = StudentKeeper.getCurrentSemesterIndex();
            if (semesterIndex == 2) {
                Semester firstSemester = StudentKeeper.getStudent().getSemester(0);
                Semester secondSemester = StudentKeeper.getStudent().getSemester(1);

                List<Subject> allSubjects = new ArrayList<>();
                allSubjects.addAll(firstSemester.getSubjects());

                Subject secondSemesterDivider = new Subject();
                secondSemesterDivider.setName(App.getContext().getString(R.string.second_semester_name));
                allSubjects.add(secondSemesterDivider);

                allSubjects.addAll(secondSemester.getSubjects());

                maxModulesCount = firstSemester.getMaxModuleCount() > secondSemester.getMaxModuleCount() ? firstSemester.getMaxModuleCount() : secondSemester.getMaxModuleCount();
                mSubjects.addAll(allSubjects);
            } else {
                Semester semester = StudentKeeper.getStudent().getSemester(semesterIndex);
                List<Subject> subjects = semester.getSubjects();
                maxModulesCount = semester.getMaxModuleCount();
                mSubjects.addAll(subjects);
            }
        } catch (Exception e) {
            e.printStackTrace();
            openLoginFragment();
        }
    }

    private static void openLoginFragment() {
        FragmentUtils.showFragment(fragmentManager.beginTransaction(), FragmentsKeeper.getLogin());
    }

    @Subscribe
    public void onEvent(RefreshEndEvent event) {
        Log.d("EVENTS-Modules", "RefreshEndEvent");
        if (FragmentsKeeper.getModules().isVisible()) {
            FragmentsKeeper.setModules(new ModulesFragment());
            FragmentUtils.showFragment(fragmentManager.beginTransaction(), FragmentsKeeper.getModules());
        } else {
            FragmentsKeeper.setModules(new ModulesFragment());
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
