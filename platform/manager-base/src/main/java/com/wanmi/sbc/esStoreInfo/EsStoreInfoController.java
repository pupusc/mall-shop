package com.wanmi.sbc.esStoreInfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.store.StoreProvider;
import com.wanmi.sbc.customer.api.request.store.EsStoreInfoQueryRequest;
import com.wanmi.sbc.customer.api.response.store.EsStoreInfoResponse;
import com.wanmi.sbc.customer.bean.vo.EsStoreInfoVo;
import com.wanmi.sbc.elastic.api.provider.storeInformation.EsStoreInformationProvider;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsInitStoreInfoRequest;
import com.wanmi.sbc.esStoreInfo.request.EsStoreInfoPageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @Author yangzhen
 * @Description //es店铺初始化
 * @Date 19:51 2020/12/9
 * @Param
 * @return
 **/
@Api(tags = "EsStoreInfoController", description = "es店铺初始化 API")
@RestController("EsStoreInfoController")
@RequestMapping("/esStoreInfo")
public class EsStoreInfoController {

    private static final Logger logger = LoggerFactory.getLogger(EsStoreInfoController.class);


    @Autowired
    private StoreProvider storeProvider;

    @Autowired
    private EsStoreInformationProvider esStoreInformationProvider;

    /**
     * 初始化es店铺信息
     *
     * @param pageRequest
     * @return BaseResponse
     */
    @ApiOperation(value = "初始化es店铺信息")
    @RequestMapping(value = "initStoreInfo", method = RequestMethod.POST)
    public BaseResponse<EsStoreInfoResponse> page(@RequestBody @Valid EsStoreInfoPageRequest
                                                                         pageRequest) {
        EsStoreInfoQueryRequest request =new EsStoreInfoQueryRequest();
        request.setPageSize(pageRequest.getPageSize() != null ? pageRequest.getPageSize() : 2000);
        int pageNum = pageRequest.getPageNum();
        while (true){
            logger.info(String.format("初始化店铺信息第[%s]页 ", pageNum));
            request.setPageNum(pageNum);
            BaseResponse<EsStoreInfoResponse> response =  storeProvider.queryEsStoreInfoByPage(request);
            if(Objects.nonNull(response.getContext())){
                List<EsStoreInfoVo> lists = response.getContext().getLists();
                if(CollectionUtils.isNotEmpty(lists)){
                    EsInitStoreInfoRequest esInitStoreInfo = new EsInitStoreInfoRequest();
                    esInitStoreInfo.setLists(lists);
                    esStoreInformationProvider.initStoreInformationList(esInitStoreInfo);
                    pageNum = pageNum + 1;
                }else{
                    break;
                }
            }else{
                break;
            }
        }
//        request.setPageNum(pageRequest.getPageNum());
//        BaseResponse<EsStoreInfoResponse> response =  storeProvider.queryEsStoreInfoByPage(request);

        return BaseResponse.SUCCESSFUL();
    }





    /**
     * 初始化es店铺信息
     *
     * @param pageRequest
     * @return BaseResponse
     */
    @ApiOperation(value = "查看es店铺信息")
    @RequestMapping(value = "queryStoreInfo", method = RequestMethod.POST)
    public BaseResponse<EsStoreInfoResponse> queryStoreInfoPage(@RequestBody @Valid EsStoreInfoPageRequest
                                                          pageRequest) {
        EsStoreInfoQueryRequest request =new EsStoreInfoQueryRequest();
        request.setPageSize(pageRequest.getPageSize() != null ? pageRequest.getPageSize() : 2000);
        request.setPageNum(pageRequest.getPageNum());
        BaseResponse<EsStoreInfoResponse> response =  storeProvider.queryEsStoreInfoByPage(request);

        return BaseResponse.success(response.getContext());
    }
}