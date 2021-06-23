package com.wanmi.sbc.exceptionoftradepoints;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.order.api.provider.exceptionoftradepoints.ExceptionOfTradePointsQueryProvider;
import com.wanmi.sbc.order.api.provider.exceptionoftradepoints.ExceptionOfTradePointsProvider;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.*;
import com.wanmi.sbc.order.api.response.exceptionoftradepoints.*;
import javax.validation.Valid;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import java.time.LocalDateTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "积分订单抵扣异常表管理API", tags = "ExceptionOfTradePointsController")
@RestController
@RequestMapping(value = "/exceptionoftradepoints")
public class ExceptionOfTradePointsController {

    @Autowired
    private ExceptionOfTradePointsQueryProvider exceptionOfTradePointsQueryProvider;

    @Autowired
    private ExceptionOfTradePointsProvider exceptionOfTradePointsProvider;

    @ApiOperation(value = "分页查询积分订单抵扣异常表")
    @PostMapping("/page")
    public BaseResponse<ExceptionOfTradePointsPageResponse> getPage(@RequestBody @Valid ExceptionOfTradePointsPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        return exceptionOfTradePointsQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询积分订单抵扣异常表")
    @PostMapping("/list")
    public BaseResponse<ExceptionOfTradePointsListResponse> getList(@RequestBody @Valid ExceptionOfTradePointsListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        return exceptionOfTradePointsQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询积分订单抵扣异常表")
    @GetMapping("/{id}")
    public BaseResponse<ExceptionOfTradePointsByIdResponse> getById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        ExceptionOfTradePointsByIdRequest idReq = new ExceptionOfTradePointsByIdRequest();
        idReq.setId(id);
        return exceptionOfTradePointsQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增积分订单抵扣异常表")
    @PostMapping("/add")
    public BaseResponse<ExceptionOfTradePointsAddResponse> add(@RequestBody @Valid ExceptionOfTradePointsAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCreateTime(LocalDateTime.now());
        return exceptionOfTradePointsProvider.add(addReq);
    }

    @ApiOperation(value = "修改积分订单抵扣异常表")
    @PutMapping("/modify")
    public BaseResponse<ExceptionOfTradePointsModifyResponse> modify(@RequestBody @Valid ExceptionOfTradePointsModifyRequest modifyReq) {
        return exceptionOfTradePointsProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除积分订单抵扣异常表")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        ExceptionOfTradePointsDelByIdRequest delByIdReq = new ExceptionOfTradePointsDelByIdRequest();
        delByIdReq.setId(id);
        return exceptionOfTradePointsProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除积分订单抵扣异常表")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid ExceptionOfTradePointsDelByIdListRequest delByIdListReq) {
        return exceptionOfTradePointsProvider.deleteByIdList(delByIdListReq);
    }

}
