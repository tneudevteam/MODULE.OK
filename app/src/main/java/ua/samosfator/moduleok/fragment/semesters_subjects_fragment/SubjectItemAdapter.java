package ua.samosfator.moduleok.fragment.semesters_subjects_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ua.samosfator.moduleok.DrawableUtils;
import ua.samosfator.moduleok.R;
import ua.samosfator.moduleok.parser.Subject;

public class SubjectItemAdapter extends RecyclerView.Adapter<SubjectItemAdapter.SubjectItemViewHolder> {

    private LayoutInflater inflater;
    private List<Subject> data = Collections.emptyList();

    public SubjectItemAdapter(Context context, List<Subject> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public SubjectItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_subject_item, parent, false);
        SubjectItemViewHolder viewHolder = new SubjectItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SubjectItemViewHolder holder, int position) {
        Subject current = data.get(position);
        holder.subjectName.setText(current.getName());
        holder.subjectDate.setText(current.getLastModule().getFormattedDate());
        holder.subjectScore.setText(String.valueOf(current.getLastModule().getScore()));
        holder.subjectScore.setBackgroundResource(DrawableUtils.getDrawableDependsOnScore(current.getLastModule().getScore()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class SubjectItemViewHolder extends RecyclerView.ViewHolder {

        private TextView subjectName;
        private TextView subjectScore;
        private TextView subjectDate;

        public SubjectItemViewHolder(View itemView) {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(R.id.subject_name);
            subjectScore = (TextView) itemView.findViewById(R.id.subject_score);
            subjectDate = (TextView) itemView.findViewById(R.id.subject_date);
        }
    }
}