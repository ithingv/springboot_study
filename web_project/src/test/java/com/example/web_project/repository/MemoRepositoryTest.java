package com.example.web_project.repository;

import com.example.web_project.entitiy.Memo;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemoRepositoryTest {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void 테스트_클래스(){
        System.out.println("check test class name");
        System.out.println(memoRepository.getClass().getName());
    }

    // 등록작업 테스트
    // 한번에 여러개의 엔티티 생성해서 저장

    @Test
    public void 테스트_더미_생성(){
        IntStream.rangeClosed(1,100).forEach(
                i-> {
                    Memo memo = Memo.builder().memoText("Sample " + i).build();
                    memoRepository.save(memo);
                }
        );

    }

    // 조회 작업 테스트
    // findById
    // getOne

    @Test
    public void testSelect(){
        // 데이터베이스에 존재하는 mno
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno); // 실제 쿼리가 날라가는 부분

        System.out.println("===============================");

        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }

    }
    @Transactional // could not initialize proxy
    @Test
    public void testGetOne(){
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);
        System.out.println("===================================");
        System.out.println(memo); // 실제 쿼리가 날라가는 부분
    }

    // 수정 작업 테스트
    // jpa는 특정 엔티티가 존재하는지 확인하는 select가 먼저 실행되고 해당 @Id를 가진 엔티티 객체가 있다면
    // update 그렇지 않다면 insert를 실행한다.

    @Test
    public void testUpdate(){
        Memo update_text = Memo.builder().mno(100L).memoText("update text").build();
        System.out.println(memoRepository.save(update_text));
    }

    // 삭제 작업 테스트
    @Transactional
    @Test
    public void testDelete(){
        Long mno = 99L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault(){
       Pageable pageable = PageRequest.of(0,10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
    }

    // page method
    @Test
    public void testPageMethod(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("=======================================");
        System.out.println("Total Pages: " + result.getTotalPages() );
        System.out.println("Total Count: " + result.getTotalElements());
        System.out.println("Page Number: " + result.getNumber());
        System.out.println("Page Size: " + result.getSize());
        System.out.println("has next page? " + result.hasNext());
        System.out.println("first page? " + result.isFirst());

        result.get().forEach(System.out::println);
    }

    // 정렬 조건 추가하기

    @Test
    public void testPageAndSort(){

        Sort sort1 = Sort.by("mno").descending();
        Pageable pageable1 = PageRequest.of(0,10,sort1);
        Page<Memo> result = memoRepository.findAll(pageable1);

        // 여러개의 정렬 조건을 다르게 지정할 수 있다.

        Sort sort2 = Sort.by("mno").ascending();
        Sort sort3 = Sort.by("memoText").descending();

        Sort sortAll = sort1.and(sort2);
        Pageable pageable2 = PageRequest.of(0,10,sortAll);

        result.get().forEach(System.out::println);
    }

    // 쿼리 메서드 테스트

    @Test
    public void testQueryMethod(){
        List<Memo> memos = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);
        for (Memo memo : memos) {
            System.out.println(memo);
        }
    }

    @Commit // 최종 결과를 커밋 미 적용시 테스트 코드의 deleteBy는 기본적으로 롤백 처리되어 결과가 반영되지 않음
    @Transactional
    @Test
    public void testQueryMethodDelete(){ // deleteBy는 하나하나 삭제되므로 실제 개발에서는 자주 사용되지 않음
        memoRepository.deleteMemoByMnoLessThan(10L);
    }


}