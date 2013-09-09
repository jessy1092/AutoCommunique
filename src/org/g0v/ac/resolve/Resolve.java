package org.g0v.ac.resolve;

import org.g0v.ac.content.Content;

public class Resolve 
{
	private String text;
	private Content[] content;
	private String[] communiqueLine;
	private String[] entries;
	
	public Resolve(String text)
	{
		this.text = text;
	}
	
	public void run()
	{
		communiqueLine = text.split("\n");
		int i = 0;
		while(i < communiqueLine.length)
		{
//			System.out.println(communiqueLine[i]);
			if(communiqueLine[i].startsWith("<p><b>"))
			{
				String date = communiqueLine[i].substring(6, communiqueLine[i].indexOf("</b>"));
				System.out.println(date);	
				
			}
			i++;
		}
		
	}
	
	public String[] getCommuniqueLine()
	{
		return this.communiqueLine;
	}
	
	public Content[] getContent()
	{
		return this.content;
	}
	
	public String[] getEntries()
	{
		return this.entries;
	}

}
