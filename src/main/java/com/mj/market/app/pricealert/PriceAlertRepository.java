package com.mj.market.app.pricealert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

@Repository
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {

    @Query(nativeQuery = true, value ="select * from price_alert Where is_active= :active")
    Vector<PriceAlert> findByIsActive(@Param("active") boolean isActive);

    @Query(nativeQuery = true, value ="select * from price_alert p left join user u ON p.user_id = u.id Where u.id = :id")
    Vector<PriceAlert> findByUserId(@Param("id") Long id);

   /* @Query(nativeQuery = true, value ="select * from price_alert p left join user u ON p.user_id = u.id Where u.id = :id And p.is_active = true")
    List<PriceAlert> findByUserIdAndIsActive(@Param("id") Long id);*/

    List<PriceAlert> findAll();

    Optional<PriceAlert> findById(Long id);

    PriceAlert save(PriceAlert entity);

    boolean existsById(Long id);

    void deleteById(Long id);

}
