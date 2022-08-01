package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaFigureAddReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaFigureBO;
import com.wanmi.sbc.bookmeta.bo.MetaFigureEditReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaFigureQueryByIdResBO;
import com.wanmi.sbc.bookmeta.bo.MetaFigureQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.enums.FigureTypeEnum;
import com.wanmi.sbc.bookmeta.provider.MetaFigureProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaFigureAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaFigureEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaFigureQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaFigureQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaFigureQueryByPageResVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 人物(MetaFigure)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-16 11:26:54
 */
@RestController
@RequestMapping("metaFigure")
public class MetaFigureController {
    /**
     * 人物-服务对象
     */
    @Resource
    private MetaFigureProvider metaFigureProvider;

    /**
     * 人物-下拉框查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("downList")
    public BusinessResponse<List<MetaFigureQueryByPageResVO>> downList(@RequestBody @Valid MetaFigureQueryByPageReqVO pageRequest) {
        MetaFigureQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaFigureQueryByPageReqBO.class);

        //打通人物=1作者/译者/绘画人/作序人；2编辑；3名家；
        Integer type = pageReqBO.getType();
        if (FigureTypeEnum.AUTHOR.getCode().equals(type) || FigureTypeEnum.EDITOR.getCode().equals(type) || FigureTypeEnum.FAMOUS.getCode().equals(type)) {
            pageReqBO.setTypeIn(Arrays.asList(FigureTypeEnum.FAMOUS.getCode(), FigureTypeEnum.AUTHOR.getCode()));
            pageReqBO.setType(null);
        }

        BusinessResponse<List<MetaFigureBO>> list = this.metaFigureProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }


    /**
     * 人物-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaFigureQueryByPageResVO>> queryByPage(@RequestBody MetaFigureQueryByPageReqVO pageRequest) {
        MetaFigureQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaFigureQueryByPageReqBO.class);

        if (Integer.valueOf(FigureTypeEnum.FAMOUS.getCode()).equals(pageReqBO.getType())) {
            pageReqBO.setType(null);
            pageReqBO.setTypeIn(Arrays.asList(FigureTypeEnum.FAMOUS.getCode(), FigureTypeEnum.AUTHOR.getCode()));
        }

        BusinessResponse<List<MetaFigureBO>> list = this.metaFigureProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 人物-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaFigureQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaFigureQueryByIdResBO> resultBO = this.metaFigureProvider.queryById(id.getId());

        BusinessResponse<MetaFigureQueryByIdResVO> resultVO = JSON.parseObject(JSON.toJSONString(resultBO), BusinessResponse.class);
        resultVO.setContext(JSON.parseObject(JSON.toJSONString(resultBO.getContext()), MetaFigureQueryByIdResVO.class));

        if (resultVO.getContext() != null) {
            resultVO.getContext().setImageList(StringSplitUtil.split(resultVO.getContext().getImage()));
        }
        return resultVO;
    }

    /**
     * 人物-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaFigureAddReqVO addReqVO) {
        addReqVO.setImage(StringSplitUtil.join(addReqVO.getImageList()));
        MetaFigureAddReqBO addReqBO = new MetaFigureAddReqBO();
        BeanUtils.copyProperties(addReqVO, addReqBO);

        if (!CollectionUtils.isEmpty(addReqVO.getAwardList())) {
            addReqBO.setAwardList(addReqVO.getAwardList().stream().map(item -> {
                MetaFigureAddReqBO.FigureAward figureAwardBO = new MetaFigureAddReqBO.FigureAward();
                BeanUtils.copyProperties(item, figureAwardBO);
                return figureAwardBO;
            }).collect(Collectors.toList()));
        }

        return this.metaFigureProvider.insert(addReqBO);
    }

    /**
     * 人物-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaFigureEditReqVO editReqVO) {
        editReqVO.setImage(StringSplitUtil.join(editReqVO.getImageList()));
        MetaFigureEditReqBO editReqVBO = new MetaFigureEditReqBO();
        BeanUtils.copyProperties(editReqVO, editReqVBO);

        if (!CollectionUtils.isEmpty(editReqVO.getAwardList())) {
            editReqVBO.setAwardList(editReqVO.getAwardList().stream().map(item -> {
                MetaFigureEditReqBO.FigureAward figureAwardBO = new MetaFigureEditReqBO.FigureAward();
                BeanUtils.copyProperties(item, figureAwardBO);
                return figureAwardBO;
            }).collect(Collectors.toList()));
        }

        return this.metaFigureProvider.update(editReqVBO);
    }

    /**
     * 人物-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaFigureProvider.deleteById(id.getId());
    }

}

