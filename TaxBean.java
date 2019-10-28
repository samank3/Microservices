package service;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaxBean
{
	private String name;
	private String code;
	private String type;
	private double pst;
	private double gst;
	public TaxBean()
	{
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public double getPst()
	{
		return pst;
	}
	public void setPst(double pst)
	{
		this.pst = pst;
	}
	public double getGst()
	{
		return gst;
	}
	public void setGst(double gst)
	{
		this.gst = gst;
	}

}
