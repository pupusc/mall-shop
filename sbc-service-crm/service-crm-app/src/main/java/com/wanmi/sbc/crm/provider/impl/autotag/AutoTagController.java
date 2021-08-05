package com.wanmi.sbc.crm.provider.impl.autotag;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.constant.CustomerTagErrorCode;
import com.wanmi.sbc.crm.api.provider.autotag.AutoTagProvider;
import com.wanmi.sbc.crm.api.request.autotag.*;
import com.wanmi.sbc.crm.api.response.autotag.AutoTagAddResponse;
import com.wanmi.sbc.crm.api.response.autotag.AutoTagModifyResponse;
import com.wanmi.sbc.crm.autotag.model.root.AutoTag;
import com.wanmi.sbc.crm.autotag.service.AutoTagService;
import com.wanmi.sbc.crm.autotagsql.model.root.AutoTagSql;
import com.wanmi.sbc.crm.autotagsql.service.AutoTagSqlService;
import com.wanmi.sbc.crm.autotagstatistics.*;
import com.wanmi.sbc.crm.bean.dto.AutoTagSelectDTO;
import com.wanmi.sbc.crm.bean.dto.AutoTagSelectValuesDTO;
import com.wanmi.sbc.crm.bean.enums.*;
import com.wanmi.sbc.crm.customgroup.service.CustomGroupService;
import com.wanmi.sbc.crm.tagdimension.model.root.TagDimension;
import com.wanmi.sbc.crm.tagdimension.service.TagDimensionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>自动标签保存服务接口实现</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@RestController
@Validated
@Slf4j
public class AutoTagController implements AutoTagProvider {
	@Autowired
	private AutoTagService autoTagService;

	@Autowired
	private CustomGroupService customGroupService;

	@Autowired
	private TagDimensionService tagDimensionService;

	@Autowired
	private SqlActionChoose sqlActionChoose;

	@Autowired
	private MultipleTagSql multipleTagSql;

	@Autowired
	private AutoTagSqlService autoTagSqlService;

	@Override
	@Transactional
	public BaseResponse<AutoTagAddResponse> add(@RequestBody @Valid AutoTagAddRequest autoTagAddRequest) {
		AutoTag autoTag = KsBeanUtil.convert(autoTagAddRequest, AutoTag.class);
		autoTag.setSystemFlag(Boolean.FALSE);
		List<AutoTag> tagList = autoTagService.list(AutoTagQueryRequest.builder()
				.checkTagName(autoTagAddRequest.getTagName())
				.delFlag(DeleteFlag.NO)
				.build());
		if(CollectionUtils.isNotEmpty(tagList)){
			throw new SbcRuntimeException(CustomerTagErrorCode.TagName_HAS_EXISTS);
		}
		long count = autoTagService.getCount();
		if (count >= 200){
			throw new SbcRuntimeException(CustomerTagErrorCode.TAG_CREATE_MAX);
		}
		autoTag = autoTagService.add(autoTag);
		fillRuleJson(autoTag, autoTagAddRequest);
		return BaseResponse.success(new AutoTagAddResponse(autoTagService.wrapperVo(autoTagService.modify(autoTag))));
	}

