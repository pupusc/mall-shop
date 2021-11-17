package com.wanmi.sbc.setting.atmosphere.repository;

import com.wanmi.sbc.setting.atmosphere.model.root.Atmosphere;
import com.wanmi.sbc.setting.topicconfig.model.root.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AtmosphereRepository extends JpaRepository<Atmosphere, Integer>,
        JpaSpecificationExecutor<Atmosphere> {


}