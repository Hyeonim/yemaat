package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Review;
import com.yi.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByRestNo(Dinning dinning);
    List<Review> findByUserNo(User userNo);




    @Query("select r from Review r inner join r.userNo.reviews reviews where reviews.userNo = ?1")
    List<Review> findByUserNo_Reviews_UserNo(int userNo);


    @Query(value = "SELECT * FROM review WHERE rev_img IS NOT NULL ORDER BY RAND() LIMIT 10", nativeQuery = true)
//    @Query("SELECT r FROM Review r where not ISNULL(r.revImg) ORDER BY RAND() LIMIT 1")
    List<Review> getRandomTen();


//    @Query("SELECT m FROM Review m WHERE m.name IN :names OR m.address IN :addresses")
//    List<Review> findByConditions(@Param("names") List<String> names, @Param("addresses") List<String> addresses);

//    @Query("SELECT m FROM Review m WHERE " +
//            "(:id is null OR m.id = :id) AND " +
//            "(:revContent is null OR m.revContent = :revContent) AND " +
//            "(:revScores is null OR m.revScore IN :revScores) AND " +
//            "(:revWriteTime is null OR m.revWriteTime = :revWriteTime)")
//    List<Review> findByRevContent(
//            @Param("id") String id,
//            @Param("revContent") String revContent,
//            @Param("revScores") List<Integer> revScores,
//            @Param("revWriteTime") String revWriteTime
//    );



}
