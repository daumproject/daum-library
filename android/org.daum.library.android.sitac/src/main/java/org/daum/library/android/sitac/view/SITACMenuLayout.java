package org.daum.library.android.sitac.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.widget.*;
import android.widget.ExpandableListView.OnChildClickListener;
import org.daum.library.android.sitac.listener.OnMenuViewEventListener;
import org.daum.library.android.sitac.view.entity.*;
import org.daum.library.android.sitac.view.menu.*;
import org.daum.common.genmodel.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 13/11/12
 * Time: 10:11
 * To change this template use File | Settings | File Templates.
 */

public class SITACMenuLayout extends RelativeLayout implements Observer {

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
    private static final String GRP_AGENTS = "Agents";
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
    private Button hideShowButton;
    private RelativeLayout.LayoutParams params;
    private Hashtable<DemandEntity, IExpandableMenuItem> noLocationDemands;
    private List<IExpandableMenuItem> noLocationItems;

    private List<IExpandableMenuItem> agents = new ArrayList<IExpandableMenuItem>();

    public SITACMenuLayout(Context context) {
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
        dangers.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_BLUE_UP, DangerEntity.WATER));
        dangers.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_RED_UP, DangerEntity.FIRE));
        dangers.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_ORANGE_UP, DangerEntity.CHEM));

        List<IExpandableMenuItem> cibles = new ArrayList<IExpandableMenuItem>();
        cibles.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_BLUE_DOWN, TargetEntity.WATER));
        cibles.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_RED_DOWN, TargetEntity.FIRE));
        cibles.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_GREEN_DOWN, TargetEntity.VICTIM));
        cibles.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_ORANGE_DOWN, TargetEntity.CHEM));

        List<IExpandableMenuItem> moyens = new ArrayList<IExpandableMenuItem>();
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_BLUE_DOTTED_AGRES, VehiculeSector.ALIM.name()));
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_RED_DOTTED_AGRES, VehiculeSector.INC.name()));
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_VIOLET_DOTTED_AGRES, VehiculeSector.COM.name()));
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_BLACK_DOTTED_AGRES, VehiculeSector.RTN.name()));
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_GREEN_DOTTED_AGRES, VehiculeSector.SAP.name()));
        moyens.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_ORANGE_DOTTED_AGRES, VehiculeSector.CHEM.name()));

        List<IExpandableMenuItem> actions = new ArrayList<IExpandableMenuItem>();
        actions.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_LINE_BLUE, ACTION_EAU));
        actions.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_LINE_RED, ACTION_EXTINCTION));
        actions.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_LINE_GREEN, ACTION_SAP));
        actions.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_LINE_ORANGE, ACTION_CHEM));
        actions.add(new ExpandableMenuItem(ctx, DrawableFactory.PICTO_ZONE, ACTION_ZONE));

        menuList = new ExpandableMenuList();
        menuList.addGroup(new ExpandableMenuItem(GRP_AGENTS), agents);
        menuList.addGroup(new ExpandableMenuItem(GRP_MOYENS), moyens);
        menuList.addGroup(new ExpandableMenuItem(GRP_DANGERS), dangers);
        menuList.addGroup(new ExpandableMenuItem(GRP_CIBLES), cibles);
        menuList.addGroup(new ExpandableMenuItem(GRP_ACTIONS), actions);

        noLocationItems = new ArrayList<IExpandableMenuItem>();
        noLocationDemands = new Hashtable<DemandEntity, IExpandableMenuItem>();

        noLocationMenuList = new ExpandableMenuList();
        noLocationMenuList.addGroup(new ExpandableMenuItem(GRP_NO_LOCATION), noLocationItems);
        noLocationAdapter = new ExpandableMenuAdapter(ctx, noLocationMenuList);
        noLocationMenu.setAdapter(noLocationAdapter);

        adapter = new ExpandableMenuAdapter(ctx, menuList);
        menu.setAdapter(adapter);

        hideShowButton = new Button(ctx);
    }

    private void configUI() {
        setBackgroundColor(Color.argb(180, 0, 0, 0));
        params = new RelativeLayout.LayoutParams(MENU_WIDTH, LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        noLocationMenu.setId(ID_NO_LOCATION_MENU);
        noLocationMenu.setChildDivider(new GradientDrawable(Orientation.LEFT_RIGHT, new int[] {0xB4D3D3D3, 0}));
        noLocationMenu.setDividerHeight(1);

        RelativeLayout.LayoutParams noLocMenuParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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

        RelativeLayout.LayoutParams menuParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        menuParams.addRule(BELOW, menuSeparation.getId());
        addView(menu, menuParams);

        hideShowButton.setId(ID_HIDE_SHOW_BTN);
        hideShowButton.setText(TEXT_HIDE_BTN);
        hideShowButton.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams hideBtnParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        hideBtnParams.setMargins(2, 0, 2, 0);
        hideBtnParams.addRule(ALIGN_PARENT_RIGHT);
        hideBtnParams.addRule(ALIGN_PARENT_TOP);
        addView(hideShowButton, hideBtnParams);
    }

    private void defineCallbacks() {
        hideShowButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (hideShowButton.getText().equals(TEXT_HIDE_BTN)) {
                    // hide the menu
                    params.width = hideShowButton.getWidth();
                    params.height = hideShowButton.getHeight();
                    menu.setVisibility(View.GONE);
                    noLocationMenu.setVisibility(View.GONE);
                    setLayoutParams(params);
                    hideShowButton.setText(TEXT_SHOW_BTN);
                } else {
                    // show the menu
                    params.width = MENU_WIDTH;
                    params.height = LayoutParams.WRAP_CONTENT;
                    menu.setVisibility(View.VISIBLE);
                    if (!noLocationItems.isEmpty()) noLocationMenu.setVisibility(View.VISIBLE);
                    setLayoutParams(params);
                    hideShowButton.setText(TEXT_HIDE_BTN);
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

                } else  if (grp.getText().equals(GRP_AGENTS))
                {
                 if (listener != null) listener.setCenterItemMap(item.getEntity());

                }
                return false;
            }

        });
    }

    /**
     * Adds an entity to the noLocationMenu
     * If the menu is not currently displayed, it will be set to VISIBLE
     *
     * @param e a demand entity with no location set
     */
    public void addEntityWithNoLocation(DemandEntity e) {
        if (hideShowButton.getText().equals(TEXT_HIDE_BTN) && noLocationMenu.getVisibility() == View.GONE) {
            noLocationMenu.setVisibility(View.VISIBLE);
        }
        IExpandableMenuItem item = new ExpandableMenuItem(e.getIcon(), e.getType()+e.getMessage());
        noLocationMenuList.getItems(0).add(item);

        e.addObserver(this);
        noLocationDemands.put(e, item);
        noLocationAdapter.notifyDataSetChanged();
    }


    public void addEntityFirefighter(FireFighterEntity e)
    {
        // TODO
        for(IExpandableMenuItem c :agents)
        {
            if(c.getEntity().getType().equals(e.getType()))
            {
                return;
            }
        }
        IExpandableMenuItem item = new ExpandableMenuItem(e.getIcon(),e.getType(),e);
        agents.add(item);
        e.addObserver(this);
    }

    @Override
    public void update(Observable observable, final Object data) {
        // TODO maybe find something cleaner than ((Activity) ctx) casting ?
        ((Activity) ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(data instanceof DemandEntity){
                    DemandEntity entity = (DemandEntity) data;
                    if (entity.isDestroyable() || entity.getGeoPoint() != null) {
                        IExpandableMenuItem item = noLocationDemands.get(entity);
                        noLocationDemands.remove(entity);
                        noLocationMenuList.removeItem(0, item);
                        noLocationAdapter.notifyDataSetChanged();
                        if (noLocationMenuList.getItems(0).size() == 0) {
                            noLocationMenu.setVisibility(View.GONE);
                        }

                    }
                    else
                    {
                        // something happened on the entity that requires a graphical update
                        IExpandableMenuItem item = noLocationDemands.get(entity);
                        item.setIcon(entity.getIcon());
                        item.setText(entity.getType()+entity.getMessage());
                        noLocationAdapter.notifyDataSetChanged();
                    }
                }  else if(data instanceof FireFighterEntity)
                {

                } else {


                }

            }
        });
    }

    private String[] getVehicleTypeValues(String menuItemText) {
        String[] ret = null;
        int i = 0;
        VehiculeType[] types = null;

        if (menuItemText.equals(VehiculeSector.SAP.name())) {
            types = VehiculeType.getValues(VehiculeSector.SAP);

        } else if (menuItemText.equals(VehiculeSector.ALIM.name())) {
            types = VehiculeType.getValues(VehiculeSector.ALIM);

        } else if (menuItemText.equals(VehiculeSector.CHEM.name())) {
            types = VehiculeType.getValues(VehiculeSector.CHEM);

        } else if (menuItemText.equals(VehiculeSector.COM.name())) {
            types = VehiculeType.getValues(VehiculeSector.COM);

        } else if (menuItemText.equals(VehiculeSector.RTN.name())) {
            types = VehiculeType.getValues(VehiculeSector.RTN);

        } else if (menuItemText.equals(VehiculeSector.INC.name())) {
            types = VehiculeType.getValues(VehiculeSector.INC);
        }

        ret = new String[types.length];
        for (VehiculeType type : types) ret[i++] = type.name();
        return ret;
    }

    public void setOnMenuViewEventListener(OnMenuViewEventListener listener) {
        this.listener = listener;
    }

    public void deleteAllEntities() {
        noLocationMenuList.deleteItems();
        noLocationDemands.clear();
        noLocationAdapter.notifyDataSetChanged();
        noLocationMenu.setVisibility(View.GONE);
    }
}