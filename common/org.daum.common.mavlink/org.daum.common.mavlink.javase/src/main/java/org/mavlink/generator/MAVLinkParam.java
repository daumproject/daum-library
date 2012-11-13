/**
 * $Id$
 * $Date$
 *
 * ======================================================
 * Copyright (C) 2012 Guillaume Helle.
 * Project : MAVLink Java Generator
 * Module : org.mavlink.generator
 * File : org.mavlink.generator.MAVLinkParam.java
 * Author : ghelle
 *
 * ======================================================
 * HISTORY
 * Who       yyyy/mm/dd   Action
 * --------  ----------   ------
 * ghelle	2 avr. 2012		Create
 * 
 * ====================================================================
 * Licence: ${licence}
 * ====================================================================
 */

package org.mavlink.generator;

/**
 * MAVLink Param type
 * @author ghelle
 * @version $Rev$
 *
 */
public class MAVLinkParam {

    private int index;

    private String comment;

    public MAVLinkParam(int index) {
        this.index = index;
    }

    /**
     * @return The index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index The index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment The comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

}
