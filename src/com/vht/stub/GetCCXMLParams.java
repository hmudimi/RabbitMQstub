package com.vht.stub;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

/**
 * Servlet implementation class GetCCXMLParams
 */
@WebServlet("/GetCCXMLParams")
public class GetCCXMLParams extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetCCXMLParams() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String responseURI = request.getParameter("responseuri");
		String sessionID = request.getParameter("sessionid");
		PrintWriter out = response.getWriter();
		out.write("<html><body><div id='serlvetResponse'>");
		out.write("<h2>Servlet HTTP Request Parameters Example</h2>");
		out.write("<p>param1 : " + responseURI + "</p>");
		out.write("<p>param2 : " + sessionID + "</p>");
		out.write("</div></body></html>");
		out.close();
		try {
			sendMessage(responseURI, sessionID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	protected void sendMessage(String uri, String sid) throws Exception {
		String QUEUE_NAME = "agent_answered.localhost:8080";
//		String QUEUE_NAME = "agentpriority";
		String EXCHANGE_NAME = "interaction_events";

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("test");
		factory.setPassword("test");
		factory.setHost("localhost");

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

			channel.exchangeDeclare(EXCHANGE_NAME, "topic");

			// Not required while using exchanges
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			// String message = "{\"data\":{\"uri\":\""+uri+"\",\"sid\":\""+sid+"\"}}";
//            String message = "uri="+uri+";sid="+sid;
			String message = getMessage();

			System.out.println("message " + EXCHANGE_NAME + " zzz " + message);

//            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
			channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, message.getBytes("UTF-8"));
//            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));

			System.out.println(" [x] Sent '" + message + "'");
		}
	}

	public String getMessage() {
		JSONObject jo = new JSONObject();

		jo.put("ID", "GUID");
		jo.put("CallId", "");
		jo.put("EventName", "AgentPriorityAgentAnswered");

		JSONArray ja = new JSONArray();
		
		JSONObject jo1 = new JSONObject();

		jo1.put("Key", "Outbound IVR Session ID");
		jo1.put("Value", "Value");
		
		ja.put(jo1);

		jo1 = new JSONObject();

		jo1.put("Key", "Outbound IVR Session ID");
		jo1.put("Value", "Value");

		ja.put(jo1);
		
		jo.put("DataValues", ja);
		
		System.out.println("getMessage : " + jo.toString());
		
		return jo.toString();
	}

}
