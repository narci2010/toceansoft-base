/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：EvercookieEtagServlet.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.cookie.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a Java Servlet port of evercookie_etag.php, the server-side component
 * of Evercookie that uses the If-None-Match and Etag headers to keep track of
 * persistent values.
 * 
 * Install this servlet at /evercookie_etag.php in your web.xml (or add
 * a @WebServlet annotation) and you won't even need to modify evercookie.js!
 * This assumes that Evercookie's assets are in your web root.
 * 
 * Of course, if you have set $_ec_baseurl to something, you should install this
 * at [$_ec_baseurl]evercookie_etag.php. Remember, $ec_baseurl needs a trailing
 * slash in the evercookie.js.
 * 
 * @author Narci.Lee <narci.ltc@toceansoft.com>
 * 
 */
// @WebServlet(name = "EvercookieEtagServlet", urlPatterns = {
// "/api/cookie/php/evercookie_etag.php" }, loadOnStartup = 1)
public class EvercookieEtagServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;



	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		boolean cookieExists = false;
		String cookieValue = null;
		Cookie[] cookies = req.getCookies();

		if (null != cookies) {
			// Iterate over cookies until we find one named evercookie_etag
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("evercookie_etag")) {
					cookieExists = true;
					cookieValue = cookie.getValue();
					break;
				}
			}
		}

		ServletOutputStream body = resp.getOutputStream();

		try {

			if (cookieExists) {
				// Cookie set; send cookie value as Etag header/response body.
				resp.addHeader("Etag", cookieValue);
				body.print(cookieValue);
			} else {
				// No cookie; set the body to the request's If-None-Match value.
				body.print(req.getHeader("If-None-Match"));
			}

		} finally {
			// close the output stream.
			body.close();
		}

		resp.setStatus(200);

	}
}
