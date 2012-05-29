package org.daum.library.fakeDemo.views;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 29/05/12
 * Time: 18:20
 */
public class FrameMoyens extends JFrame {
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 600;
    private JTable table;


    // String rowData[][] = { { "BLS", "RENNES", "LIBRE" },   { "FPT", "RENNES", "LIBRE" } };

    String rowData[][] =  { };


    Object columnNames[] = { "#ID","Type de Moyen", "Caserne", "Etat" };

    public FrameMoyens(String nodeName)
    {

        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setLayout(new BorderLayout());
        setTitle(nodeName);
        setVisible(true);

        table = new JTable(rowData, columnNames);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }





    public void updateMoyens( Map<Object,Object> moyens)
    {

        ModelJtable model =  new ModelJtable(moyens);
        //binding the jtable to the model
        table.setModel(model);
    }


}