package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookFigureQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.MetaBookFigureProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookFigureAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookFigureEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookFigureQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookFigureQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookFigureQueryByPageResVO;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 书籍人物(MetaBookFigure)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@RestController
@RequestMapping("metaBookFigure")
public class MetaBookFigureController {
    /**
     * 书籍人物-服务对象
     */
    @Resource
    private MetaBookFigureProvider metaBookFigureProvider;

    /**
     * 书籍人物-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookFigureQueryByPageResVO>> queryByPage(@RequestBody MetaBookFigureQueryByPageReqVO pageRequest) {
        MetaBookFigureQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookFigureQueryByPageReqBO.class);
        BusinessResponse<List<MetaBookFigure>> list = this.metaBookFigureProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 书籍人物-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookFigureQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBookFigure> resBO = this.metaBookFigureProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 书籍人物-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookFigureAddReqVO addReqVO) {
        MetaBookFigure addReqBO = new MetaBookFigure();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookFigureProvider.insert(addReqBO).getContext());
    }

    /**
     * 书籍人物-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookFigureEditReqVO editReqVO) {
        MetaBookFigure editReqVBO = new MetaBookFigure();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookFigureProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 书籍人物-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookFigureProvider.deleteById(id.getId());
    }

}

