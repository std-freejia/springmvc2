package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); // static 사용
    private static long sequence = 0L; // static 사용

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    // null 도 받아주고 에러 발생시키지 않는 Optional
    public Optional<Member> findByLoginId(String loginId){
/*
        List<Member> all =findAll();
        for(Member m : all){
            if(m.getLoginId().equals(loginId)){
                return Optional.of(m);
            }
        }
        return Optional.empty();
 */
        // List -> stream() 으로 변경해서 filter 에 조건을 써준다.
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    // 전체조회
    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    // 초기화
    public void clearStore(){
        store.clear();
    }
}
