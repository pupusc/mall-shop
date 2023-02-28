package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookLabelQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.MetaBookLabelProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookLabelAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookLabelEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookLabelQueryByIdResVO;
import com.wanmi.sbc.bookmeta.bo.MetaBookLabelBO;
import com.wanmi.sbc.bookmeta.vo.MetaBookLabelQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookLabelQueryByPageResVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签(MetaBookLabel)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@RestController
@RequestMapping("metaBookLabel")
public class MetaBookLabelController {
    /**
     * 标签-服务对象
     */
    @Resource
    private MetaBookLabelProvider metaBookLabelProvider;

    /**
     * 标签-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookLabelQueryByPageResVO>> queryByPage(@RequestBody MetaBookLabelQueryByPageReqVO pageRequest) {
        MetaBookLabelQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookLabelQueryByPageReqBO.class);
        BusinessResponse<List<MetaBookLabelBO>> list = this.metaBookLabelProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 标签-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookLabelQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBookLabelBO> resBO = this.metaBookLabelProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 标签-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookLabelAddReqVO addReqVO) {
        MetaBookLabelBO addReqBO = new MetaBookLabelBO();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookLabelProvider.insert(addReqBO).getContext());
    }

    /**
     * 标签-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookLabelEditReqVO editReqVO) {
        MetaBookLabelBO editReqVBO = new MetaBookLabelBO();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookLabelProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }


    /**
     * 标签-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookLabelProvider.deleteById(id.getId());
    }

}

