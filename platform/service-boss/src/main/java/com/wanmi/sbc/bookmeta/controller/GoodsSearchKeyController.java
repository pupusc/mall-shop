package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.GoodsBO;
import com.wanmi.sbc.bookmeta.bo.GoodsKeyWordsDownLoadBO;
import com.wanmi.sbc.bookmeta.bo.GoodsNameBySpuIdBO;
import com.wanmi.sbc.bookmeta.bo.GoodsSearchKeyAddBo;
import com.wanmi.sbc.bookmeta.provider.GoodsSearchKeyProvider;
import com.wanmi.sbc.bookmeta.vo.GoodsSearchBySpuIdReqVO;
import com.wanmi.sbc.bookmeta.vo.GoodsSearchBySpuIdRespVO;
import com.wanmi.sbc.bookmeta.vo.GoodsSearchKeyAddReqVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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
        return BusinessResponse.success(goodsSearchBySpuIdRespVOS);
    }

    @PostMapping("addGoodsSearchKey")
    public BusinessResponse<Integer> addGoodsSearch(@RequestBody GoodsSearchKeyAddReqVO pageRequest) {
        GoodsSearchKeyAddBo convert = KsBeanUtil.convert(pageRequest, GoodsSearchKeyAddBo.class);
        boolean matches = convert.getName().matches("(,)(.*?)(,)");
        if(!matches){
            return BusinessResponse.error("必须以,开头,以,为结尾");
        }
        int i = goodsSearchKeyProvider.addGoodsSearchKey(convert);
        return BusinessResponse.success(i);
    }
    @PostMapping("updateGoodsSearchKey")
    public BusinessResponse<Integer> updateGoodsSearch(@RequestBody GoodsSearchBySpuIdReqVO pageRequest) {
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
                    row.createCell(0).setCellValue(map.getName().toString());
                    row.createCell(1).setCellValue(map.getSpuId().toString());
                    row.createCell(2).setCellValue(map.getGoodsName().toString());

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
    public BusinessResponse<List<GoodsBO>> importGoodsSearchKey(@RequestBody GoodsSearchBySpuIdReqVO pageRequest) {
        GoodsNameBySpuIdBO convert = KsBeanUtil.convert(pageRequest, GoodsNameBySpuIdBO.class);
        BusinessResponse<List<GoodsBO>> goodsList = goodsSearchKeyProvider.getGoodsList(convert);
        return goodsList;
    }

}
