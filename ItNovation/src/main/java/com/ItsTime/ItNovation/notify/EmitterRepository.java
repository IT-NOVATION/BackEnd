package com.ItsTime.ItNovation.notify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@RequiredArgsConstructor
public class EmitterRepository {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    /**
     * 주어진 아이디와 이미터를 저장
     *
     * @param id      - 사용자 아이디.
     * @param emitter - 이벤트 Emitter.
     */
    public void save(Long id, SseEmitter emitter) {
        emitters.put(id, emitter);
    }
    /**
     * 주어진 아이디의 Emitter를 제거
     *
     * @param id - 사용자 아이디.
     */
    public void deleteById(Long id) {
        emitters.remove(id);
    }
    /**
     * 주어진 아이디의 Emitter를 가져옴.
     *
     * @param id - 사용자 아이디.
     * @return SseEmitter - 이벤트 Emitter.
     */
    public SseEmitter get(Long id) {
        return emitters.get(id);
    }

}
