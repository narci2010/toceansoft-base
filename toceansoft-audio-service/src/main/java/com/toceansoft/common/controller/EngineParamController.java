/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：EngineParamController.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.controller;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.model.EngineParam;
import com.toceansoft.common.service.EngineParamService;
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
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Map;

/**
 * 引擎参数EngineParamController
 */

@Slf4j
@RestController
@RequestMapping("/sys/engineParamController")
public class EngineParamController {

    @Autowired
    private EngineParamService engineParamService;


    /**
     *  修改引擎参数详情
     *  @param object 修改引擎参数对象
     *  @param key 参数key 的 id
     * @return R
     */
    @PutMapping("/updateEngineParam")
    @ApiOperation(value = "修改引擎参数详情", tags = "EngineParam")
    public R updateEngineParam(@RequestBody Object object, Integer key) {
            Map<String, Object> map = (Map) object;
            int count = 0;
            for (Map.Entry<String, Object> tmp: map.entrySet()) {
                boolean result = engineParamService.updateParamKey(key, tmp.getKey(), tmp.getValue().toString());
                if (result) {
                    count++;
                }
            }
            if (map.size() != count) {
                return R.ok("修改失败");
            }
        return R.ok("修改成功");
    }

    /**
     *  获取引擎参数详情
     *  @param engineId 引擎id
     *  @param paramKey 参数key
     * @return R
     */
    @GetMapping("/getOptionValue/{engineId}/{paramKey}")
    @ApiOperation(value = "获取引擎参数详情", tags = "EngineParam")
    public R getAllSysConfig(@PathVariable Integer engineId, @PathVariable String paramKey) {
        EngineParam param = engineParamService.queryObject(engineId, paramKey);
        if (param == null) {
            return R.ok("暂无数据");
        }
        log.info("获取引擎参数详情...");
        return R.ok().put("list", param);
    }




    /**
     *  获取引擎参数列表
     *  @param vo 参数
     *  @param engineId 引擎id
     * @return R
     */
    @GetMapping
    @ApiOperation(value = "获取引擎参数列表", tags = "EngineParam")
    public R getAllSysConfig(QueryVo vo, @RequestParam Integer engineId) {
        Query query = new Query(vo);
        List<EngineParam> list = engineParamService.queryList(query, engineId);
        if (list == null) {
            return R.ok("暂无数据");
        }
        int total = engineParamService.queryTotal(engineId);
        PageUtils pageUtils = new PageUtils(list, total, query.getLimit(), query.getPage());
        log.info("获取引擎参数列表...");
        return R.ok().put("page", pageUtils);
    }

    /**
     * 删除引擎参数
     * @param id id列表
     * @param key 键
     * @return R
     */
    @DeleteMapping
    @ApiOperation(value = "删除引擎参数", tags = "EngineParam")
    public R deleteSysConfig(Integer id, @RequestBody String[] key) {

        boolean result = engineParamService.delete(id, key);
        if (!result) {
            throw new RRException("删除失败");
        }
        log.info("删除引擎参数...");
        return R.ok("删除成功");
    }

    /**
     * 保存引擎参数
     * @param engine 引擎参数
     * @return R
     */
    @PostMapping
    @ApiOperation(value = "保存引擎参数", tags = "EngineParam")
    public R deleteSysConfig(@RequestBody EngineParam engine) {
        if (engine == null) {
            throw new RRException("新增引擎参数不能为空");
        }
        int count = engineParamService.queryParamExits(engine.getParamKey(), engine.getEngineId());
        if (count > 0) {
            throw new RRException("已存在该key值，key值不能重复");
        }
        boolean result = engineParamService.save(engine);
        if (!result) {
            throw new RRException("保存失败");
        }
        log.info("保存引擎参数...");
        return R.ok("保存成功");
    }

    /**
     * 修改引擎参数
     * @param engine 引擎参数
     * @param key 键
     * @return R
     */
    @PutMapping
    @ApiOperation(value = "修改引擎参数", tags = "EngineParam")
    public R updateSysConfig(@RequestBody EngineParam engine, @RequestParam String key) {
        boolean result = engineParamService.update(engine, key);
        if (!result) {
            throw new RRException("修改失败");
        }
        log.info("修改引擎参数...");
        return R.ok("修改成功");
    }
}
