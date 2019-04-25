/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysMenuServiceImpl.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.Constant;
import com.toceansoft.sys.dao.SysMenuDao;
import com.toceansoft.sys.entity.SysMenuEntity;
import com.toceansoft.sys.service.SysMenuService;
import com.toceansoft.sys.service.SysUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Service("sysMenuService")
@Slf4j
public class SysMenuServiceImpl implements SysMenuService {
	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysUserService sysUserService;

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenuEntity> menuList = queryListParentId(parentId);
		if (menuIdList == null) {
			return menuList;
		}

		// menuIdList是当前用户有权限访问的菜单
		// 这里把无权限的过滤掉
		List<SysMenuEntity> userMenuList = new ArrayList<>();
		for (SysMenuEntity menu : menuList) {
			if (menuIdList.contains(menu.getMenuId())) {
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId) {
		return sysMenuDao.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return sysMenuDao.queryNotButtonList();
	}

	@Override
	public List<SysMenuEntity> getUserMenuList(Long userId) {
		// 系统管理员，拥有最高权限
		if (userId == Constant.SUPER_ADMIN_ID) {
			return getAllMenuList(null);
		}

		// 用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
//		for (Long menuId : menuIdList) {
//			log.info(menuId + ":menuId");
//		}
		return getAllMenuList(menuIdList);
	}

	@Override
	public SysMenuEntity queryObject(Long menuId) {
		SysMenuEntity entity = sysMenuDao.queryObject(menuId);
		if (entity == null) {
			throw new RRException("该记录不存在：" + menuId);
		}
		return entity;
	}

	@Override
	public List<SysMenuEntity> queryList(Map<String, Object> map) {
		return sysMenuDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysMenuDao.queryTotal(map);
	}

	@Override
	public void save(SysMenuEntity menu) {
		sysMenuDao.save(menu);
	}

	@Override
	public void update(SysMenuEntity menu) {
		sysMenuDao.update(menu);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] menuIds) {
		sysMenuDao.deleteBatch(menuIds);
	}

	@Override
	public List<SysMenuEntity> queryUserList(Long userId) {
		return sysMenuDao.queryUserList(userId);
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList) {
		// 查询根菜单列表
		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
//		for (SysMenuEntity entity : menuList) {
//			log.info("entity:" + entity.getName());
//		}
		// 递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
//		for (SysMenuEntity entity : menuList) {
//			log.info("entity2:" + entity.getName());
//		}
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList) {
		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();

		for (SysMenuEntity entity : menuList) {
			if (entity.getType() == Constant.MenuType.CATALOG.getValue()) { // 目录
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}

		return subMenuList;
	}
}
