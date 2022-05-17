package com.wanmi.sbc.goods.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.goods.bo.MetaProducerQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaProducer;
import com.wanmi.sbc.goods.provider.MetaProducerProvider;
import com.wanmi.sbc.goods.vo.IntegerIdVO;
import com.wanmi.sbc.goods.vo.MetaProducerAddReqVO;
import com.wanmi.sbc.goods.vo.MetaProducerEditReqVO;
import com.wanmi.sbc.goods.vo.MetaProducerQueryByIdResVO;
import com.wanmi.sbc.goods.vo.MetaProducerQueryByPageReqVO;
import com.wanmi.sbc.goods.vo.MetaProducerQueryByPageResVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 出品方(MetaProducer)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@RestController
@RequestMapping("metaProducer")
public class MetaProducerController {
    /**
     * 出品方-服务对象
     */
    @Resource
    private MetaProducerProvider metaProducerProvider;

    /**
     * 出品方-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaProducerQueryByPageResVO>> queryByPage(@RequestBody MetaProducerQueryByPageReqVO pageRequest) {
        MetaProducerQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaProducerQueryByPageReqBO.class);
        BusinessResponse<List<MetaProducer>> list = this.metaProducerProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 出品方-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaProducerQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaProducer> resBO = this.metaProducerProvider.queryById(id.getId());
        BusinessResponse<MetaProducerQueryByIdResVO> result = JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
        if (result.getContext() != null) {
            result.getContext().setImageList(ImageListConvert.imageToList(result.getContext().getImage()));
        }
        return result;
    }

    /**
     * 出品方-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaProducerAddReqVO addReqVO) {
        addReqVO.setImage(ImageListConvert.listToImage(addReqVO.getImageList()));
        MetaProducer addReqBO = new MetaProducer();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaProducerProvider.insert(addReqBO).getContext());
    }

    /**
     * 出品方-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaProducerEditReqVO editReqVO) {
        editReqVO.setImage(ImageListConvert.listToImage(editReqVO.getImageList()));
        MetaProducer editReqVBO = new MetaProducer();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaProducerProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 出品方-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaProducerProvider.deleteById(id.getId());
    }
}

