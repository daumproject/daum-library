/**
 * $Id$
 * $Date$
 *
 * ======================================================
 * Copyright (C) 2012 Guillaume Helle.
 * Project : MAVLink Java Generator
 * Module : org.mavlink.generator
 * File : org.mavlink.generator.MAVLinkGenerator.java
 * Author : ghelle
 *
 * ======================================================
 * HISTORY
 * Who       yyyy/mm/dd   Action
 * --------  ----------   ------
 * ghelle   31 mars 2012        Create
 * 
 * ====================================================================
 * Licence: ${licence}
 * ====================================================================
 */

package org.mavlink.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;



/**
 * MAVLink Java generator.
 * @author ghelle
 * @version $Rev$
 * @see main
 */
public class MAVLinkGenerator {

    public final static String MAVLINK_MSG = "MAVLINK_MSG";

    private String imports = "";

    protected boolean debug = true;

    protected boolean forEmbeddedJava = true;

    protected boolean isLittleEndian = true;

    protected boolean useExtraByte = true;

    protected String source = "resources/v1.0/";

    protected String target = "target";

    public static int[] MAVLINK_MESSAGE_CRCS = new int[256];

    /**
     * Main class for the generator.
     * Command line arguments are :
     *   source : directory path containing xml files to parse for generation
     *   target : directory path for output Java source files
     *   isLittleEndian : true if type are stored in LittleEndian in buffer, false for BigEndian
     *   forEmbeddedJava : true if generated code must use apis for embedded code, false else
     *   useExtraByte : if true use extra crc byte to compute CRC
     *   debug : true to generate toString methods in each message class
     *   
     * Example :
     *   java org.mavlink.generator.MAVLinkGenerator  resources/1.0 target/ true true true true
     *   Generate MAVLink message Java classes for mavlink xml files contains in resources/1.0 
     *   in target diretory for Little Endian data, embedded code using extra byte for crc and generating debug code
     * @param args
     */
    public static void main(String[] args) {

        MAVLinkGenerator generator = new MAVLinkGenerator();
        if (args.length != 6) {
            generator.usage();
            System.exit(-1);
        }
        generator.source = args[0];
        generator.target = args[1];
        generator.isLittleEndian = Boolean.parseBoolean(args[2]);
        generator.forEmbeddedJava = Boolean.parseBoolean(args[3]);
        generator.useExtraByte = Boolean.parseBoolean(args[4]);
        generator.debug = Boolean.parseBoolean(args[5]);

        generator.parseDirectory(generator.source);

        System.exit(0);
    }

    /**
     * Usage method to display manual on console.
     */
    public void usage() {
        System.out.println("Wrong number of arguments for the generator.");
        System.out.println("Command line arguments are :");
        System.out.println("  source : directory path containing xml files to parse for generation");
        System.out.println("  target : directory path for output Java source files");
        System.out.println("  isLittleEndian : true if type are stored in LittleEndian in buffer, false for BigEndian");
        System.out.println("  forEmbeddedJava : true if generated code must use apis for embedded code, false else");
        System.out.println("  debug : true to generate toString methods in each message class");
        System.out.println("  ");
        System.out.println("Example :");
        System.out.println("  java org.mavlink.generator.MAVLinkGenerator  resources/v1.0 target/ true true true");
        System.out.println("  Generate MAVLink message Java classes for mavlink xml files contains in resources/v1.0 ");
        System.out.println("  in target diretory for Little Endian data, embedded code with debug code");
    }

