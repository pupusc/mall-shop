package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaFigureAwardQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.MetaFigureAwardProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.entity.MetaFigureAward;
import com.wanmi.sbc.bookmeta.vo.MetaFigureAwardQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaFigureAwardQueryByPageResVO;
import com.wanmi.sbc.bookmeta.vo.MetaFigureAwardQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaFigureAwardAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaFigureAwardEditReqVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 人物获奖(MetaFigureAward)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@RestController
@RequestMapping("metaFigureAward")
public class MetaFigureAwardController {
    /**
     * 人物获奖-服务对象
     */
    @Resource
    private MetaFigureAwardProvider metaFigureAwardProvider;

    /**
     * 人物获奖-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaFigureAwardQueryByPageResVO>> queryByPage(@RequestBody MetaFigureAwardQueryByPageReqVO pageRequest) {
        MetaFigureAwardQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaFigureAwardQueryByPageReqBO.class);
        BusinessResponse<List<MetaFigureAward>> list = this.metaFigureAwardProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 人物获奖-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaFigureAwardQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaFigureAward> resBO = this.metaFigureAwardProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 人物获奖-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaFigureAwardAddReqVO addReqVO) {
        MetaFigureAward addReqBO = new MetaFigureAward();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaFigureAwardProvider.insert(addReqBO).getContext());
    }

    /**
     * 人物获奖-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaFigureAwardEditReqVO editReqVO) {
        MetaFigureAward editReqVBO = new MetaFigureAward();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaFigureAwardProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 人物获奖-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaFigureAwardProvider.deleteById(id.getId());
    }

}

