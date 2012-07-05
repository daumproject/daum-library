package org.daum.library.javase.osm;

import org.codehaus.cargo.container.ContainerType;
import org.codehaus.cargo.container.InstalledLocalContainer;
import org.codehaus.cargo.container.configuration.ConfigurationType;
import org.codehaus.cargo.container.configuration.LocalConfiguration;
import org.codehaus.cargo.container.deployable.Deployable;
import org.codehaus.cargo.container.deployable.DeployableType;
import org.codehaus.cargo.generic.DefaultContainerFactory;
import org.codehaus.cargo.generic.configuration.DefaultConfigurationFactory;
import org.codehaus.cargo.generic.deployable.DefaultDeployableFactory;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/4/12
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class testLunchTomcat {
    public static void main(String[] args)
    {
        Deployable war = new DefaultDeployableFactory().createDeployable(
                "tomcat6x", "/udd/pdespagn/Desktop/apache-tomcat-6.0.35/webapps/tila-0.1.1.war", DeployableType.WAR);

        LocalConfiguration configuration = (LocalConfiguration) new DefaultConfigurationFactory().createConfiguration(
                "tomcat6x", ContainerType.INSTALLED, ConfigurationType.STANDALONE);

        configuration.addDeployable(war);

        InstalledLocalContainer container =
                (InstalledLocalContainer) new DefaultContainerFactory().createContainer(
                        "tomcat6x", ContainerType.INSTALLED, configuration);

        container.setHome("/udd/pdespagn/Desktop/apache-tomcat-6.0.35/");
        container.setOutput("/udd/pdespagn/Desktop/output.log");


        container.start();
        System.out.println(container.getOutput());


        /*  container.stop();
        System.out.println("Container is stopped");*/



        // Here you are assured the container is started.

        //container.stop();
        // Here you are assured the container is stopped.


    }
}
