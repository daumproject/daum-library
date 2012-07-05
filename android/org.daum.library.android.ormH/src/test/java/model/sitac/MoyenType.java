package model.sitac;

import org.daum.library.ormH.annotations.Id;

import java.io.Serializable;

public class MoyenType implements Serializable {

    @Id
	private int code;
	
	public MoyenType(int code)
	{
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}
	
	public void setCode(int newCode)
	{
		code = newCode;
	}
}
