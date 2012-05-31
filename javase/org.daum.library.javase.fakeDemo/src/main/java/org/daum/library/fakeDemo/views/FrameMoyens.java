package org.daum.library.fakeDemo.views;

import org.daum.library.fakeDemo.ReaderDaum;

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
    private  Map<Object,Object> prevmoyens=null;

    private String rowData[][] =  { };
    private Object columnNames[] = { "#ID","Type de Moyen", "Caserne", "Etat" };

    private  ReaderDaum readerDaum;

    public FrameMoyens(String nodeName,ReaderDaum readerDaum)
    {

        this.readerDaum = readerDaum;
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





    public void updateMoyens( final Map<Object,Object> moyens)
    {
        //Update Table
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    if(prevmoyens != null){
                        if(!prevmoyens.equals(moyens)){
                            ModelJtable model =  new ModelJtable(moyens,readerDaum);
                            //binding the jtable to the model
                            table.setModel(model);
                            prevmoyens = moyens;
                        }
                    }else {
                        ModelJtable model =  new ModelJtable(moyens,readerDaum);
                        //binding the jtable to the model
                        table.setModel(model);
                        prevmoyens = moyens;
                    }

                }   catch (Exception e)
                {

                }

            }
        });
    }


}