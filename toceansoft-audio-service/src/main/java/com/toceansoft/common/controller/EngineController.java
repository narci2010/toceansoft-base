/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：EngineController.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.controller;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.model.Engine;
import com.toceansoft.common.service.EngineService;
import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.vo.QueryVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

/**
 *  引擎类型EngineController
 */
@Slf4j
@RestController
@RequestMapping("/sys/engineController")
public class EngineController {

    @Autowired
    private EngineService engineService;

    /**
     *  根据id查询引擎详情
     *  @param engineId 引擎id
     * @return R
     */
    @GetMapping("/{engineId}")
    @ApiOperation(value = "根据id查询引擎详情", tags = "Engine")
    public R getEngineDetail(@PathVariable Integer engineId) {
        Engine eng = engineService.getEngineDetail(engineId);
        if (eng == null) {
            return R.ok("暂无数据");
        }
        log.info("根据id查询引擎详情...");
        return R.ok().put("list", eng);
    }


    /**
     *  获取引擎类型列表 -不分页
     * @return R
     */
    @GetMapping("/getOptionValue")
    @ApiOperation(value = "获取引擎类型列表（不分页）", tags = "Engine")
    public R getAllSysConfig() {
        List<Engine> list = engineService.findEngine();
        if (list == null) {
            return R.ok("暂无数据");
        }
        log.info("获取引擎类型列表（不分页）...");
        return R.ok().put("data", list);
    }




    /**
     *  获取引擎类型列表
     * @param vo 参数
     * @return R
     */
    @GetMapping
    @ApiOperation(value = "获取引擎类型列表", tags = "Engine")
    public R getAllSysConfig(QueryVo vo) {
        Query query = new Query(vo);
        List<Engine> list = engineService.queryList(query);
        if (list == null) {
            return R.ok("暂无数据");
        }
        int total = engineService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(),
                query.getPage());
        log.info("获取引擎类型列表...");
        return R.ok().put("page", pageUtil);
    }

    /**
     * 删除引擎类型
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    @ApiOperation(value = "删除引擎类型", tags = "Engine")
    public R deleteSysConfig(@RequestBody Long[] ids) {
        boolean result = engineService.delete(ids);
        if (!result) {
            throw new RRException("删除失败");
        }
        log.info("删除引擎类型...");
        return R.ok("删除成功");
    }

    /**
     * 保存引擎类型
     * @param engine 引擎类型
     * @return R
     */
    @PostMapping
    @ApiOperation(value = "保存引擎类型", tags = "SysConfig")
    public R deleteSysConfig(@RequestBody Engine engine) {
        boolean result = engineService.save(engine);
        if (!result) {
            throw new RRException("保存失败");
        }
        log.info("保存引擎类型...");
        return R.ok("保存成功");
    }

    /**
     * 修改引擎类型
     * @param engine 引擎类型
     * @return R
     */
    @PutMapping
    @ApiOperation(value = "修改引擎类型", tags = "SysConfig")
    public R updateSysConfig(@RequestBody Engine engine) {
        boolean result = engineService.update(engine);
        if (!result) {
            throw new RRException("修改失败");
        }
        log.info("修改引擎类型...");
        return R.ok("修改成功");
    }
}
