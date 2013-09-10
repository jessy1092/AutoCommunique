package org.g0v.ac;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.g0v.ac.content.Content;
import org.g0v.ac.resolve.Resolve;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;



public class AutoCommunique 
{
	private String CLIENT_ID;
	private String SECRET;
	private String[] COMMUNIQUES_URI;
	private List<Content> content;
	private static final String API_KEYS_FILE = "api_key.txt";
	private static final String COMMUNIQUES_FILE = "communiques.txt";
	
	public AutoCommunique()
	{
		this.content = new ArrayList<Content>();
		setApiKey();
		setCommuniqueUri();
	}
	
	public void run()
	{
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		OAuthClientFilter filter = new OAuthClientFilter(
				client.getProviders(), 
				new OAuthParameters().consumerKey(CLIENT_ID), 
				new OAuthSecrets().consumerSecret(SECRET));		
		client.addFilter(filter);
		for(int i = 0; i < COMMUNIQUES_URI.length; i++)
		{
			WebResource service = client.resource(
					UriBuilder.fromUri(COMMUNIQUES_URI[i]).build());
			String communiqueText = service.accept(MediaType.APPLICATION_JSON).get(String.class);
//			System.out.println(communiqueText);
			Resolve resolveText = new Resolve(communiqueText);
			resolveText.run();
			content.addAll(resolveText.getContent());
		}
		sortContent();
		for(Content o: content)
		{
			System.out.println(o.toString());
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
	
	public void setApiKey()
	{
		try
		{
			FileReader fr = new FileReader(API_KEYS_FILE);
			BufferedReader br = new BufferedReader(fr);
			String line;
			if((line = br.readLine()) != null)
			{
				String[] para= line.split(" ");
				CLIENT_ID = para[0];
				SECRET = para[1];
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
