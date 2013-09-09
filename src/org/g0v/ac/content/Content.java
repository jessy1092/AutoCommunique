package org.g0v.ac.content;

public class Content 
{
	private String content;
	private String date;
	private String tag;
	private String comment;
	
	public Content()
	{
		this.content = "";
		this.date = "";
		this.tag = "";
		this.comment = "";
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
	
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	public String getDate()
	{
		return this.date;
	}
	
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	
	public String getTag()
	{
		return this.tag;
	}
	
	public String getComment()
	{
		return this.comment;
	}

}
