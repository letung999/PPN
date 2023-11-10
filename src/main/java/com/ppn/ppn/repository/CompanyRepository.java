package com.ppn.ppn.repository;

import com.ppn.ppn.entities.CompanyProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyProfile, Integer> {
    @Query(value = "select new CompanyProfile (c.companyId, c.companyIdName," +
            " c.dateOfEstablishment,c.companyProfileMapping)" +
            " from CompanyProfile c " +
            "join CompanyProfileMapping cp" +
            " on c.companyId = cp.companyId and c.dateOfEstablishment >= ?1 " +
            " order by c.dateOfEstablishment desc ")
    List<CompanyProfile> getListCompanyProfile(Date date);

    @Query(value = "select new CompanyProfile (c.companyId, c.companyIdName, c.dateOfEstablishment, c.companyProfileMapping)" +
            " from CompanyProfile c" +
            " inner join CompanyProfileMapping cp" +
            " on c.companyId = cp.companyId" +
            " order by cp.ParentCompanyId, c.companyId")
    Page<CompanyProfile> getListCompanyProfileId(Pageable pageable);
}
