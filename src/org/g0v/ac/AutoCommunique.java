package org.g0v.ac;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.g0v.ac.content.Content;
import org.g0v.ac.resolve.Resolve;
import org.lee.hackpad.jackpad.JackpadClient;
import org.lee.hackpad.jackpad.content.Pad;

public class AutoCommunique 
{
	private String HACKPAD_CLIENT_ID;
	private String HACKPAD_SECRET;
//	private String TUMBLR_CONSUMER_KEY;
//	private String TUMBLR_CONSUMER_SECRET;
//	private String TUMBLR_TOKEN_KEY;
//	private String TUMBLR_TOKEN_SECRET;
	private String[] COMMUNIQUES_PADID;
	private List<Content> content;
//	private String tumblrContent;
	private Pad pad;
	
	private JackpadClient jackpadClient;
//	private JumblrClient jumblrClient;
	
	private static final String API_KEYS_FILE = "api_keys.txt";
	private static final String COMMUNIQUES_FILE = "communiques.txt";
	private static final String TITLE_FILE = "title.txt";
	private static final String OUTPUT_FILE = "pads.txt";
	
	public AutoCommunique()
	{
		this.content = new ArrayList<Content>();
		pad = new Pad();
		pad.setSite("g0v");
		pad.setContentType("text/html");
		setApiKey();
		setCommuniquePadID();
		jackpadBuild();
	}
	
	public void run()
	{
		getCommunique();
		sortContent();
		for(Content o: content)
		{
			System.out.println(o.toString());
		}
//		updateCommuniqueToTumblr();
		updateCommuniqueToHackpad();
	}
	
	public void jackpadBuild()
	{
		jackpadClient = new JackpadClient(HACKPAD_CLIENT_ID, HACKPAD_SECRET);
		jackpadClient.build();
	}
	
	/*public void tumblrBuild()
	{
		jumblrClient = new JumblrClient(TUMBLR_CONSUMER_KEY, TUMBLR_CONSUMER_SECRET);
		jumblrClient.setToken(TUMBLR_TOKEN_KEY, TUMBLR_TOKEN_SECRET);
	}*/
	
	public void getCommunique()
	{
		
		for(int i = 0; i < COMMUNIQUES_PADID.length; i++)
		{
			String communiqueText = jackpadClient.getPadContentHTML("g0v", COMMUNIQUES_PADID[i], "latest");
			//System.out.println(communiqueText);
			Resolve resolveText = new Resolve(communiqueText);
			resolveText.run();
			content.addAll(resolveText.getContent());
		}
		
	}
	
	public void updateCommuniqueToHackpad()
	{
		setTitle();
		setPadContent();
		
		String tmpPad = new String();
		tmpPad = jackpadClient.createPad(pad);
		System.out.println(tmpPad);
		outputPadID(tmpPad);
	}
	
	/*public void updateCommuniqueToTumblr()
	{
		setTitle();
		setTumblrContent();
		
		// Create a new client
		JumblrClient clientTrumblr = new JumblrClient(TUMBLR_CONSUMER_KEY, TUMBLR_CONSUMER_SECRET);
		clientTrumblr.setToken(TUMBLR_TOKEN_KEY, TUMBLR_TOKEN_SECRET);
		
		try
		{
			TextPost post = clientTrumblr.newPost("g0vtw.tumblr.com", TextPost.class);
			post.setFormat("html");
			post.setBody(tumblrContent);
			post.setTitle(title);
			post.setState("draft");
			post.save();
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
	}*/
	
	public void setPadContent()
	{
		String padContent = new String(); 
		String tmpTag = content.get(0).getTag();
		String tmpHeader = String.format("<p></p><p><b>%s</b></p><ul>", content.get(0).getTag());
		String tmpLine = "";
		tmpLine = String.format("<li>%s %s %s</li> \n", content.get(0).getDate(), content.get(0).getContent().substring(4), content.get(0).getComment());
		padContent += tmpHeader + tmpLine;
		for(int i = 1; i < content.size(); i++)
		{
			if(!tmpTag.equals(content.get(i).getTag()))
			{
				tmpHeader = String.format("</ul><p></p><p><b>%s</b></p><ul>", content.get(i).getTag());
				tmpTag = content.get(i).getTag();
				padContent += tmpHeader;
			}
			tmpLine = String.format("<li>%s %s %s</li> \n", content.get(i).getDate(), content.get(i).getContent().substring(4), content.get(i).getComment());
			padContent += tmpLine;
			
		}
		pad.setContent(padContent);
		System.out.println(padContent);
	}
	
