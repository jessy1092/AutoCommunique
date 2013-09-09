package org.g0v.ac.resolve;

import java.util.ArrayList;
import java.util.List;

import org.g0v.ac.content.Content;

public class Resolve 
{
	private String text;
	private List<Content> content;
	private String[] communiqueLine;
	private String[] entries;
	
	public Resolve(String text)
	{
		content = new ArrayList<Content>();
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
				String date = communiqueLine[i].substring(6, communiqueLine[i].indexOf("</b></p>"));
				System.out.println(date);	
				int j = i;
				while(j < communiqueLine.length)
				{
					if(communiqueLine[j].equals(""))
					{
						System.out.println("next date");
						break;
					}
					String tmpLine = communiqueLine[j].substring(communiqueLine[j].indexOf("<li>") > 0 ?communiqueLine[j].indexOf("<li>"):0);
					Content tmpContent = new Content();
					tmpContent.setDate(date);
					
					int commentLine = -1;
					if(tmpLine.indexOf("<ul class=\"comment\"><li>") > 0)
					{
						String tmpComment = tmpLine.substring(tmpLine.indexOf("<ul class=\"comment\"><li>"));
						int k = j;
						String comment = tmpComment;
						while(k < communiqueLine.length)
						{
							if(comment.indexOf("</ul>") > 0)
							{
								break;
							}
							k++;
							tmpComment = communiqueLine[k];
							comment += tmpComment;
						}
						tmpContent.setComment(comment);
						commentLine = k;
					}
					else if(tmpLine.indexOf("<ul>") > 0)
					{
						String tmpComment = tmpLine.substring(tmpLine.indexOf("<ul>"));
						int k = j;
						String comment = tmpComment;
						while(k < communiqueLine.length)
						{
							if(comment.indexOf("</ul>") > 0)
							{
								break;
							}
							k++;
							tmpComment = communiqueLine[k];
							comment += tmpComment;
						}
						tmpContent.setComment(comment);
						commentLine = k;
					}
					
					
					
					if(commentLine > 0)
					{
						System.out.println(tmpContent.getComment());
						j = commentLine;
					}
					
					j++;
				}
				i = j;
			}
			i++;
		}
		
	}
	
	public String getLineTag()
	{
		
		
		return " ";
	}
	
	public String[] getCommuniqueLine()
	{
		return this.communiqueLine;
	}
	
	public List<Content> getContent()
	{
		return this.content;
	}
	
	public String[] getEntries()
	{
		return this.entries;
	}

}
