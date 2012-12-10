package org.daum.library.android.touchDB;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.listener.TDListener;
import org.slf4j.LoggerFactory;
import org.sqlite.Driver;
import org.sqlite.jdbc.JdbcConnection;

import java.io.IOException;
import java.sql.*;

import org.sqlite.jdbc.JdbcConnection;
import org.sqlite.jdbc.JdbcPreparedStatement;
import org.sqlite.text.Collator;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 03/12/12
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
public class Tester {




    public static void  main(String argv[]) throws IOException, SQLException, ClassNotFoundException {

          /*
        org.sqlite.Driver driver  = new org.sqlite.Driver();
        DriverManager.registerDriver(driver);

      String  url = "jdbc:sqlite:file:/home/jed/jed2.touchdb";
        final Connection conn = DriverManager.getConnection(url);


        Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT revid, deleted, sequence, json FROM revs, docs WHERE docs.docid='1005adf13b6549cea44540d79f4d4e29' AND revs.doc_id=docs.doc_id");
            ResultSetMetaData rsMeta = rs.getMetaData();
            final int cntCol = rsMeta.getColumnCount();
            while (rs.next()) {

                for (int i = 1; i <= cntCol; ++i) {
                    int val = 0;

                    if (rsMeta.getColumnType(i) != Types.BLOB) {
                        System.out.println("ici "+rsMeta.getColumnType(i));
                        val = rs.getInt(i);
                    } else {
                        // convert UNICODE to java.lang.String



                    }
                    System.out.print(val + "|");
                }
                System.out.println();
            }
            rs.close();

            stmt.close();

              System.exit(0); */
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.DEBUG);







        TDServer server = new TDServer("/home/jed/");

        TDListener   listener = new TDListener(server, 8889);
        listener.start();



    }
}
