/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysTableRefService.java
 * 描述：
 * 修改人： zhao
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.service;

import com.toceansoft.common.excel.entity.SysTableRef;

import java.util.List;
import java.util.Map;

/**
 *  @author: zhao
 */
public interface SysTableRefService {

    /**
     * 获取关键词key导入行数
     * @param key 关键词key
     * @param tableName 表名
     * @return SysTableRef
     */
    List<SysTableRef> getInsertLine(String key, String tableName);

    /**
     * 新增
     * @param ref 引用关系实体类
     * @return 记录行
     */
    int insertSysTableRef(SysTableRef ref);

    /**
     * 获取key要导出行数的表以及记录行
     * @param line 行数
     * @param key 关键词
     * @return SysTableRef
     */
    List<SysTableRef> getLineSysTableRef(String line, String key);

    /**
     *  查询表的总记录条数
     * @param tableName 表名
     * @return 记录行
     */
    int getCount(String tableName);

    /**
     * 获取table导出记录行
     * @param keyName 关键词key
     * @param tableName 表名
     * @param result 插入条数
     * @return entity
     */
    int getTableLine(String keyName, String tableName,
                     Integer result);

    /**
     * @param keyName 关键词key
     * @param tableName 表名
     * @param result 插入条数
     */
    void insertTableLine(String keyName, String tableName, Integer result);

    /**
     *  查询key关键词导出表中是否存在相同的行
     * @param keyName 关键词
     * @param tableName 表名
     * @param line 行数
     * @return int
     */
    int findIsExistsLine(String keyName, String tableName, String line);


    /**
     * 获取某个表的某条导出数据
     * @param tableName 表名
     * @param id 编号
     * @param idVal id值
     * @return map
     */
    Map getLineByKeyAndName(String tableName, String id, long idVal);
}
