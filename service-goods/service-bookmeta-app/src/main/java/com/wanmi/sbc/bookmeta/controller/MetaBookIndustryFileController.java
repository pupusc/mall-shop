package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookIndustryFileQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.MetaBookIndustryFileProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookIndustryFileAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookIndustryFileEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookIndustryFileQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookIndustryFileQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookIndustryFileQueryByPageResVO;
import com.wanmi.sbc.bookmeta.entity.MetaBookIndustryFile;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 行业数据文件(MetaBookIndustryFile)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@RestController
@RequestMapping("metaBookIndustryFile")
public class MetaBookIndustryFileController {
    /**
     * 行业数据文件-服务对象
     */
    @Resource
    private MetaBookIndustryFileProvider metaBookIndustryFileProvider;

    /**
     * 行业数据文件-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookIndustryFileQueryByPageResVO>> queryByPage(@RequestBody MetaBookIndustryFileQueryByPageReqVO pageRequest) {
        MetaBookIndustryFileQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookIndustryFileQueryByPageReqBO.class);
        BusinessResponse<List<MetaBookIndustryFile>> list = this.metaBookIndustryFileProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 行业数据文件-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookIndustryFileQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBookIndustryFile> resBO = this.metaBookIndustryFileProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 行业数据文件-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookIndustryFileAddReqVO addReqVO) {
        MetaBookIndustryFile addReqBO = new MetaBookIndustryFile();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookIndustryFileProvider.insert(addReqBO).getContext());
    }

    /**
     * 行业数据文件-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookIndustryFileEditReqVO editReqVO) {
        MetaBookIndustryFile editReqVBO = new MetaBookIndustryFile();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookIndustryFileProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 行业数据文件-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookIndustryFileProvider.deleteById(id.getId());
    }

}

