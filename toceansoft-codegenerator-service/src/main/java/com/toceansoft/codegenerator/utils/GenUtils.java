/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：GenUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.codegenerator.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.toceansoft.codegenerator.entity.ColumnEntity;
import com.toceansoft.codegenerator.entity.TableEntity;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.DateUtils;
import com.toceansoft.common.validator.Judge;

/**
 * 
 * @author Narci.Lee
 *
 */
public class GenUtils {

	/**
	 * 
	 * @return List<String>
	 */
	public static List<String> getTemplates() {
		List<String> templates = new ArrayList<String>();
		templates.add("template/Entity.java.vm");
		templates.add("template/Dao.java.vm");
		templates.add("template/Dao.xml.vm");
		templates.add("template/Service.java.vm");
		templates.add("template/ServiceImpl.java.vm");
		templates.add("template/Controller.java.vm");
		templates.add("template/list.html.vm");
		templates.add("template/list.js.vm");
		templates.add("template/menu.sql.vm");
		return templates;
	}

	/**
	 * 生成代码
	 * 
	 * @param table
	 *            String
	 * @param columns
	 *            List<Map<String, String>>
	 * @param zip
	 *            ZipOutputStream
	 * @param sysName
	 *            String
	 * @param moduleName
	 *            String
	 */
	public static void generatorCode(Map<String, String> table, List<Map<String, String>> columns,
			ZipOutputStream zip, String sysName, String moduleName) {
		// 配置信息
		Configuration config = getConfig();
		Boolean hasBigDecimal = false;
		Boolean hasDate = false;
		// 表信息
		TableEntity tableEntity = new TableEntity();
		tableEntity.setTableName(table.get("tableName"));
		tableEntity.setComments(table.get("tableComment"));
		// 表名转换成Java类名
		String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
		tableEntity.setClassName(className);
		tableEntity.setClassname(StringUtils.uncapitalize(className));

		// 列信息
		List<ColumnEntity> columsList = new ArrayList<>();
		for (Map<String, String> column : columns) {
			ColumnEntity columnEntity = new ColumnEntity();
			columnEntity.setColumnName(column.get("columnName"));
			columnEntity.setDataType(column.get("dataType"));
			columnEntity.setComments(column.get("columnComment"));
			columnEntity.setExtra(column.get("extra"));

			// 列名转换成Java属性名
			String attrName = columnToJava(columnEntity.getColumnName());
			columnEntity.setAttrName(attrName);
			columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

			// 列的数据类型，转换成Java类型
			String attrType = config.getString(columnEntity.getDataType(), "unknowType");
			columnEntity.setAttrType(attrType);
			if (!hasBigDecimal && attrType.equals("BigDecimal")) {
				hasBigDecimal = true;
			}
			if (!hasDate && attrType.equals("Date")) {
				hasDate = true;
			}
			// 是否主键
			if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
				tableEntity.setPk(columnEntity);
			}

			columsList.add(columnEntity);
		}
		tableEntity.setColumns(columsList);

		// 没主键，则第一个字段为主键
		if (tableEntity.getPk() == null) {
			tableEntity.setPk(tableEntity.getColumns().get(0));
		}

		// 设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(prop);
		// String mainPath = config.getString("mainPath");
		// mainPath = StringUtils.isBlank(mainPath) ? "com.toceansoft.base" : mainPath;
		// 封装模板数据
		Map<String, Object> map = new HashMap<>();
		String packageStr = config.getString("package") + "." + sysName;

		// 准备基本信息
		map.put("hasBigDecimal", hasBigDecimal);
		map.put("hasDate", hasDate);
		map.put("author", config.getString("author"));
		map.put("email", config.getString("email"));
		map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
		map.put("package", packageStr);
		map.put("moduleName", moduleName);

		prepareTableEntityConfig(tableEntity, map);
		VelocityContext context = new VelocityContext(map);

