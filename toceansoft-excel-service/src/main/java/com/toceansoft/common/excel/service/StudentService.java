/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：StudentService.java
 * 描述：
 * 修改人： ZhaoQ
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.toceansoft.common.excel.service;


import com.toceansoft.common.excel.entity.Student;

import java.util.List;

/**
 *  StudentService
 */
public interface StudentService {

    /**
     * 查询学生列表
     * @return List<Student>
     */
    List<Student> getStudentList();
}
