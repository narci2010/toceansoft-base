/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Student.java
 * 描述：
 * 修改人： ZhaoQ
 * 修改时间：2019-02-20 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.entity;

import lombok.Getter;
import lombok.Setter;

/**
 *  学生表
 */

@Getter
@Setter
public class Student {

    /** 编号 */
    private int id;

    /** 姓名 */
    private String name;

    /** 年龄 */
    private int age;

    /** 性别 */
    private int sex;

    /** 地址 */
    private String address;

    /** 电话 */
    private String mobile;
}
