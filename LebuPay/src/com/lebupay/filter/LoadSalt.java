/**
 * @formatter:off
 *
 */
package com.lebupay.filter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;

import com.lebupay.common.Util;

/**
 * This class is used to validate invalid inputs coming form front end.
 * @author Java Team
 *
 */
public class LoadSalt implements Filter, com.lebupay.common.SaltTracker {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// Assume its HTTP
		HttpServletRequest httpReq = (HttpServletRequest) request;

		// Generate the salt and store it in the users cache
		String salt = RandomStringUtils.random(20, 0, 0, true, true, null,
				new SecureRandom());
		Util util = new Util();
		try {
			salt = util.encrypt(salt);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// csrfPreventionSaltCache.put(salt, Boolean.TRUE);

		String sessionId = httpReq.getSession().getId();
		Boolean check = SALT_TRACKER.containsKey(sessionId);
		// List<String> saltList = null;
		CopyOnWriteArrayList<String> saltList = null;
		if (check == true) {
			saltList = (CopyOnWriteArrayList<String>) SALT_TRACKER
					.get(sessionId);
			saltList.add(salt);

			// synchronized (SALT_TRACKER) {
			SALT_TRACKER.replace(sessionId, saltList);

			// }

		} else {
			saltList = new CopyOnWriteArrayList<String>();
			saltList.add(salt);
			// synchronized (SALT_TRACKER) {
			SALT_TRACKER.put(sessionId, saltList);
			// }
		}
		// Add the salt to the current request so it can be used
		// by the page rendered in this request

		httpReq.setAttribute("csrfPreventionSaltPage", salt);

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}