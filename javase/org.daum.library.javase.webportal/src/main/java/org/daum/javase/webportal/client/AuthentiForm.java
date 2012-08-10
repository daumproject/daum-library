package org.daum.javase.webportal.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;


public class AuthentiForm extends VLayout{

    private final AuthentificationServiceAsync loginService = GWT
            .create(AuthentificationService.class);

    private TextItem matriculeItem;
    private PasswordItem passwordItem;
    private Label errorLoginPassword = new Label("Bad login / password");


    public AuthentiForm(){
        this.setMargin(20);

        final DynamicForm authentiForm = new DynamicForm();
        authentiForm.setHeight("64px");
        authentiForm.setWidth(250);
        authentiForm.setLayoutAlign(VerticalAlignment.CENTER);

        errorLoginPassword.setVisible(false);
        errorLoginPassword.setBackgroundColor("red");
        errorLoginPassword.setWidth("150px");
        errorLoginPassword.setHeight("25px");

        matriculeItem = new TextItem("admin");
        matriculeItem.setTitle("Matricule");
        matriculeItem.setRequired(true);

        passwordItem = new PasswordItem("admin");
        passwordItem.setTitle("Password");
        passwordItem.setRequired(true);

        errorLoginPassword.setVisible(false);

        IButton btnValider = new IButton("Valider");
        btnValider.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if(!authentiForm.validate()){

                }else{
                    loginService.authenticateAgent(matriculeItem.getValueAsString(),
                            passwordItem.getValueAsString(), new AsyncCallback<Boolean>() {

                        @Override
                        public void onSuccess(Boolean result) {
                            if(result){
                                RootPanel.get().clear();
                                RootPanel.get().add(new MainGUI());
                            } else{
                                errorLoginPassword.setVisible(true);
                            }
                        }

                        @Override
                        public void onFailure(Throwable error) {
                            authentiForm.validate(false);
                        }
                    });
                }

            }
        });

        authentiForm.setFields(new FormItem[] {matriculeItem, passwordItem});
        addMember(authentiForm);
        addMember(errorLoginPassword);
        addMember(btnValider);
        this.draw();
    }

}
