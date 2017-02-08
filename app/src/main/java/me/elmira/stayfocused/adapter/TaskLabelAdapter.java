package me.elmira.stayfocused.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.elmira.stayfocused.R;
import me.elmira.stayfocused.data.Label;

/**
 * Created by elmira on 1/27/17.
 */

public class TaskLabelAdapter extends BaseAdapter {
    private List<Label> mLabels;

    public TaskLabelAdapter() {
        this(null);
    }

    public TaskLabelAdapter(List<Label> labels) {
        this.mLabels = labels;
    }

    @Override
    public int getCount() {
        return mLabels == null ? 0 : mLabels.size();
    }

    @Override
    public Object getItem(int i) {
        return mLabels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setLabels(List<Label> labels) {
        this.mLabels = labels;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task_label, viewGroup, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        Label label = mLabels.get(i);
        viewHolder.nameTextView.setText(label.getName());

        return convertView;
    }

    private class ViewHolder {
        TextView nameTextView;

        public ViewHolder(View view) {
            nameTextView = (TextView) view.findViewById(R.id.task_label_text);
        }
    }
}
