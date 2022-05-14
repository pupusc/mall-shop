package com.wanmi.sbc.goods.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.entity.MetaFigure;
import com.wanmi.sbc.goods.provider.MetaFigureProvider;
import com.wanmi.sbc.goods.vo.MetaFigureQueryByPageReqVO;
import com.wanmi.sbc.goods.vo.MetaFigureQueryByPageResVO;
import com.wanmi.sbc.goods.vo.MetaFigureQueryByIdResVO;
import com.wanmi.sbc.goods.vo.MetaFigureAddReqVO;
import com.wanmi.sbc.goods.vo.MetaFigureEditReqVO;
import com.wanmi.sbc.goods.vo.IntegerIdVO;
import com.wanmi.sbc.goods.bo.MetaFigureQueryByPageReqBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 人物(MetaFigure)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@RestController
@RequestMapping("metaFigure")
public class MetaFigureController {
    /**
     * 人物-服务对象
     */
    @Resource
    private MetaFigureProvider metaFigureProvider;

    /**
     * 人物-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaFigureQueryByPageResVO>> queryByPage(@RequestBody MetaFigureQueryByPageReqVO pageRequest) {
        MetaFigureQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaFigureQueryByPageReqBO.class);
        BusinessResponse<List<MetaFigure>> list = this.metaFigureProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 人物-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaFigureQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaFigure> resBO = this.metaFigureProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 人物-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaFigureAddReqVO addReqVO) {
        MetaFigure addReqBO = new MetaFigure();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaFigureProvider.insert(addReqBO).getContext());
    }

    /**
     * 人物-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaFigureEditReqVO editReqVO) {
        MetaFigure editReqVBO = new MetaFigure();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaFigureProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 人物-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaFigureProvider.deleteById(id.getId());
    }

}

