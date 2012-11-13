/**
 * $Id$
 * $Date$
 *
 * ======================================================
 * Copyright (C) 2012 Guillaume Helle.
 * Project : MAVLINK Java
 * Module : org.mavlink.generator
 * File : org.mavlink.generator.FieldCompare.java
 * Author : ghelle
 *
 * ======================================================
 * HISTORY
 * Who       yyyy/mm/dd   Action
 * --------  ----------   ------
 * ghelle	7 sept. 2012		Create
 * 
 * ====================================================================
 * Licence: ${licence}
 * ====================================================================
 */

package org.mavlink.generator;

import java.util.Comparator;

/**
 * Comparator to sort field in MAVLink messages.
 * Sort only on the size of field type and ignore array size
 * @author ghelle
 * @version $Rev$
 *
 */
public class FieldCompare implements Comparator<MAVLinkField> {

    /**
     * {@inheritDoc}
     * @see java.util.Comparator#compare(Object, Object)
     */
    @Override
    public int compare(MAVLinkField field2, MAVLinkField field1) {
        //Sort on type size
        if (field1.getType().getTypeSize() > field2.getType().getTypeSize()) {
            return 1;
        }
        else if (field1.getType().getTypeSize() < field2.getType().getTypeSize()) {
            return -1;
        }
        return 0;
    }

}
