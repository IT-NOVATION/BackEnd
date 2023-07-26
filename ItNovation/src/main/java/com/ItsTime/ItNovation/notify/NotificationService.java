package com.ItsTime.ItNovation.notify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT=60L*1000*60;
    private final EmitterRepository emitterRepository;
    /**
     * 클라이언트가 구독을 위해 호출하는 메서드.
     *
     * @param userId - 구독하는 클라이언트의 사용자 아이디.
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     * @param eventCategory - 이벤트이름
     */
    public SseEmitter subscribe(Long userId,String eventCategory) {
        SseEmitter emitter = createEmitter(userId);
        sendToClient(userId, "EventSteam created. [ userId=" + userId + "]",eventCategory);
        return emitter;
    }
    /**
     * 클라이언트에게 데이터를 전송
     *
     * @param userId   - 데이터를 받을 사용자의 아이디.
     * @param data - 전송할 데이터.
     * @param eventCategory - 이벤트이름
     */
    private void sendToClient(Long userId, Object data,String eventCategory) {
        eventCategory = eventCategory != null ? eventCategory : "notify";
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try{
                emitter.send(SseEmitter.event().id(String.valueOf(userId)).name(eventCategory).data(data));
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
                emitter.completeWithError(e);
            }
        }
    }
    /**
     * 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     *
     * @param userId - 사용자 아이디.
     * @return SseEmitter - 생성된 이벤트 Emitter.
     */
    private SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId,emitter);
        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));
        return emitter;
    }
    /**
     * 서버의 이벤트를 클라이언트에게 보내는 메서드
     * 다른 서비스 로직에서 이 메서드를 사용해 데이터를 Object event에 넣고 전송하면 된다.
     *
     * @param userId - 메세지를 전송할 사용자의 아이디.
     * @param event  - 전송할 이벤트 객체.
     * @param eventCategory - 이벤트이름
     */
    public void notify(Long userId, Object event,String eventCategory) {
        sendToClient(userId, event,eventCategory);
    }
}
