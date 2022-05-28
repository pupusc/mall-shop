package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookIndustryInfoQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.MetaBookIndustryInfoProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookIndustryInfoAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookIndustryInfoEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookIndustryInfoQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookIndustryInfoQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookIndustryInfoQueryByPageResVO;
import com.wanmi.sbc.bookmeta.bo.MetaBookIndustryInfoBO;
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
        BusinessResponse<List<MetaBookIndustryInfoBO>> list = this.metaBookIndustryInfoProvider.queryByPage(pageReqBO);
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
        BusinessResponse<MetaBookIndustryInfoBO> resBO = this.metaBookIndustryInfoProvider.queryById(id.getId());
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
        MetaBookIndustryInfoBO addReqBO = new MetaBookIndustryInfoBO();
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
        MetaBookIndustryInfoBO editReqVBO = new MetaBookIndustryInfoBO();
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

