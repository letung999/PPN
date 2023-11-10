package com.ppn.ppn.repository;

import com.ppn.ppn.entities.CacheData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


import java.util.List;

public interface CacheDataRepository extends CrudRepository<CacheData, String> {
    List<CacheData> findByKeyContainsIgnoreCase(String keyword);
}
