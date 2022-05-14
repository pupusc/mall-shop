package com.wanmi.sbc.goods.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.entity.MetaBookIndustryInfo;
import com.wanmi.sbc.goods.provider.MetaBookIndustryInfoProvider;
import com.wanmi.sbc.goods.vo.MetaBookIndustryInfoQueryByPageReqVO;
import com.wanmi.sbc.goods.vo.MetaBookIndustryInfoQueryByPageResVO;
import com.wanmi.sbc.goods.vo.MetaBookIndustryInfoQueryByIdResVO;
import com.wanmi.sbc.goods.vo.MetaBookIndustryInfoAddReqVO;
import com.wanmi.sbc.goods.vo.MetaBookIndustryInfoEditReqVO;
import com.wanmi.sbc.goods.vo.IntegerIdVO;
import com.wanmi.sbc.goods.bo.MetaBookIndustryInfoQueryByPageReqBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 行业数据(MetaBookIndustryInfo)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@RestController
@RequestMapping("metaBookIndustryInfo")
public class MetaBookIndustryInfoController {
    /**
     * 行业数据-服务对象
     */
    @Resource
    private MetaBookIndustryInfoProvider metaBookIndustryInfoProvider;

    /**
     * 行业数据-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookIndustryInfoQueryByPageResVO>> queryByPage(@RequestBody MetaBookIndustryInfoQueryByPageReqVO pageRequest) {
        MetaBookIndustryInfoQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookIndustryInfoQueryByPageReqBO.class);
        BusinessResponse<List<MetaBookIndustryInfo>> list = this.metaBookIndustryInfoProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 行业数据-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookIndustryInfoQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBookIndustryInfo> resBO = this.metaBookIndustryInfoProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 行业数据-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookIndustryInfoAddReqVO addReqVO) {
        MetaBookIndustryInfo addReqBO = new MetaBookIndustryInfo();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookIndustryInfoProvider.insert(addReqBO).getContext());
    }

    /**
     * 行业数据-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookIndustryInfoEditReqVO editReqVO) {
        MetaBookIndustryInfo editReqVBO = new MetaBookIndustryInfo();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookIndustryInfoProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 行业数据-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookIndustryInfoProvider.deleteById(id.getId());
    }

}

