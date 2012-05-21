package model.sitac;

import java.util.ArrayList;
import java.util.List;

public class SitacModel
{
	private List<Intervention> interventions = new ArrayList<Intervention>();
	private List<InterventionType> interventionTypes = new ArrayList<InterventionType>();

	public SitacModel()
	{
		
	}
	
	public List<Intervention> getInterventions()
	{
		return interventions;
	}
	
	public List<InterventionType> getInterventionTypes()
	{
		return interventionTypes;
	}
	

}
