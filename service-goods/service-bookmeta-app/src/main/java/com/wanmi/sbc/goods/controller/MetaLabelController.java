package com.wanmi.sbc.goods.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.goods.bo.MetaLabelQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaLabel;
import com.wanmi.sbc.goods.provider.MetaLabelProvider;
import com.wanmi.sbc.goods.vo.IntegerIdVO;
import com.wanmi.sbc.goods.vo.MetaLabelAddReqVO;
import com.wanmi.sbc.goods.vo.MetaLabelEditReqVO;
import com.wanmi.sbc.goods.vo.MetaLabelQueryByIdResVO;
import com.wanmi.sbc.goods.vo.MetaLabelQueryByPageReqVO;
import com.wanmi.sbc.goods.vo.MetaLabelQueryByPageResVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签(MetaLabel)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-17 11:38:03
 */
@RestController
@RequestMapping("metaLabel")
public class MetaLabelController {
    private static final String PATH_SPLIT_SYMBOL = "_";
    /**
     * 标签-服务对象
     */
    @Resource
    private MetaLabelProvider metaLabelProvider;

    /**
     * 标签-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaLabelQueryByPageResVO>> queryByPage(@RequestBody MetaLabelQueryByPageReqVO pageRequest) {
        MetaLabelQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaLabelQueryByPageReqBO.class);
        BusinessResponse<List<MetaLabel>> list = this.metaLabelProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 标签-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaLabelQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaLabel> resBO = this.metaLabelProvider.queryById(id.getId());
        BusinessResponse<MetaLabelQueryByIdResVO> result = JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
        if (result.getContext() != null) {
            result.getContext().setPathList(StringSplitUtil.split(result.getContext().getPath(), PATH_SPLIT_SYMBOL));
        }
        return result;
    }

    /**
     * 标签-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaLabelAddReqVO addReqVO) {
        addReqVO.setPath(StringSplitUtil.join(addReqVO.getPathList(), PATH_SPLIT_SYMBOL));
        MetaLabel addReqBO = new MetaLabel();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaLabelProvider.insert(addReqBO).getContext());
    }

    /**
     * 标签-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaLabelEditReqVO editReqVO) {
        editReqVO.setPath(StringSplitUtil.join(editReqVO.getPathList(), PATH_SPLIT_SYMBOL));
        MetaLabel editReqVBO = new MetaLabel();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaLabelProvider.update(editReqVBO);
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
        return this.metaLabelProvider.deleteById(id.getId());
    }

}

