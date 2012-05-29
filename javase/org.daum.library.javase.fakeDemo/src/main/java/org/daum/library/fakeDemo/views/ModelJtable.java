package org.daum.library.fakeDemo.views;

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
               return "id";

           default:
               return null; //Ne devrait jamais arriver
           }
       }
}
