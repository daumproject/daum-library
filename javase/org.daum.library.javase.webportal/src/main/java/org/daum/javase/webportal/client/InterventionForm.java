package org.daum.javase.webportal.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import org.daum.javase.webportal.shared.InterventionDTO;
import org.daum.javase.webportal.shared.PersonneDTO;

import java.util.ArrayList;
import java.util.List;


public class InterventionForm extends VLayout{

	private final WebServiceAsync loginService = GWT.create(WebService.class);
    private TextItem itemCodeSinitre,itemNomRequerant,itemPrenomRequerant,itemVille,itemCp,itemAdresse;
    private List<String> listeIdAgent = new ArrayList<String>();
    private Label labelAjoutSucces = new Label("Intervention créée avec succes");
    private Label labelAjoutFail = new Label("Probleme lors de la creation de l'intervention");
    private Label labelNoAgent = new Label("Pas d'agent ajouté à l'intervention");
    private DynamicForm formInterventionBasics;
    private TextAreaItem itemDescription;


	public InterventionForm(){

        labelAjoutSucces.setVisible(false);
        labelAjoutSucces.setBackgroundColor("green");
        labelAjoutSucces.setWidth("200px");
        labelAjoutSucces.setHeight("25px");

        labelNoAgent.setVisible(false);
        labelNoAgent.setBackgroundColor("blue");
        labelNoAgent.setWidth("200px");
        labelNoAgent.setHeight("25px");


        labelAjoutFail.setVisible(false);
        labelAjoutFail.setBackgroundColor("red");
        labelAjoutFail.setWidth("200px");
        labelAjoutFail.setHeight("25px");


        formInterventionBasics = new DynamicForm();
		formInterventionBasics.setID("basicsInterventionForm");

		itemAdresse = new TextItem();
		itemAdresse.setName("adresse");  
		itemAdresse.setTitle("Adresse");
        itemAdresse.setRequired(true);

		itemCp = new TextItem();
		itemCp.setName("cp");  
		itemCp.setTitle("CP");
        itemCp.setRequired(true);
		
		itemVille = new TextItem();
		itemVille.setName("ville");  
		itemVille.setTitle("Ville");
        itemVille.setRequired(true);

        itemNomRequerant = new TextItem();
        itemNomRequerant.setName("nomRequerant");
        itemNomRequerant.setTitle("nom requerant");
        itemNomRequerant.setRequired(true);

        itemPrenomRequerant = new TextItem();
        itemPrenomRequerant.setName("prenomRequerant");
        itemPrenomRequerant.setTitle("Prenom requerant");
        itemPrenomRequerant.setRequired(true);
		
		itemCodeSinitre = new TextItem();
		itemCodeSinitre.setName("codeSinistre");  
		itemCodeSinitre.setTitle("Code sinistre");
        itemCodeSinitre.setRequired(true);

        itemDescription = new TextAreaItem();
        itemDescription.setName("description");
        itemDescription.setTitle("Description");
        itemDescription.setRequired(true);
		
		VStack agentVStack = new VStack();
        agentVStack.setHeight("50%");
		agentVStack.setLayoutMargin(10);  
        agentVStack.setShowEdges(true);  
        agentVStack.setCanAcceptDrop(true);  
        agentVStack.setAnimateMembers(true);  
        agentVStack.setDropLineThickness(4);
        agentVStack.addDropHandler(new DropHandler() {
            public void onDrop(DropEvent event) {
                Canvas target = EventHandler.getDragTarget();
                listeIdAgent.add(target.getID());
            }
        });



        formInterventionBasics.setFields(new FormItem[]{itemAdresse,itemCp, itemVille, itemNomRequerant,
                itemPrenomRequerant, itemCodeSinitre, itemDescription});

        formInterventionBasics.setTitleOrientation(TitleOrientation.LEFT);


        IButton submit = new IButton();
		submit.setTitle("Submit");  
		submit.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {

                if(formInterventionBasics.validate()){
                    if(listeIdAgent.size() == 0){
                        labelNoAgent.setVisible(true);
                    }else{
                        String codePostal = itemCp.getValueAsString();
                        String adresse = itemAdresse.getValueAsString();

                        String description = itemDescription.getValueAsString();

                        PersonneDTO requerant = new PersonneDTO();
                        requerant.setNom(itemNomRequerant.getValueAsString());

                        loginService.createIntervention(description, requerant, codePostal, adresse, listeIdAgent, new AsyncCallback<InterventionDTO>(){

                                @Override
                                public void onFailure(Throwable throwable) {
                                    labelAjoutSucces.setVisible(false);
                                    labelNoAgent.setVisible(false);
                                    labelAjoutFail.setVisible(true);
                                }

                                @Override
                                public void onSuccess(InterventionDTO intervention) {
                                    labelNoAgent.setVisible(false);
                                    labelAjoutSucces.setVisible(true);
                                    labelAjoutFail.setVisible(false);
                                }

                        });
                    }
                }

			}
		});
		
		this.setWidth100();
		this.setHeight100();
		this.setMembersMargin(10);
        this.addMember(labelAjoutFail);
        this.addMember(labelAjoutSucces);
        this.addMember(labelNoAgent);
		this.addMember(formInterventionBasics);
		this.addMember(agentVStack);
		this.addMember(submit);  
		this.draw();  
	}

    public void reset(){
        formInterventionBasics.clearValues();
    }


}
