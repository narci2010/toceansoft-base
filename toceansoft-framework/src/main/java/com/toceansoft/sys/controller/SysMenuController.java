/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysMenuController.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toceansoft.common.annotation.SysLog;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.common.utils.Constant.MenuType;
import com.toceansoft.common.utils.R;
import com.toceansoft.sys.entity.SysMenuEntity;
import com.toceansoft.sys.service.ShiroService;
import com.toceansoft.sys.service.SysMenuService;
import com.toceansoft.sys.service.SysUserService;

/**
 * 系统菜单
 * 
 * @author Narci.Lee
 * 
 * 
 */
// @ConditionalOnProperty(prefix = "spring.devos.default", name = "service",
// havingValue = "true")
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractUserController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private ShiroService shiroService;

	/**
	 * 导航菜单
	 * 
	 * @return R
	 * @throws ServiceException
	 *             se
	 */
	@GetMapping("/nav")
	public R nav() throws ServiceException {
		List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
		Set<String> permissions = shiroService.getUserPermissions(getUserId());
		return R.ok().put("menuList", menuList).put("permissions", permissions);
	}

	/**
	 * 所有菜单列表
	 * 
	 * @return R
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public R list() {
		List<SysMenuEntity> menuList = sysMenuService.queryList(new HashMap<String, Object>());

		// return menuList;
		return R.ok().put("menuList", menuList);
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 * 
	 * @return R
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public R select() {
		// 查询列表数据
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

		// 添加顶级菜单
		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);

		return R.ok().put("menuList", menuList);
	}

	/**
	 * 菜单信息
	 * 
	 * @param menuId
	 *            Long
	 * @return R
	 */
	@GetMapping("/info/{menuId}")
	@RequiresPermissions("sys:menu:info")
	public R info(@PathVariable("menuId") Long menuId) {
		SysMenuEntity menu = sysMenuService.queryObject(menuId);
		return R.ok().put("menu", menu);
	}

	/**
	 * 保存
	 * 
	 * @param menu
	 *            SysMenuEntity
	 * @return R
	 */
	@SysLog("保存菜单")
	@PostMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public R save(@RequestBody SysMenuEntity menu) {
		// 数据校验
		verifyForm(menu);

		sysMenuService.save(menu);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param menu
	 *            SysMenuEntity
	 * @return R
	 */
	@SysLog("修改菜单")
	@PutMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public R update(@RequestBody SysMenuEntity menu) {
		// 数据校验
		verifyForm(menu);

		sysMenuService.update(menu);

		return R.ok();
	}

	/**
	 * 删除
	 * 
	 * @param menuId
	 *            long
	 * @return R
	 */
	@SysLog("删除菜单")
	@DeleteMapping("/delete/{menuId}")
	@RequiresPermissions("sys:menu:delete")
	public R delete(@PathVariable("menuId") long menuId) {
		// 判断是否有子菜单或按钮
		List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId);
		if (!menuList.isEmpty()) {
			return R.error("请先删除子菜单或按钮");
		}

		sysMenuService.deleteBatch(new Long[] { menuId });

		return R.ok();
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenuEntity menu) {
		if (StringUtils.isBlank(menu.getName())) {
			throw new RRException("菜单名称不能为空");
		}

		if (menu.getParentId() == null) {
			throw new RRException("上级菜单不能为空");
		}

		// 菜单
		if (menu.getType() == MenuType.MENU.getValue()) {
			if (StringUtils.isBlank(menu.getUrl())) {
				throw new RRException("菜单URL不能为空");
			}
		}

		// 上级菜单类型
		int parentType = MenuType.CATALOG.getValue();
		if (menu.getParentId() != 0) {
			SysMenuEntity parentMenu = sysMenuService.queryObject(menu.getParentId());
			parentType = parentMenu.getType();
		}

		// 目录、菜单
		if (menu.getType() == MenuType.CATALOG.getValue()
				|| menu.getType() == MenuType.MENU.getValue()) {
			if (parentType != MenuType.CATALOG.getValue()) {
				throw new RRException("上级菜单只能为目录类型");
			}
			return;
		}

		// 按钮
		if (menu.getType() == MenuType.BUTTON.getValue()) {
			if (parentType != MenuType.MENU.getValue()) {
				throw new RRException("上级菜单只能为菜单类型");
			}
			// return;
		}
	}

	@Override
	protected SysUserService getSysUserService() {

		return this.sysUserService;
	}

}
