package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface DiningRestRepository extends JpaRepository<Dinning, Integer> {

    Optional<Dinning> findByUserNo(User userNo);

    Optional<Dinning> findByUserNoAndRestStatusNot(User userNo, String restStatus);


    Page<Dinning> findByRestNameContainingIgnoreCase(String name, Pageable pageable);

    // 방금 추가
//    @Query("SELECT d FROM Dinning d JOIN FETCH d.userNo u WHERE u.userAuth = '2'")

    @Query("SELECT d FROM Dinning d JOIN FETCH d.userNo u WHERE u.userAuth = '2'")
//    @Query("SELECT d FROM Dinning d inner join d.userNo u WHERE u.userAuth = '2'")
    List<Dinning> getAllByUserAuthIsOwner();


//    @Query(value = "select d.rest_no, avg(r.rev_score) from dining_rest d left join review r on d.rest_no = r.rest_no group by d.rest_no order by avg(r.rev_score) desc", nativeQuery = true)
//    Map<Integer, Float> getRestScore();

    @Query("select d, COALESCE((SELECT avg(r.revScore) FROM Review r WHERE r.restNo.restNo = d.restNo), 0) AS restScore from Dinning d group by d.restNo order by restScore desc")
    List<Dinning> getRestScore();
    @Query("select d, COALESCE((SELECT avg(r.revScore) FROM Review r WHERE r.restNo.restNo = d.restNo), 0) AS restScore from Dinning d group by d.restNo order by restScore desc")
    List<Object[]> getRestScore2();
}
