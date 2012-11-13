/**
 * $Id$
 * $Date$
 *
 * ======================================================
 * Copyright (C) 2012 Guillaume Helle.
 * Project : MAVLink Java Generator
 * Module : org.mavlink.generator
 * File : org.mavlink.generator.MAVLinkEnum.java
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
 * MAVLink Enum data
 * @author ghelle
 * @version $Rev$
 *
 */
public class MAVLinkEnum {

    /**
     * MAVLink Enum name
     */
    private String name;

    /**
     * MAVLink Enum description
     */
    private String description;

    /**
     * MAVLink Enum entries
     */
    private List<MAVLinkEntry> entries;

    /**
     * MAVLink Enum constructor
     * @param name
     */
    public MAVLinkEnum(String name) {
        this.name = name;
        ;
        entries = new ArrayList<MAVLinkEntry>();
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
     * @return The entries
     */
    public List<MAVLinkEntry> getEntries() {
        return entries;
    }

    /**
     * @param entries The entries to set
     */
    public void setEntries(List<MAVLinkEntry> entries) {
        this.entries = entries;
    }

}
