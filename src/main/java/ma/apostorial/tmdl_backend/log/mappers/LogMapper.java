package ma.apostorial.tmdl_backend.log.mappers;

import org.mapstruct.Mapper;

import ma.apostorial.tmdl_backend.log.dtos.LogResponse;
import ma.apostorial.tmdl_backend.log.entities.Log;

@Mapper(componentModel = "spring")
public interface LogMapper {
    LogResponse fromEntityTResponse(Log entity);
}
