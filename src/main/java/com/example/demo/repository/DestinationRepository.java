package com.example.demo.repository;

import com.example.demo.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Pageable;


@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    @Query("SELECT d FROM Destination d ORDER BY d.rating DESC")
    List<Destination> findTopNDestinationsByAverageRating(Pageable pageable);

    @Query("SELECT d FROM Destination d WHERE d.features LIKE %?1%")
    List<Destination> findByFeature(String feature);

    @Query("SELECT d FROM Destination d WHERE d.authorName = :authorName")
    List<Destination> findByAuthorName(@Param("authorName") String authorName);


    Destination findDestinationById(long id);
}
