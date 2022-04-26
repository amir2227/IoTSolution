package com.shd.cloud.iot.repositorys;

import java.util.List;
import java.util.Optional;

import com.shd.cloud.iot.models.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByUser_id(Long user_id);

    Optional<Location> findByIdAndUser_id(Long id, Long user_id);

    @Query(value = "select * from locations loc where loc.user_id= :id and (loc.name like %:key% or loc.type like %:key%)", nativeQuery = true)
    List<Location> search(@Param("key") String key, @Param("id") Long id);
}
