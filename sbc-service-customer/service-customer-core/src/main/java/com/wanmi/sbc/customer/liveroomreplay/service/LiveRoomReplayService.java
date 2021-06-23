package com.wanmi.sbc.customer.liveroomreplay.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.customer.liveroomreplay.repository.LiveRoomReplayRepository;
import com.wanmi.sbc.customer.liveroomreplay.model.root.LiveRoomReplay;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayQueryRequest;
import com.wanmi.sbc.customer.bean.vo.LiveRoomReplayVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.List;
import java.util.Optional;

/**
 * <p>直播回放业务逻辑</p>
 * @author zwb
 * @date 2020-06-17 09:24:26
 */
@Service("LiveRoomReplayService")
public class LiveRoomReplayService {
	@Autowired
	private LiveRoomReplayRepository liveRoomReplayRepository;

	/**
	 * 新增直播回放
	 * @author zwb
	 */
	@Transactional
	public LiveRoomReplay add(LiveRoomReplay entity) {
		liveRoomReplayRepository.save(entity);
		return entity;
	}

	/**
	 * 修改直播回放
	 * @author zwb
	 */
	@Transactional
	public LiveRoomReplay modify(LiveRoomReplay entity) {
		Optional<LiveRoomReplay> liveRoomReplay = liveRoomReplayRepository.findByMediaUrlAndDelFlag(entity.getMediaUrl(), entity.getDelFlag());
		//如果为空则存储
		if (!liveRoomReplay.isPresent()){
			liveRoomReplayRepository.save(entity);

		}
		return entity;
	}

	/**
	 * 单个删除直播回放
	 * @author zwb
	 */
	@Transactional
	public void deleteById(LiveRoomReplay entity) {
		liveRoomReplayRepository.save(entity);
	}

	/**
	 * 批量删除直播回放
	 * @author zwb
	 */
	@Transactional
	public void deleteByIdList(List<LiveRoomReplay> infos) {
		liveRoomReplayRepository.saveAll(infos);
	}

	/**
	 * 单个查询直播回放
	 * @author zwb
	 */
	public LiveRoomReplay getOne(Long id){
		return liveRoomReplayRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播回放不存在"));
	}

	/**
	 * 分页查询直播回放
	 * @author zwb
	 */
	public Page<LiveRoomReplay> page(LiveRoomReplayQueryRequest queryReq){
		return liveRoomReplayRepository.findAll(
				LiveRoomReplayWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询直播回放
	 * @author zwb
	 */
	public List<LiveRoomReplay> list(LiveRoomReplayQueryRequest queryReq){
		return liveRoomReplayRepository.findAll(LiveRoomReplayWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author zwb
	 */
	public LiveRoomReplayVO wrapperVo(LiveRoomReplay liveRoomReplay) {
		if (liveRoomReplay != null){
			LiveRoomReplayVO liveRoomReplayVO = KsBeanUtil.convert(liveRoomReplay, LiveRoomReplayVO.class);
			return liveRoomReplayVO;
		}
		return null;
	}

	public List<LiveRoomReplay> getByRoomId(Long roomId) {

		return liveRoomReplayRepository.findByRoomIdAndDelFlag(roomId,DeleteFlag.NO);
	}
}

