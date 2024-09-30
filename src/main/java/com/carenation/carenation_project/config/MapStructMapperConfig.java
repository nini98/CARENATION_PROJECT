package com.carenation.carenation_project.config;

import org.mapstruct.Builder;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
    componentModel = "spring",
    nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    builder = @Builder(disableBuilder = false),
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MapStructMapperConfig {
}
