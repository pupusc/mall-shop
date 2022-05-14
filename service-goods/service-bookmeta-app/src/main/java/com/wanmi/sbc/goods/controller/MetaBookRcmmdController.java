package com.wanmi.sbc.goods.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.entity.MetaBookRcmmd;
import com.wanmi.sbc.goods.provider.MetaBookRcmmdProvider;
import com.wanmi.sbc.goods.vo.MetaBookRcmmdQueryByPageReqVO;
import com.wanmi.sbc.goods.vo.MetaBookRcmmdQueryByPageResVO;
import com.wanmi.sbc.goods.vo.MetaBookRcmmdQueryByIdResVO;
import com.wanmi.sbc.goods.vo.MetaBookRcmmdAddReqVO;
import com.wanmi.sbc.goods.vo.MetaBookRcmmdEditReqVO;
import com.wanmi.sbc.goods.vo.IntegerIdVO;
import com.wanmi.sbc.goods.bo.MetaBookRcmmdQueryByPageReqBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 书籍推荐(MetaBookRcmmd)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@RestController
@RequestMapping("metaBookRcmmd")
public class MetaBookRcmmdController {
    /**
     * 书籍推荐-服务对象
     */
    @Resource
    private MetaBookRcmmdProvider metaBookRcmmdProvider;

    /**
     * 书籍推荐-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookRcmmdQueryByPageResVO>> queryByPage(@RequestBody MetaBookRcmmdQueryByPageReqVO pageRequest) {
        MetaBookRcmmdQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookRcmmdQueryByPageReqBO.class);
        BusinessResponse<List<MetaBookRcmmd>> list = this.metaBookRcmmdProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 书籍推荐-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookRcmmdQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBookRcmmd> resBO = this.metaBookRcmmdProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 书籍推荐-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookRcmmdAddReqVO addReqVO) {
        MetaBookRcmmd addReqBO = new MetaBookRcmmd();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookRcmmdProvider.insert(addReqBO).getContext());
    }

    /**
     * 书籍推荐-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookRcmmdEditReqVO editReqVO) {
        MetaBookRcmmd editReqVBO = new MetaBookRcmmd();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookRcmmdProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 书籍推荐-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookRcmmdProvider.deleteById(id.getId());
    }

}

