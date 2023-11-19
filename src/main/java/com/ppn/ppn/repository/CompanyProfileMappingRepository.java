package com.ppn.ppn.repository;

import com.ppn.ppn.entities.CompanyProfileMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyProfileMappingRepository extends JpaRepository<CompanyProfileMapping, Integer> {
}
