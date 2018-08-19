package com.lebupay.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Class is used for Image Creator.
 * @author Java Team
 *
 */
public class ImageCreator extends HttpServlet {

	private static final long serialVersionUID = -4379039633305535658L;

	/**
	 * This method is used to process request.
	 * @param request 
	 * @param response
	 */
	protected void processRequest(HttpServletRequest request,HttpServletResponse response) {

		try {
			
			int width = 150;
			int height = 50;
	
			SessionIdentifierGenerator sessionIdentifierGenerator1 = new SessionIdentifierGenerator();
			
			String dynamicString1 = sessionIdentifierGenerator1.nextSessionId();
			dynamicString1 = dynamicString1.substring(5, 11);
			char dynamicChar1[] = new char[dynamicString1.length()];
			for (int i = 0; i < dynamicString1.length(); i++) {
				dynamicChar1[i] = dynamicString1.charAt(i);
			}
	
			SessionIdentifierGenerator sessionIdentifierGenerator2 = new SessionIdentifierGenerator();
			
			String dynamicString2 = sessionIdentifierGenerator2.nextSessionId();
			dynamicString2 = dynamicString2.substring(5, 11);
			char dynamicChar2[] = new char[dynamicString2.length()];
			for (int i = 0; i < dynamicString2.length(); i++) {
				dynamicChar2[i] = dynamicString2.charAt(i);
			}
	
			SessionIdentifierGenerator sessionIdentifierGenerator3 = new SessionIdentifierGenerator();
			
			String dynamicString3 = sessionIdentifierGenerator3.nextSessionId();
			dynamicString3 = dynamicString3.substring(5, 11);
			char dynamicChar3[] = new char[dynamicString3.length()];
			for (int i = 0; i < dynamicString3.length(); i++) {
				dynamicChar3[i] = dynamicString3.charAt(i);
			}
	
			SessionIdentifierGenerator sessionIdentifierGenerator4 = new SessionIdentifierGenerator();
			
			String dynamicString4 = sessionIdentifierGenerator4.nextSessionId();
			dynamicString4 = dynamicString4.substring(5, 11);
			char dynamicChar4[] = new char[dynamicString4.length()];
			for (int i = 0; i < dynamicString4.length(); i++) {
				dynamicChar4[i] = dynamicString4.charAt(i);
			}
	
			SessionIdentifierGenerator sessionIdentifierGenerator5 = new SessionIdentifierGenerator();
			
			String dynamicString5 = sessionIdentifierGenerator5.nextSessionId();
			dynamicString5 = dynamicString5.substring(5, 11);
			char dynamicChar5[] = new char[dynamicString5.length()];
			for (int i = 0; i < dynamicString5.length(); i++) {
				dynamicChar5[i] = dynamicString5.charAt(i);
			}
	
			char data[][] = { dynamicChar1, dynamicChar2, dynamicChar3, dynamicChar4, dynamicChar5 };
	
			BufferedImage bufferedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	
			Graphics2D g2d = bufferedImage.createGraphics();
	
			Font font = new Font("Georgia", Font.ITALIC, 18);
			g2d.setFont(font);
	
			RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	
			rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
	
			g2d.setRenderingHints(rh);
	
			GradientPaint gp = new GradientPaint(0, 0, Color.LIGHT_GRAY, 0, height / 2,Color.black, true);
	
			g2d.setPaint(gp);
			g2d.fillRect(0, 0, width, height);
	
			g2d.setColor(new Color(255, 153, 0));
	
			Random r = new Random();
			int index = Math.abs(r.nextInt()) % 5;
	
			String captcha = String.copyValueOf(data[index]);
			request.getSession().setAttribute("captcha", captcha);
	
			int x = 0;
			int y = 0;
	
			for (int i = 0; i < data[index].length; i++) {
				x += 10 + (Math.abs(r.nextInt()) % 15);
				y = 20 + Math.abs(r.nextInt()) % 20;
				g2d.drawChars(data[index], i, 1, x, y);
			}
	
			g2d.dispose();
	
			response.setContentType("image/png");
			OutputStream os = response.getOutputStream();
			ImageIO.write(bufferedImage, "png", os);
			os.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
}