		genCodeByTemplates(zip, moduleName, tableEntity, packageStr, context);
	}

	private static void genCodeByTemplates(ZipOutputStream zip, String moduleName,
			TableEntity tableEntity, String packageStr, VelocityContext context) {
		// 获取模板列表
		List<String> templates = getTemplates();
		for (String template : templates) {
			// 渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, "UTF-8");
			tpl.merge(context, sw);

			try {
				// 添加到zip
				zip.putNextEntry(new ZipEntry(
						getFileName(template, tableEntity.getClassName(), packageStr, moduleName)));
				IOUtils.write(sw.toString(), zip, "UTF-8");
				try {
					if (!Judge.isNull(sw)) {
						sw.close();
					}
				} catch (Exception e) {
					throw new RRException("关闭资源失败。", e);
				}
				zip.closeEntry();
			} catch (IOException e) {
				throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
			}
		}
	}

	private static void prepareTableEntityConfig(TableEntity tableEntity, Map<String, Object> map) {
		map.put("tableName", tableEntity.getTableName());
		map.put("comments", tableEntity.getComments());
		map.put("pk", tableEntity.getPk());
		map.put("className", tableEntity.getClassName());
		map.put("classname", tableEntity.getClassname());
		map.put("pathName", tableEntity.getClassname().toLowerCase(Locale.CHINA));
		map.put("columns", tableEntity.getColumns());

	}

	/**
	 * 列名转换成Java属性名
	 * 
	 * @param columnName
	 *            String
	 * @return String
	 */
	public static String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[] { '_' }).replace("_", "");
	}

	/**
	 * 表名转换成Java类名
	 * 
	 * @param tableName
	 *            String
	 * @param tablePrefix
	 *            String
	 * @return String
	 */
	public static String tableToJava(String tableName, String tablePrefix) {
		if (StringUtils.isNotBlank(tablePrefix)) {
			tableName = tableName.replace(tablePrefix, "");
		}
		return columnToJava(tableName);
	}

	/**
	 * 获取配置信息
	 * 
	 * @return Configuration
	 */
	public static Configuration getConfig() {
		try {
			return new PropertiesConfiguration("generator.properties");
		} catch (ConfigurationException e) {
			throw new RRException("获取配置文件失败，", e);
		}
	}

	/**
	 * 获取文件名
	 * 
	 * @param template
	 *            String
	 * @param className
	 *            String
	 * @param packageName
	 *            String
	 * @param moduleName
	 *            String
	 * @return String
	 */
	public static String getFileName(String template, String className, String packageName,
			String moduleName) {
		String packagePath = "main" + File.separator + "java" + File.separator;
		String filename = null;
		if (StringUtils.isNotBlank(packageName)) {
			packagePath += packageName.replace(".", File.separator) + File.separator + moduleName
					+ File.separator;
		}

		if (template.contains("Entity.java.vm")) {
			filename = packagePath + "entity" + File.separator + className + "Entity.java";
		} else

		if (template.contains("Dao.java.vm")) {
			filename = packagePath + "dao" + File.separator + className + "Dao.java";
		} else

		if (template.contains("Service.java.vm")) {
			filename = packagePath + "service" + File.separator + className + "Service.java";
		} else

		if (template.contains("ServiceImpl.java.vm")) {
			filename = packagePath + "service" + File.separator + "impl" + File.separator
					+ className + "ServiceImpl.java";
		} else

		if (template.contains("Controller.java.vm")) {
			filename = packagePath + "controller" + File.separator + className + "Controller.java";
		} else

		if (template.contains("Dao.xml.vm")) {
			filename = "main" + File.separator + "resources" + File.separator + "mapper"
					+ File.separator + moduleName + File.separator + className + "Dao.xml";
		} else

		if (template.contains("list.html.vm")) {
			filename = "main" + File.separator + "resources" + File.separator + "views"
					+ File.separator + "modules" + File.separator + moduleName + File.separator
					+ className.toLowerCase(Locale.CHINA) + ".html";
		} else

		if (template.contains("list.js.vm")) {
			filename = "main" + File.separator + "resources" + File.separator + "static"
					+ File.separator + "js" + File.separator + "modules" + File.separator
					+ moduleName + File.separator + className.toLowerCase(Locale.CHINA) + ".js";
		} else

		if (template.contains("menu.sql.vm")) {
			filename = className.toLowerCase(Locale.CHINA) + "_menu.sql";
		}

		return filename;
	}
}
