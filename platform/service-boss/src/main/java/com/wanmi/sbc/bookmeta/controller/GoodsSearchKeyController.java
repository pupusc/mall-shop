package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.*;
import com.wanmi.sbc.bookmeta.provider.GoodsSearchKeyProvider;
import com.wanmi.sbc.bookmeta.vo.GoodsSearchBySpuIdReqVO;
import com.wanmi.sbc.bookmeta.vo.GoodsSearchBySpuIdRespVO;
import com.wanmi.sbc.bookmeta.vo.GoodsSearchKeyAddReqVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/16:43
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("goodsSearchKey")
public class GoodsSearchKeyController {

    @Resource
    private GoodsSearchKeyProvider goodsSearchKeyProvider;
    @Value("classpath:/download/good_search_key.xlsx")
    private org.springframework.core.io.Resource templateLabelFile;

    @PostMapping("queryGoodsSearchKey")
    public BusinessResponse<List<GoodsSearchBySpuIdRespVO>> getGoodsSearch(@RequestBody GoodsSearchBySpuIdReqVO pageRequest) {
        List<GoodsNameBySpuIdBO> goodsNameBySpuId = goodsSearchKeyProvider.getGoodsNameBySpuId(pageRequest.getName());
        List<GoodsSearchBySpuIdRespVO> goodsSearchBySpuIdRespVOS = KsBeanUtil.convertList(goodsNameBySpuId, GoodsSearchBySpuIdRespVO.class);
        return BusinessResponse.success(goodsSearchBySpuIdRespVOS);
    }

    @PostMapping("getAllGoodsSearchKey")
    public BusinessResponse<List<GoodsSearchBySpuIdRespVO>> getAllGoodsSearch(@RequestBody GoodsSearchBySpuIdReqVO pageRequest) {
        GoodsNameBySpuIdBO convert = KsBeanUtil.convert(pageRequest, GoodsNameBySpuIdBO.class);
        BusinessResponse<List<GoodsNameBySpuIdBO>> goodsNameBySpuId = goodsSearchKeyProvider.getAllGoodsSearchKey(convert);
        List<GoodsSearchBySpuIdRespVO> goodsSearchBySpuIdRespVOS = KsBeanUtil.convertList(goodsNameBySpuId.getContext(), GoodsSearchBySpuIdRespVO.class);
        return BusinessResponse.success(goodsSearchBySpuIdRespVOS, goodsNameBySpuId.getPage());
    }

    @PostMapping("addGoodsSearchKey")
    public BusinessResponse<Integer> addGoodsSearch(@RequestBody GoodsSearchKeyAddReqVO pageRequest) {
        GoodsSearchKeyAddBo convert = KsBeanUtil.convert(pageRequest, GoodsSearchKeyAddBo.class);
/*        boolean matches = convert.getName().matches("(,)(.*?)(,)");
        if(!matches){
            return BusinessResponse.error("必须以,开头,以,为结尾");
        }*/
        int i = goodsSearchKeyProvider.addGoodsSearchKey(convert);
        return BusinessResponse.success(i);
    }

    @PostMapping("updateGoodsSearchKey")
    public BusinessResponse<Integer> updateGoodsSearch(@RequestBody GoodsSearchKeyAddReqVO pageRequest) {
        GoodsSearchKeyAddBo convert = KsBeanUtil.convert(pageRequest, GoodsSearchKeyAddBo.class);
        int i = goodsSearchKeyProvider.updateGoodsSearchKey(convert);
        return BusinessResponse.success(i);
    }

    @PostMapping("deleteGoodsSearchKey")
    public BusinessResponse<Integer> deleteGoodsSearch(@RequestBody GoodsSearchBySpuIdReqVO pageRequest) {
        GoodsNameBySpuIdBO convert = KsBeanUtil.convert(pageRequest, GoodsNameBySpuIdBO.class);
        int i = goodsSearchKeyProvider.deleteGoodsSearchKey(convert);
        return BusinessResponse.success(i);
    }

