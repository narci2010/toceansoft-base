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

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author Narci.Lee
 *
 */
@Data
public class CouchDBParams implements Serializable {

	private static final long serialVersionUID = 1967812332796521315L;

	private String _id;
	private String _rev;
	// private String name;
	private boolean _deleted = true;

}
