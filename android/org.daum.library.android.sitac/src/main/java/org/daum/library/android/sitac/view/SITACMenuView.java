package org.daum.library.android.sitac.view;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.daum.common.model.api.VehicleSector;
import org.daum.common.model.api.VehicleType;
import org.daum.library.android.sitac.listener.OnMenuViewEventListener;
import org.daum.library.android.sitac.view.entity.ArrowEntity;
import org.daum.library.android.sitac.view.entity.DangerEntity;
import org.daum.library.android.sitac.view.entity.DemandEntity;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.entity.TargetEntity;
import org.daum.library.android.sitac.view.entity.ZoneEntity;
import org.daum.library.android.sitac.view.menu.ExpandableMenuAdapter;
import org.daum.library.android.sitac.view.menu.ExpandableMenuItem;
import org.daum.library.android.sitac.view.menu.ExpandableMenuList;
import org.daum.library.android.sitac.view.menu.IExpandableMenuItem;
import org.daum.library.android.sitac.view.menu.PopupSideMenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SITACMenuView extends RelativeLayout implements Observer {
	
	// debugging
	private static final String TAG = "SITACMenuView";
	
	// menu UI constants
	private static final int MENU_WIDTH = 265;
	
	// view ids
	private static final int ID_HIDE_SHOW_BTN		= 42;
	private static final int ID_NO_LOCATION_MENU	= 1664;
	private static final int ID_MENU_SEPARATOR		= 1337;
	
	// string UI constants
	private static final String TEXT_HIDE_BTN = "X";
	private static final String TEXT_SHOW_BTN = "[ ]";
	
	// menu data constants
	private static final String GRP_DANGERS = "Dangers";
	private static final String GRP_CIBLES = "Cibles";
	private static final String GRP_MOYENS = "Moyens";
	private static final String GRP_ACTIONS = "Actions";
	private static final String GRP_NO_LOCATION = "Position à définir";
	private static final String ACTION_EAU = "Eau";
	private static final String ACTION_EXTINCTION = "Extinction";
	private static final String ACTION_SAP = "Secours à personnes";
	private static final String ACTION_CHEM = "Risques particuliers";
	private static final String ACTION_ZONE = "Zone";

	private Context ctx;
	private ExpandableListView menu;
	private ExpandableListView noLocationMenu;
	private ExpandableMenuAdapter adapter, noLocationAdapter;
	private ExpandableMenuList menuList;
	private ExpandableMenuList noLocationMenuList;
	private OnMenuViewEventListener listener;
	private Button resizeButton;
	private Button hideShowButton;
	private RelativeLayout.LayoutParams params;
	private Hashtable<DemandEntity, IExpandableMenuItem> noLocationDemands;
	private List<IExpandableMenuItem> noLocationItems;
	
	public SITACMenuView(Context context) {
		super(context);
		this.ctx = context;
		initUI();
		configUI();
		defineCallbacks();
	}
	
	private void initUI() {
		menu = new ExpandableListView(ctx);
		noLocationMenu = new ExpandableListView(ctx);
		
        List<IExpandableMenuItem> dangers = new ArrayList<IExpandableMenuItem>();
        dangers.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_BLUE_UP, "Eau"));
		dangers.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_RED_UP, "Incendie"));
        dangers.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_ORANGE_UP, "Risques particuliers"));
        
        List<IExpandableMenuItem> cibles = new ArrayList<IExpandableMenuItem>();
        cibles.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_BLUE_DOWN, "Eau"));
        cibles.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_RED_DOWN, "Incendie"));
        cibles.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_GREEN_DOWN, "Personne"));
        cibles.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_ORANGE_DOWN, "Risque particulier"));
        
        List<IExpandableMenuItem> moyens = new ArrayList<IExpandableMenuItem>();
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_BLUE_DOTTED_AGRES, VehicleSector.ALIM.name()));
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_RED_DOTTED_AGRES, VehicleSector.INC.name()));
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_VIOLET_DOTTED_AGRES, VehicleSector.COM.name()));
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_BLACK_DOTTED_AGRES, VehicleSector.RTN.name()));
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_GREEN_DOTTED_AGRES, VehicleSector.SAP.name()));
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_ORANGE_DOTTED_AGRES, VehicleSector.CHEM.name()));
        
        List<IExpandableMenuItem> actions = new ArrayList<IExpandableMenuItem>();
        actions.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_LINE_BLUE, ACTION_EAU));
        actions.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_LINE_RED, ACTION_EXTINCTION));
        actions.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_LINE_GREEN, ACTION_SAP));
        actions.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_LINE_ORANGE, ACTION_CHEM));
        actions.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_ZONE, ACTION_ZONE));
        
        menuList = new ExpandableMenuList();
        menuList.addGroup(new ExpandableMenuItem(GRP_DANGERS), dangers);
        menuList.addGroup(new ExpandableMenuItem(GRP_CIBLES), cibles);
        menuList.addGroup(new ExpandableMenuItem(GRP_MOYENS), moyens);
        menuList.addGroup(new ExpandableMenuItem(GRP_ACTIONS), actions);
        
        noLocationItems = new ArrayList<IExpandableMenuItem>();
        noLocationDemands = new Hashtable<DemandEntity, IExpandableMenuItem>();
        
        noLocationMenuList = new ExpandableMenuList();
        noLocationMenuList.addGroup(new ExpandableMenuItem(GRP_NO_LOCATION), noLocationItems);
        noLocationAdapter = new ExpandableMenuAdapter(ctx, noLocationMenuList);
        noLocationMenu.setAdapter(noLocationAdapter);
        
        adapter = new ExpandableMenuAdapter(ctx, menuList);
        menu.setAdapter(adapter);
        
        resizeButton = new Button(ctx);
        hideShowButton = new Button(ctx);
	}
	
	private void configUI() {
		setBackgroundColor(Color.argb(180, 0, 0, 0));
		params = new RelativeLayout.LayoutParams(MENU_WIDTH, LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);

        noLocationMenu.setId(ID_NO_LOCATION_MENU);
        noLocationMenu.setChildDivider(new GradientDrawable(Orientation.LEFT_RIGHT, new int[] {0xB4D3D3D3, 0}));
        noLocationMenu.setDividerHeight(1);
        
		RelativeLayout.LayoutParams noLocMenuParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		noLocMenuParams.addRule(ALIGN_PARENT_TOP);
		addView(noLocationMenu, noLocMenuParams);
		noLocationMenu.setVisibility(View.GONE); // do not display this menu by default
		
		GradientDrawable menuSeparationDrawable = new GradientDrawable(Orientation.LEFT_RIGHT, new int[] {0xB4000000, 0xB4FFFFFF, 0xB4000000});
		menuSeparationDrawable.setSize(MENU_WIDTH, 3);
		ImageView menuSeparation = new ImageView(ctx);
		menuSeparation.setId(ID_MENU_SEPARATOR);
		menuSeparation.setImageDrawable(menuSeparationDrawable);
		RelativeLayout.LayoutParams sepParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		sepParams.addRule(BELOW, noLocationMenu.getId());
		addView(menuSeparation, sepParams);

        menu.setDivider(new GradientDrawable(Orientation.LEFT_RIGHT, new int[] {0xB4FFFFFF, 0}));
        menu.setChildDivider(new GradientDrawable(Orientation.LEFT_RIGHT, new int[] {0xB4D3D3D3, 0}));
        menu.setDividerHeight(1);
        
		RelativeLayout.LayoutParams menuParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		menuParams.addRule(BELOW, menuSeparation.getId());
		addView(menu, menuParams);
		
		hideShowButton.setId(ID_HIDE_SHOW_BTN);
		hideShowButton.setText(TEXT_HIDE_BTN);
        hideShowButton.setTextColor(Color.WHITE);
		RelativeLayout.LayoutParams hideBtnParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		hideBtnParams.setMargins(2, 0, 2, 0);
		hideBtnParams.addRule(ALIGN_PARENT_RIGHT);
		hideBtnParams.addRule(ALIGN_PARENT_TOP);
		addView(hideShowButton, hideBtnParams);
	}
	
	private void defineCallbacks() {
		hideShowButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Button b = (Button) v;
				if (b.getText().equals(TEXT_HIDE_BTN)) {
					// hide the menu
					params.width = hideShowButton.getWidth();
					params.height = hideShowButton.getHeight();
					menu.setVisibility(View.GONE);
					noLocationMenu.setVisibility(View.GONE);
					resizeButton.setVisibility(View.GONE);
					setLayoutParams(params);
					b.setText(TEXT_SHOW_BTN);
				} else {
					// show the menu
					params.width = MENU_WIDTH;
					params.height = LayoutParams.WRAP_CONTENT;
					menu.setVisibility(View.VISIBLE);
					if (!noLocationItems.isEmpty()) noLocationMenu.setVisibility(View.VISIBLE);
					resizeButton.setVisibility(View.VISIBLE);
					setLayoutParams(params);
					b.setText(TEXT_HIDE_BTN);
				}
			}
		});
		
		noLocationMenu.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView list, View itemView, int grpPos, int childPos, long id) {
				final IExpandableMenuItem item = noLocationAdapter.getChild(grpPos, childPos);
				if (listener != null) {
					DemandEntity de = new DemandEntity(item.getIcon(), item.getText());
					Enumeration<DemandEntity> demands = noLocationDemands.keys();
					while (demands.hasMoreElements()) {
						DemandEntity d = demands.nextElement();
						if (noLocationDemands.get(d).equals(item)) de = d;
					}
					listener.onMenuItemSelected(de);
					return true;
				}
				return false;
			}
		});
		
		menu.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView list, View itemView, int grpPos, int childPos, long id) {
				final IExpandableMenuItem grp = adapter.getGroup(grpPos);
				final IExpandableMenuItem item = adapter.getChild(grpPos, childPos);
				
				if (grp.getText().equals(GRP_MOYENS)) {
					// selected child is a "Moyens", open the popup menu to specify the demand type
					final String[] data = getVehicleTypeValues(item.getText());
					final PopupSideMenu sideMenu = new PopupSideMenu(ctx, itemView);
					sideMenu.populateMenu(data);
					sideMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> listV, View v, int pos, long id) {
							if (listener != null) listener.onMenuItemSelected(new DemandEntity(item.getIcon(), data[pos]));
							sideMenu.dismiss();
						}
					});
					sideMenu.show();
					
				} else if (grp.getText().equals(GRP_ACTIONS)) {
					if (listener != null) {
						int arrowColor = Color.BLACK;
						
						if (item.getText().equals(ACTION_EAU)) {
							arrowColor = Color.BLUE;
						} else if (item.getText().equals(ACTION_CHEM)) {
							arrowColor = Color.rgb(255, 156, 0);
						} else if (item.getText().equals(ACTION_EXTINCTION)) {
							arrowColor = Color.RED;
						} else if (item.getText().equals(ACTION_SAP)) {
							arrowColor = Color.GREEN;
						} else if (item.getText().equals(ACTION_ZONE)) {
							listener.onMenuItemSelected(new ZoneEntity(item.getIcon(), item.getText(), arrowColor));
							return false;
						}
						
						listener.onMenuItemSelected(new ArrowEntity(item.getIcon(), item.getText(), arrowColor));
					}
					
				} else if (grp.getText().equals(GRP_DANGERS)) {
					if (listener != null) listener.onMenuItemSelected(new DangerEntity(item.getIcon(), item.getText()));
					
				} else  if (grp.getText().equals(GRP_CIBLES)){
					if (listener != null) listener.onMenuItemSelected(new TargetEntity(item.getIcon(), item.getText()));
				
				}
				return false;
			}
			
		});
	}
	
	/**
	 * Adds an entity to the noLocationMenu
	 * If the menu is not currently displayed, it will be added to the view
	 * 
	 * @param e a demand entity with no location set
	 */
	public void addEntityWithNoLocation(DemandEntity e) {
		IExpandableMenuItem item = new ExpandableMenuItem(e.getIcon(), e.getMessage());
		noLocationMenuList.getItems(0).add(item);
		if (hideShowButton.getText().equals(TEXT_HIDE_BTN) && noLocationMenu.getVisibility() == View.GONE) {
			noLocationMenu.setVisibility(View.VISIBLE);
		}
		
		e.addObserver(this);
		noLocationDemands.put(e, item);
		noLocationAdapter.notifyDataSetChanged();
	}

	@Override
	public void update(Observable observable, Object data) {
		// an entity of the NO_LOCATION group has its state changed
		// if the entity gets a location, remove it from the menuGrp
		IEntity entity = (IEntity) data;
		if (entity.getGeoPoint() != null) {
			IExpandableMenuItem item = noLocationDemands.get(entity);
			noLocationDemands.remove(entity);
			noLocationMenuList.removeItem(0, item);
			noLocationAdapter.notifyDataSetChanged();
			if (noLocationMenuList.getItems(0).size() == 0) {
				noLocationMenu.setVisibility(View.GONE);
			}
		}
	}
	
	private String[] getVehicleTypeValues(String menuItemText) {
		String[] ret = null;
		int i = 0;
		VehicleType[] types = null;
		
		if (menuItemText.equals(VehicleSector.SAP.name())) {
			types = VehicleType.getValues(VehicleSector.SAP);
			
		} else if (menuItemText.equals(VehicleSector.ALIM.name())) {
			types = VehicleType.getValues(VehicleSector.ALIM);
			
		} else if (menuItemText.equals(VehicleSector.CHEM.name())) {
			types = VehicleType.getValues(VehicleSector.CHEM);
			
		} else if (menuItemText.equals(VehicleSector.COM.name())) {
			types = VehicleType.getValues(VehicleSector.COM);
			
		} else if (menuItemText.equals(VehicleSector.RTN.name())) {
			types = VehicleType.getValues(VehicleSector.RTN);
			
		} else if (menuItemText.equals(VehicleSector.INC.name())) {
			types = VehicleType.getValues(VehicleSector.INC);
		}
		
		ret = new String[types.length];
		for (VehicleType type : types) ret[i++] = type.name();
		return ret;
	}
	
	public void setOnMenuViewEventListener(OnMenuViewEventListener listener) {
		this.listener = listener;
	}
}