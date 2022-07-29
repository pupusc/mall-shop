package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaZoneAddReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneByIdResBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneByIdResBO$Book;
import com.wanmi.sbc.bookmeta.bo.MetaZoneEditReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneEnableReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaZone;
import com.wanmi.sbc.bookmeta.provider.MetaZoneProvider;
import com.wanmi.sbc.bookmeta.vo.MetaZoneAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaZoneEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaZoneEnableReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaZoneQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaZoneQueryByIdResVO$Book;
import com.wanmi.sbc.bookmeta.vo.MetaZoneQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaZoneQueryByPageResVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书分区(MetaZone)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-07-27 10:45:10
 */
@RestController
@RequestMapping("metaZone")
public class MetaZoneController {
    /**
     * 图书分区-服务对象
     */
    @Resource
    private MetaZoneProvider metaZoneProvider;

    /**
     * 图书分区-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaZoneQueryByPageResVO>> queryByPage(@RequestBody MetaZoneQueryByPageReqVO pageRequest) {
        MetaZoneQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaZoneQueryByPageReqBO.class);
        BusinessResponse<List<MetaZone>> pageResult = this.metaZoneProvider.queryByPage(pageReqBO);

        List<MetaZoneQueryByPageResVO> voList = new ArrayList<>();
        if (pageResult.getContext() != null) {
            for (MetaZone bo : pageResult.getContext()) {
                MetaZoneQueryByPageResVO vo = new MetaZoneQueryByPageResVO();
                BeanUtils.copyProperties(bo, vo);
                voList.add(vo);
            }
        }
        return BusinessResponse.success(voList, pageResult.getPage());
    }

    /**
     * 图书分区-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaZoneQueryByIdResVO> queryById(@RequestBody @Valid IntegerIdVO id) {
        MetaZoneByIdResBO resBO = this.metaZoneProvider.queryById(id.getId()).getContext();

        if (resBO == null) {
            return BusinessResponse.success();
        }

        MetaZoneQueryByIdResVO resVO = new  MetaZoneQueryByIdResVO();
        BeanUtils.copyProperties(resBO, resVO);
        resVO.setBooks(new ArrayList<>());
        if (resBO.getBooks() != null) {
            for (MetaZoneByIdResBO$Book bookBO : resBO.getBooks()) {
                MetaZoneQueryByIdResVO$Book bookVO = new MetaZoneQueryByIdResVO$Book();
                bookVO.setId(bookBO.getId());
                bookVO.setIsbn(bookBO.getIsbn());
                bookVO.setName(bookBO.getName());
                bookVO.setAuthorName(bookBO.getAuthorName());
                resVO.getBooks().add(bookVO);
            }
        }

        return BusinessResponse.success(resVO);
    }

    /**
     * 图书分区-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaZoneAddReqVO addReqVO) {
        MetaZoneAddReqBO addReqBO = new MetaZoneAddReqBO();
        addReqBO.setType(addReqVO.getType());
        addReqBO.setName(addReqVO.getName());
        addReqBO.setDescr(addReqVO.getDescr());
        addReqBO.setBooks(addReqVO.getBooks());
        return BusinessResponse.success(this.metaZoneProvider.insert(addReqBO).getContext());
    }

    /**
     * 图书分区-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaZoneEditReqVO editReqVO) {
        MetaZoneEditReqBO editReqBO = new MetaZoneEditReqBO();
        editReqBO.setId(editReqVO.getId());
        editReqBO.setType(editReqVO.getType());
        editReqBO.setName(editReqVO.getName());
        editReqBO.setDescr(editReqVO.getDescr());
        editReqBO.setBooks(editReqVO.getBooks());
        return this.metaZoneProvider.update(editReqBO);
    }

    /**
     * 图书分区-开启关闭
     */
    @PostMapping("enable")
    public BusinessResponse<Boolean> enable(@RequestBody MetaZoneEnableReqVO paramVO) {
        MetaZoneEnableReqBO paramBO = new MetaZoneEnableReqBO();
        paramBO.setId(paramVO.getId());
        paramBO.setFlag(paramVO.getFlag());
        return this.metaZoneProvider.enable(paramBO);
    }
}

