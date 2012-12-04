package org.daum.library.android.touchDB;


import java.io.*;
import java.net.HttpURLConnection;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 03/12/12
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
public class Tester {

    //curl -H 'Content-Type: application/json' -X POST -d '{"source":"http://192.168.1.121:8888/jed","target":"jed","continuous":true}' http://192.168.1.113:8888/_replicate



    public static  String getBaseUri(String host,int port){

        return "http://"+host+":"+port;
    }



    public static String excutePost(String targetURL, String urlParameters)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("Cache-Control", "no-cache");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void  main(String argv[]) throws IOException {


        String localDB= "jed2";
        String target_db = "jed2";

        String contentType = "application/json";
        String json = "{\"source\":\""+getBaseUri("192.168.1.113",8888)+localDB+"\",\"target\":\""+target_db+"\", \"continuous\":true}'";


        excutePost(" http://192.168.1.121:8888/_replicate",json);







    }
}
