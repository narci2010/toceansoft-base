/*  
 * Copyright 2010-2018 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Constants.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2018年11月1日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.couchdb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.toceansoft.common.HttpsUtil;
import com.toceansoft.common.exception.CouchDBException;
import com.toceansoft.common.json.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j

public final class CouchDBUtil {

	// String couchdb管理员账号
	private String admin;
	private String password;
	// couchdb访问url包含域名和端口号(含协议)
	private String dbUrl;

	private CouchDBUtil(String admin, String password, String dbUrl) {
		this.admin = admin;
		this.password = password;
		this.dbUrl = dbUrl;

	}

	/**
	 * 释放系统资源
	 * 
	 * @throws CouchDBException
	 *             cdbe
	 */
	public void release() throws CouchDBException {
		// release resources

	}

	/**
	 * 
	 * @param admin
	 *            String
	 * @param password
	 *            String
	 * @param dbUrl
	 *            String
	 * @return CouchDBUtil
	 */
	public static CouchDBUtil build(String admin, String password, String dbUrl) {
		return new CouchDBUtil(admin, password, dbUrl);
	}

	/**
	 * 设置couchdb用户数据库_users的权限:拥有者为root，且其他用户不能访问
	 * 
	 * @throws CouchDBException
	 *             cdbe
	 * 
	 * 
	 */
	public void onlyAdminCanCreateUser() throws CouchDBException {
		String userDBUrl = this.dbUrl + "/_users";
		if (!this.isDBExists(userDBUrl)) {
			// 创建这个数据库
			this.createCouchDBDatabase("_users");
			this.setDbOwner("_users", this.admin);
			return;
		}

		//@formatter:off
		String reqJsonStr = "{\"admins\":{\"names\":[\"root\"],\"roles\":[]},"
				+ "\"members\":{\"names\": [\"root\"],\"roles\": []}}";
	
		//@formatter:on
		String securityUrl = this.dbUrl + "/_users/_security";
		// log.debug("url:" + securityUrl);
		// log.debug(reqJsonStr);

		HttpPut httpput = new HttpPut(securityUrl);
		// 设置header
		httpput.setHeader("Content-type", "application/json");
		httpput.setHeader("Accept", "application/json");

		httpput.setHeader("Cookie", this.login());
		// 组织请求参数
		StringEntity stringEntity = null;
		try {
			stringEntity = new StringEntity(reqJsonStr);
		} catch (UnsupportedEncodingException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		// 发送Json格式的数据请求
		stringEntity.setContentType("application/json");
		httpput.setEntity(stringEntity);

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httpput);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}
		}
		log.debug("set security policy:onlyAdminCanCreateUser:" + content);

	}

	/**
	 * 把数据库的revs_limit值设置为n
	 * 
	 * @param couchDBUserName
	 *            String
	 * @param n
	 *            int
	 * @throws CouchDBException
	 *             cdbe
	 */
	public void setRevLimitsForDB(String couchDBUserName, int n) throws CouchDBException {
		String fullUrl = this.dbUrl + "/" + couchDBUserName + "/_revs_limit";

		HttpPut httpput = new HttpPut(fullUrl);
		// 设置header
		httpput.setHeader("Content-type", "application/json");
		httpput.setHeader("Accept", "application/json");

		httpput.setHeader("Cookie", this.login());
		// 组织请求参数
		StringEntity stringEntity = null;
		try {
			stringEntity = new StringEntity("" + n);
		} catch (UnsupportedEncodingException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		// 发送Json格式的数据请求
		stringEntity.setContentType("application/json");
		httpput.setEntity(stringEntity);

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httpput);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}
		}

		log.debug("setRevLimitsForDB:" + content);

	}

	/**
	 * 把数据库的revs_limit值设置为1
	 * 
	 * @param couchDBUserName
	 *            String
	 * @throws CouchDBException
	 *             cdbe
	 */
	public void setRevLimitsForDB(String couchDBUserName) throws CouchDBException {
		this.setRevLimitsForDB(couchDBUserName, 1);
	}

	/**
	 * 
	 * @param couchDBUserName
	 *            String
	 * @return String
	 * @throws CouchDBException
	 *             cdbe
	 */
	public String getRevLimitsOfDB(String couchDBUserName) throws CouchDBException {
		String fullUrl = this.dbUrl + "/" + couchDBUserName + "/_revs_limit";

		HttpGet httpGet = new HttpGet(fullUrl);
		// 设置header
		httpGet.setHeader("Content-type", "application/json");
		httpGet.setHeader("Accept", "application/json");

		httpGet.setHeader("Cookie", this.login());

		CloseableHttpResponse httpResponse = null;
		String content = null;
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httpGet);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}
		return content;

	}

	/**
	 * 
	 * @throws CouchDBException
	 *             cdbe
	 * @return String
	 */
	private String login() throws CouchDBException {
		String fullUrl = this.dbUrl + "/_session";
		String reqJsonStr = "{\"name\":\"" + this.admin + "\",\"password\":\"" + this.password
				+ "\"}";
		HttpPost post = new HttpPost(fullUrl);
		// 构造消息头
		post.setHeader("Content-type", "application/json");
		post.setHeader("Accept", "application/json");
		// 组织请求参数
		StringEntity stringEntity;
		try {
			stringEntity = new StringEntity(reqJsonStr);
		} catch (UnsupportedEncodingException e) {
			throw new CouchDBException(e.getMessage(), e);

		}
		// 发送Json格式的数据请求
		stringEntity.setContentType("application/json");
		post.setEntity(stringEntity);
		// log.debug(reqJsonStr);
		// log.debug(url);
		CloseableHttpResponse httpResponse = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(post);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					log.debug("close CloseableHttpClient fail.");
				}
			}

		}
		// HttpEntity entity = httpResponse.getEntity();
		// Set-Cookie →AuthSession=cm9vdDo1QkRBRTc4NTo5y7EH40VUp1Xujw5yrKWQWDii4A;
		// Version=1; Secure; Path=/; HttpOnly
		// String content = null;
		// try {
		// content = EntityUtils.toString(entity);
		// } catch (ParseException e) {
		// throw new CouchDBException(e.getMessage(), e);
		// } catch (IOException e) {
		// throw new CouchDBException(e.getMessage(), e);
		// }
		String setCookie = httpResponse.getFirstHeader("Set-Cookie").getValue();
		// log.debug("old:" + setCookie);
		// setCookie = setCookie.substring(0, setCookie.indexOf(";"));
		// log.debug("set security:" + content);
		//
		// log.debug("setCookie" + setCookie);
		// String cookieKey = "";
		// String cookieValue = "";
		// BasicClientCookie cookie = new BasicClientCookie(cookieKey, cookieValue);
		// cookie.setPath("/");
		// cookie.setVersion(1);
		// cookie.setSecure(true);
		log.debug("setCookie:" + setCookie);

		return setCookie;

	}

	/**
	 * 
	 * 
	 * @param databaseName
	 *            String
	 * @param owner
	 *            String
	 * 
	 * @throws CouchDBException
	 *             cdbe
	 */
	private void setDbOwner(String databaseName, String owner) throws CouchDBException {

		//@formatter:off
		String reqJsonStr = "{\"admins\":{\"names\":[\"" + owner + "\"],\"roles\":[]},"
				+ "\"members\":{\"names\": [\"" + owner + "\"],\"roles\": []}}";
	
		//@formatter:on
		String securityUrl = this.dbUrl + "/" + databaseName + "/_security";
		// log.debug("url:" + securityUrl);
		// log.debug(reqJsonStr);

		HttpPut httpput = new HttpPut(securityUrl);
		// 设置header
		httpput.setHeader("Content-type", "application/json");
		httpput.setHeader("Accept", "application/json");

		httpput.setHeader("Cookie", this.login());
		// 组织请求参数
		StringEntity stringEntity = null;
		try {
			stringEntity = new StringEntity(reqJsonStr);
		} catch (UnsupportedEncodingException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		// 发送Json格式的数据请求
		stringEntity.setContentType("application/json");
		httpput.setEntity(stringEntity);

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httpput);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}
		log.debug("setDbOwner:" + content);
	}

	/**
	 * 
	 * @param couchDBUserName
	 *            String
	 * @param couchDBUserPassword
	 *            String
	 * @throws CouchDBException
	 *             cdbe
	 */
	public void addCouchDBUser(String couchDBUserName, String couchDBUserPassword)
			throws CouchDBException {
		String fullUrl = this.dbUrl + "/_users/org.couchdb.user:" + couchDBUserName;
		log.debug(fullUrl);
		String reqJsonStr = "{\"name\":\"" + couchDBUserName + "\",\"password\":\""
				+ couchDBUserPassword + "\",\"roles\":[],\"type\":\"user\"}";
		HttpPut httpput = new HttpPut(fullUrl);
		// 设置header
		httpput.setHeader("Content-type", "application/json;charset=UTF-8");
		httpput.setHeader("Accept", "application/json");

		httpput.setHeader("Cookie", this.login());
		// 组织请求参数
		StringEntity stringEntity = new StringEntity(reqJsonStr, "UTF-8");
		// 发送Json格式的数据请求
		stringEntity.setContentType("application/json");
		// stringEntity.setContentEncoding("UTF-8");
		httpput.setEntity(stringEntity);

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httpput);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}
		// log.debug("reqJsonStr:" + reqJsonStr);
		log.debug("create user:" + content);
	}

	/**
	 * 批量插入用户
	 * 
	 * @param users
	 *            Map<String, String>
	 * @throws CouchDBException
	 *             cdbe
	 */
	public void addCouchDBUserBatch(Map<String, String> users) throws CouchDBException {
		String fullUrl = this.dbUrl + "/_users/_bulk_docs";
		if (users == null || users.isEmpty()) {
			return;
		}
		//@formatter:off

		StringBuffer reqJsonStr = new StringBuffer();
		reqJsonStr.append("\r\n{\r\n" + " \"docs\": [\r\n");
	
		Iterator<Map.Entry<String, String>> it = users.entrySet().iterator();
		while (it.hasNext()) {
             Map.Entry<String, String> entry = it.next();
             String key = entry.getKey();
             String value = entry.getValue();
            	 reqJsonStr.append("  {\r\n");
            	 reqJsonStr.append("    \"_id\": \"org.couchdb.user:" + key + "\",\r\n");
            	 reqJsonStr.append("    \"name\": \"" + key + "\",\r\n");
            	 reqJsonStr.append("    \"password\": \"" + value + "\",\r\n");
            	 reqJsonStr.append("    \"roles\": [],\r\n");
            	 reqJsonStr.append("    \"type\": \"user\"\r\n");
            	 reqJsonStr.append("  }");
            	 if (it.hasNext()) {
            		 reqJsonStr.append(", ");	
            		
            	 }
            	 reqJsonStr.append("\r\n");
            
		}
             
        reqJsonStr.append("  ]\r\n}");
        log.debug("reqJsonStr:" + reqJsonStr);
        
        HttpPost httppost = new HttpPost(fullUrl);
		// 设置header
		httppost.setHeader("Content-type", "application/json;charset=UTF-8");
		httppost.setHeader("Accept", "application/json");

		httppost.setHeader("Cookie", this.login());
		// 组织请求参数
		StringEntity stringEntity = new StringEntity(reqJsonStr.toString(), "UTF-8");
		// 发送Json格式的数据请求
		stringEntity.setContentType("application/json");
		// stringEntity.setContentEncoding("UTF-8");
		httppost.setEntity(stringEntity);

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}
		 log.debug("content:" + content);
	
		log.debug("batch create  users:" + users.size());
	}

	/**
	 * 
	 * @param couchDBUserName
	 *            String
	 * @throws CouchDBException
	 *             cdbe
	 */
	public void deleteCouchDBUser(String couchDBUserName) throws CouchDBException {
		String fullUrl = this.dbUrl + "/_users/org.couchdb.user:" + couchDBUserName;
		String rev = findCouchDBUser(fullUrl);
		fullUrl = fullUrl + "?rev=" + rev;
		log.debug(fullUrl);
		HttpDelete httpDelete = new HttpDelete(fullUrl);
		// 设置header
		httpDelete.setHeader("Content-type", "application/json;charset=UTF-8");
		httpDelete.setHeader("Accept", "application/json");

		httpDelete.setHeader("Cookie", this.login());

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httpDelete);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}

		log.debug("delete user:" + content);
	}

	/**
	 * 批量删除用户
	 * 
	 * @param params
	 *           List<CouchDBParams>   
	 * @throws CouchDBException
	 *             cdbe
	 */
	private void deleteUsers(List<CouchDBParams> params) throws CouchDBException {
		this.delete(params, "_users");
		
	}
	/**
	 * 批量删除
	 * 
	 * @param params
	 *           List<CouchDBParams>
	 * @param couchDBName String          
	 * @throws CouchDBException
	 *             cdbe
	 */
	private void delete(List<CouchDBParams> params, String couchDBName) throws CouchDBException {
		String fullUrl = this.dbUrl + "/" + couchDBName + "/_bulk_docs";
		if (params == null || params.isEmpty()) {
			return;
		}
		//@formatter:off

		StringBuffer reqJsonStr = new StringBuffer();
		reqJsonStr.append("\r\n{\r\n" + " \"docs\": [\r\n");
	
		Iterator<CouchDBParams> it = params.iterator();
		while (it.hasNext()) {
			CouchDBParams param = it.next();

            	 reqJsonStr.append("  {\r\n");
            	 reqJsonStr.append("    \"_id\": \"" + param.get_id() + "\",\r\n");
            	 reqJsonStr.append("    \"_rev\": \"" + param.get_rev() + "\",\r\n");
            	 reqJsonStr.append("    \"_deleted\": " + param.is_deleted() + "\r\n");
            	 reqJsonStr.append("  }");
            	 if (it.hasNext()) {
            		 reqJsonStr.append(", ");	
            		
            	 }
            	 reqJsonStr.append("\r\n");
            
		}
             
        reqJsonStr.append("  ]\r\n}");
        log.debug("reqJsonStr:" + reqJsonStr);
        
        HttpPost httppost = new HttpPost(fullUrl);
		// 设置header
		httppost.setHeader("Content-type", "application/json;charset=UTF-8");
		httppost.setHeader("Accept", "application/json");

		httppost.setHeader("Cookie", this.login());
		// 组织请求参数
		StringEntity stringEntity = new StringEntity(reqJsonStr.toString(), "UTF-8");
		// 发送Json格式的数据请求
		stringEntity.setContentType("application/json");
		// stringEntity.setContentEncoding("UTF-8");
		httppost.setEntity(stringEntity);

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}
		
		log.debug("batch delete:" + content);
	}
	private String findCouchDBUser(String fullUrl) throws CouchDBException {
		HttpGet httpGet = new HttpGet(fullUrl);

		// 设置header
		httpGet.setHeader("Content-type", "application/json;charset=UTF-8");
		httpGet.setHeader("Accept", "application/json");

		httpGet.setHeader("Cookie", this.login());

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httpGet);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		// log.debug("content:" + content);
		String rev = null;
		try {
			Map<String, String> map = JsonUtil.json2Bean(content, Map.class);
			rev = map.get("_rev");
			log.debug("rev:" + rev);
		} catch (JsonParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}
		return rev;
	}
	/**
	 * 批量删除用户
	 * 
	 * @param users
	 *            Set<String>
	 * @throws CouchDBException
	 *             cdbe
	 */
	public void deleteCouchDBUsersBatch(Set<String> users) throws CouchDBException {
		String fullUrl = this.dbUrl + "/_users/_find";
		if (users == null || users.isEmpty()) {
			throw new CouchDBException("疯了，你确认要删除空集合？", null);
		}
		//@formatter:off
		StringBuffer reqJsonStr = new StringBuffer();
		reqJsonStr.append("\r\n{\r\n" + " \"selector\": {\r\n");
		reqJsonStr.append("  \"name\": {\r\n");
		reqJsonStr.append("      \"$in\": [");
		Iterator<String> it = users.iterator();
		while (it.hasNext()) {
			String key  = it.next();          
            	 reqJsonStr.append("\"" + key + "\"");          
            	 if (it.hasNext()) {
            		 reqJsonStr.append(", ");	   		
            	 }
		}
		reqJsonStr.append("]\r\n");             
        reqJsonStr.append("   }\r\n  },\r\n");
        reqJsonStr.append(" \"fields\": [\"_id\", \"_rev\"]");
        reqJsonStr.append("\r\n}");
        log.debug("reqJsonStr:" + reqJsonStr);
        
        HttpPost httppost = new HttpPost(fullUrl);
		// 设置header
		httppost.setHeader("Content-type", "application/json;charset=UTF-8");
		httppost.setHeader("Accept", "application/json");
		httppost.setHeader("Cookie", this.login());
		// 组织请求参数
		StringEntity stringEntity = new StringEntity(reqJsonStr.toString(), "UTF-8");
		// 发送Json格式的数据请求
		stringEntity.setContentType("application/json");
		// stringEntity.setContentEncoding("UTF-8");
		httppost.setEntity(stringEntity);
		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}
		Map<String, List> map;
		try {
			map = JsonUtil.json2Bean(content, Map.class);
		} catch (JsonParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		 List<Map> docs = map.get("docs");
		 List<CouchDBParams> dbUsers = new ArrayList<CouchDBParams>();
		 for (Map doc:docs) {
			 try {
				 dbUsers.add(JsonUtil.bean2Bean(doc, CouchDBParams.class));
			} catch (JsonParseException e) {
				throw new CouchDBException(e.getMessage(), e);
			} catch (JsonMappingException e) {
				throw new CouchDBException(e.getMessage(), e);
			} catch (IOException e) {
				throw new CouchDBException(e.getMessage(), e);
			}
		 }
	     log.debug("content:" + content);
		 this.deleteUsers(dbUsers);
	     log.debug("batch delete  users:" + users.size());
	}

	private boolean isDBExists(String fullUrl) throws CouchDBException {
		HttpGet httpGet = new HttpGet(fullUrl);

		// 设置header
		httpGet.setHeader("Content-type", "application/json;charset=UTF-8");
		httpGet.setHeader("Accept", "application/json");

		httpGet.setHeader("Cookie", this.login());

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httpGet);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		// log.debug("content:" + content);
		boolean isExists = true;
		try {
			Map<String, String> map = JsonUtil.json2Bean(content, Map.class);
			String error = map.get("error");
			if (error != null && "not_found".equals(error)) {
				isExists = false;
			}

		} catch (JsonParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}
		return isExists;
	}

	/**
	 * 创建数据库，并指定数据库的所有者
	 * 
	 * @param databaseName
	 *            String
	 * @param owner
	 *            String
	 * @throws CouchDBException
	 *             cdbe
	 */
	public void addCouchDBDatabase(String databaseName, String owner) throws CouchDBException {
		this.createCouchDBDatabase(databaseName);
		this.setDbOwner(databaseName, owner);
	}

	/**
	 * 创建数据库，默认管理员才能访问
	 * 
	 * @param databaseName
	 *            String
	 * @throws CouchDBException
	 *             cdbe
	 */
	public void addCouchDBDatabase(String databaseName) throws CouchDBException {
		this.addCouchDBDatabase(databaseName, this.admin);

	}

	/**
	 * 创建数据库，默认管理员才能访问
	 * 
	 * @param databaseName
	 *            String
	 * @throws CouchDBException
	 *             cdbe
	 */
	private void createCouchDBDatabase(String databaseName) throws CouchDBException {
		String fullUrl = this.dbUrl + "/" + databaseName;
		HttpPut httpput = new HttpPut(fullUrl);
		// 设置header
		httpput.setHeader("Content-type", "application/json;charset=UTF-8");
		httpput.setHeader("Accept", "application/json");

		httpput.setHeader("Cookie", this.login());

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httpput);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}
		log.debug("create db:" + content);
	}

	/**
	 * 
	 * 
	 * @param databaseName
	 *            String
	 * @throws CouchDBException
	 *             cdbe
	 * 
	 */
	public void deleteCouchDBDatabase(String databaseName) throws CouchDBException {
		String fullUrl = this.dbUrl + "/" + databaseName;
		HttpDelete httpDelete = new HttpDelete(fullUrl);
		// 设置header
		httpDelete.setHeader("Content-type", "application/json;charset=UTF-8");
		httpDelete.setHeader("Accept", "application/json");

		httpDelete.setHeader("Cookie", this.login());

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httpDelete);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}

		log.debug("delete db:" + content);

	}
	
	/**
	 * 创建索引
	 * 
	 * @param fields
	 *           List<String>
	 * @param indexName String          
	 * @param couchDBName String          
	 * @throws CouchDBException
	 *             cdbe
	 */
	public void createIndex(List<String> fields, String indexName, String couchDBName) throws CouchDBException {
		String fullUrl = this.dbUrl + "/" + couchDBName + "/_index";
		if (fields == null || fields.isEmpty() || StringUtils.isEmpty(couchDBName)) {
			return;
		}
		//@formatter:off

		StringBuffer reqJsonStr = new StringBuffer();
		reqJsonStr.append("\r\n{\r\n" + " \"index\": {\r\n");
		reqJsonStr.append("   \"fields\": [");
		Iterator<String> it = fields.iterator();
		while (it.hasNext()) {
			String field = it.next();         
            	 reqJsonStr.append("\"" + field + "\"");
            	 if (it.hasNext()) {
            		 reqJsonStr.append(", ");	
            		            	 }           
		}
		reqJsonStr.append("] ");
		reqJsonStr.append("\r\n  },\r\n");
        reqJsonStr.append("\"name\" : \"" + indexName + "\",\r\n");
        reqJsonStr.append("\"type\" : \"json\"");
        reqJsonStr.append("\r\n  }");
        log.debug("reqJsonStr:" + reqJsonStr);
        
        HttpPost httppost = new HttpPost(fullUrl);
		// 设置header
		httppost.setHeader("Content-type", "application/json;charset=UTF-8");
		httppost.setHeader("Accept", "application/json");

		httppost.setHeader("Cookie", this.login());
		// 组织请求参数
		StringEntity stringEntity = new StringEntity(reqJsonStr.toString(), "UTF-8");
		// 发送Json格式的数据请求
		stringEntity.setContentType("application/json");
		// stringEntity.setContentEncoding("UTF-8");
		httppost.setEntity(stringEntity);

		CloseableHttpResponse httpResponse = null;
		String content = null;
		// 响应信息
		CloseableHttpClient httpclient = null;
		try {
			httpclient = HttpsUtil.createSSLClientDefault();
			httpResponse = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		}
		HttpEntity entity = httpResponse.getEntity();
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException e) {
			throw new CouchDBException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CouchDBException(e.getMessage(), e);
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug("close CloseableHttpClient fail.");
			}

		}
		
		log.debug("batch delete:" + content);
	}

}
