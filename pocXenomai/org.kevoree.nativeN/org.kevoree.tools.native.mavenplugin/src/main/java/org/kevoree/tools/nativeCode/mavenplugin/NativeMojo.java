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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.kevoree.*;
import org.kevoree.nativeN.gen.NativeSourceGen;
import org.kevoree.nativeN.utils.FileManager;
import org.kevoree.nativeN.utils.IKevScriptLoader;
import org.kevoree.nativeN.utils.KevScriptLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jedartois
 * @author <a href="mailto:jedartois@gmail.com">Jean-Emile DARTOIS</a>
 * @version $Id$
 * @goal generate
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class NativeMojo extends AbstractMojo {

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

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if(inputCFile == null || !(inputCFile.exists())){
            getLog().warn("InputDir null => ");
        } else {
            List<File> componentFiles = scanForKevScript(inputCFile);
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

    /* Check file header */
    private List<File> scanForKevScript(File modelDir) {
        List<File> models = new ArrayList();
        for (File f : modelDir.listFiles()) {
            if(f.isDirectory()){
                return scanForKevScript(f);
            } else
            {
                if (f.getName().endsWith(".kevs"))
                {
                    models.add(f);
                }
            }
        }
        return models;
    }

    /**
     * Generate interfaces
     * @param componentName
     * @param path_file
     * @param path_out
     * @throws Exception
     */
    public  void generateSources(String componentName, String path_file, String path_out) throws Exception {


        NativeSourceGen nativeSourceGen = new NativeSourceGen();
        IKevScriptLoader loader = new KevScriptLoader();

        ContainerRoot model =  loader.loadKevScript(path_file);

        for(TypeDefinition type :  model.getTypeDefinitionsForJ())
        {
            if(type instanceof ComponentType)
            {
                ComponentType c = (ComponentType)type;
                if(c.getName().equals(componentName))
                {
                    for(PortTypeRef portP :  c.getProvidedForJ() )
                    {
                        nativeSourceGen.create_input(portP.getName());
                    }

                    for(PortTypeRef portR :  c.getRequiredForJ())
                    {
                        nativeSourceGen.create_output(portR.getName());
                    }
                    break;
                }
            }
        }


        FileWriter fstream_c = new FileWriter(path_out+componentName+".c");
        BufferedWriter out_c = new BufferedWriter(fstream_c);

        out_c.write(nativeSourceGen.generateStepPreCompile().replace("$NAME$", componentName));
        out_c.close();


        FileWriter fstream_h = new FileWriter(path_out+componentName+".h");
        BufferedWriter out_h = new BufferedWriter(fstream_h);
        out_h.write(nativeSourceGen.generateStepCompile());
        //Close the output stream
        out_h.close();


        // lib
        File file = new File(path_out+"/thirdparty");
        if(file.mkdir()){

           FileManager.copyFileFromStream(NativeMojo.class.getClassLoader().getResourceAsStream("component.h"), file.getPath(), "component.h");

            FileManager.copyFileFromStream(NativeMojo.class.getClassLoader().getResourceAsStream("events.h"), file.getPath(), "events.h");

            FileManager.copyFileFromStream(NativeMojo.class.getClassLoader().getResourceAsStream("kqueue.h"), file.getPath(), "kqueue.h");



        }  else {

            // WTF !!!!
        }


    }

}
