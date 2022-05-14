package com.wanmi.sbc.goods.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.entity.MetaLabelCate;
import com.wanmi.sbc.goods.provider.MetaLabelCateProvider;
import com.wanmi.sbc.goods.vo.MetaLabelCateQueryByPageReqVO;
import com.wanmi.sbc.goods.vo.MetaLabelCateQueryByPageResVO;
import com.wanmi.sbc.goods.vo.MetaLabelCateQueryByIdResVO;
import com.wanmi.sbc.goods.vo.MetaLabelCateAddReqVO;
import com.wanmi.sbc.goods.vo.MetaLabelCateEditReqVO;
import com.wanmi.sbc.goods.vo.IntegerIdVO;
import com.wanmi.sbc.goods.bo.MetaLabelCateQueryByPageReqBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签目录(MetaLabelCate)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@RestController
@RequestMapping("metaLabelCate")
public class MetaLabelCateController {
    /**
     * 标签目录-服务对象
     */
    @Resource
    private MetaLabelCateProvider metaLabelCateProvider;

    /**
     * 标签目录-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaLabelCateQueryByPageResVO>> queryByPage(@RequestBody MetaLabelCateQueryByPageReqVO pageRequest) {
        MetaLabelCateQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaLabelCateQueryByPageReqBO.class);
        BusinessResponse<List<MetaLabelCate>> list = this.metaLabelCateProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 标签目录-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaLabelCateQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaLabelCate> resBO = this.metaLabelCateProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 标签目录-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaLabelCateAddReqVO addReqVO) {
        MetaLabelCate addReqBO = new MetaLabelCate();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaLabelCateProvider.insert(addReqBO).getContext());
    }

    /**
     * 标签目录-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaLabelCateEditReqVO editReqVO) {
        MetaLabelCate editReqVBO = new MetaLabelCate();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaLabelCateProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 标签目录-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaLabelCateProvider.deleteById(id.getId());
    }

}

