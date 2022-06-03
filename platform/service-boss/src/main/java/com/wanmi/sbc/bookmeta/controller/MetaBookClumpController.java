package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookClumpBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookClumpQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.MetaBookClumpProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookClumpAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookClumpEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookClumpQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookClumpQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookClumpQueryByPageResVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 丛书(MetaBookClump)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@RestController
@RequestMapping("metaBookClump")
public class MetaBookClumpController {
    /**
     * 丛书-服务对象
     */
    @Resource
    private MetaBookClumpProvider metaBookClumpProvider;

    /**
     * 丛书-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookClumpQueryByPageResVO>> queryByPage(@RequestBody MetaBookClumpQueryByPageReqVO pageRequest) {
        MetaBookClumpQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookClumpQueryByPageReqBO.class);
        BusinessResponse<List<MetaBookClumpBO>> list = this.metaBookClumpProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 丛书-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookClumpQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBookClumpBO> resBO = this.metaBookClumpProvider.queryById(id.getId());
        BusinessResponse<MetaBookClumpQueryByIdResVO> result = JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
        result.setContext(JSON.parseObject(JSON.toJSONString(resBO.getContext()), MetaBookClumpQueryByIdResVO.class));

        if (result.getContext() != null) {
            result.getContext().setImageList(StringSplitUtil.split(result.getContext().getImage()));
        }
        return result;
    }

    /**
     * 丛书-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookClumpAddReqVO addReqVO) {
        addReqVO.setImage(StringSplitUtil.join(addReqVO.getImageList()));
        MetaBookClumpBO addReqBO = new MetaBookClumpBO();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookClumpProvider.insert(addReqBO).getContext());
    }

    /**
     * 丛书-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookClumpEditReqVO editReqVO) {
        editReqVO.setImage(StringSplitUtil.join(editReqVO.getImageList()));
        MetaBookClumpBO editReqVBO = new MetaBookClumpBO();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookClumpProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 丛书-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookClumpProvider.deleteById(id.getId());
    }

}

