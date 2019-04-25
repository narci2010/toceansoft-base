/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysRoleController.java
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
import java.util.Map;

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
import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.common.utils.Constant;
import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Judge;
import com.toceansoft.common.validator.ValidatorUtils;
import com.toceansoft.common.vo.QueryVo;
import com.toceansoft.sys.entity.SysRoleEntity;
import com.toceansoft.sys.service.SysRoleMenuService;
import com.toceansoft.sys.service.SysRoleService;
import com.toceansoft.sys.service.SysUserService;

/**
 * 角色管理
 * 
 * @author Narci.Lee
 * 
 * 
 */
// @ConditionalOnProperty(prefix = "spring.devos.default", name = "service",
// havingValue = "true")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractUserController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	/**
	 * 角色列表
	 * 
	 * @param queryVo
	 *            QueryVo
	 * @param roleName
	 *            String
	 * @return R
	 * @throws ServiceException
	 *             se
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:role:list")
	public R list(QueryVo queryVo, String roleName) throws ServiceException {
		// 查询列表数据
		Query query = new Query(queryVo);
		// 只有超级管理员，才能查看所有角色列表
		List<String> currentRoles = sysUserService.queryAllRoles(getUserId());
		if (Judge.isEmtpy(currentRoles) || !currentRoles.contains(Constant.SUPER_ADMIN_NAME)) {
			query.put("createUserId", getUserId());
		}
		if (roleName != null) {
			query.put("roleName", roleName);
		}

		List<SysRoleEntity> list = sysRoleService.queryList(query);
		int total = sysRoleService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 角色列表
	 * 
	 * @return R
	 * @throws ServiceException
	 *             se
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:role:select")
	public R select() throws ServiceException {
		Map<String, Object> map = new HashMap<>();

		// 只有超级管理员，才能查看所有角色列表
		List<String> currentRoles = sysUserService.queryAllRoles(getUserId());
		if (Judge.isEmtpy(currentRoles) || !currentRoles.contains(Constant.SUPER_ADMIN_NAME)) {
			map.put("createUserId", getUserId());
		}
		List<SysRoleEntity> list = sysRoleService.queryList(map);

		return R.ok().put("list", list);
	}

	/**
	 * 角色信息
	 * 
	 * @param roleId
	 *            Long
	 * @return R
	 */
	@GetMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public R info(@PathVariable("roleId") Long roleId) {
		SysRoleEntity role = sysRoleService.queryObject(roleId);

		// 查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);

		return R.ok().put("role", role);
	}

	/**
	 * 保存角色
	 * 
	 * @param role
	 *            SysRoleEntity
	 * @return R
	 * @throws ServiceException
	 *             se
	 */
	@SysLog("保存角色")
	@PostMapping("/save")
	@RequiresPermissions("sys:role:save")
	public R save(@RequestBody SysRoleEntity role) throws ServiceException {
		ValidatorUtils.validateEntity(role);

		role.setCreateUserId(getUserId());
		sysRoleService.save(role);

		return R.ok();
	}

	/**
	 * 修改角色
	 * 
	 * @param role
	 *            SysRoleEntity
	 * @return R
	 * @throws ServiceException
	 *             se
	 */
	@SysLog("修改角色")
	@PutMapping("/update")
	@RequiresPermissions("sys:role:update")
	public R update(@RequestBody SysRoleEntity role) throws ServiceException {
		ValidatorUtils.validateEntity(role);

		role.setCreateUserId(getUserId());
		sysRoleService.update(role);

		return R.ok();
	}

	/**
	 * 删除角色
	 * 
	 * @param roleIds
	 *            Long[]
	 * @return R
	 */
	@SysLog("删除角色")
	@DeleteMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public R delete(@RequestBody Long[] roleIds) {
		sysRoleService.deleteBatch(roleIds);

		return R.ok();
	}

	@Override
	protected SysUserService getSysUserService() {

		return this.sysUserService;
	}

}
