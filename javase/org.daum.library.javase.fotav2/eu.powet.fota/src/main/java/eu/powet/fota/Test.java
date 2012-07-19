package eu.powet.fota;

import eu.powet.fota.api.FotaEventListener;
import eu.powet.fota.events.FotaEvent;
import eu.powet.fota.utils.Board;


public class Test {

    /**
     * @param args
     * @throws Exception
     */


    public static void main(String[] args) throws Exception {

        try
        {
            Fota fota = new Fota("*", Board.ATMEGA328);

            fota.upload("/home/jed/DAUM_PROJECT/daum-library/javase/org.daum.library.javase.fota/src/main/resources/programTest/test.hex");


            fota.addEventListener(new FotaEventListener()
            {
                @Override
                public void progressEvent(FotaEvent evt) {
                    System.out.println(" Uploaded " + evt.getSize_uploaded()+"/"+evt.getProgram_size() + " octets");
                }

                @Override
                public void completedEvent(FotaEvent evt) {
                    System.out.println("Transmission completed successfully <" + evt.getProgram_size() + " octets "+evt.getDuree()+" secondes >");
                    System.exit(0);
                }
            });

            Thread.sleep(3000);


        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
