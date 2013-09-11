package org.g0v.ac;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.g0v.ac.content.Content;
import org.g0v.ac.resolve.Resolve;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.TextPost;
import com.tumblr.jumblr.types.User;



public class AutoCommunique 
{
	private String HACKPAD_CLIENT_ID;
	private String HACKPAD_SECRET;
	private String TUMBLR_CONSUMER_KEY;
	private String TUMBLR_CONSUMER_SECRET;
	private String TUMBLR_TOKEN_KEY;
	private String TUMBLR_TOKEN_SECRET;
	private String[] COMMUNIQUES_URI;
	private List<Content> content;
	private String padContent;
	private String tumblrContent;
	private String title;
	private ClientConfig hackpadConfig;
	private Client client;
	private static final String API_KEYS_FILE = "api_key.txt";
	private static final String COMMUNIQUES_FILE = "communiques.txt";
	private static final String TITLE_FILE = "title.txt";
	
	public AutoCommunique()
	{
		this.content = new ArrayList<Content>();
		padContent = "";
		setApiKey();
		setCommuniqueUri();
	}
	
	public void run()
	{
		getCommunique();
		sortContent();
		for(Content o: content)
		{
			System.out.println(o.toString());
		}
		updateCommuniqueToTumblr();
		updateCommuniqueToHackpad();
	}
	
	public void getCommunique()
	{
		hackpadConfig = new DefaultClientConfig();
		client = Client.create(hackpadConfig);
		OAuthClientFilter filter = new OAuthClientFilter(
				client.getProviders(), 
				new OAuthParameters().consumerKey(HACKPAD_CLIENT_ID), 
				new OAuthSecrets().consumerSecret(HACKPAD_SECRET));		
		client.addFilter(filter);
		for(int i = 0; i < COMMUNIQUES_URI.length; i++)
		{
			WebResource serviceGET = client.resource(
					UriBuilder.fromUri(COMMUNIQUES_URI[i]).build());
			String communiqueText = serviceGET.accept(MediaType.APPLICATION_JSON).get(String.class);
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
		WebResource servicePOST = client.resource(UriBuilder.fromUri("https://g0v.hackpad.com/api/1.0/pad/create").build());			
		String json = servicePOST.accept(MediaType.APPLICATION_JSON).header("Content-Type", "text/plain").post(String.class, title);
		System.out.println(json);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new LinkedHashMap<String, String>();
		try
		{
			map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});	
			System.out.println(map.get("padId").toString());
			
		}
		catch (IOException e)
		{
			System.out.println(e.toString());
		}
		
		String updatepad = String.format("https://g0v.hackpad.com/api/1.0/pad/%s/content", map.get("padId").toString());
		WebResource service = client.resource(UriBuilder.fromUri(updatepad).build());
		System.out.println(service.accept(MediaType.APPLICATION_JSON).header("Content-Type", "text/html").post(String.class, padContent));
	}
	
	public void updateCommuniqueToTumblr()
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
		
	}
	
	public void setPadContent()
	{
		padContent += title; 
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
		//System.out.println(padContent);
	}
	
	public void setTumblrContent()
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
	}
	
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
			String line;
			if((line = br.readLine()) != null)
			{
				this.title = line;
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
				else if(i ==1 && line !=null)
				{
					TUMBLR_CONSUMER_KEY = para[0];
					TUMBLR_CONSUMER_SECRET = para[1];
				}
				else if(i ==2 && line !=null)
				{
					TUMBLR_TOKEN_KEY = para[0];
					TUMBLR_TOKEN_SECRET = para[1];
				}
			}
			br.close();
			fr.close();
		}
		catch(IOException e)
		{
			System.out.println(e.toString());
		}
	}
	
	public void setCommuniqueUri()
	{
		try
		{
			FileReader fr = new FileReader(COMMUNIQUES_FILE);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String tmpCom = "";
			String[] tmpuri;
			while((line = br.readLine()) != null)
			{
				tmpCom += line + " ";
			}
			br.close();
			fr.close();
			tmpuri = tmpCom.split(" ");
			COMMUNIQUES_URI = new String[tmpuri.length];
			for(int i = 0; i < tmpuri.length; i++)
			{
				COMMUNIQUES_URI[i] = String.format("https://g0v.hackpad.com/api/1.0/pad/%s/content/latest.html", tmpuri[i]);
			}
			
		}
		catch(IOException e)
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
