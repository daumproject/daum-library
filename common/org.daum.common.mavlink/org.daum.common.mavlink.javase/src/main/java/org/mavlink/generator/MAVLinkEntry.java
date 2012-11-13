/**
 * $Id$
 * $Date$
 *
 * ======================================================
 * Copyright (C) 2012 Guillaume Helle.
 * Project : MAVLink Java Generator
 * Module : org.mavlink.generator
 * File : org.mavlink.generator.MAVLinkEntry.java
 * Author : ghelle
 *
 * ======================================================
 * HISTORY
 * Who       yyyy/mm/dd   Action
 * --------  ----------   ------
 * ghelle	30 mars 2012		Create
 * 
 * ====================================================================
 * Licence: ${licence}
 * ====================================================================
 */

package org.mavlink.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * MAVLink Entry data
 * @author ghelle
 * @version $Rev$
 *
 */
public class MAVLinkEntry {

    /**
     * MAVLink Entry value
     */
    private int value;

    /**
     * MAVLink Entry name
     */
    private String name;

    /**
     * MAVLink Entry description
     */
    private String description;

    /**
     * MAVLink Entry params
     */
    private List<MAVLinkParam> params;

    /**
     * MAVLink Entry constructor
     * @param value
     * @param name
     */
    public MAVLinkEntry(int value, String name) {
        this.value = value;
        this.name = name;
        params = new ArrayList<MAVLinkParam>();
    }

    /**
     * @return The value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value The value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The params
     */
    public List<MAVLinkParam> getParams() {
        return params;
    }

    /**
     * @param params The params to set
     */
    public void setParams(List<MAVLinkParam> params) {
        this.params = params;
    }

}
