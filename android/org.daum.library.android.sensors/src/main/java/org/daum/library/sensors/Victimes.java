package org.daum.library.sensors;

import android.app.Activity;
import android.view.View;
import android.widget.*;
import org.daum.library.sensors.pojo.Victime;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/11/12
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 */

@ComponentType
@Library(name = "Android")
public class Victimes extends AbstractComponentType {

    KevoreeAndroidService uiService = null;
    LinearLayout layout;
    EditText rfid_tag =null;
    Button button = null;
    private CheckBox dead, alive, dying;
    private Activity ctx;
    private CouchDbClient dbClient;

    @Start
    public void start()
    {
        uiService = UIServiceHandler.getUIService();
        ctx = uiService.getRootActivity();


        button = new Button(uiService.getRootActivity());

        dead= new CheckBox(ctx);
        alive= new CheckBox(ctx);
        alive.setEnabled(true);
        dying= new CheckBox(ctx);

        dbClient = new CouchDbClient("daum",true,"HTTP","192.168.1.101",5984,"","");


        button.setText("DONE");
        button.setWidth(300);

        rfid_tag.setText("RFID_TAG");


        layout = new LinearLayout(uiService.getRootActivity());
        layout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,AbsListView.LayoutParams.FILL_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);


        layout.addView(rfid_tag);
        layout.addView(alive);
        layout.addView(dead);
        layout.addView(dying);

        layout.addView(button);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                Victime t1 = new Victime();
                t1.id = rfid_tag.getText().toString();
                t1.date = new Date();

                if(alive.isChecked())
                    t1.state =  Victime.STATES.ALIVE;
                if(dead.isChecked())
                    t1.state =  Victime.STATES.DEAD;
                if(dying.isChecked())
                    t1.state =  Victime.STATES.DYING;

                Response response = dbClient.save(t1);

                t1 = dbClient.find(Victime.class,response.getId());


                System.out.println(response.getRev());



            }
        });

        uiService.addToGroup("Victime"+getName(), layout);
    }


    @Stop
    public void stop() {
        uiService.remove(layout);
        dbClient.shutdown();
    }
}
