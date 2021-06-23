package com.wanmi.sbc.setting.config;

import com.wanmi.sbc.setting.api.response.TradeConfigGetByTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ConfigMapper {

    @Mappings({})
    TradeConfigGetByTypeResponse ConfigToTradeConfigGetByTypeResponse(Config config);
}
