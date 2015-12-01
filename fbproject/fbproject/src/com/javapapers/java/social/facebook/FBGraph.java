package com.javapapers.java.social.facebook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;

public class FBGraph {
	private String accessToken;
	HashMap<String,ArrayList<String>> videosdata=new HashMap<String, ArrayList<String>>();
	HashMap<String,ArrayList<String>> photosdata=new HashMap<String, ArrayList<String>>();
	HashMap<String,ArrayList<String>> statusdata=new HashMap<String, ArrayList<String>>();
	HashMap<String,ArrayList<String>> linksdata=new HashMap<String, ArrayList<String>>();

	public FBGraph(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getFBGraph() {
		String graph = null;
		try {
			
			String g = "https://graph.facebook.com/me?fields=id,name,posts{status_type,type,message,description,story,likes},birthday&key=value&" + accessToken;			
			System.out.print(accessToken);
			String[] rest = null;
			String t = null;
			
			rest = accessToken.split("=");
			t = rest[1];
			//System.out.println("rest: "+rest[1]);
			rest = t.split("&");
			//System.out.println("New Rest: "+rest[0]);
			
			FacebookClient facebookClient = new DefaultFacebookClient(rest[0],Version.VERSION_2_3);
			FetchObjectsResults fetchObjectsResults = facebookClient.fetchObjects(Arrays.asList("me", "cocacola"), FetchObjectsResults.class);
			System.out.println("User name: " + fetchObjectsResults.me.getName());
			
			Connection<Post> myFeed = facebookClient.fetchConnection("me/posts", Post.class,Parameter.with("limit", 1000),Parameter.with("fields", "type,message,likes.limit(1000).summary(true){name}"));
			
			ArrayList<String> FriendsLike = null;
			
			for (List<Post> myFeedConnectionPage : myFeed)
			  for (Post post : myFeedConnectionPage){
				  //System.out.println("Post ID: "+post.getId());
				  FriendsLike = new ArrayList<String>();
				if(post.getMessage()!=null){
					//System.out.println("	Message:"+post.getMessage().toLowerCase());
				}  
				
				//System.out.println("Type:"+post.getType()+" Likes:" + post.getLikesCount());
				  
				for(NamedFacebookType it:post.getLikes().getData()){
					FriendsLike.add(it.getName().toString());	
				}
				if(post.getType().equals("photo")){
					//System.out.println(FriendsLike);
					photosdata.put(post.getId(), FriendsLike);
				}else if(post.getType().equals("link")){
					linksdata.put(post.getId(), FriendsLike);
				}else if(post.getType().equals("video")){
					videosdata.put(post.getId(), FriendsLike);
				}else if(post.getType().equals("status")){
					statusdata.put(post.getId(), FriendsLike);
				}
			  }
			System.out.println("PhotoData");
			System.out.println(photosdata);
			
			System.out.println("Links");
			System.out.println(linksdata);
			
			System.out.println("Videos");
			System.out.println(videosdata);
			
			System.out.println("Status");
			System.out.println(statusdata);
			URL u = new URL(g);
			URLConnection c = u.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					c.getInputStream()));
			String inputLine;
			StringBuffer b = new StringBuffer();
			while ((inputLine = in.readLine()) != null)
				b.append(inputLine + "\n");
			in.close();
			graph = b.toString();
			System.out.println(graph);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("ERROR in getting FB graph data. " + e);
		}
		return graph;
	}
	
	public Map<String, String> getGraphData(String fbGraph) {
		Map<String, String> fbProfile = new HashMap<String, String>();
		try {
			JSONObject json = new JSONObject(fbGraph);
			fbProfile.put("id", json.getString("id"));
		//	fbProfile.put("first_name", json.getString("first_name"));
			if (json.has("email"))
				fbProfile.put("email", json.getString("email"));
			if (json.has("gender"))
				fbProfile.put("gender", json.getString("gender"));
			System.out.println(json.getString("name"));
			if(json.has("posts{likes}"))
			{
			System.out.println("POSTSSS");
			JSONArray jarray = json.getJSONArray("data");
			for(int i = 0; i < jarray.length(); i++){
			  JSONObject oneAlbum = jarray.getJSONObject(i);
			  System.out.println(jarray.getJSONObject(i));
			  //get your values
			
			  System.out.println(oneAlbum.get("likes"));
			}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("ERROR in parsing FB graph data. " + e);
		}
	
		return fbProfile;
	}
}
