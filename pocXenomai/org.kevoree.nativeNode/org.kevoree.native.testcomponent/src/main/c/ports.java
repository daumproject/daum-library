/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 27/09/12
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */

import org.kevoree.annotation.*;

@Library(name = "native")
@Requires({
        @RequiredPort(name = "output_port", type = PortType.MESSAGE)
})
@Provides({
        @ProvidedPort(name = "input_port", type = PortType.MESSAGE)
})
public interface ports {


}
