package com.wanmi.sbc.message.smssend.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.constant.SendDataConstants;
import com.wanmi.sbc.message.api.request.smssend.SmsSendQueryRequest;
import com.wanmi.sbc.message.bean.enums.SendStatus;
import com.wanmi.sbc.message.bean.enums.SendType;
import com.wanmi.sbc.message.bean.vo.SmsSendVO;
import com.wanmi.sbc.message.smssend.model.root.SmsSend;
import com.wanmi.sbc.message.smssend.repository.SmsSendRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.data.domain.Page;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>短信发送业务逻辑</p>
 * @author zgl
 * @date 2019-12-03 15:36:05
 */
@Service("SmsSendService")
public class SmsSendService {
	@Autowired
	private SmsSendRepository smsSendRepository;
	@Autowired
	private SmsSendTaskService smsSendTaskService;
	/*@Autowired
	private SmsSendDataRequestSink smsSendDataRequestSink;*/

    @Autowired
    private BinderAwareChannelResolver resolver;

	/** 
	 * 新增短信发送
	 * @author zgl
	 */
	@Transactional
	public SmsSend add(SmsSend entity) {
		if(entity.getSendType()==SendType.NOW){
			entity.setStatus(SendStatus.BEGIN);
			smsSendRepository.save(entity);
//			this.smsSendTaskService.send(entity);
            resolver.resolveDestination(SendDataConstants.Q_SMS_SEND_OUTPUT).send(new GenericMessage<>(JSONObject.toJSONString(entity)));
			//smsSendDataRequestSink.output().send(new GenericMessage<>(JSONObject.toJSONString(entity)));
			return entity;
		}else{
			entity.setStatus(SendStatus.NO_BEGIN);
			smsSendRepository.save(entity);
		}
		return entity;
	}
	
	/** 
	 * 修改短信发送
	 * @author zgl
	 */
	@Transactional
	public SmsSend modify(SmsSend entity) {
		if(entity.getSendType()==SendType.NOW){
			entity.setStatus(SendStatus.BEGIN);
			smsSendRepository.save(entity);
//			this.smsSendTaskService.send(entity);
//			smsSendDataRequestSink.output().send(new GenericMessage<>(JSONObject.toJSONString(entity)));
            resolver.resolveDestination(SendDataConstants.Q_SMS_SEND_OUTPUT).send(new GenericMessage<>(JSONObject.toJSONString(entity)));
			return entity;
		}else{
			entity.setStatus(SendStatus.NO_BEGIN);
			smsSendRepository.save(entity);
		}
		return entity;
	}

    /**
     * 保存
     * @param smsSend
     */
    @Transactional
	public void save(SmsSend smsSend){
	    if(smsSend.getId()!= null) {
            SmsSend oldSmsSend = getById(smsSend.getId());
            if(Objects.nonNull(oldSmsSend)) {
                KsBeanUtil.copyPropertiesThird(smsSend, oldSmsSend);
                smsSendRepository.save(smsSend);
            }
            return;
        }
        smsSendRepository.save(smsSend);
    }

	/**
	 * 单个删除短信发送
	 * @author zgl
	 */
	@Transactional
	public void deleteById(Long id) {
		smsSendRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除短信发送
	 * @author zgl
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		ids.forEach(id -> smsSendRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询短信发送
	 * @author zgl
	 */
	public SmsSend getById(Long id){
		return smsSendRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询短信发送
	 * @author zgl
	 */
	public Page<SmsSend> page(SmsSendQueryRequest queryReq){
		return smsSendRepository.findAll(
				SmsSendWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询短信发送
	 * @author zgl
	 */
	public List<SmsSend> list(SmsSendQueryRequest queryReq){
		return smsSendRepository.findAll(
				SmsSendWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

    /**
     * 统计查询短信发送
     * @author zgl
     */
    public Long count(SmsSendQueryRequest queryReq){
        return smsSendRepository.count(SmsSendWhereCriteriaBuilder.build(queryReq));
    }

	/**
	 * 将实体包装成VO
	 * @author zgl
	 */
	public SmsSendVO wrapperVo(SmsSend smsSend) {
		if (smsSend != null){
			SmsSendVO smsSendVO=new SmsSendVO();
			KsBeanUtil.copyPropertiesThird(smsSend,smsSendVO);
			return smsSendVO;
		}
		return null;
	}

	/**
	 * 扫描定时发送的数据
	 */
	public void scanSendTask(){
		List<SmsSend> list = this.smsSendRepository.findAll(
				SmsSendWhereCriteriaBuilder
						.build(SmsSendQueryRequest
								.builder()
								.sendType(SendType.DELAY)
								.status(SendStatus.NO_BEGIN)
								.sendTimeEnd(LocalDateTime.now().minusMinutes(-5))
								.build()
						)
		);
		if(CollectionUtils.isNotEmpty(list)){
			for (SmsSend smsSend : list) {
                resolver.resolveDestination(SendDataConstants.Q_SMS_SEND_OUTPUT).send(new GenericMessage<>(JSONObject.toJSONString(smsSend)));
				smsSend.setStatus(SendStatus.BEGIN);
				smsSendRepository.save(smsSend);
			}
		}
	}
}
