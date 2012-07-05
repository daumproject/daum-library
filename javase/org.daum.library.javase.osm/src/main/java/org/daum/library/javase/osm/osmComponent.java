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
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Library;
import org.kevoree.annotation.Start;
import org.kevoree.annotation.Stop;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/5/12
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "JavaSE")
@ComponentType
public class osmComponent extends AbstractComponentType implements IOSM{

   // private Logger logger = LoggerFactory.getLogger(osmComponent.class);
    private InstalledLocalContainer container;
    private LocalConfiguration configuration;

    @Override
    @Start
    public void startupTomcat() {
        Deployable war = new DefaultDeployableFactory().createDeployable(
                "tomcat6x", "/udd/pdespagn/Desktop/apache-tomcat-6.0.35/webapps/tila-0.1.1.war", DeployableType.WAR);

        configuration = (LocalConfiguration) new DefaultConfigurationFactory().createConfiguration(
                "tomcat6x", ContainerType.INSTALLED, ConfigurationType.STANDALONE);

        configuration.addDeployable(war);
        container =
                (InstalledLocalContainer) new DefaultContainerFactory().createContainer(
                        "tomcat6x", ContainerType.INSTALLED, configuration);

        container.setHome("/udd/pdespagn/Desktop/apache-tomcat-6.0.35/");
        container.setOutput("/udd/pdespagn/Desktop/output.log");

        container.start();
      //  logger.debug("Container started");     */
    }

    @Override
    @Stop
    public void shutdownTomcat() {
        //container.stop();
        //logger.debug("Container stopped");
    }
}
