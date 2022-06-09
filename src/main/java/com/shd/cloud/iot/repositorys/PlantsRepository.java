package com.shd.cloud.iot.repositorys;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shd.cloud.iot.models.Plants;
import com.shd.cloud.iot.payload.response.PlantsIdAndTitle;

@Repository
public interface PlantsRepository extends PagingAndSortingRepository<Plants, Integer> {

    @Query(value = "select id, title from plants where title like %:key%", countQuery = "select count(id) from plants where title like %:key%", nativeQuery = true)
    Page<PlantsIdAndTitle> search(@Param("key") String key, Pageable pageable);
}
