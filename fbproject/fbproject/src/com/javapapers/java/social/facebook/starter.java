package com.javapapers.java.social.facebook;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/starter")
public class starter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String code="";
	public String accessToken="";
    public starter() {
        super();
        // TODO Auto-generated constructor stub
    }

		protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
			code = req.getParameter("code");
			if (code == null || code.equals("")) {
				throw new RuntimeException(
						"ERROR: Didn't get code parameter in callback.");
			}
			System.out.println("Start");
			FBConnection fbConnection = new FBConnection();
			String accessToken = fbConnection.getAccessToken(code);
			
			FBGraph fbGraph = new FBGraph(accessToken);
			String graph = fbGraph.getFBGraph();
			Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
			ServletOutputStream out = res.getOutputStream();
			out.println("<h1>Facebook Login using Java</h1>");
			out.println("<h2>Application Main Menu</h2>");
			out.println("<div>Welcome "+fbProfileData.get("first_name"));
			
		}

	}
	

