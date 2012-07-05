package org.daum.library.android.sitac.view.menu;

import java.util.ArrayList;
import java.util.List;

public class ExpandableMenuList {

	private ArrayList<Pair<IExpandableMenuItem, List<IExpandableMenuItem>>> list =
			new ArrayList<Pair<IExpandableMenuItem, List<IExpandableMenuItem>>>();

	/**
	 * Adds a new menu group and fill it with the item list given in parameter.
	 * If the given item list is null, then it will automatically create an
	 * empty list for the group
	 * 
	 * @param grpItem
	 *            an ExpandableMenuItem
	 * @param items
	 *            the list of ExpandableMenuItem to fill the group with
	 */
	public void addGroup(IExpandableMenuItem grpItem,
			List<IExpandableMenuItem> items) {
		list.add(new Pair<IExpandableMenuItem, List<IExpandableMenuItem>>(grpItem, items));
	}

	/**
	 * Adds a new menu group at the specified index and fill it with the given
	 * item list If the given item list is null, then it will automatically
	 * create an empty list for the group
	 * 
	 * @param index
	 *            position in the menu where to put the group (if < 0 or >
	 *            size() then it will be added at the end)
	 * @param grpItem
	 *            an ExpandableMenuItem
	 * @param items
	 *            the list of ExpandableMenuItem to fill the group with
	 */
	public void addGroup(int index, IExpandableMenuItem grpItem,
			List<IExpandableMenuItem> items) {
		if (index < 0 || index > list.size())
			index = list.size();
		if (items == null)
			items = new ArrayList<IExpandableMenuItem>();
		list.add(index,
				new Pair<IExpandableMenuItem, List<IExpandableMenuItem>>(
						grpItem, items));
	}

	/**
	 * Adds a new menu group and create an empty list for it
	 * 
	 * @param grpItem
	 *            an ExpandableMenuItem
	 */
	public void addGroup(IExpandableMenuItem grpItem) {
		addGroup(grpItem, null);
	}

	/**
	 * Adds an item to a group that was previously added. If the group doesn't
	 * exist in the list, the group and its item will be added at the end of the
	 * list
	 * 
	 * @param grpItem
	 *            an IExpandableMenuItem
	 * @param item
	 *            the item to add to the group
	 */
	public void addItem(IExpandableMenuItem grpItem, IExpandableMenuItem item) {
		for (Pair<IExpandableMenuItem, List<IExpandableMenuItem>> pair : list) {
			if (pair.first.equals(grpItem)) {
				// group exists, add the item to it
				pair.second.add(item);
				return;
			}
		}

		// group doesn't exists, create a new list to store the item
		ArrayList<IExpandableMenuItem> items = new ArrayList<IExpandableMenuItem>();
		items.add(item);
		addGroup(grpItem, items);
	}

	public void addItems(int grpPos, List<IExpandableMenuItem> items) {
		Pair<IExpandableMenuItem, List<IExpandableMenuItem>> pair = list
				.get(grpPos);
		if (pair != null) {
			pair.second.addAll(items);
		}
	}

	public void removeGroup(IExpandableMenuItem grpItem) {
		for (Pair<IExpandableMenuItem, List<IExpandableMenuItem>> pair : list) {
			if (pair.first.equals(grpItem)) {
				list.remove(pair);
				return;
			}
		}
	}
	
	public void removeGroup(int grpPos) {
		list.remove(grpPos);
	}

	public void removeItem(IExpandableMenuItem grpItem, IExpandableMenuItem item) {
		for (Pair<IExpandableMenuItem, List<IExpandableMenuItem>> pair : list) {
			if (pair.first.equals(grpItem)) {
				pair.second.remove(item);
				return;
			}
		}
	}
	
	public void removeItem(int grpPos, IExpandableMenuItem item) {
		if (grpPos < 0 || grpPos > list.size()-1) return;
		Pair<IExpandableMenuItem, List<IExpandableMenuItem>> pair = list.get(grpPos);
		pair.second.remove(item);
	}

	public IExpandableMenuItem getGroup(int position) {
        if (list.get(position) != null) return list.get(position).first;
        else return null;
	}

	public List<IExpandableMenuItem> getItems(int grpIndex) {
        if (list.get(grpIndex) != null) return list.get(grpIndex).second;
        else return null;
	}

	public List<IExpandableMenuItem> getItems(IExpandableMenuItem grpItem) {
		for (Pair<IExpandableMenuItem, List<IExpandableMenuItem>> pair : list) {
			if (pair.first.equals(grpItem))
				return pair.second;
		}
		return null;
	}

	public Pair<IExpandableMenuItem, List<IExpandableMenuItem>> get(int position) {
		return list.get(position);
	}

	public int getGroupCount() {
		return list.size();
	}

	public int getItemsCount(int grpPos) {
		return list.get(grpPos).second.size();
	}

	public int getItemsCount(IExpandableMenuItem grpItem) {
		for (Pair<IExpandableMenuItem, List<IExpandableMenuItem>> pair : list) {
			if (pair.first.equals(grpItem))
				return pair.second.size();
		}
		return 0;
	}

	public IExpandableMenuItem getItem(int grpPos, int childPos) {
		return getItems(grpPos).get(childPos);
	}
	
	public ArrayList<Pair<IExpandableMenuItem, List<IExpandableMenuItem>>> getList() {
		return list;
	}
	
	@Override
	public String toString() {
		return list.toString();
	}
	
	private class Pair<F, S> extends android.util.Pair<F, S> {

		public Pair(F first, S second) {
			super(first, second);
		}
		
		@Override
		public String toString() {
			return first.toString()+"-"+second.toString();
		}
	}
}
