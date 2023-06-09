package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaPublisherQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.MetaPublisherProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.bookmeta.bo.MetaPublisherBO;
import com.wanmi.sbc.bookmeta.vo.MetaPublisherAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaPublisherEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaPublisherQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaPublisherQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaPublisherQueryByPageResVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 出版社(MetaPublisher)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@RestController
@RequestMapping("metaPublisher")
public class MetaPublisherController {
    /**
     * 出版社-服务对象
     */
    @Resource
    private MetaPublisherProvider metaPublisherProvider;

    /**
     * 出版社-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaPublisherQueryByPageResVO>> queryByPage(@RequestBody MetaPublisherQueryByPageReqVO pageRequest) {
        MetaPublisherQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaPublisherQueryByPageReqBO.class);
        BusinessResponse<List<MetaPublisherBO>> list = this.metaPublisherProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 出版社-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaPublisherQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaPublisherBO> resBO = this.metaPublisherProvider.queryById(id.getId());
        BusinessResponse<MetaPublisherQueryByIdResVO> result = JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
        result.setContext(JSON.parseObject(JSON.toJSONString(resBO.getContext()), MetaPublisherQueryByIdResVO.class));

        if (result.getContext() != null) {
            result.getContext().setImageList(StringSplitUtil.split(result.getContext().getImage()));
        }
        return result;
    }

    /**
     * 出版社-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody @Valid MetaPublisherAddReqVO addReqVO) {
        addReqVO.setImage(StringSplitUtil.join(addReqVO.getImageList()));
        MetaPublisherBO addReqBO = new MetaPublisherBO();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaPublisherProvider.insert(addReqBO).getContext());
    }

    /**
     * 出版社-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody @Valid MetaPublisherEditReqVO editReqVO) {
        editReqVO.setImage(StringSplitUtil.join(editReqVO.getImageList()));
        MetaPublisherBO editReqVBO = new MetaPublisherBO();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaPublisherProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 出版社-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaPublisherProvider.deleteById(id.getId());
    }

}

