package org.daum.library.android.moyens.view.quickactionbar;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ActionsAdapter extends BaseAdapter {

	private Context ctx;
	private ArrayList<String> actions;
	
	public ActionsAdapter(Context ctx, ArrayList<String> actions) {
		this.ctx = ctx;
		this.actions = actions;
	}
	
	@Override
	public int getCount() {
		return actions.size();
	}

	@Override
	public Object getItem(int i) {
		return actions.get(i);
	}

	@Override
	public long getItemId(int i) {
		// FIXME this is not really the expected result
		return i;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// create a new view
			Button b = new Button(ctx);
			b.setText(actions.get(i));
			convertView = b;
			
		} else if (convertView instanceof Button) {
			// update the old view UI
			((Button) convertView).setText(actions.get(i));
		}
		
		return convertView;
	}

}
