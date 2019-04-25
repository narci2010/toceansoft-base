/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：HeaderRememberMeManager.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.AbstractRememberMeManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.util.WebUtils;

import com.google.common.base.Strings;
import com.toceansoft.common.utils.JWTUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class HeaderRememberMeManager extends AbstractRememberMeManager {

	// header 中 固定使用的 key
	public static final String DEFAULT_REMEMBER_ME_HEADER_NAME = "remember-me";

	@Override
	protected void rememberSerializedIdentity(Subject subject, byte[] serialized) {
		if (!WebUtils.isHttp(subject)) {
			if (log.isDebugEnabled()) {
				String msg = "Subject argument is not an HTTP-aware instance.  "
						+ "This is required to obtain a servlet request and "
						+ "response in order to set the rememberMe cookie. Returning immediately and ignoring rememberMe operation.";
				log.debug(msg);
			}

		} else {
			HttpServletResponse response = WebUtils.getHttpResponse(subject);

			String base64 = Base64.encodeToString(serialized);

			base64 = JWTUtil.createJWT(base64);

			// 设置 rememberMe 信息到 response header 中
			response.setHeader(DEFAULT_REMEMBER_ME_HEADER_NAME, base64);
		}
	}

	private boolean isIdentityRemoved(WebSubjectContext subjectContext) {
		ServletRequest request = subjectContext.resolveServletRequest();
		if (request == null) {
			return false;
		} else {
			Boolean removed = (Boolean) request
					.getAttribute(ShiroHttpServletRequest.IDENTITY_REMOVED_KEY);
			return removed != null && removed;
		}
	}

	@Override
	protected byte[] getRememberedSerializedIdentity(SubjectContext subjectContext) {
		if (!WebUtils.isHttp(subjectContext)) {
			if (log.isDebugEnabled()) {
				String msg = "SubjectContext argument is not an HTTP-aware instance.  "
						+ "This is required to obtain a servlet request and response in order to "
						+ "retrieve the rememberMe cookie. Returning immediately and ignoring rememberMe operation.";
				log.debug(msg);
			}

			return new byte[0];
		} else {
			WebSubjectContext wsc = (WebSubjectContext) subjectContext;
			if (this.isIdentityRemoved(wsc)) {
				return new byte[0];
			} else {
				HttpServletRequest request = WebUtils.getHttpRequest(wsc);
				// 在request header 中获取 rememberMe信息
				String base64 = request.getHeader(DEFAULT_REMEMBER_ME_HEADER_NAME);
				if ("deleteMe".equals(base64)) {
					return new byte[0];
				} else if (base64 != null) {
					base64 = JWTUtil.validateJWT(base64);
					if (Strings.isNullOrEmpty(base64)) {
						return new byte[0];
					}

					base64 = this.ensurePadding(base64);
					if (log.isTraceEnabled()) {
						log.trace("Acquired Base64 encoded identity [" + base64 + "]");
					}

					byte[] decoded = Base64.decode(base64);
					if (log.isTraceEnabled()) {
						log.trace("Base64 decoded byte array length: "
								+ (decoded != null ? decoded.length : 0) + " bytes.");
					}

					return decoded;
				} else {
					return new byte[0];
				}
			}
		}
	}

	private String ensurePadding(String base64) {
		int length = base64.length();
		if (length % 4 != 0) {
			StringBuilder sb = new StringBuilder(base64);

			for (int i = 0; i < length % 4; ++i) {
				sb.append('=');
			}

			base64 = sb.toString();
		}

		return base64;
	}

	@Override
	protected void forgetIdentity(Subject subject) {
		if (WebUtils.isHttp(subject)) {
			HttpServletRequest request = WebUtils.getHttpRequest(subject);
			HttpServletResponse response = WebUtils.getHttpResponse(subject);
			this.forgetIdentity(request, response);
		}

	}

	@Override
	public void forgetIdentity(SubjectContext subjectContext) {
		if (WebUtils.isHttp(subjectContext)) {
			HttpServletRequest request = WebUtils.getHttpRequest(subjectContext);
			HttpServletResponse response = WebUtils.getHttpResponse(subjectContext);
			this.forgetIdentity(request, response);
		}
	}

	private void forgetIdentity(HttpServletRequest request, HttpServletResponse response) {
		// 设置删除标示
		log.debug("request:" + request);
		response.setHeader(DEFAULT_REMEMBER_ME_HEADER_NAME, "deleteMe");
	}
}
