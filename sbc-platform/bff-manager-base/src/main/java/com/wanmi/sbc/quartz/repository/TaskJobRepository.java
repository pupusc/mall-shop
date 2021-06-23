package com.wanmi.sbc.quartz.repository;


import com.wanmi.sbc.quartz.model.entity.TaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Geek Wang
 * @createDate: 2019/8/5 19:24
 * @version: 1.0
 */
@Repository
public interface TaskJobRepository extends JpaRepository<TaskInfo, Long>, JpaSpecificationExecutor<TaskInfo> {

	/**
	 * 根据业务ID查询任务信息
	 * @param bizId
	 * @return
	 */
	TaskInfo findByBizId(String bizId);

	/**
	 * 查询所有自动运行&未结束的任务列表
	 * @return
	 */
	@Query(" from TaskInfo t  where t.autoRun = 1 and t.state != 2 ")
	List<TaskInfo> findAllAutoRunAndState();

}
