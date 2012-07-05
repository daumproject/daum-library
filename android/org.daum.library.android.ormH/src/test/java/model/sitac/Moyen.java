package model.sitac;

import org.daum.library.ormH.annotations.Generated;
import org.daum.library.ormH.annotations.Id;
import org.daum.library.ormH.annotations.ManyToOne;
import org.daum.library.ormH.annotations.OneToMany;
import org.daum.library.ormH.persistence.GeneratedType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Moyen  implements Serializable {

    @Id()
    @Generated(strategy = GeneratedType.UUID)
    private String id = new String();

    @ManyToOne
	private MoyenType type;

	private String name;

    @OneToMany
	private List<Moyen> children = new ArrayList<Moyen>();
	
	public Moyen(MoyenType type)
	{
		this.type=type;
	}
	
	public Moyen(MoyenType type, String name)
	{
		this(type);
		this.name = name;
	}
	
	public Moyen(MoyenType type, String name, String id)
	{
		this(type, name);
		this.id = id;
	}

	
	public MoyenType getType()
	{
		return type;
	}
	
	public void setType(MoyenType newType)
	{
		type=newType;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String newName)
	{
		name=newName;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}


	public List<Moyen> getChildren()
	{
		return children;
	}
	
	public int getHeight()
	{
		if (children.size() == 0)
			return 0;
		else
		{
			int[] val = new int[children.size()];
			for (int i=0; i<children.size(); i++)
				val[i] = children.get(i).getHeight();
			return 1 + max(val);
		}
	}
	
	private int max(int[] val)
	{
		int max = val[0];
		for (int i=1; i<val.length; i++)
			if (val[i] > max)
				max = val[i];
		return max;
	}
}
