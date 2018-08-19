/**
 * @formatter:off
 *
 */
package com.lebupay.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lebupay.common.PrintlnSeterToFile;

/**
 * This class is used to validate invalid inputs coming form front end.
 * @author Java Team
 *
 */
@WebFilter("/*")
public class NoCacheFilter implements Filter {
	

	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
	}

	private FilterConfig filterConfig;

	public FilterConfig getFilterConfig() {
		return this.filterConfig;
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws NullPointerException {
		
		PrintlnSeterToFile.setPrintlnSeterToFile(request);

		try {
			if (response instanceof HttpServletResponse) {
				HttpServletResponse httpresponse = (HttpServletResponse) response;
				// Set the C ache-Control and Expires header

				// Print out the URL we're filtering
				String name = ((HttpServletRequest) request).getRequestURI();
				HttpSession session = ((HttpServletRequest) request)
						.getSession();

				if (session != null) {

					httpresponse.setHeader("Cache-Control",
							"no-cache, no-store, must-revalidate"); // HTTP 1.1
					httpresponse.setHeader("Pragma", "no-cache"); // HTTP 1.0
					httpresponse.setHeader("Cache-Control", "no-store");
					httpresponse.setDateHeader("Expires", 0);
					
					String projectName = ((HttpServletRequest) request).getContextPath();
					
					if(name.contains("admin")) {
						
						if (!name.contains("resources") && !name.contains("admin/login") && !name.contains("admin-get-token") && !name.contains("login")
								&& !name.contains("forgot-password") && !name.contains("mail-forgot-password") && !name.contains("logout")) {
							
							if (session.getAttribute("adminModel") == null) {
								
								if (name.equals(projectName) || name.equals(projectName+"/admin/login")) { // For First Time User

									((HttpServletResponse) response).sendRedirect(projectName+"/admin/login");
									return;

								} else if (name.equals(projectName+"/admin/login")) { // For Login

									chain.doFilter(request, response);
									return;

								} else {
									
									session.invalidate();
									((HttpServletResponse) response).sendRedirect(projectName+"/admin/login");
									return;
								} 
							} else {

								// For Logged In User
								if (name.equals(projectName+"/admin/login")) { // For wrong inputs
									session.invalidate();
									((HttpServletResponse) response).sendRedirect(projectName+"/admin/login");
									return;
							}
						 }
					  } else {
						  
					  }
					
					} else if(name.contains("ebl")) {
						
					} else if(name.contains("merchant")){
						
						if (!name.contains("resources") && !name.contains("registration") && !name.contains("get-token") && !name.contains("phone-validation") 
								&& !name.contains("forgot-password") && !name.contains("login") && !name.contains("logout")
								&& !name.contains("ImageCreator") && !name.contains("mail-forgot-password") && !name.contains("complete-validation")) {
							
							if (session.getAttribute("merchantModel") == null) {
								
								if (name.equals(projectName) || name.equals(projectName+"/index")) { // For First Time User

									((HttpServletResponse) response).sendRedirect(projectName+"/index");
									return;

								} else if (name.equals(projectName+"/index")) { // For Login
									chain.doFilter(request, response);
									return;

								} else {
									session.invalidate();
									((HttpServletResponse) response).sendRedirect(projectName+"/index");
									return;
								} 
							} else {
								// For Logged In User
								if (name.equals(projectName+"/index")) { // For wrong inputs
									session.invalidate();
									((HttpServletResponse) response).sendRedirect(projectName+"/index");
									return;
							}
						 }
					} else {
							  
						  }
					} 

				} else {
					
				}
			}
			chain.doFilter(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
