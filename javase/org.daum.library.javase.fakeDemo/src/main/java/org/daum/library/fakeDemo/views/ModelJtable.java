package org.daum.library.fakeDemo.views;

import org.daum.library.fakeDemo.pojos.Moyen;

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


   	public ModelJtable(Map<Object,Object> moyens) {
   		super();
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

   	public Object getValueAt(int rowIndex, int columnIndex) {


           switch(columnIndex){
           case 0:
               return  ((Moyen) moyens.values().toArray()[rowIndex]).getNumber();
           case 1 :
               return  ((Moyen) moyens.values().toArray()[rowIndex]).getName();
           case 2 :
               return  ((Moyen) moyens.values().toArray()[rowIndex]).getName();
           case 3 :
               return ((Moyen) moyens.values().toArray()[rowIndex]).isAvailable();

           default:
               return null; //Ne devrait jamais arriver
           }
       }
}
