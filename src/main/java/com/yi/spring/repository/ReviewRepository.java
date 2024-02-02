package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Review;
import com.yi.spring.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface ReviewRepository extends PagingAndSortingRepository<Review, Integer> {

    List<Review> findByRestNo(Dinning dinning);
    List<Review> findByUserNo(User userNo);



    @Override
    Page<Review> findAll(Pageable pageable);

    @Query("select r from Review r inner join r.userNo.reviews reviews where reviews.userNo = ?1")
    List<Review> findByUserNo_Reviews_UserNo(int userNo);


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
