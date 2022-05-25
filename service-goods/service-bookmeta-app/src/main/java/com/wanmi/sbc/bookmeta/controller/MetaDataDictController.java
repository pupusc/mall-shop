package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaDataDictQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.MetaDataDictProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.entity.MetaDataDict;
import com.wanmi.sbc.bookmeta.vo.MetaDataDictQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaDataDictQueryByPageResVO;
import com.wanmi.sbc.bookmeta.vo.MetaDataDictQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaDataDictAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaDataDictEditReqVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据字典(MetaDataDict)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-24 00:37:02
 */
@RestController
@RequestMapping("metaDataDict")
public class MetaDataDictController {
    /**
     * 数据字典-服务对象
     */
    @Resource
    private MetaDataDictProvider metaDataDictProvider;

    /**
     * 数据字典-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaDataDictQueryByPageResVO>> queryByPage(@RequestBody MetaDataDictQueryByPageReqVO pageRequest) {
        MetaDataDictQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaDataDictQueryByPageReqBO.class);
        BusinessResponse<List<MetaDataDict>> list = this.metaDataDictProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 数据字典-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaDataDictQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaDataDict> resBO = this.metaDataDictProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 数据字典-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaDataDictAddReqVO addReqVO) {
        MetaDataDict addReqBO = new MetaDataDict();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaDataDictProvider.insert(addReqBO).getContext());
    }

    /**
     * 数据字典-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaDataDictEditReqVO editReqVO) {
        MetaDataDict editReqVBO = new MetaDataDict();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaDataDictProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 数据字典-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaDataDictProvider.deleteById(id.getId());
    }

}

