package com.sinse.universe.model.artist;

import com.sinse.universe.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    @Query("" +
            "SELECT DISTINCT a " +
            "FROM Artist a " +
            "LEFT JOIN FETCH a.members " +
            "WHERE a.id = :id")
    Optional<Artist> findByIdWithMembers(@Param("id") int id);

    @Query("""
    select a
    from Artist a
    where a.partner.id = (
        select b.partner.id
        from Artist b
        where b.id = :artistId
    )
""")
    List<Artist> findByPartnerId(@Param("artistId") Integer artistId);
}