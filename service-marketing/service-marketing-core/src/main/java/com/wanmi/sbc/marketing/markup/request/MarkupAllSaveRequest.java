package com.wanmi.sbc.marketing.markup.request;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.common.request.MarketingSaveRequest;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevel;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevelDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>加价购活动新增参数</p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkupAllSaveRequest extends MarketingSaveRequest {
  private static final long serialVersionUID = 1L;

  /**
   * 加价购活动阶梯
   */
  @ApiModelProperty(value = "活动名称")
  private List<MarkupLevel> markupLevelList;

  /**
   *  生成相应活动阶梯
   * @param markupId
   * @return
   */
  public List<MarkupLevel> generateMarkupLevelList(Long markupId) {
      if (CollectionUtils.isEmpty(markupLevelList)) {
          throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
      }
    return markupLevelList.stream().map((level) -> {
      level.setId(null);
      level.setMarkupId(markupId);
      return level;
    }).collect(Collectors.toList());
  }

  public List<MarkupLevelDetail> generateMarkupLeveDetailList(List<MarkupLevel> markupLevelList) {
      if (CollectionUtils.isEmpty(markupLevelList)) {
          throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
      }
    return markupLevelList.stream().map((level) ->
            level.getMarkupLevelDetailList().stream().map((detail) -> {
              detail.setMarkupId(level.getMarkupId());
              detail.setMarkupLevelId(level.getId());
              return detail;
            })).flatMap((detail) -> detail).collect(Collectors.toList());
  }
}