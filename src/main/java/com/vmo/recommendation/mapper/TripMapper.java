package com.vmo.recommendation.mapper;

import com.vmo.recommendation.controller.request.TripCreateRequest;
import com.vmo.recommendation.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TripMapper {
    TripMapper INSTANCE = Mappers.getMapper(TripMapper.class);

    Trip toEntity(TripCreateRequest request);
//    UserDetailResponse toDetail(UserEntity userEntity);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateEntity(UserUpdateRequest request, @MappingTarget UserEntity userEntity);
//
}
