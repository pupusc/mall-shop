package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.*;
import com.wanmi.sbc.bookmeta.enums.LabelTypeEnum;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.bookmeta.vo.*;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    @Value("classpath:/download/lable.xlsx")
    private org.springframework.core.io.Resource templateLabelFile;

    /**
     * 标签-分类查询
     *
     * @param pageRequest 分类查询
     * @return 查询结果
     */
    @PostMapping("queryCate")
    public BusinessResponse<List<Map>> queryCate(@RequestBody MetaLabelQueryByPageReqVO pageRequest) {

        List<Map> list = this.metaLabelProvider.getLabelCate(pageRequest.getParentId());

        return BusinessResponse.success(list);
    }

    /**
     * 标签-分类查询2
     *
     * @return 查询结果
     */
    @PostMapping("queryCate2")
    public BusinessResponse<List<Map>> queryCate2(@RequestBody @NotNull String parent_id) {

        List<Map> list = this.metaLabelProvider.getLabelCate2(parent_id);

        return BusinessResponse.success(list);
    }




    /**
     * 标签-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaLabelQueryByPageResVO>> queryByPage(@RequestBody MetaLabelQueryByPageReqVO pageRequest) {
        MetaLabelQueryByPageReqBO pageReqBO = new MetaLabelQueryByPageReqBO();
        BeanUtils.copyProperties(pageRequest, pageReqBO);
        pageReqBO.setType(LabelTypeEnum.LABEL.getCode());

        BusinessResponse<List<MetaLabelBO>> boResult = this.metaLabelProvider.queryByPage(pageReqBO);
        if (!CommonErrorCode.SUCCESSFUL.equals(boResult.getCode())) {
            return BusinessResponse.error(boResult.getCode(), boResult.getMessage());
        }

        List<MetaLabelQueryByPageResVO> voList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(boResult.getContext())) {
            voList = boResult.getContext().stream().map(item -> {
                MetaLabelQueryByPageResVO resVO = new MetaLabelQueryByPageResVO();
                BeanUtils.copyProperties(item, resVO);
                resVO.setPathList(StringSplitUtil.split(resVO.getPathName(), PATH_SPLIT_SYMBOL));
                return resVO;
            }).collect(Collectors.toList());
        }
        return BusinessResponse.success(voList, boResult.getPage());
    }

    /**
     * 标签分类-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryCateByPage")
    public BusinessResponse<List<MetaLabelQueryByPageResVO>> queryCateByPage(@RequestBody MetaLabelQueryByPageReqVO pageRequest) {
        MetaLabelQueryByPageReqBO pageReqBO = new MetaLabelQueryByPageReqBO();
        BeanUtils.copyProperties(pageRequest, pageReqBO);
        pageReqBO.setType(LabelTypeEnum.CATEGORY.getCode());
        BusinessResponse<List<MetaLabelBO>> boResult = this.metaLabelProvider.queryByPage(pageReqBO);

        if (!CommonErrorCode.SUCCESSFUL.equals(boResult.getCode())) {
            return BusinessResponse.error(boResult.getCode(), boResult.getMessage());
        }

        List<MetaLabelQueryByPageResVO> voList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(boResult.getContext())) {
            voList = boResult.getContext().stream().map(item -> {
                MetaLabelQueryByPageResVO resVO = new MetaLabelQueryByPageResVO();
                BeanUtils.copyProperties(item, resVO);
                resVO.setPathList(StringSplitUtil.split(resVO.getPathName(), PATH_SPLIT_SYMBOL));
                return resVO;
            }).collect(Collectors.toList());
        }
        return BusinessResponse.success(voList, boResult.getPage());
    }

    /**
     * 标签-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaLabelQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaLabelBO> resBO = this.metaLabelProvider.queryById(id.getId());
        BusinessResponse<MetaLabelQueryByIdResVO> result = JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
        result.setContext(JSON.parseObject(JSON.toJSONString(resBO.getContext()), MetaLabelQueryByIdResVO.class));

        if (result.getContext() != null) {
            result.getContext().setPathList(StringSplitUtil.split(result.getContext().getPath(), PATH_SPLIT_SYMBOL));
        }
        return result;
    }

    @PostMapping("getLabelsByName")
    public BusinessResponse<List<MetaLabelGetAllByNameRespVO>> getLabels(@RequestBody MetaLabelQueryByIdResVO resVO) {
        MetaLabelQueryByPageReqBO convert = KsBeanUtil.convert(resVO, MetaLabelQueryByPageReqBO.class);
        BusinessResponse<List<MetaLabelBO>> labels = this.metaLabelProvider.getLabels(convert);
        List<MetaLabelGetAllByNameRespVO> metaLabelQueryByIdResVOS = KsBeanUtil.convertList(labels.getContext(), MetaLabelGetAllByNameRespVO.class);
        return BusinessResponse.success(metaLabelQueryByIdResVOS);
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
        MetaLabelBO addReqBO = new MetaLabelBO();
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
        MetaLabelBO editReqVBO = new MetaLabelBO();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaLabelProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 标签-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("editName")
    public BusinessResponse<Boolean> editName(@RequestBody MetaLabelEditReqVO editReqVO) {
        editReqVO.setPath(StringSplitUtil.join(editReqVO.getPathList(), PATH_SPLIT_SYMBOL));
        MetaLabelBO editReqVBO = new MetaLabelBO();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaLabelProvider.updateName(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 标签-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("editStatus")
    public BusinessResponse<Boolean> editStatus(@RequestBody @Valid MetaLabelEditStatusReqVO editReqVO) {
        MetaLabelUpdateStatusReqBO reqBO =  new MetaLabelUpdateStatusReqBO();
        reqBO.setId(editReqVO.getId());
        reqBO.setEnable(Integer.valueOf(1).equals(editReqVO.getStatus()));
        return this.metaLabelProvider.updateStatus(reqBO);
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

    /**
     * 下载模板
     */
    @PostMapping("/templateLabel")
    public void templateLabel() {
        InputStream is = null;
        org.springframework.core.io.Resource file=templateLabelFile;
        try{

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            is = file.getInputStream();
            Workbook wk = WorkbookFactory.create(is);

            Sheet expressCompanySheet = wk.getSheetAt(0);
            List<Map> bookMap = metaLabelProvider.queryAllLabel();
            AtomicInteger rowCount= new AtomicInteger(1);
            bookMap.stream().forEach(map -> {
                Row row = expressCompanySheet.createRow(rowCount.getAndIncrement());
                row.createCell(0).setCellValue(map.get("id").toString());
                row.createCell(1).setCellValue(map.get("name").toString());
            });
            wk.write(outputStream);
            String fileName = URLEncoder.encode("lable.xlsx", "UTF-8");
            HttpUtil.getResponse().setHeader("Access-Control-Expose-Headers","Content-Disposition");
            HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            HttpUtil.getResponse().getOutputStream().write(outputStream.toByteArray());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("addGoodsLabel")
    public BusinessResponse<Integer> addGoodsLabel(@RequestBody GoodsLabelAddReqVO reqVO) {
        GoodsLabelSpuReqBO convert = KsBeanUtil.convert(reqVO, GoodsLabelSpuReqBO.class);
        BusinessResponse<Integer> integerBusinessResponse = metaLabelProvider.insertGoodsLabel(convert);
        return integerBusinessResponse;
    }

    @PostMapping("updateGoodsLabel")
    public BusinessResponse<Integer> updateGoodsLabel(@RequestBody GoodsLabelAddReqVO reqVO) {
        GoodsLabelSpuReqBO convert = KsBeanUtil.convert(reqVO, GoodsLabelSpuReqBO.class);
        BusinessResponse<Integer> integerBusinessResponse = metaLabelProvider.updateGoodsLabel(convert);
        return integerBusinessResponse;
    }

    @PostMapping("deleteGoodsLabel")
    public BusinessResponse<Integer> deleteGoodsLabel(@RequestBody GoodsLabelAddReqVO reqVO) {
        GoodsLabelSpuReqBO convert = KsBeanUtil.convert(reqVO, GoodsLabelSpuReqBO.class);
        return BusinessResponse.success(metaLabelProvider.deleteGoodsLabel(convert));
    }


}

