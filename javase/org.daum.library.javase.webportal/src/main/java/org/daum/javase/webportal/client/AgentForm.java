package org.daum.javase.webportal.client;


import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.layout.HLayout;
import org.daum.javase.webportal.shared.Agent;


public class AgentForm extends HLayout{

    private final AuthentificationServiceAsync loginService = GWT.create(AuthentificationService.class);
    private Label labelAjoutSucces = new Label("Agent ajoute avec succes");
    private Label labelAjoutFail = new Label("Probleme lors de l'ajout de l'agent");
    private Agent agentEdited;


    public AgentForm(){


        final DynamicForm form = new DynamicForm();
        form.setTop(50);
        form.setLeft(50);
        form.setWidth(250);
        form.setHeight(350);


        labelAjoutSucces.setVisible(false);
        labelAjoutSucces.setBackgroundColor("green");
        labelAjoutSucces.setWidth("200px");
        labelAjoutSucces.setHeight("25px");


        labelAjoutFail.setVisible(false);
        labelAjoutFail.setBackgroundColor("red");
        labelAjoutFail.setWidth("200px");
        labelAjoutFail.setHeight("25px");

        HeaderItem header = new HeaderItem();
        header.setDefaultValue("Ajout d'un agent");


        final TextItem nomItem = new TextItem();
        nomItem.setTitle("Nom");
        nomItem.setRequired(true);

        final TextItem prenomItem = new TextItem();
        prenomItem.setTitle("Prenom");
        prenomItem.setRequired(true);

        final TextItem matriculeItem = new TextItem();
        matriculeItem.setTitle("Matricule");
        matriculeItem.setRequired(true);

        final PasswordItem passwordItem = new PasswordItem();
        passwordItem.setTitle("Password");
        passwordItem.setRequired(true);

        ButtonItem btnValider = new ButtonItem("Valider");
        btnValider.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if(form.validate()){
                    String nom = nomItem.getValueAsString();
                    String prenom = prenomItem.getValueAsString();
                    String matricule = matriculeItem.getValueAsString();
                    String password = passwordItem.getValueAsString();

                    loginService.createAgent(nom, prenom, matricule, password, new AsyncCallback<Agent>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            labelAjoutSucces.setVisible(false);
                            labelAjoutFail.setVisible(true);
                        }

                        @Override
                        public void onSuccess(Agent agent) {
                            labelAjoutFail.setVisible(false);
                            labelAjoutSucces.setVisible(true);
                        }
                    });
                }
            }
        });


        form.setFields(new FormItem[] {header, nomItem, prenomItem, matriculeItem, passwordItem, btnValider});
        this.addChild(form);
        this.addChild(labelAjoutFail);
        this.addChild(labelAjoutSucces);
        this.draw();
    }

    public AgentForm(Agent agent){
        agentEdited = agent;

        final DynamicForm form = new DynamicForm();
        form.setTop(50);
        form.setLeft(50);
        form.setWidth(250);
        form.setHeight(350);


        labelAjoutSucces.setVisible(false);
        labelAjoutSucces.setBackgroundColor("green");
        labelAjoutSucces.setWidth("200px");
        labelAjoutSucces.setHeight("25px");


        labelAjoutFail.setVisible(false);
        labelAjoutFail.setBackgroundColor("red");
        labelAjoutFail.setWidth("200px");
        labelAjoutFail.setHeight("25px");

        HeaderItem header = new HeaderItem();
        header.setDefaultValue("Edition d'un agent");


        final TextItem nomItem = new TextItem();
        nomItem.setTitle("Nom");
        nomItem.setRequired(true);
        nomItem.setValue(agent.getNom());

        final TextItem prenomItem = new TextItem();
        prenomItem.setTitle("Prenom");
        prenomItem.setRequired(true);
        prenomItem.setValue(agent.getPrenom());

        final TextItem matriculeItem = new TextItem();
        matriculeItem.setTitle("Matricule");
        matriculeItem.setRequired(true);
        matriculeItem.setValue(agent.getMatricule());

        final PasswordItem passwordItem = new PasswordItem();
        passwordItem.setTitle("Password");

        ButtonItem btnValider = new ButtonItem("Valider");
        btnValider.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                String password =  passwordItem.getValueAsString();
                if(password == "null" || password == null){
                    passwordItem.setValue(agentEdited.getPassword());
                }

                if(form.validate()){
                    Agent agentEditedTemp = new Agent();
                    agentEditedTemp.setId(agentEdited.getId());
                    agentEditedTemp.setNom(nomItem.getValueAsString());
                    agentEditedTemp.setPrenom(prenomItem.getValueAsString());
                    agentEditedTemp.setMatricule(matriculeItem.getValueAsString());
                    agentEditedTemp.setPassword(passwordItem.getValueAsString());

                    loginService.editAgent(agentEditedTemp, new AsyncCallback<Agent>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            labelAjoutSucces.setVisible(false);
                            labelAjoutFail.setVisible(true);
                        }

                        @Override
                        public void onSuccess(Agent agent) {
                            labelAjoutFail.setVisible(false);
                            labelAjoutSucces.setVisible(true);
                        }
                    });
                }
            }
        });


        form.setFields(new FormItem[] {header, nomItem, prenomItem, matriculeItem, passwordItem, btnValider});
        this.addChild(form);
        this.addChild(labelAjoutFail);
        this.addChild(labelAjoutSucces);
        this.draw();
    }

}
