package org.daum.javase.webportal.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Img;

public class Firefighter extends Img {
	
	 public Firefighter() {  
         setWidth(48);  
         setHeight(48);  
         setLayoutAlign(Alignment.CENTER);  
         setCanDragReposition(true);  
         setCanDrop(true);  
         setDragAppearance(DragAppearance.TARGET);  
     }
	 
	 public Firefighter(String src) {  
         this();  
         setSrc("bandeBlanche.jpg");
         setTitle(src);
     }  
       
     public Firefighter(String src, String nom) {  
         this();  
         setSrc(src);
    	 setTitle(nom);
     }  
     
     
}
