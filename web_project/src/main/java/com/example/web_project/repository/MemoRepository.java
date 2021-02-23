package com.example.web_project.repository;

import com.example.web_project.entitiy.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    void deleteMemoByMnoLessThan(Long mno);

    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getMemoListDesc();

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
    int updateMemoTextObj(@Param("param") Memo memo);


    @Query(value = "select m from Memo m where m.mno > :mno",
        countQuery = "select count(m) from Memo m where  m.mno > :mno")
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);

    // Object[] 리턴
    @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno",
        countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);

    // Native SQL
    @Query(value = "select * from where mno > 0", nativeQuery = true)
    List<Object[]> getNativeResult();
}
