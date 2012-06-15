package org.daum.library.android.sitac.view.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableMenuAdapter extends BaseExpandableListAdapter {

	private Context ctx;
	private ExpandableMenuList data;
	
	public ExpandableMenuAdapter(Context context, ExpandableMenuList data) {
		this.ctx = context;
		this.data = data;
	}
	
	@Override
	public View getChildView(int grpPos, int childPos, boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			// create a new view
			convertView = new MenuItemView(ctx, data.getItem(grpPos, childPos));
			holder = new ViewHolder();
			holder.imgView = ((MenuItemView) convertView).getIcon();
			holder.txtView = ((MenuItemView) convertView).getText();
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (data.getItem(grpPos, childPos).getIcon() != null) {
			holder.imgView.setImageDrawable(data.getItem(grpPos, childPos).getIcon());
		}
		holder.txtView.setText(data.getItem(grpPos, childPos).getText());
		holder.txtView.setTextSize(17);
		return convertView;
	}
	
	@Override
	public View getGroupView(int grpPos, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			// create a new view
			convertView = new MenuItemView(ctx, data.getGroup(grpPos));
			holder = new ViewHolder();
			holder.txtView = ((MenuItemView) convertView).getText();
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtView.setText(data.getGroup(grpPos).getText());
		return convertView;
	}
	
	// useful to save memory
	static class ViewHolder {
		public ImageView imgView;
		public TextView txtView;
	}
	
	@Override
	public IExpandableMenuItem getChild(int grpPos, int childPos) {
		return data.getItems(grpPos).get(childPos);
	}

	@Override
	public long getChildId(int grpPos, int childPos) {
		return childPos;
	}

	@Override
	public int getChildrenCount(int grpPos) {
		return data.getItemsCount(grpPos);
	}

	@Override
	public IExpandableMenuItem getGroup(int grpPos) {
		return data.getGroup(grpPos);
	}

	@Override
	public int getGroupCount() {
		return data.getGroupCount();
	}

	@Override
	public long getGroupId(int grpPos) {
		return grpPos;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int grpPos, int childPos) {
		return true;
	}
}
