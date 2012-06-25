package org.daum.library.web;

import org.daum.library.fakeDemo.pojos.TemperatureMonitor;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.kevoree.annotation.*;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/06/12
 * Time: 09:54
 * To change this template use File | Settings | File Templates.
 */
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true) })
@ComponentType
public class ImagePage extends AbstractPage
{

    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;
    private PersistenceSession s=null;

    public void init()
    {
        if(replicaService == null )
        {
            try
            {
                configuration = new PersistenceConfiguration(getNodeName());
                replicaService =   this.getPortByName("service", ReplicaService.class);
                ReplicaStore store = new ReplicaStore(replicaService);
                configuration.setStore(store);
                configuration.addPersistentClass(TemperatureMonitor.class);
                factory = configuration.getPersistenceSessionFactory();

            } catch (PersistenceException e)
            {
                logger.error("init : ",e);
            }
        }
    }



    @Override
    public KevoreeHttpResponse process (KevoreeHttpRequest request, KevoreeHttpResponse response) {

        try
        {
            init();

            s = factory.getSession();
            BufferedImage bufferedImage = new BufferedImage(500, 100, BufferedImage.TYPE_INT_RGB);

            //Draw an oval
            Graphics2D g = (Graphics2D)bufferedImage.getGraphics();
            g.setColor(Color.RED);
            g.setBackground(Color.WHITE);
            g.clearRect(0, 0, 500, 100);

            Courbe tempCourbe = new Courbe();
            Map<Object,TemperatureMonitor> listtemp = (Map<Object, TemperatureMonitor>) s.getAll(TemperatureMonitor.class);
            if(listtemp != null)
            {
                int i=0;
                if(listtemp.size() > 0)
                {
                    for(Object key : listtemp.keySet())
                    {
                        TemperatureMonitor t = (TemperatureMonitor) listtemp.get(key);
                        tempCourbe.add(t.getValue());
                       i++;
                    }
                }

                double pas = 800/tempCourbe.length();
                g.drawLine(0,50+(int)tempCourbe.getVal(0),(int)pas,50+(int)tempCourbe.getVal(0));
                for(i=0;i<tempCourbe.length()-1;i++) {
                    g.drawLine((int)((i+1)*pas),50+(int)tempCourbe.getVal(i),(int)((i+2)*pas),50+(int)tempCourbe.getVal(i + 1));
                }

            }

            g.dispose();



            ByteArrayOutputStream baos = new ByteArrayOutputStream();


            ImageIO.write(bufferedImage, "PNG", baos);

            response.setRawContent(baos.toByteArray());
        } catch (Exception e)
        {
            replicaService = null;
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }   finally {
            if(s != null){

                s.close();
            }
            s = null;
        }

        return response;
    }



}
