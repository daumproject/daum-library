/**
 * $Id$
 * $Date$
 *
 * ======================================================
 * Copyright (C) 2012 Guillaume Helle.
 * Project : MAVLink Java Generator
 * Module : org.mavlink.generator
 * File : org.mavlink.generator.MAVLinkField.java
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

/**
 * MAVLink Field data
 * @author ghelle
 * @version $Rev$
 *
 */
public class MAVLinkField {

    /**
     * MAVLink Field type
     */
    private MAVLinkDataType type;

    /**
     * MAVLink Field name
     */
    private String name;

    /**
     * MAVLink Field description
     */
    private String description;

    /**
     * MAVLink Field constructor
     * @param type
     * @param name
     */
    public MAVLinkField(MAVLinkDataType type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * @return The type
     */
    public MAVLinkDataType getType() {
        return type;
    }

    /**
     * @param type The type to set
     */
    public void setType(MAVLinkDataType type) {
        this.type = type;
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

}
