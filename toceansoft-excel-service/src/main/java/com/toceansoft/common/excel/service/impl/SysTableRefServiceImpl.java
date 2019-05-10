/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysTableRefServiceImpl.java
 * 描述：
 * 修改人： zhao
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.service.impl;

import com.toceansoft.common.excel.dao.SysTableRefDao;
import com.toceansoft.common.excel.dao.TableEntityDao;
import com.toceansoft.common.excel.entity.SysTableRef;
import com.toceansoft.common.excel.entity.TableEntity;
import com.toceansoft.common.excel.service.SysTableRefService;
import com.toceansoft.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  SysTableRefServiceImpl
 */
@Slf4j
@Service("sysTableRefService")
public class SysTableRefServiceImpl implements SysTableRefService {

    /**
     *  导出关系引用Dao
     */
    @Autowired
    private SysTableRefDao sysTableRefDao;

    @Autowired
    private TableEntityDao tableEntityDao;

    @Override
    public List<SysTableRef> getInsertLine(String key, String tableName) {
        return sysTableRefDao.getInsertLine(key, tableName);
    }

    @Override
    public int insertSysTableRef(SysTableRef ref) {
        return sysTableRefDao.insertSysTableRef(ref);
    }

    @Override
    public List<SysTableRef> getLineSysTableRef(String line, String key) {
        return sysTableRefDao.getLineSysTableRef(line, key);
    }

    @Override
    public int getCount(String tableName) {
        return sysTableRefDao.getCount(tableName);
    }

    @Override
    public void insertTableLine(String keyName, String tableName, Integer result) {
        this.getTableLine(keyName, tableName, result);
    }

    @Override
    public int findIsExistsLine(String keyName, String tableName, String line) {
        return sysTableRefDao.findIsExistsLine(keyName, tableName, line);
    }

    @Override
    public Map getLineByKeyAndName(String tableName, String id, long idVal) {
        return sysTableRefDao.getLineByKeyAndName(tableName, id, idVal);
    }

    @Override
    public int getTableLine(String keyName, String tableName, Integer result) {
        int index = 1, total = 0;
        try {
            // 获取导入表主键
            TableEntity primary = tableEntityDao.getTableInfoByPrimary(tableName);
            int count = sysTableRefDao.getCount(tableName);
            // 获取插入该表的总记录数
            List<Map> map = sysTableRefDao.getTableLine(tableName, primary.getName(), (count - result), result);

            SysTableRef table = new SysTableRef();
            table.setRefKey(keyName);
            table.setRefTableName(tableName);
            for (Map tmp: map) {

                for (int i = 0; i < index; i++) {
                    String line = "line" + index;
                    // 判断关系表中是否存在相同记录行
                    int num = sysTableRefDao.findIsExistsLine(keyName, tableName, line);
                    if (num > 0) {
                        index++;
                        continue;
                    }
                }

                Set<Map.Entry> entry =  tmp.entrySet();
                Iterator<Map.Entry> it = entry.iterator();
                // 判断是否是主键，是则拿到主键值插入导出表记录行一列
                while (it.hasNext()) {
                    Map.Entry entry1 = it.next();
                    if (primary.getName().equals(entry1.getKey())) {
                        table.setRefParam((long) entry1.getValue());
                        table.setRefLine("line" + index);
                        break;
                    }
                }
                index++;
                total += sysTableRefDao.insertSysTableRef(table);
            }
            if (total != map.size()) {
                throw new ServiceException("插入数据至‘导出引用关系表’失败");
            }
        } catch (ServiceException e) {
            log.debug("Error getTableLine: " + e.getLocalizedMessage());
        }
        return total;
    }
}
