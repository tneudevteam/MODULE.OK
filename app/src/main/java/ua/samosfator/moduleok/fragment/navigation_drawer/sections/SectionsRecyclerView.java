package ua.samosfator.moduleok.fragment.navigation_drawer.sections;

import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;
import ua.samosfator.moduleok.utils.App;
import ua.samosfator.moduleok.R;
import ua.samosfator.moduleok.event.LoginEvent;
import ua.samosfator.moduleok.event.LogoutEvent;

public class SectionsRecyclerView {

    private List<SectionDrawer> mSections;
    private SectionAdapter mSectionAdapter;
    private RecyclerView mRecyclerView;

    private View mParentLayout;
    private FragmentActivity mFragmentActivity;

    public SectionsRecyclerView(View layout, FragmentActivity activity) {
        App.registerClassForEventBus(this);
        mParentLayout = layout;
        mFragmentActivity = activity;
    }

    public void init(DrawerLayout drawerLayout) {
        if (mSectionAdapter == null) initSectionAdapter(mFragmentActivity);
        mRecyclerView = (RecyclerView) mParentLayout.findViewById(R.id.sections_list);
        mRecyclerView.setAdapter(mSectionAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mFragmentActivity));
        initOnClickListener(drawerLayout);
    }

    private void initSectionAdapter(FragmentActivity activity) {
        initSections();
        mSectionAdapter = new SectionAdapter(activity, mSections);
    }

    private void initOnClickListener(DrawerLayout drawerLayout) {
        SectionClickListener sectionClickListener = new SectionClickListener(mFragmentActivity, drawerLayout, mRecyclerView);
        RecyclerItemClickListener recyclerItemClickListener = new RecyclerItemClickListener(mFragmentActivity, sectionClickListener);
        mRecyclerView.addOnItemTouchListener(recyclerItemClickListener);
    }

    private void initSections() {
        mSections = new ArrayList<>();

        SectionDrawer lastTotalSection = new SectionDrawer(App.getContext().getString(R.string.home_section), R.drawable.ic_format_list_numbers_grey600_24dp);
        mSections.add(lastTotalSection);

        SectionDrawer modulesSection = new SectionDrawer(App.getContext().getString(R.string.modules_section), R.drawable.ic_file_document_box_grey600_24dp);
        mSections.add(modulesSection);

        SectionDrawer detailedSubjects = new SectionDrawer(mFragmentActivity.getString(R.string.detailed_subjects_section), R.drawable.ic_poll_grey600_24dp);
        mSections.add(detailedSubjects);

        addLoginOrLogoutSection();

        SectionDrawer divider = new SectionDrawer("", R.drawable.empty);
        mSections.add(divider);

        SectionDrawer versionSection = new SectionDrawer("v" + App.getVersion(), R.drawable.ic_information_grey600_24dp);
        mSections.add(versionSection);

        SectionDrawer feedbackSection = new SectionDrawer(App.getContext().getString(R.string.feedback_section), R.drawable.ic_help_circle_grey600_24dp);
        mSections.add(feedbackSection);
    }

    private void addLoginOrLogoutSection() {
        if (App.isLoggedIn()) {
            addLogoutSection();
        } else {
            addLoginSection();
        }
    }

    private void addLogoutSection() {
        SectionDrawer logoutSection = new SectionDrawer(App.getContext().getString(R.string.logout_section), R.drawable.ic_logout_grey600_24dp);
        insertSectionToLastPosition(logoutSection);
    }

    public void addLoginSection() {
        SectionDrawer loginSection = new SectionDrawer(App.getContext().getString(R.string.login_section), R.drawable.ic_login_grey600_24dp);
        insertSectionToLastPosition(loginSection);
    }

    private void insertSectionToLastPosition(SectionDrawer section) {
        if (mSections.size() == 3) {
            mSections.add(section);
        } else {
            mSections.set(3, section);
            mSectionAdapter.notifyItemChanged(3);
        }
    }

    @Subscribe
    public void onEvent(LoginEvent event) {
        Log.d("EVENTS-Sections", "LoginEvent");
        mFragmentActivity.runOnUiThread(this::addLoginOrLogoutSection);
    }

    @Subscribe
    public void onEvent(LogoutEvent event) {
        Log.d("EVENTS-Sections", "LogoutEvent");
        addLoginOrLogoutSection();
    }
}
