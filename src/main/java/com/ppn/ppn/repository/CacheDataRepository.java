package com.ppn.ppn.repository;

import com.ppn.ppn.entities.CacheData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CacheDataRepository extends CrudRepository<CacheData, String> {
    List<CacheData> findByKeyContainsIgnoreCase(String keyword);
}
