/**
 * $Id$
 * $Date$
 *
 * ======================================================
 * Copyright (C) 2012 Guillaume Helle.
 * Project : MAVLink Java Generator
 * Module : org.mavlink.generator
 * File : org.mavlink.generator.IMAVLinkTag.java
 * Author : ghelle
 *
 * ======================================================
 * HISTORY
 * Who       yyyy/mm/dd   Action
 * --------  ----------   ------
 * ghelle	31 mars 2012		Create
 * 
 * ====================================================================
 * Licence: ${licence}
 * ====================================================================
 */

package org.mavlink.generator;

/**
 * MAVLink tags in xml files
 * @author ghelle
 * @version $Rev$
 *
 */
public interface IMAVLinkTag {

    public final static String INCLUDE_TAG = "include";

    public final static String MAVLINK_TAG = "mavlink";

    public final static String VERSION_TAG = "version";

    public final static String DESCRIPTION_TAG = "description";

    public final static String ENUMS_TAG = "enums";

    public final static String ENUM_TAG = "enum";

    public final static String ENTRY_TAG = "entry";

    public final static String PARAM_TAG = "param";

    public final static String MESSAGES_TAG = "messages";

    public final static String MESSAGE_TAG = "message";

    public final static String FIELD_TAG = "field";

    public final static String ID_ATTR = "id";

    public final static String NAME_ATTR = "name";

    public final static String TYPE_ATTR = "type";

    public final static String PRINT_FORMAT_ATTR = "print_format";

    public final static String VALUE_ATTR = "value";

    public final static String INDEX_ATTR = "index";

}