	/*public void setTumblrContent()
	{
		String tmpTag = content.get(0).getTag();
		String tmpHeader = String.format("  <p><b>%s</b></p><ul>", content.get(0).getTag().substring(1));
		String tmpLine = "";
		tmpLine = String.format("<li>%s %s %s</li> \n", content.get(0).getDate(), content.get(0).getContent().substring(4), content.get(0).getComment());
		tumblrContent += tmpHeader + tmpLine;
		for(int i = 1; i < content.size(); i++)
		{
			if(!tmpTag.equals(content.get(i).getTag()))
			{
				tmpHeader = String.format("</ul><p></p><p><b>%s</b></p><ul>", content.get(i).getTag().substring(1));
				tmpTag = content.get(i).getTag();
				tumblrContent += tmpHeader;
			}
			tmpLine = String.format("<li>%s %s %s</li> \n", content.get(i).getDate(), content.get(i).getContent().substring(4), content.get(i).getComment());
			tumblrContent += tmpLine;
			
		}
	}*/
	
	public void sortContent()
	{
		Collections.sort(content, new Comparator<Content>() 
		{
			@Override
			public int compare(Content o1, Content o2) 
			{
				// TODO Auto-generated method stub
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		
		Collections.sort(content, new Comparator<Content>() 
		{
			@Override
			public int compare(Content o1, Content o2) 
			{
				// TODO Auto-generated method stub
				return o1.getTag().compareTo(o2.getTag());
			}
		});
	}
	
	public void setTitle()
	{
		try
		{
			FileReader fr = new FileReader(TITLE_FILE);
			BufferedReader br = new BufferedReader(fr);
			String line = new String();
			if((line = br.readLine()) != null)
			{
				pad.setTitle(line);
			}
			br.close();
			fr.close();
		}
		catch(IOException e)
		{
			System.out.println(e.toString());
		}
	}
	
	public void setApiKey()
	{
		try
		{
			FileReader fr = new FileReader(API_KEYS_FILE);
			BufferedReader br = new BufferedReader(fr);
			String line;
			for(int i = 0; i < 3; i++)
			{
				line = br.readLine();
				String[] para= line.split(" ");
				if(i == 0 && line != null)
				{
					HACKPAD_CLIENT_ID = para[0];
					HACKPAD_SECRET = para[1];
				}
//				else if(i ==1 && line !=null)
//				{
//					TUMBLR_CONSUMER_KEY = para[0];
//					TUMBLR_CONSUMER_SECRET = para[1];
//				}
//				else if(i ==2 && line !=null)
//				{
//					TUMBLR_TOKEN_KEY = para[0];
//					TUMBLR_TOKEN_SECRET = para[1];
//				}
			}
			br.close();
			fr.close();
		}
		catch(IOException e)
		{
			System.out.println(e.toString());
		}
	}
	
	public void setCommuniquePadID()
	{
		try
		{
			FileReader fr = new FileReader(COMMUNIQUES_FILE);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String tmpCom = "";
			while((line = br.readLine()) != null)
			{
				tmpCom += line + " ";
			}
			br.close();
			fr.close();
			COMMUNIQUES_PADID = tmpCom.split(" ");
			
		}
		catch(IOException e)
		{
			System.out.println(e.toString());
		}
	}
	
	public void outputPadID(String PadID)
	{
		try
		{
			FileWriter fw = new FileWriter(OUTPUT_FILE);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(PadID);
			bw.newLine();
			bw.close();
			fw.close();
		}
		catch (IOException e)
		{
			System.out.println(e.toString());
		}
		
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		AutoCommunique autoCommunique = new AutoCommunique();		
		autoCommunique.run();
	}

}