    @PostMapping("goodsList")
    public BusinessResponse<List<GoodsBO>> getGoodsList(@RequestBody GoodsSearchBySpuIdReqVO pageRequest) {
        GoodsNameBySpuIdBO convert = KsBeanUtil.convert(pageRequest, GoodsNameBySpuIdBO.class);
        BusinessResponse<List<GoodsBO>> goodsList = goodsSearchKeyProvider.getGoodsList(convert);
        return goodsList;
    }

    @PostMapping("export")
    public void export() {
        InputStream is = null;
        org.springframework.core.io.Resource file = templateLabelFile;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            is = file.getInputStream();
            Workbook wk = WorkbookFactory.create(is);
            Sheet expressCompanySheet = wk.getSheetAt(0);
            List<GoodsKeyWordsDownLoadBO> goodsBOS = goodsSearchKeyProvider.downloadQuery();
            AtomicInteger rowCount = new AtomicInteger(1);
            for (GoodsKeyWordsDownLoadBO map : goodsBOS) {
                Row row = expressCompanySheet.createRow(rowCount.getAndIncrement());
                row.createCell(0).setCellValue(map.getId().toString());
                row.createCell(1).setCellValue(map.getName().toString());
                row.createCell(2).setCellValue(map.getSpuId().toString());
                row.createCell(3).setCellValue(map.getGoodsName().toString());
                row.createCell(4).setCellValue(map.getType().toString());
                if (1 == map.getType()) {
                    row.createCell(5).setCellValue(map.getRelSpuId().toString());
                    row.createCell(6).setCellValue(map.getRelSkuId().toString());
                }
                if (2 == map.getType()) {
                    row.createCell(7).setCellValue(map.getGoUrl().toString());
                }
            }
            wk.write(outputStream);
            String fileName = URLEncoder.encode("good_search_key.xlsx", "UTF-8");
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

    @PostMapping("import")
    public BusinessResponse<String> importGoodsSearchKey(MultipartFile multipartFile) {
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
                    if (cell == null) {
                        cells[cellNum] = "";
                    } else {
                        cell.setCellType(CellType.STRING);
                        cells[cellNum] = cell.getStringCellValue();
                    }
                }
                GoodsSearchKeyAddBo goodsSearchKeyAddBo = new GoodsSearchKeyAddBo();
                if (StringUtils.isBlank(cells[0])){
                    goodsSearchKeyAddBo.setId(null);
                }else {
                    goodsSearchKeyAddBo.setId(Integer.parseInt(cells[0]));
                }
                goodsSearchKeyAddBo.setName(cells[1]);
                goodsSearchKeyAddBo.setSpuId(cells[2]);
                if (cells.length > 2 && StringUtils.isNotBlank(cells[4])) {
                    goodsSearchKeyAddBo.setType(Integer.parseInt(cells[4]));
                    switch (cells[4]) {
                        case "1":
                            goodsSearchKeyAddBo.setRelSpuId(cells[5]);
                            goodsSearchKeyAddBo.setRelSkuId(cells[6]);
                            break;
                        case "2":
                            goodsSearchKeyAddBo.setGoUrl(cells[7]);
                            break;
                        default:
                            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                                    com.wanmi.sbc.common.util.DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                                    "downloadQuery",
                                    multipartFile,
                                    "type类型不支持");
                    }
                }
                goodsSearchKeyAddBo.setCreateTime(new Date());
                res = goodsSearchKeyProvider.importGoodsSearchKey(goodsSearchKeyAddBo).getContext();
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

    @RequestMapping("/getList")
    public BusinessResponse<List<Map<String, Object>>> getSpuIdAndSkuId(@RequestBody GoodsSearchKeyAddBo bo) {
        return BusinessResponse.success(goodsSearchKeyProvider.getList(bo));
    }
}
