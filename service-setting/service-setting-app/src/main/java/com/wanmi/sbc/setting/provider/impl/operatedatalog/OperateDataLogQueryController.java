package com.wanmi.sbc.setting.provider.impl.operatedatalog;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.operatedatalog.OperateDataLogQueryProvider;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogPageRequest;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogQueryRequest;
import com.wanmi.sbc.setting.api.response.operatedatalog.OperateDataLogPageResponse;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogListRequest;
import com.wanmi.sbc.setting.api.response.operatedatalog.OperateDataLogListResponse;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogByIdRequest;
import com.wanmi.sbc.setting.api.response.operatedatalog.OperateDataLogByIdResponse;
import com.wanmi.sbc.setting.bean.vo.OperateDataLogVO;
import com.wanmi.sbc.setting.operatedatalog.service.OperateDataLogService;
import com.wanmi.sbc.setting.operatedatalog.model.root.OperateDataLog;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>系统日志查询服务接口实现</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@RestController
@Validated
public class OperateDataLogQueryController implements OperateDataLogQueryProvider {
	@Autowired
	private OperateDataLogService operateDataLogService;

	@Override
	public BaseResponse<OperateDataLogPageResponse> page(@RequestBody @Valid OperateDataLogPageRequest operateDataLogPageReq) {
		OperateDataLogQueryRequest queryReq = new OperateDataLogQueryRequest();
		KsBeanUtil.copyPropertiesThird(operateDataLogPageReq, queryReq);
		Page<OperateDataLog> operateDataLogPage = operateDataLogService.page(queryReq);
		Page<OperateDataLogVO> newPage = operateDataLogPage.map(entity -> operateDataLogService.wrapperVo(entity));
		MicroServicePage<OperateDataLogVO> microPage = new MicroServicePage<>(newPage, operateDataLogPageReq.getPageable());
		OperateDataLogPageResponse finalRes = new OperateDataLogPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<OperateDataLogListResponse> list(@RequestBody @Valid OperateDataLogListRequest operateDataLogListReq) {
		OperateDataLogQueryRequest queryReq = new OperateDataLogQueryRequest();
		KsBeanUtil.copyPropertiesThird(operateDataLogListReq, queryReq);
		List<OperateDataLog> operateDataLogList = operateDataLogService.list(queryReq);
		List<OperateDataLogVO> newList = operateDataLogList.stream().map(entity -> operateDataLogService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new OperateDataLogListResponse(newList));
	}

	@Override
	public BaseResponse<OperateDataLogByIdResponse> getById(@RequestBody @Valid OperateDataLogByIdRequest operateDataLogByIdRequest) {
		OperateDataLog operateDataLog = operateDataLogService.getById(operateDataLogByIdRequest.getId());
		return BaseResponse.success(new OperateDataLogByIdResponse(operateDataLogService.wrapperVo(operateDataLog)));
	}

}