    /**
     * Parse all xml files in directory and call generators methods
     * @param path
     */
    protected void parseDirectory(String path) {
        File directory = new File(path);
        String files[] = directory.list();
        Map<String, String> implementations = new HashMap<String, String>();
        String file = "";
        try {
            for (int i = 0; i < files.length; i++) {
                if (files[i].toLowerCase().indexOf("common") != -1) {
                    if (i != 0) {
                        String tmp = files[0];
                        files[0] = files[i];
                        files[i] = tmp;
                    }
                }
            }
            SAXParserFactory fabrique = SAXParserFactory.newInstance();
            SAXParser parseur = fabrique.newSAXParser();
            MAVLinkData mavlink = null;
            MAVLinkData current;
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(".xml")) {
                    MAVLinkHandler gestionnaire = new MAVLinkHandler();
                    file = path + File.separator + files[i];
                    System.out.println("Parse : " + file);
                    parseur.parse(new File(file), gestionnaire);
                    current = gestionnaire.getMavlink();
                    current.setFile(files[i].substring(0, files[i].indexOf('.')));
                    System.out.println("MAVLinkData : " + current.getFile());
                    generateMessageClass(current, target);
                    if (mavlink == null) {
                        mavlink = current;
                    }
                    else {
                        mavlink.getEnums().putAll(current.getEnums());
                        mavlink.getMessages().putAll(current.getMessages());
                    }
                }
            }
            generateEnumClass(mavlink, target, implementations);
            generateMAVLinkClass(target, implementations);
            generateFactoryClass(mavlink, target);
            generateIMavlinkId(mavlink, target);
            generateMavlinkCoder(mavlink, target);
            generateIMavlinkCRC(target);
        }
        catch (Exception e) {
            System.out.println("MAVLinkGenerator Error : " + file + "  =  " + e);
            e.printStackTrace();
        }
    }

    /**
     * Generate MAVLink messages Java classes
     * @param mavlink
     * @param targetPath
     */
    protected void generateMessageClass(MAVLinkData mavlink, String targetPath) {
        StringBuffer sbRead, sbWrite, fieldWrite;
        String packageRootName = "org.mavlink.messages";
        String xmlFilename = mavlink.getFile();
        String packageName = packageRootName + "." + xmlFilename;
        String directory = targetPath + "/org/mavlink/messages/" + xmlFilename + "/";
        OutputStream output = null;
        PrintWriter writer = null;
        String forToString = "";
        if (xmlFilename.equalsIgnoreCase("common")) {
            imports = imports + "import " + packageName + ".*;\n";
        }
        for (MAVLinkMessage message : mavlink.getMessages().values()) {
            String className = "msg_" + message.getName().toLowerCase();
            String filename = directory + className + ".java";
            if (!xmlFilename.equalsIgnoreCase("common")) {
                imports = imports + "import " + packageName + "." + className + ";\n";
            }
            try {
                File file = new File(directory);
                file.mkdirs();
                output = new FileOutputStream(filename, false);
                writer = new PrintWriter(output);
                sbRead = new StringBuffer();
                sbWrite = new StringBuffer();
                fieldWrite = new StringBuffer();
                if (forEmbeddedJava) {
                    sbWrite.append("  dos.writeByte((byte)0xFE);\n");
                    sbWrite.append("  dos.writeByte(length & 0x00FF);\n");
                    sbWrite.append("  dos.writeByte(sequence & 0x00FF);\n");
                    sbWrite.append("  dos.writeByte(sysId & 0x00FF);\n");
                    sbWrite.append("  dos.writeByte(componentId & 0x00FF);\n");
                    sbWrite.append("  dos.writeByte(messageType & 0x00FF);\n");
                }
                else {
                    sbWrite.append("  dos.put((byte)0xFE);\n");
                    sbWrite.append("  dos.put((byte)(length & 0x00FF));\n");
                    sbWrite.append("  dos.put((byte)(sequence & 0x00FF));\n");
                    sbWrite.append("  dos.put((byte)(sysId & 0x00FF));\n");
                    sbWrite.append("  dos.put((byte)(componentId & 0x00FF));\n");
                    sbWrite.append("  dos.put((byte)(messageType & 0x00FF));\n");
                }
                // Write Header
                writer.print("/**\n * Generated class : " + className + "\n * DO NOT MODIFY!\n **/\n");
                writer.print("package " + packageName + ";\n");
                writer.print("import " + packageRootName + ".MAVLinkMessage;\n");
                writer.print("import org.mavlink.IMAVLinkCRC;\n");
                writer.print("import org.mavlink.MAVLinkCRC;\n");
                writer.print("import java.io.ByteArrayOutputStream;\n");
                writer.print("import java.io.IOException;\n");
                if (forEmbeddedJava) {
                    if (isLittleEndian) {
                        writer.print("import org.mavlink.io.LittleEndianDataInputStream;\n");
                        writer.print("import org.mavlink.io.LittleEndianDataOutputStream;\n");
                    }
                    else {
                        writer.print("import java.io.DataInputStream;\n");
                        writer.print("import java.io.DataOutputStream;\n");
                    }
                }
                else {
                    writer.print("import java.nio.ByteBuffer;\n");
                    writer.print("import java.nio.ByteOrder;\n");
                }
                String description = message.getDescription();
                writer.print("/**\n * Class " + className + "\n * " + (description == null ? "" : message.getDescription().trim()) + "\n **/\n");
                writer.print("public class " + className + " extends MAVLinkMessage {\n");
                String id = MAVLINK_MSG + "_ID_" + message.getName();
                writer.print("  public static final int " + id + " = " + message.getId() + ";\n");
                writer.print("  private static final long serialVersionUID = " + id + ";\n");
                writer.print("  public " + className + "(int sysId, int componentId) {\n    messageType = " + id
                             + ";\n    this.sysId = sysId;\n    this.componentId = componentId;\n");

                // Calculate extra_crc for Mavlinl 1.0
                String extraCrcBuffer = message.getName() + " ";
                // Write Fields
                int fieldLen = 0;
                Collections.sort(message.getFields(), new FieldCompare());
                for (int j = 0; j < message.getFields().size(); j++) {
                    MAVLinkField field = message.getFields().get(j);
                    fieldWrite.append("  /**\n   * " + field.getDescription().trim() + "\n   */\n");
                    MAVLinkDataType type = field.getType();
                    fieldWrite.append("  public " + type.getJavaType(field.getName()) + "\n");
                    sbRead.append(type.getReadType(field.getName(), forEmbeddedJava));
                    sbWrite.append(type.getWriteType(field.getName(), forEmbeddedJava));
                    String attr = field.getName();
                    if (type.isArray && type.type == MAVLinkDataType.CHAR) {
                        String first = "" + attr.charAt(0);
                        attr = first.toUpperCase() + field.getName().substring(1);
                        fieldWrite.append("  public void set" + attr + "(String tmp) {\n");
                        fieldWrite.append("    int len = Math.min(tmp.length(), " + type.arrayLenth + ");\n");
                        fieldWrite.append("    for (int i=0; i<len; i++) {\n      " + field.getName() + "[i] = tmp.charAt(i);\n    }\n");
                        fieldWrite.append("    for (int i=len; i<" + type.arrayLenth + "; i++) {\n      " + field.getName()
                                          + "[i] = 0;\n    }\n  }\n");
                        fieldWrite.append("  public String get" + attr + "() {\n");
                        fieldWrite.append("    String result=\"\";\n");
                        fieldWrite.append("    for (int i=0; i<" + type.arrayLenth + "; i++) {\n      if (" + field.getName()
                                          + "[i] != 0) result=result+" + field.getName() + "[i]; else break;\n    }\n    return result;\n  }\n");
                    }
                    fieldLen += type.getLengthType();
                    forToString = forToString + (j != 0 ? "+" : "") + "  \"  " + field.getName() + "=\"+"
                                  + (field.getType().isArray && field.getType().type == MAVLinkDataType.CHAR ? "get" + attr + "()" : field.getName());
                    extraCrcBuffer = extraCrcBuffer + type.getCType() + " " + field.getName() + " ";
                    if (type.isArray) {
                        extraCrcBuffer = extraCrcBuffer + (char) type.arrayLenth;
                    }
                }
                writer.print("    length = " + fieldLen + ";\n}\n\n");
                writer.print(fieldWrite.toString());
                int extra_crc = MAVLinkCRC.crc_calculate(MAVLinkCRC.stringToByte(extraCrcBuffer));
                int magicNumber = (extra_crc & 0x00FF) ^ ((extra_crc >> 8 & 0x00FF));
                MAVLINK_MESSAGE_CRCS[message.getId()] = magicNumber;

                writer.print("/**\n");
                writer.print(" * Decode message with raw data\n");
                writer.print(" */\n");
                if (forEmbeddedJava) {
                    if (isLittleEndian) {
                        writer.print("public void decode(LittleEndianDataInputStream dis) throws IOException {\n");
                    }
                    else {
                        writer.print("public void decode(DataInputStream dis) throws IOException {\n");
                    }
                }
                else {
                    writer.print("public void decode(ByteBuffer dis) throws IOException {\n");
                }

                writer.print(sbRead.toString());
                writer.print("}\n");

                writer.print("/**\n");
                writer.print(" * Encode message with raw data and other informations\n");
                writer.print(" */\n");
                writer.print("public byte[] encode() throws IOException {\n");
                writer.print("  byte[] buffer = new byte[8+" + fieldLen + "];\n");
                if (forEmbeddedJava) {
                    if (isLittleEndian) {
                        writer.print("   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());\n");
                    }
                    else {
                        writer.print("   DataOutputStream dos = new DataOutputStream(new ByteArrayOutputStream());\n");
                    }
                }
                else {
                    if (isLittleEndian) {
                        writer.print("   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);\n");
                    }
                    else {
                        writer.print("   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.BIG_ENDIAN);\n");
                    }
                }
                writer.print(sbWrite.toString());
                if (forEmbeddedJava) {
                    writer.print("  dos.flush();\n  byte[] tmp = dos.toByteArray();\n");
                    writer.print("  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];\n");
                }
                else {
                    // nothing
                }
                writer.print("  int crc = MAVLinkCRC.crc_calculate_encode(buffer, " + fieldLen + ");\n");
                writer.print("  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);\n");
                writer.print("  byte crcl = (byte) (crc & 0x00FF);\n");
                writer.print("  byte crch = (byte) ((crc >> 8) & 0x00FF);\n");
                writer.print("  buffer[" + (fieldLen + 6) + "] = crcl;\n");
                writer.print("  buffer[" + (fieldLen + 7) + "] = crch;\n");
                writer.print("  return buffer;\n}\n");

                if (debug) {
                    writer.print("public String toString() {\n");
                    writer.print("return \"" + id + " : \" + " + forToString + ";");
                    writer.print("}\n");
                }
                writer.print("}\n");
                forToString = "";
            }
            catch (Exception e) {
                System.out.println("ERROR : " + e);
                e.printStackTrace();
            }
            finally {
                try {
                    writer.close();
                    output.close();
                }
                catch (Exception ex) {
                }
            }
        }
    }

    /**
     * Generate MAVLink Java Enum classes
     * @param mavlink
     * @param targetPath
     * @param implementations
     */
    protected void generateEnumClass(MAVLinkData mavlink, String targetPath, Map<String, String> implementations) {
        String packageRootName = "org.mavlink.messages";
        String packageName = packageRootName;
        String directory = targetPath + "/org/mavlink/messages/";
        OutputStream output = null;
        PrintWriter writer = null;

        try {
            for (MAVLinkEnum message : mavlink.getEnums().values()) {
                String className = message.getName();
                String filename = directory + className + ".java";
                output = new FileOutputStream(filename, false);
                writer = new PrintWriter(output);
                writer.print("/**\n * Generated class : " + className + "\n * DO NOT MODIFY!\n **/\n");
                writer.print("package " + packageName + ";\n");
                String description = message.getDescription();
                writer.print("/**\n * Interface " + className + "\n * " + (description == null ? "" : message.getDescription().trim()) + "\n **/\n");
                writer.print("public interface " + className + " {\n");
                implementations.put(className.trim(), className.trim());
                for (int j = 0; j < message.getEntries().size(); j++) {
                    MAVLinkEntry entry = message.getEntries().get(j);
                    writer.print("    /**\n    /* " + entry.getDescription() + "\n    */\n");
                    writer.print("    public final static int " + entry.getName() + " = " + entry.getValue() + ";\n");
                    for (int k = 0; k < entry.getParams().size(); k++) {
                        MAVLinkParam param = entry.getParams().get(k);
                        //                        if (debug)
                        //                            System.out.println("???   PARAM  " + param.getIndex() + " " + param.getComment());
                    }
                }
                writer.print("}\n");
                writer.close();
                output.close();
            }
        }
        catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
                output.close();
            }
            catch (Exception ex) {
            }
        }
    }

    /**
     * Generate Interface which extends all enums classes
     * @param targetPath
     * @param implementation
     */
    protected void generateMAVLinkClass(String targetPath, Map<String, String> implementation) {
        String packageRootName = "org.mavlink.messages";
        String packageName = packageRootName;
        String directory = targetPath + "/org/mavlink/messages/";
        OutputStream output = null;
        PrintWriter writer = null;
        String allImpl = "";
        for (String impl : implementation.values()) {
            allImpl = allImpl + impl + ",";
        }
        try {
            String className = "IMAVLink";
            String filename = directory + className + ".java";
            output = new FileOutputStream(filename, false);
            writer = new PrintWriter(output);
            writer.print("/**\n * Generated class : " + className + "\n * DO NOT MODIFY!\n **/\n");
            writer.print("package " + packageName + ";\n");
            writer.print("/**\n * Interface " + className + "\n * Implement all constants in enums and entries \n **/\n");
            writer.print("public interface " + className + " extends " + allImpl.substring(0, allImpl.length() - 1) + " {\n}\n");
        }
        catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
                output.close();
            }
            catch (Exception ex) {
            }
        }
    }

    /**
     * Generate a factory classe which generate MAVLink messages from byte array
     * @param mavlink
     * @param targetPath
     */
    protected void generateFactoryClass(MAVLinkData mavlink, String targetPath) {
        StringBuffer sb;
        String packageRootName = "org.mavlink.messages";
        String packageName = packageRootName;
        String directory = targetPath + "/org/mavlink/messages/";
        OutputStream output = null;
        PrintWriter writer = null;
        String className = "MAVLinkMessageFactory";
        String filename = directory + className + ".java";
        try {
            File file = new File(directory);
            file.mkdirs();
            output = new FileOutputStream(filename, false);
            writer = new PrintWriter(output);
            sb = new StringBuffer();
            // Write Header
            writer.print("/**\n * Generated class : " + className + "\n * DO NOT MODIFY!\n **/\n");
            writer.print("package " + packageName + ";\n");
            writer.print("import " + packageRootName + ".MAVLinkMessage;\n");
            writer.print("import org.mavlink.IMAVLinkMessage;\n");
            writer.print("import java.io.IOException;\n");
            if (forEmbeddedJava) {
                if (isLittleEndian) {
                    writer.print("import org.mavlink.io.LittleEndianDataInputStream;\n");
                    writer.print("import java.io.ByteArrayInputStream;\n");
                }
                else {
                    writer.print("import java.io.DataInputStream;\n");
                    writer.print("import java.io.ByteArrayInputStream;\n");
                }
            }
            else {
                writer.print("import java.nio.ByteBuffer;\n");
                writer.print("import java.nio.ByteOrder;\n");
            }
            writer.print(imports);
            writer.print("/**\n * Class MAVLinkMessageFactory\n * Generate MAVLink message classes from byte array\n **/\n");
            writer.print("public class MAVLinkMessageFactory implements IMAVLinkMessage, IMAVLinkMessageID {\n");
            writer.print("public static MAVLinkMessage getMessage(int msgid, int sysId, int componentId, byte[] rawData) throws IOException {\n");
            writer.print("    MAVLinkMessage msg=null;\n");
            if (forEmbeddedJava) {
                if (isLittleEndian) {
                    writer.print("    LittleEndianDataInputStream dis = new LittleEndianDataInputStream(new ByteArrayInputStream(rawData));\n");
                }
                else {
                    writer.print("    DataInputStream dis = new DataInputStream(new ByteArrayInputStream(rawData));\n");
                }
            }
            else {
                if (isLittleEndian) {
                    writer.print("    ByteBuffer dis = ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN);\n");
                }
                else {
                    writer.print("    ByteBuffer dis = ByteBuffer.wrap(rawData).order(ByteOrder.BIG_ENDIAN);\n");
                }
            }
            writer.print("    switch(msgid) {\n");
            for (MAVLinkMessage message : mavlink.getMessages().values()) {
                String msgClassName = "msg_" + message.getName().toLowerCase();
                String id = MAVLINK_MSG + "_ID_" + message.getName();
                writer.print("  case " + id + ":\n");
                writer.print("      msg = new " + msgClassName + "(sysId, componentId);\n");
                writer.print("      msg.decode(dis);\n");
                writer.print("      break;\n");
            }
            writer.print("  default:\n");
            writer.print("      System.out.println(\"Mavlink Factory Error : unknown MsgId : \" + msgid);\n");
            writer.print("    }\n");
            writer.print("    return msg;\n");
            writer.print("  }\n");
            writer.print("}\n");
        }
        catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
                output.close();
            }
            catch (Exception ex) {
            }
        }
    }

    /**
     * Generate Interface with all MAVLink messages ID
     * @param mavlink
     * @param targetPath
     */
    protected void generateIMavlinkId(MAVLinkData mavlink, String targetPath) {
        StringBuffer sb;
        String packageRootName = "org.mavlink.messages";
        String packageName = packageRootName;
        String directory = targetPath + "/org/mavlink/messages/";
        OutputStream output = null;
        PrintWriter writer = null;
        String className = "IMAVLinkMessageID";
        String filename = directory + className + ".java";
        try {
            File file = new File(directory);
            file.mkdirs();
            output = new FileOutputStream(filename, false);
            writer = new PrintWriter(output);
            sb = new StringBuffer();
            // Write Header
            writer.print("/**\n * Generated class : " + className + "\n * DO NOT MODIFY!\n **/\n");
            writer.print("package " + packageName + ";\n");
            writer.print("/**\n * Interface IMAVLinkMessageId\n * Generate al MAVLink message Id in an interface\n **/\n");
            writer.print("public interface IMAVLinkMessageID {\n");
            for (MAVLinkMessage message : mavlink.getMessages().values()) {
                String id = MAVLINK_MSG + "_ID_" + message.getName();
                writer.print("  public static int " + id + " = " + message.getId() + ";\n");
            }
            writer.print("}\n");
        }
        catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
                output.close();
            }
            catch (Exception ex) {
            }
        }
    }

    /**
     * Generate interface with all extra crc for messages
     * @param targetPath
     */
    protected void generateIMavlinkCRC(String targetPath) {
        StringBuffer sb;
        String packageRootName = "org.mavlink";
        String packageName = packageRootName;
        String directory = targetPath + "/org/mavlink/";
        OutputStream output = null;
        PrintWriter writer = null;
        String className = "IMAVLinkCRC";
        String filename = directory + className + ".java";
        try {
            File file = new File(directory);
            file.mkdirs();
            output = new FileOutputStream(filename, false);
            writer = new PrintWriter(output);
            sb = new StringBuffer();
            // Write Header
            writer.print("/**\n * Generated class : " + className + "\n * DO NOT MODIFY!\n **/\n");
            writer.print("package " + packageName + ";\n");
            writer.print("/**\n * Interface IMAVLinkCRC\n * Extra byte to compute CRC in Mavlink 1.0\n **/\n");
            writer.print("public interface IMAVLinkCRC {\n");
            if (useExtraByte) {
                writer.print("  public static boolean MAVLINK_EXTRA_CRC = true;\n");
            }
            else {
                writer.print("  public static boolean MAVLINK_EXTRA_CRC = false;\n");
            }
            writer.print("  public static char[] MAVLINK_MESSAGE_CRCS = {\n");
            for (int i = 0; i < MAVLINK_MESSAGE_CRCS.length; i++) {
                if (i % 25 == 0)
                    writer.print("\n          ");
                writer.print(MAVLINK_MESSAGE_CRCS[i]);
                if (i != MAVLINK_MESSAGE_CRCS.length - 1)
                    writer.print(", ");
            }
            writer.print("};\n}");
        }
        catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
                output.close();
            }
            catch (Exception ex) {
            }
        }
    }

    /**
     * Generate encode and decode methods for all MAVLink messages
     * @param mavlink
     * @param targetPath
     */
    protected void generateMavlinkCoder(MAVLinkData mavlink, String targetPath) {
        StringBuffer sb;
        String packageRootName = "org.mavlink.messages";
        String packageName = packageRootName;
        String directory = targetPath + "/org/mavlink/messages/";
        OutputStream output = null;
        PrintWriter writer = null;
        String className = "MAVLinkMessageCoder";
        String filename = directory + className + ".java";
        try {
            File file = new File(directory);
            file.mkdirs();
            output = new FileOutputStream(filename, false);
            writer = new PrintWriter(output);
            sb = new StringBuffer();
            // Write Header
            writer.print("/**\n * Generated class : " + className + "\n * DO NOT MODIFY!\n **/\n");
            writer.print("package " + packageName + ";\n");
            writer.print("import java.io.IOException;\n");
            if (forEmbeddedJava) {
                if (isLittleEndian) {
                    writer.print("import org.mavlink.io.LittleEndianDataInputStream;\n");
                }
                else {
                    writer.print("import java.io.DataInputStream;\n");
                }
            }
            else {
                writer.print("import java.io.Serializable;");
                writer.print("import java.nio.ByteBuffer;\n");
                writer.print("import java.nio.ByteOrder;\n");
            }
            writer.print("/**\n * Class MAVLinkMessageCoder\n * Use to declarate encode and decode functions\n **/\n");
            writer.print("public abstract class MAVLinkMessageCoder ");
            if (forEmbeddedJava) {
                writer.print("{\n");
            }
            else {
                writer.print(" implements Serializable{\n");
            }
            writer.print("  /**\n");
            writer.print("   * Decode message with raw data\n");
            writer.print("   */\n");
            if (forEmbeddedJava) {
                if (isLittleEndian) {
                    writer.print("  public abstract void decode(LittleEndianDataInputStream dis) throws IOException ;\n");
                }
                else {
                    writer.print("  public abstract void decode(DataInputStream dis) throws IOException ;\n");
                }
            }
            else {
                writer.print("  public abstract void decode(ByteBuffer dis) throws IOException ;\n");
            }
            writer.print("  /**\n");
            writer.print("   * Encode message in raw data\n");
            writer.print("   */\n");
            if (forEmbeddedJava) {
                if (isLittleEndian) {
                    writer.print("  public abstract byte[] encode() throws IOException ;\n");
                }
                else {
                    writer.print("  public abstract byte[] encode() throws IOException ;\n");
                }
            }
            else {
                writer.print("  public abstract byte[] encode() throws IOException ;\n");
            }
            writer.print("}\n");
        }
        catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
                output.close();
            }
            catch (Exception ex) {
            }
        }
    }
}
