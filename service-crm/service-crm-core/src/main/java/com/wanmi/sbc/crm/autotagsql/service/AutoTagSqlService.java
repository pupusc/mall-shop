package com.wanmi.sbc.crm.autotagsql.service;

import com.wanmi.sbc.crm.autotagsql.model.root.AutoTagSql;
import com.wanmi.sbc.crm.autotagsql.repository.AutoTagSqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service("AutoTagSqlService")
public class AutoTagSqlService {
	@Autowired
	private AutoTagSqlRepository autoTagSqlRepository;

	/**
	 * 新增标签参数
	 * @author dyt
	 */
	@Transactional
	public AutoTagSql add(AutoTagSql entity) {
		AutoTagSql autoTagSql =autoTagSqlRepository.findByAutoTagIdAndDelFlag(entity.getAutoTagId(),
				entity.getDelFlag());
		if (Objects.nonNull(autoTagSql)){
			entity.setId(autoTagSql.getId());
		}
		autoTagSqlRepository.save(entity);
		return entity;
	}

	/**
	 * 修改标签参数
	 * @author dyt
	 */
	@Transactional
	public AutoTagSql modify(AutoTagSql entity) {
		autoTagSqlRepository.save(entity);
		return entity;
	}

	/**
	 * @Author lvzhenwei
	 * @Description 刪除标签sql
	 * @Date 17:54 2020/10/12
	 * @Param [autoTagId]
	 * @return void
	 **/
	@Transactional
	public void delAutoSql(Long autoTagId){
		autoTagSqlRepository.deleteAutoTagSqlByAutoTagId(autoTagId);
	}

	@Transactional
	public void delAutoSqls(List<Long> autoTagId){
		autoTagSqlRepository.deleteAutoTagSqlsByAutoTagIdIn(autoTagId);
	}
}

