/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kevoree.tools.nativeCode.mavenplugin;

import org.apache.maven.model.Model;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.kevoree.*;
import org.kevoree.api.service.core.script.KevScriptEngineException;
import org.kevoree.nativeN.gen.NativeSourceGen;
import org.kevoree.nativeN.utils.FileManager;
import org.kevoree.nativeN.utils.IKevScriptLoader;
import org.kevoree.nativeN.utils.KevScriptLoader;
import org.kevoree.tools.nativeCode.mavenplugin.utils.MavenHelper;

import java.io.*;
import java.util.List;

/**
 * @author jedartois
 * @author <a href="mailto:jedartois@gmail.com">Jean-Emile DARTOIS</a>
 * @version $Id$
 * @goal generate
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class GenerateFilesMojo extends AbstractMojo {

    /**
     * The directory root under which processor-generated source files will be placed; files are placed in
     * subdirectories based on package namespace. This is equivalent to the <code>-s</code> argument for apt.
     *
     * @parameter default-value="${project.build.directory}/generated-sources/kevoree"
     */
    private File sourceOutputDirectory;

    /**
     *
     * @parameter default-value="${basedir}/src/main"
     */
    private File inputCFile;


    /**
     * POM
     *
     * @parameter expression="${project}"
     * @readonly
     * @required
     */

    protected MavenProject project;
    private  NativeSourceGen nativeSourceGen = new NativeSourceGen();
    private IKevScriptLoader loader = new KevScriptLoader();


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if(inputCFile == null || !(inputCFile.exists())){
            getLog().warn("InputDir null => ");
        } else {
            List<File> componentFiles = MavenHelper.scanForKevScript(inputCFile);
            System.out.println(inputCFile + " size = " + componentFiles.size());
            for(File f : componentFiles){
                getLog().info("File found =>"+f.getAbsolutePath());
                try
                {
                    generateSources(f.getName().replace(".kevs", ""), f.getPath(), f.getPath().replace(f.getName(), ""));
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }


    public  void parseKevscript(String path_file, String componentName) throws KevScriptEngineException {
        getLog().info("Parsing KevSript "+path_file);
        ContainerRoot model =  loader.loadKevScript(path_file);
        for(TypeDefinition type :  model.getTypeDefinitionsForJ()) {
            if(type instanceof ComponentType) {
                ComponentType c = (ComponentType)type;
                if(c.getName().equals(componentName)){
                    for(PortTypeRef portP :  c.getProvidedForJ() )    {  nativeSourceGen.create_input(portP.getName()); }
                    for(PortTypeRef portR :  c.getRequiredForJ()) { nativeSourceGen.create_output(portR.getName()); }
                    break;
                }
            }
        }
    }

    /**
     * Generate interfaces
     * @param componentName
     * @param path_file
     * @param path_out
     * @throws Exception
     */
    public  void generateSources(String componentName, String path_file, String path_out) throws Exception {

        parseKevscript(path_file, componentName);

        Model component_java =     MavenHelper.createModel(project.getGroupId(), project.getArtifactId() + "_bridge", project.getVersion(), MavenHelper.createParent(project), project);
        component_java.setName("Kevoree :: Native :: Bridge Java");

        Model component_c=     MavenHelper.createModel(project.getGroupId(), project.getArtifactId() + "_c", project.getVersion(), MavenHelper.createParent(project), project);
        component_c.setName("Kevoree :: Native :: C");

        MavenHelper.createPom("pom.xml.component", component_java, project,component_java.getPomFile().getPath());
        MavenHelper.createPom("pom.xml.c", component_c, project,component_c.getPomFile().getPath());
        MavenHelper.createPom("pom.xml.c.nix32", component_c, project,component_c.getPomFile().getPath().replace("pom.xml","nix32/pom.xml"));
        MavenHelper.createPom("pom.xml.c.nix64", component_c, project,component_c.getPomFile().getPath().replace("pom.xml","nix64/pom.xml"));
        MavenHelper.createPom("pom.xml.c.osx", component_c, project,component_c.getPomFile().getPath().replace("pom.xml", "osx/pom.xml"));

        /// GENERATE JAVA FILES
        File file_java = new File(component_java.getPomFile().getAbsolutePath().replace("pom.xml","")+"src/main/java/org/kevoree/nativeN/"+componentName);

        if(file_java.mkdirs())
        {
            String bridge = new String(FileManager.load(GenerateFilesMojo.class.getClassLoader().getResourceAsStream("Bridge.template")));
            bridge =  bridge.replace("$PACKAGE$","package org.kevoree.nativeN."+componentName+";");
            bridge =   bridge.replace("$HEADER_PORTS$",nativeSourceGen.gen_bridge_ProvidedPort()+"\n"+nativeSourceGen.gen_bridge_RequiredPort());
            bridge =    bridge.replace("$PORTS$",nativeSourceGen.gen_bridge_Ports());
            bridge = bridge.replace("$CLASS$",componentName);
            bridge = bridge.replace("$artifactId$",component_c.getArtifactId());
            bridge = bridge.replace("$groupId$",component_c.getGroupId());
            bridge = bridge.replace("$version$",project.getVersion());
            MavenHelper.writeFile(file_java.getPath() + "/" + componentName + ".java", bridge, false);
        }  else
        {
            getLog().error("Generating component java");
        }


        /// GENERATE C FILES
        String path_c = component_c.getPomFile().getAbsolutePath().replace("pom.xml","")+"src/main/c/";

        File file_c = new File(path_c);
        if(file_c.mkdirs())
        {

            getLog().info("Generating "+componentName+".c");
            MavenHelper.writeFile(file_c.getPath() + "/" + componentName + ".c", nativeSourceGen.generateStepPreCompile().replace("$NAME$", componentName), false);

            getLog().info("Generating " + componentName + ".h");

            MavenHelper.writeFile(file_c.getPath() + "/" + componentName + ".h", nativeSourceGen.generateStepCompile(), false);

            // lib
            File file = new File(path_c+"/thirdparty");
            if(file.mkdir())
            {
                getLog().info("Generating Thirdparty files");

                FileManager.copyFileFromStream(GenerateFilesMojo.class.getClassLoader().getResourceAsStream("component.h"), file.getPath(), "component.h");
                FileManager.copyFileFromStream(GenerateFilesMojo.class.getClassLoader().getResourceAsStream("kqueue.h"), file.getPath(), "kqueue.h");
                // events
                FileManager.copyFileFromStream(GenerateFilesMojo.class.getClassLoader().getResourceAsStream("events_common.h"), file.getPath(), "events_common.h");
                FileManager.copyFileFromStream(GenerateFilesMojo.class.getClassLoader().getResourceAsStream("events_udp.h"), file.getPath(), "events_udp.h");
                FileManager.copyFileFromStream(GenerateFilesMojo.class.getClassLoader().getResourceAsStream("events_tcp.h"), file.getPath(), "events_tcp.h");
            }  else
            {
                getLog().error("Creating thirdparty directory");
            }

        }
        else
        {
            getLog().error("Creating sources directory");
        }


        // add modules
        project.getModel().addModule("./" + component_java.getArtifactId());
        project.getModel().addModule("./" + component_c.getArtifactId());
        project.getModel().setBuild(null);
        project.getModel().setPackaging("pom");
        //MavenHelper.writeModel(project.getModel());


        String path_pom_root =project.getModel().getPomFile().getPath();

        String pom_root = new String(FileManager.load(path_pom_root));

       if(!pom_root.contains("modules"))
       {
           String modules = "    <modules>\n" +
                   "        <module>"+component_c.getArtifactId()+"</module>\n" +
                   "        <module>"+component_java.getArtifactId()+"</module>\n" +
                   "    </modules>\n" +
                   "</project>";
           MavenHelper.writeFile(path_pom_root,pom_root.replace("</project>",modules),false);

       }





    }

}
