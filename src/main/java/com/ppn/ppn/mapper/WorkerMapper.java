package com.ppn.ppn.mapper;

import com.ppn.ppn.dto.WorkerDto;
import com.ppn.ppn.entities.Worker;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorkerMapper {
    WorkerMapper INSTANCE = Mappers.getMapper(WorkerMapper.class);

    WorkerDto workerToWorkerDto(Worker worker);

    Worker workDtoToWorker(WorkerDto workerDto);
}
