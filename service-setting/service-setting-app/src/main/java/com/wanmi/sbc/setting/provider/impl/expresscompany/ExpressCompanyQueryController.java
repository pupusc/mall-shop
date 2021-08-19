package com.wanmi.sbc.setting.provider.impl.expresscompany;

import com.aliyun.oss.common.utils.IOUtils;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.expresscompany.ExpressCompanyQueryProvider;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyPageRequest;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyQueryRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyPageResponse;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyListRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyListResponse;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyByIdRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyByIdResponse;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import com.wanmi.sbc.setting.expresscompany.service.ExpressCompanyService;
import com.wanmi.sbc.setting.expresscompany.model.root.ExpressCompany;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>物流公司查询服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@RestController
@Validated
public class ExpressCompanyQueryController implements ExpressCompanyQueryProvider {
	@Autowired
	private ExpressCompanyService expressCompanyService;

	@Override
	public BaseResponse<ExpressCompanyPageResponse> page(@RequestBody @Valid ExpressCompanyPageRequest expressCompanyPageReq) {
		ExpressCompanyQueryRequest queryReq = new ExpressCompanyQueryRequest();
		KsBeanUtil.copyPropertiesThird(expressCompanyPageReq, queryReq);
		Page<ExpressCompany> expressCompanyPage = expressCompanyService.page(queryReq);
		Page<ExpressCompanyVO> newPage = expressCompanyPage.map(entity -> expressCompanyService.wrapperVo(entity));
		MicroServicePage<ExpressCompanyVO> microPage = new MicroServicePage<>(newPage, expressCompanyPageReq.getPageable());
		ExpressCompanyPageResponse finalRes = new ExpressCompanyPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<ExpressCompanyListResponse> list() {
		List<ExpressCompany> expressCompanyList = expressCompanyService.list();
		if(CollectionUtils.isNotEmpty(expressCompanyList)){
			List<ExpressCompanyVO> newList = expressCompanyList.stream().map(entity -> expressCompanyService.wrapperVo(entity)).collect(Collectors.toList());
			return BaseResponse.success(new ExpressCompanyListResponse(newList));
		}
		//为空，则插入默认的50条
		try{
			ClassPathResource resource = new ClassPathResource("express-com");
			String expressCom = IOUtils.readStreamAsString( resource.getInputStream(), "UTF-8");
			List<ExpressCompany> expressCompanyList1 = Arrays.stream(expressCom.split("\\n")).map(express -> {
				String[] expr = express.split(",");
				return ExpressCompany.builder()
						.expressName( expr[0])
						.expressCode( expr[1])
						.isAdd(0)
						.isChecked(0)
						.delFlag(DeleteFlag.NO)
						.build();
			}).collect(Collectors.toList());
			//批量存储
			List<ExpressCompanyVO> expressCompanyVOS = KsBeanUtil.convert(expressCompanyService.addBatch(expressCompanyList1), ExpressCompanyVO.class);
			return BaseResponse.success(new ExpressCompanyListResponse(expressCompanyVOS));
		}catch(IOException e){
			throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
		}
	}

	@Override
	public BaseResponse<ExpressCompanyListResponse>listByIds(@RequestBody @Valid ExpressCompanyListRequest expressCompanyListRequest){
		List<ExpressCompany> expressCompanyList = expressCompanyService.listByIds(expressCompanyListRequest.getExpressCompanyIdList());
		if(CollectionUtils.isNotEmpty(expressCompanyList)){
			List<ExpressCompanyVO> newList = expressCompanyList.stream().map(entity -> expressCompanyService.wrapperVo(entity)).collect(Collectors.toList());
			return BaseResponse.success(new ExpressCompanyListResponse(newList));
		}
		return BaseResponse.success(new ExpressCompanyListResponse(Collections.singletonList(new ExpressCompanyVO())));
	}

	@Override
	public BaseResponse<ExpressCompanyByIdResponse> getById(@RequestBody @Valid ExpressCompanyByIdRequest expressCompanyByIdRequest) {
		ExpressCompany expressCompany = expressCompanyService.getById(expressCompanyByIdRequest.getExpressCompanyId());
		return BaseResponse.success(new ExpressCompanyByIdResponse(expressCompanyService.wrapperVo(expressCompany)));
	}

}

