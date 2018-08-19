package com.lebupay.common;

import java.io.IOException;
import java.io.StringReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This Class is used for CityBank Payment Operations. 
 * @author Java Team
 */
public class CityBankUtil {

	public static String MerchantID = "";
	public static String OrderID = "";
	public static String SessionID = "";

	/**
	 * This method is used for CityBank API.
	 * 
	 * @param data
	 * @return String
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static String PostQW(String data) throws IOException, ParserConfigurationException, SAXException {

		String hostname = "127.0.0.1";
		// int port = 743;// For sandbox
		 int port = 843; // For Live Server
		String headers;
		String Response = "";

		try {
			String path = "/Exec";

			headers = "POST " + path + " HTTP/1.0\r\n";
			headers += "Host: " + hostname + "\r\n";
			headers += "Content-type: application/x-www-form-urlencoded\r\n";
			headers += "Content-Length: " + data.length() + "\r\n\r\n";
			headers += data;

			Socket socket = new Socket(hostname, port);

			OutputStream os = socket.getOutputStream();
			OutputStream buf = new BufferedOutputStream(os);
			OutputStreamWriter out = new OutputStreamWriter(buf);

			out.write(headers);
			out.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String ResponseLine;

			while ((ResponseLine = in.readLine()) != null)
				Response += ResponseLine;

			in.close();
			out.close();
			socket.close();
		}

		// Host not found
		catch (UnknownHostException e) {
			return "Don't know about host : " + hostname;
		}

		return Response;
	}

	/**
	 * This method is used for CityBank API.
	 * @param req
	 * @return Document
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public static Document RequestHandler(HttpServletRequest req) throws SAXException, ParserConfigurationException, IOException {
		
		try {
			String xml = req.getParameter("xmlmsg");

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlDoc = builder.parse(new InputSource(new StringReader(xml)));

			return xmlDoc;
		}

		// Host not found
		catch (UnknownHostException e) {
			return null;
		}
	}
}
