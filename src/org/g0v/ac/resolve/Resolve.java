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
				String tmpLine = communiqueLine[j].substring(communiqueLine[j].indexOf("<li>"));
				
				while(j < communiqueLine.length)
				{
					if(communiqueLine[j].equals(""))
					{
						System.out.println("next date");
						break;
					}
					Content tmpContent = new Content();
					tmpContent.setDate(date);
					
					if(tmpLine.startsWith("<li>"))
					{
						int end = 0;
						end = getLineEnd(tmpLine);
//						System.out.println(tmpLine.lastIndexOf("#", end) + " " + end + " " + tmpLine);
						if(tmpLine.lastIndexOf("#", end) > 0)
						{
							String tmpTag = tmpLine.substring(tmpLine.lastIndexOf("#", end), end);
//							System.out.println(tmpTag);
							tmpContent.setTag(tmpTag);
						}

					}
					
					int commentLine = -1;
					if(tmpLine.indexOf("<ul class=\"comment\"><li>") > 0)
					{
						String tmpComment = tmpLine.substring(tmpLine.indexOf("<ul class=\"comment\"><li>"));
						int k = j;
						String comment = tmpComment;
						while(k < communiqueLine.length)
						{
							if((comment.indexOf("</ul>") > 0) && (communiqueLine[k+1].startsWith("</")))
							{
//								System.out.println(communiqueLine[k+1]);
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
							if((comment.indexOf("</ul>") > 0) && (communiqueLine[k+1].startsWith("</")))
							{
//								System.out.println(communiqueLine[k+1]);
								break;
							}
							k++;
							tmpComment = communiqueLine[k];
							comment += tmpComment;
						}
						tmpContent.setComment(comment);
						commentLine = k;
					}
					
					if(commentLine >= 0)
					{
						System.out.println(tmpContent.getComment());
						j = commentLine;
					}
					
					j++;
					tmpLine = communiqueLine[j];
				}
				i = j;
			}
			i++;
		}
		
	}
	
	public int getLineEnd(String line)
	{
		int endli = line.indexOf("</li>");
		int endul = line.indexOf("<ul>");
		if(endli < 0)
		{
			return endul;
		}
		else if(endul < 0)
		{
			return endli;
		}
		else
		{
			return endli < endul?endli:endul;
		}
	}
	
	public int getLineTag(String line)
	{
		
		return 1;
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
