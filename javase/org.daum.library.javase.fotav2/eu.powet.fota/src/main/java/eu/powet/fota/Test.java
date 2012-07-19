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



        // System.load("/home/jed/githubJED/org.powet.fota/eu.powet.fota.native/nux32/target/eu.powet.fota.native.nux32.so");
        Fota fota = new Fota("*", Board.ATMEGA328);




        //  Byte[] intel = Helpers.read_file(NativeLoader.class.getClassLoader().getResourceAsStream("programTest/test.hex"));

        fota.upload("/home/jed/DAUM_PROJECT/daum-library/javase/org.daum.library.javase.fota/src/main/resources/programTest/test.hex");


        //  System.exit(0);
        fota.addEventListener(new FotaEventListener()
        {
            @Override
            public void progressEvent(FotaEvent evt) {
                System.out.println(" Uploaded " + evt.getSize_uploaded()+"/"+evt.getFota().getProgram_size() + " octets");
            }

            @Override
            public void completedEvent(FotaEvent evt) {
                System.out.println("Transmission completed successfully <" + evt.getFota().getProgram_size() + " octets "+evt.getFota().getDuree()+" secondes >");
                System.exit(0);
            }
        });

        Thread.sleep(5000);
        fota.close();


    }

}
