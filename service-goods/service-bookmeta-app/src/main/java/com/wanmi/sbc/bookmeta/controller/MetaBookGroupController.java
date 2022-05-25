package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookGroupQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.MetaBookGroupProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookGroupAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookGroupEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookGroupQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookGroupQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookGroupQueryByPageResVO;
import com.wanmi.sbc.bookmeta.entity.MetaBookGroup;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 书组(MetaBookGroup)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@RestController
@RequestMapping("metaBookGroup")
public class MetaBookGroupController {
    /**
     * 书组-服务对象
     */
    @Resource
    private MetaBookGroupProvider metaBookGroupProvider;

    /**
     * 书组-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookGroupQueryByPageResVO>> queryByPage(@RequestBody MetaBookGroupQueryByPageReqVO pageRequest) {
        MetaBookGroupQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookGroupQueryByPageReqBO.class);
        BusinessResponse<List<MetaBookGroup>> list = this.metaBookGroupProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 书组-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookGroupQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBookGroup> resBO = this.metaBookGroupProvider.queryById(id.getId());
        BusinessResponse<MetaBookGroupQueryByIdResVO> result = JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
        result.setContext(JSON.parseObject(JSON.toJSONString(resBO.getContext()), MetaBookGroupQueryByIdResVO.class));

        if (result.getContext() != null) {
            result.getContext().setImageList(StringSplitUtil.split(result.getContext().getImage()));
        }
        return result;
    }

    /**
     * 书组-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookGroupAddReqVO addReqVO) {
        addReqVO.setImage(StringSplitUtil.join(addReqVO.getImageList()));
        MetaBookGroup addReqBO = new MetaBookGroup();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookGroupProvider.insert(addReqBO).getContext());
    }

    /**
     * 书组-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookGroupEditReqVO editReqVO) {
        editReqVO.setImage(StringSplitUtil.join(editReqVO.getImageList()));
        MetaBookGroup editReqVBO = new MetaBookGroup();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookGroupProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 书组-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookGroupProvider.deleteById(id.getId());
    }

}

