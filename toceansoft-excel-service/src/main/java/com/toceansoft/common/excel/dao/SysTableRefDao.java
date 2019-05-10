/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysTableRefDao.java
 * 描述：
 * 修改人： zhao
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.dao;

import com.toceansoft.common.excel.entity.SysTableRef;
import com.toceansoft.framework.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *  导出引用关系dao
 */
@Mapper
public interface SysTableRefDao extends BaseDao<SysTableRef> {

    /**
     * 获取关键词key导入行数
     * @param key 关键词key
     * @param tableName 表名
     * @return SysTableRef
     */
    List<SysTableRef> getInsertLine(@Param("key") String key, @Param("tableName") String tableName);

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
    List<SysTableRef> getLineSysTableRef(@Param("line") String line, @Param("key") String key);

    /**
     *  查询表的总记录条数
     * @param tableName 表名
     * @return 记录行
     */
    int getCount(@Param("tableName") String tableName);

    /**
     * 获取table导出记录行
     * @param tableName 表名
     * @param order 排序字段
     * @param page 起始页
     * @param size 显示条数
     * @return entity
     */
    List<Map> getTableLine(@Param("tableName") String tableName, @Param("order") String order,
                     @Param("page") Integer page, @Param("size") Integer size);

    /**
     *  查询key关键词导出表中是否存在相同的行
     * @param keyName 关键词
     * @param tableName 表名
     * @param line 行数
     * @return int
     */
    int findIsExistsLine(@Param("keyName") String keyName, @Param("tableName") String tableName,
                         @Param("line") String line);

    /**
     * 获取某个表的某条导出数据
     * @param tableName 表名
     * @param id 编号
     * @param idVal id值
     * @return map
     */
    Map getLineByKeyAndName(@Param("tableName") String tableName,
                            @Param("id") String id, @Param("idVal") long idVal);
}
