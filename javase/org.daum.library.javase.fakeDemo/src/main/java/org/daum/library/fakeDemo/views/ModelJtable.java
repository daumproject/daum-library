package org.daum.library.fakeDemo.views;

import org.daum.library.fakeDemo.ReaderDaum;
import org.daum.library.fakeDemo.pojos.Moyen;
import org.daum.library.ormHM.persistence.PersistenceConfiguration;
import org.daum.library.ormHM.persistence.PersistenceSession;
import org.daum.library.ormHM.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormHM.utils.PersistenceException;

import javax.swing.table.AbstractTableModel;
import java.util.Map;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 29/05/12
 * Time: 19:09
 */
public class ModelJtable extends AbstractTableModel{
    private Map<Object,Object> moyens;
    String columnNames[] = { "#ID","Type de Moyen", "Caserne", "Etat" };
    private  ReaderDaum readerDaum;
    PersistenceConfiguration configuration=null;
    PersistenceSessionFactoryImpl factory=null;
    PersistenceSession s=null;

    public ModelJtable(Map<Object,Object> moyens,ReaderDaum readerDaum)
    {
        super();
        this.readerDaum = readerDaum;
        this.moyens = moyens;

    }

    public int getRowCount() {
        return moyens.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public boolean isCellEditable(int row, int col)
    {
        return true;
    }

    public void setValueAt(Object obj, int rowIndex, int columnIndex)
    {
        factory =  readerDaum.configuration.getPersistenceSessionFactory();
        Moyen update;
        switch(columnIndex){
            case 1 :

                update  =     ((Moyen) moyens.values().toArray()[rowIndex]);
                update.setName(obj.toString());
                try
                {
                    s = factory.openSession();
                    s.save(update);
                    s.close();

                } catch (PersistenceException e) {
                    e.printStackTrace();
                }

                break;

            case 2:

                update =     ((Moyen) moyens.values().toArray()[rowIndex]);
                update.setCaserne(obj.toString());
                try
                {
                    s = factory.openSession();
                    s.save(update);
                    s.close();

                } catch (PersistenceException e) {
                    e.printStackTrace();
                }


                break;
        }


    }


    public Object getValueAt(int rowIndex, int columnIndex) {


        switch(columnIndex){
            case 0:
                return  ((Moyen) moyens.values().toArray()[rowIndex]).getNumber();
            case 1 :
                return  ((Moyen) moyens.values().toArray()[rowIndex]).getName();
            case 2 :
                return  ((Moyen) moyens.values().toArray()[rowIndex]).getCaserne();
            case 3 :
                return ((Moyen) moyens.values().toArray()[rowIndex]).isAvailable();

            default:
                return null; //Ne devrait jamais arriver
        }
    }
}
