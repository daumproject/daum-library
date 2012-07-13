package org.kevoree.library.javase.webserver.collaborationToolsBasics.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.kevoree.library.javase.webserver.collaborationToolsBasics.shared.AbstractItem;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 6/25/12
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class UploadFileForm extends PopupPanel{

    private static final String UPLOAD_ACTION_URL = GWT.getModuleBaseURL() + "upload";

    private final StructureServiceAsync structureService = GWT
            .create(StructureService.class);

    private final RepositoryToolsServicesAsync repositoryToolsServices = GWT
            .create(RepositoryToolsServices.class);

    private AbstractItem fileUpload;



    public UploadFileForm (AbstractItem fileToUpload){
        super(true);

        this.fileUpload = fileToUpload;

        // Create a FormPanel and point it at a service.
        final FormPanel form = new FormPanel();
        form.setSize("300px","400px");
        form.setAction(UPLOAD_ACTION_URL);

        // Because we're going to add a FileUpload widget, we'll need to set the
        // form to use the POST method, and multipart MIME encoding.
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        // Create a panel to hold all of the form widgets.
        VerticalPanel panel = new VerticalPanel();
        form.setWidget(panel);

        // Create a FileUpload widget.
        TextBox test = new TextBox();
        test.setText(fileToUpload.getName());
        test.setName("nomDossier");
        panel.add(test);

        // Create a FileUpload widget.
        FileUpload upload = new FileUpload();
        upload.setName("uploadFormElement");
        panel.add(upload);

        // Add a 'submit' button.
        panel.add(new Button("Submit", new ClickHandler() {
            public void onClick(ClickEvent event) {
                form.submit();
            }
        }));

        form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                repositoryToolsServices.commitRepository("add file", "", "", new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        structureService.getArborescence(fileUpload, new AsyncCallback<AbstractItem>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                //To change body of implemented methods use File | Settings | File Templates.
                            }

                            @Override
                            public void onSuccess(AbstractItem abstractItem) {

                            }
                        });
                    }
                });
            }
        });

        this.add(form);

    }

}