	@Override
	@Transactional
	public BaseResponse<AutoTagModifyResponse> modify(@RequestBody @Valid AutoTagModifyRequest autoTagModifyRequest) {
		AutoTag autoTag = autoTagService.getOne(autoTagModifyRequest.getId());
		KsBeanUtil.copyPropertiesThird(autoTagModifyRequest, autoTag);
		AutoTagAddRequest autoTagAddRequest = KsBeanUtil.convert(autoTagModifyRequest, AutoTagAddRequest.class);
		fillRuleJson(autoTag, autoTagAddRequest);
		List<AutoTag> tagList = autoTagService.list(AutoTagQueryRequest.builder()
				.checkTagName(autoTagAddRequest.getTagName())
				.delFlag(DeleteFlag.NO)
				.build());
		if(CollectionUtils.isNotEmpty(tagList)){
			if(tagList.get(0).getId() != autoTag.getId() || tagList.size()>1){
				throw new SbcRuntimeException(CustomerTagErrorCode.TagName_HAS_EXISTS);
			}
		}
		return BaseResponse.success(new AutoTagModifyResponse(
				autoTagService.wrapperVo(autoTagService.modify(autoTag))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid AutoTagDelByIdRequest autoTagDelByIdRequest) {
        AutoTag autoTag = autoTagService.getOne(autoTagDelByIdRequest.getId());
        if (TagType.PREFERENCE.equals(autoTag.getType())
                && customGroupService.countByPreferenceTags(autoTagDelByIdRequest.getId()) > 0) {
            //偏好类的标签引用人群验证
            throw new SbcRuntimeException(CustomerTagErrorCode.TAG_EXISTS_CUSTOM_GROUP);
        } else if ((!TagType.PREFERENCE.equals(autoTag.getType()))
                && customGroupService.countByAutoTags(autoTagDelByIdRequest.getId()) > 0) {
            //非偏好类的标签引用人群验证
            throw new SbcRuntimeException(CustomerTagErrorCode.TAG_EXISTS_CUSTOM_GROUP);
        }
	    autoTagService.deleteById(autoTagDelByIdRequest.getId());
		autoTagSqlService.delAutoSql(autoTagDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid AutoTagDelByIdListRequest autoTagDelByIdListRequest) {
		autoTagService.deleteByIdList(autoTagDelByIdListRequest.getIdList());
		autoTagSqlService.delAutoSqls(autoTagDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
    public BaseResponse init(@RequestBody @Valid AutoTagInitRequest autoTagInitRequest) {
        List<AutoTag> autoTags = autoTagService.init(autoTagInitRequest.getTagIds(),
				autoTagInitRequest.getCreatePerson());
		autoTags.forEach(autoTag -> {
			String json = autoTag.getRuleJsonSql();
			StatisticsTagInfo info = JSON.parseObject(json, StatisticsTagInfo.class);
			generateSql(info);
		});
        return BaseResponse.SUCCESSFUL();
    }

	@Override
	public BaseResponse getSql(@Valid AutoTagPageRequest request) {
		AutoTagQueryRequest queryReq = KsBeanUtil.convert(request, AutoTagQueryRequest.class);
		if (CollectionUtils.isNotEmpty(request.getIdList())){
			List<AutoTag> autoTags = autoTagService.list(queryReq);
			autoTags.forEach(autoTag -> {
				String json = autoTag.getRuleJsonSql();
				StatisticsTagInfo info = JSON.parseObject(json, StatisticsTagInfo.class);
				generateSql(info);
			});
		} else {
			int nowPage = 0,totalPage = 0;
			do {
				queryReq.setPageNum(nowPage);
				Page<AutoTag> autoTagPage =  autoTagService.page(queryReq);
				totalPage = autoTagPage.getTotalPages();
				nowPage += 1;
				List<AutoTag> autoTags = autoTagPage.getContent();
				autoTags.forEach(autoTag -> {
					String json = autoTag.getRuleJsonSql();
					StatisticsTagInfo info = JSON.parseObject(json, StatisticsTagInfo.class);
					log.info("===========================================:{}", info.getTagId());
					try {
						generateSql(info);
					} catch (Exception e){
						log.error("定时tagID:{}异常", info.getTagId());
					}
				});
			} while (nowPage < totalPage);
		}
		return BaseResponse.SUCCESSFUL();
	}

    /**
     * 填充json 和 sql_json
     * 将指标值范围首部规则是结果又是条件的情况，将复制移到RuleParamList条件类参数列表做为条件一部分
     * @param autoTag
     * @param autoTagAddRequest
     */
    private void fillRuleJson(AutoTag autoTag, AutoTagAddRequest autoTagAddRequest) {
        //转json
        autoTag.setRuleJson(JSON.toJSONString(autoTagAddRequest));
		StatisticsTagInfo statisticsTagInfo = new StatisticsTagInfo();
		statisticsTagInfo.setTagId(autoTag.getId());
		statisticsTagInfo.setTagName(autoTagAddRequest.getTagName());
		statisticsTagInfo.setTagType(autoTagAddRequest.getType());
		statisticsTagInfo.setRelationType(autoTagAddRequest.getRelationType());
		statisticsTagInfo.setDayNum(autoTagAddRequest.getDay());

		List<Long> selectIds = autoTagAddRequest.getAutoTagSelectMap().values().stream()
				.filter(autoTagSelectDTO -> autoTagSelectDTO.getSelectedId() != null)
				.map(AutoTagSelectDTO::getSelectedId).collect(Collectors.toList());
		Map<Long, TagDimension> tagDimensionMap =
				tagDimensionService.listByIds(selectIds).stream().collect(Collectors.toMap(TagDimension::getId,
				Function.identity()));
		List<StatisticsDimensionInfo> dimensionInfoList = new ArrayList<>();
		autoTagAddRequest.getAutoTagSelectMap().values().forEach(autoTagSelectDTO -> {
			StatisticsDimensionInfo dimensionInfo = new StatisticsDimensionInfo();
			TagDimension tagDimension = tagDimensionMap.get(autoTagSelectDTO.getSelectedId());
			dimensionInfo.setDimensionName(tagDimension.getDimensionName());
			dimensionInfo.setDimensionType(tagDimension.getFirstLastType());
			dimensionInfo.setRelationType(autoTagSelectDTO.getRelationType() == null ?
					RelationType.AND : autoTagSelectDTO.getRelationType());

			List<AutoTagSelectValuesDTO> selectValuesDTOs =
					new ArrayList<>(autoTagSelectDTO.getAutoTagSelectValues().values());
			List<StatisticsTagParamInfo> paramInfoList = new ArrayList<>();
			List<TagParamColumn> tagParamColumnList = Arrays.asList(TagParamColumn.NUM, TagParamColumn.DAY_NUM,
					TagParamColumn.TO_DATE_NUM, TagParamColumn.MONEY, TagParamColumn.SHARE_GOODS_SALE_NUM,
					TagParamColumn.SHARE_GOODS_COMMISSION_NUM, TagParamColumn.INVITE_NEW_NUM,
					TagParamColumn.EFFECTIVE_INVITE_NEW_NUM, TagParamColumn.INVITE_NEW_REWARD);

			if (autoTag.getType().equals(TagType.PREFERENCE)){
				StatisticsTagParamInfo resultParamInfo = new StatisticsTagParamInfo();
				AutoTagSelectValuesDTO selectValuesDTO = selectValuesDTOs.get(0);
				resultParamInfo.setParamName(TagParamColumn.valueOf(selectValuesDTO.getColumnName().toUpperCase()));
				resultParamInfo.setDataRange(autoTagAddRequest.getDataRange());
				resultParamInfo.setParamValue(selectValuesDTO.getValue().get(0));
				dimensionInfo.setParamResult(resultParamInfo);
			}

			for (int i = 0; i< selectValuesDTOs.size(); i++){
				// 偏好类标签ParamResult参数在上面做处理。
				if (autoTag.getType().equals(TagType.PREFERENCE) && i == 0) continue;
				AutoTagSelectValuesDTO selectValuesDTO = selectValuesDTOs.get(i);
				StatisticsTagParamInfo paramInfo = new StatisticsTagParamInfo();
				TagParamColumn tagParamColumn = TagParamColumn.valueOf(selectValuesDTO.getColumnName().toUpperCase());
				paramInfo.setParamName(tagParamColumn);
				paramInfo.setDataRange(autoTagAddRequest.getDataRange());
				if (TagParamColumn.TERMINAL_SOURCE.equals(tagParamColumn)){
					List<Integer> terminalSources = new ArrayList<>();
					selectValuesDTO.getValue().forEach(s -> {
						if (TerminalSourceType.APP.toValue().equals(s)){
							terminalSources.add(TerminalSource.APP.toValue());
						} else if (TerminalSourceType.H5.toValue().equals(s)){
							terminalSources.add(TerminalSource.H5.toValue());
						} else if (TerminalSourceType.PC.toValue().equals(s)){
							terminalSources.add(TerminalSource.PC.toValue());
						} else if (TerminalSourceType.WEIXIN.toValue().equals(s)){
							terminalSources.add(TerminalSource.MINIPROGRAM.toValue());
						}
					});
					paramInfo.setParamValue(Joiner.on(",").join(terminalSources));
				} else if (TagParamColumn.DATE.equals(tagParamColumn)){
					List<Integer> terminalSources = new ArrayList<>();
					selectValuesDTO.getValue().forEach(s -> {
						if (DateType.MONDAY.toValue().equals(s)){
							terminalSources.add(0);
						} else if (DateType.TUESDAY.toValue().equals(s)){
							terminalSources.add(1);
						} else if (DateType.WEDNESDAY.toValue().equals(s)){
							terminalSources.add(2);
						} else if (DateType.THURSDAY.toValue().equals(s)){
							terminalSources.add(3);
						} else if (DateType.FRIDAY.toValue().equals(s)){
							terminalSources.add(4);
						} else if (DateType.SATURDAY.toValue().equals(s)){
							terminalSources.add(5);
						} else if (DateType.SUNDAY.toValue().equals(s)){
							terminalSources.add(6);
						}
					});
					paramInfo.setParamValue(Joiner.on(",").join(terminalSources));
				} else if (tagParamColumnList.contains(tagParamColumn)){
					List<String> strList = selectValuesDTO.getValue();
					List<String> items = new ArrayList<>();
					for (int j = 0;j< strList.size(); j++){
						String str = strList.get(j);
						if (str != null){
							if (j == 0){
								items.add(">=".concat(str));
							} else {
								items.add("<=".concat(str));
							}
						}
					}
					paramInfo.setParamValue(Joiner.on(",").join(items));
				} else {
					paramInfo.setParamValue(Joiner.on(",").join(selectValuesDTO.getValue()));
				}

				if (i != 0){
					paramInfoList.add(paramInfo);
				} else {
					dimensionInfo.setParamResult(paramInfo);
				}
			}
			dimensionInfo.setParamInfoList(paramInfoList);
			dimensionInfoList.add(dimensionInfo);
		});
		statisticsTagInfo.setDimensionInfoList(dimensionInfoList);
        autoTag.setRuleJsonSql(JSON.toJSONString(statisticsTagInfo));
		generateSql(statisticsTagInfo);
    }

    private void generateSql(StatisticsTagInfo statisticsTagInfo){
		String sql = "";
		String bigDataSql = "";
		if (TagType.MULTIPLE.equals(statisticsTagInfo.getTagType())){
			sql = multipleTagSql.getSql(statisticsTagInfo);
			statisticsTagInfo.setBigData(true);
			bigDataSql = multipleTagSql.getSql(statisticsTagInfo);
		} else {
			DimensionName dimensionName = statisticsTagInfo.getDimensionInfoList().get(0).getDimensionName();
			SqlTool tool = sqlActionChoose.choose(dimensionName);
			sql = tool.getSql(statisticsTagInfo);

			statisticsTagInfo.setBigData(true);
			bigDataSql = tool.getSql(statisticsTagInfo);

		}

		// 大数据weekday替换
		Pattern pattern = Pattern.compile("WEEKDAY\\(.*?\\)");
		Matcher matcher = pattern.matcher(bigDataSql);
		while (matcher.find()){
			log.info("大数据weekday替换");
			String weekDay = matcher.group().trim();
			log.info("替换字段：{}", weekDay);
			String subStr = weekDay.substring(8, weekDay.length()-1);
			bigDataSql = bigDataSql.replace(weekDay, "pmod(datediff("+subStr+",'2018-01-01'),7)");
		}
		bigDataSql = bigDataSql.replaceAll("NOW\\(\\)", "date_format\\(CURRENT_TIMESTAMP,'yyyy-MM-dd HH:mm:ss'\\)");
		bigDataSql = bigDataSql.replaceAll("%Y-%m-%d", "yyyy-MM-dd");
		bigDataSql = bigDataSql.replaceAll("interval", "").replaceAll("day\\)", ")");
		AutoTagSql autoTagSql = new AutoTagSql();
		autoTagSql.setAutoTagId(statisticsTagInfo.getTagId());
		autoTagSql.setDelFlag(DeleteFlag.NO);
		autoTagSql.setSqlStr(sql);
		autoTagSql.setBigDataSqlStr(bigDataSql);
		autoTagSql.setType(statisticsTagInfo.getTagType());
		autoTagSqlService.add(autoTagSql);
	}
}

