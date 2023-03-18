package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.ares.provider.GoodsServiceProvider;
import com.wanmi.sbc.bookmeta.bo.*;
import com.wanmi.sbc.bookmeta.enums.LabelTypeEnum;
import com.wanmi.sbc.bookmeta.provider.GoodsSearchKeyProvider;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.bookmeta.vo.*;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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
 *
 * @author Liang Jun
 * @menu 图书基础库
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

    @Resource
    private GoodsSearchKeyProvider goodsSearchKeyProvider;

    @Value("classpath:/download/lable.xlsx")
    private org.springframework.core.io.Resource templateLabelFile;

    @Value("classpath:/download/goods_label.xlsx")
    private org.springframework.core.io.Resource goodsLabelFile;
    @Value("classpath:/download/goods.xlsx")
    private org.springframework.core.io.Resource goodsFile;

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
    public BusinessResponse<List<MetaLabelBO>> getLabels(@RequestBody MetaLabelQueryByIdResVO resVO) {
        MetaLabelQueryByPageReqBO convert = KsBeanUtil.convert(resVO, MetaLabelQueryByPageReqBO.class);
        BusinessResponse<List<MetaLabelBO>> labels = this.metaLabelProvider.getLabels(convert);
        return labels;
    }

    @PostMapping("getLabelsByGoods")
    public BusinessResponse<List<MetaLabelBO>> getLabelsByGood(@RequestBody MetaLabelQueryByIdResVO resVO) {
        MetaLabelQueryByPageReqBO convert = KsBeanUtil.convert(resVO, MetaLabelQueryByPageReqBO.class);
        BusinessResponse<List<MetaLabelBO>> labels = this.metaLabelProvider.getLabelByGoodsId(convert);
        return labels;
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
        MetaLabelUpdateStatusReqBO reqBO = new MetaLabelUpdateStatusReqBO();
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

    @PostMapping("deleteMetaLabel")
    public BusinessResponse<Boolean> deleteMetaLabel(@RequestBody IntegerIdVO id) {
        return this.metaLabelProvider.deleteById(id.getId());
    }

    /**
     * 下载模板
     */
    @PostMapping("/templateLabel")
    public void templateLabel() {
        InputStream is = null;
        org.springframework.core.io.Resource file = templateLabelFile;
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            is = file.getInputStream();
            Workbook wk = WorkbookFactory.create(is);

            Sheet expressCompanySheet = wk.getSheetAt(0);
            List<Map> bookMap = metaLabelProvider.queryAllLabel();
            AtomicInteger rowCount = new AtomicInteger(1);
            bookMap.stream().forEach(map -> {
                Row row = expressCompanySheet.createRow(rowCount.getAndIncrement());
                row.createCell(0).setCellValue(map.get("id").toString());
                row.createCell(1).setCellValue(map.get("name").toString());
            });
            wk.write(outputStream);
            String fileName = URLEncoder.encode("lable.xlsx", "UTF-8");
            HttpUtil.getResponse().setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            HttpUtil.getResponse().getOutputStream().write(outputStream.toByteArray());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("addGoodsLabel")
    public BusinessResponse<String> addGoodsLabel(@RequestBody GoodsLabelAddReqVO reqVO) {
        GoodsLabelSpuReqBO convert = KsBeanUtil.convert(reqVO, GoodsLabelSpuReqBO.class);
        BusinessResponse<String> integerBusinessResponse = metaLabelProvider.insertGoodsLabel(convert);
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

    @PostMapping("getList")
    public List<MetaLabelBO> getList(@RequestBody  LabelListReqVO reqVO){
        List<MetaLabelBO> type2Label = metaLabelProvider.getType2Label(reqVO);
        return type2Label;
    }

    @PostMapping("importGoodsLabel")
    public BusinessResponse<String> importGoodsLabel(MultipartFile multipartFile) {
        String res = null;
        Workbook wb = null;
        try {
            try {
                wb = new HSSFWorkbook(new POIFSFileSystem(multipartFile.getInputStream()));
            } catch (Exception e) {
                wb = new XSSFWorkbook(multipartFile.getInputStream());        //XSSF不能读取Excel2003以前（包括2003）的版本
            }

            Sheet sheet = wb.getSheetAt(0);

            if (sheet == null) {
                return null;
            }

            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了第一行的所有行
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                //获得当前行的列数
                int lastCellNum = row.getLastCellNum();
                String[] cells = new String[row.getLastCellNum()];

                //循环当前行
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    DataFormatter dataFormatter = new DataFormatter();
                    String s = dataFormatter.formatCellValue(cell);
                    cells[cellNum] = s;
                }
                GoodsLabelSpuReqBO goodsLabelSpuReqBO = new GoodsLabelSpuReqBO();
                goodsLabelSpuReqBO.setGoodsId(String.valueOf(cells[0]));
                goodsLabelSpuReqBO.setGoodsName((cells[1]));
                goodsLabelSpuReqBO.setLabelId(Integer.parseInt(cells[2]));
                goodsLabelSpuReqBO.setLabelName((cells[3]));
                goodsLabelSpuReqBO.setId(Integer.parseInt(cells[4]));
                if (lastCellNum > 5) {
                    goodsLabelSpuReqBO.setFirstId(Integer.parseInt(cells[5]));
                }
                if (lastCellNum > 6) {
                    goodsLabelSpuReqBO.setSecondId(Integer.parseInt(cells[6]));
                }
                if (lastCellNum > 7) {
                    goodsLabelSpuReqBO.setId(Integer.parseInt(cells[7]));
                }
                res = metaLabelProvider.importGoodsLabel(goodsLabelSpuReqBO).getContext();
            }
        } catch (Exception e) {
            return BusinessResponse.error("文件不符合要求");
        } finally {
            try {
                wb.close();
                multipartFile.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != res) {
            if (res.contains("failed")) {
                return BusinessResponse.error(res);
            }
        }
        return BusinessResponse.success(res);
    }

    @PostMapping("exportGoodsLabel")
    public void exportGoodsLabel() {
        InputStream is = null;
        org.springframework.core.io.Resource file = goodsLabelFile;
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            is = file.getInputStream();
            Workbook wk = WorkbookFactory.create(is);

            Sheet expressCompanySheet = wk.getSheetAt(0);
            List<GoodsLabelSpuReqBO> goodsLabelSpuReqBOS = metaLabelProvider.queryAllGoodsLabel();
            AtomicInteger rowCount = new AtomicInteger(1);
            for (GoodsLabelSpuReqBO good : goodsLabelSpuReqBOS) {
                Row row = expressCompanySheet.createRow(rowCount.getAndIncrement());
                row.createCell(0).setCellValue(good.getGoodsId());
                row.createCell(1).setCellValue(good.getGoodsName());
                row.createCell(2).setCellValue(good.getLabelId());
                row.createCell(3).setCellValue(good.getLabelName());
                row.createCell(4).setCellValue(good.getId());
                row.createCell(5).setCellValue(good.getFirstId());
                row.createCell(6).setCellValue(good.getSecondId());
                row.createCell(7).setCellValue(good.getOrderNum());
            }
            wk.write(outputStream);
            String fileName = URLEncoder.encode("goods_label.xlsx", "UTF-8");
            HttpUtil.getResponse().setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            HttpUtil.getResponse().getOutputStream().write(outputStream.toByteArray());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("exportGoods")
    public void exportGoods() {
        InputStream is = null;
        org.springframework.core.io.Resource file = goodsFile;
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            is = file.getInputStream();
            Workbook wk = WorkbookFactory.create(is);

            Sheet expressCompanySheet = wk.getSheetAt(0);
            List<GoodsBO> goodsList = metaLabelProvider.queryAllGoods();
            AtomicInteger rowCount = new AtomicInteger(1);
            for (GoodsBO good : goodsList) {
                Row row = expressCompanySheet.createRow(rowCount.getAndIncrement());
                row.createCell(0).setCellValue(good.getGoodsId());
                row.createCell(1).setCellValue(good.getGoodsName());
            }
            wk.write(outputStream);
            String fileName = URLEncoder.encode("goods.xlsx", "UTF-8");
            HttpUtil.getResponse().setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            HttpUtil.getResponse().getOutputStream().write(outputStream.toByteArray());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("getGoodsDetailOther")
    public BusinessResponse<List<GoodDetailOtherRespBO>> getGoodsDetailOther(@RequestBody GoodsLabelAddReqVO reqVO) {
        GoodsOtherDetailBO convert = KsBeanUtil.convert(reqVO, GoodsOtherDetailBO.class);
        BusinessResponse<List<GoodDetailOtherRespBO>> goodsDetailAndOther = metaLabelProvider.getGoodsDetailAndOther(convert);
        return goodsDetailAndOther;
    }

    @PostMapping("updateGoodsDetailOther")
    public BusinessResponse<Integer> updateGoodsDetailOther(@RequestBody GoodDetailOtherRespBO reqVO) {
        int i = metaLabelProvider.updateGoodsDetailAndOther(reqVO);
        return BusinessResponse.success(i);
    }
}

