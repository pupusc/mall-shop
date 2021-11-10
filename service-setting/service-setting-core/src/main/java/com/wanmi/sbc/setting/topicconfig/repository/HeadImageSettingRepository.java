package com.wanmi.sbc.setting.topicconfig.repository;

import com.wanmi.sbc.setting.topicconfig.model.root.HeadImageSetting;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface HeadImageSettingRepository extends JpaRepository<HeadImageSetting, Integer>,
        JpaSpecificationExecutor<HeadImageSetting> {
}