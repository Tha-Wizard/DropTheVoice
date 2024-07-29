package com.ssafy.a505.controller;

import com.ssafy.a505.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/")
@CrossOrigin(origins = "localhost:3000/*", allowCredentials = "true")
@Slf4j
public class StompController {
    private final SimpMessagingTemplate messagingTemplate;

    public Set<String> sessionIDs;

    public StompController(SimpMessagingTemplate simpMessagingTemplate){
        sessionIDs = new HashSet<>();
        this.messagingTemplate = simpMessagingTemplate;
    }

    /**
     *
     * @param member
     * @param message
     *
     * 매 10초마다 프론트엔드에서 호출되는 메서드.
     * 사용자 위치정보를 계속해서 업데이트한다
     */
    @MessageMapping(value = "/position")
    public void message(Member member, Message<Member> message) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String sessionId = headerAccessor.getSessionId();

        log.info(sessionId+"is sessionId");
        log.info("Session Logged In Member Info: "+member.toString());

        //레디스에 위경도 좌표와 세션ID를 포함해서 저장하자.
        //redisService.update(member,lat,lng);
    }

    /**
     *
     * @param sessionId - 내 세션ID
     * @param latitude - 위도
     * @param longitude - 경도
     * @throws NullPointerException
     *
     * 사용자가 음원을 퍼트리기 위해 사용
     * 반환값으로 인근 1km이내 세션들에 대한 리스트를 반환
     * 만약 1km 이내에 사람이 없다면 에러 반환
     * 현재 미개발 상태라 등록된 세션정보 모두를 반환하고 있다.
     */
    @MessageMapping("/spread/{latitude}/{longitude}")
    public void spread(@Payload String sessionId,@DestinationVariable(value = "latitude") double latitude, @DestinationVariable(value = "longitude") double longitude) {
        log.info("[Key] : {}  [lat,lng] : {} : {}", sessionId,latitude,longitude);

        //임시적으로 맵에 모든 세션정보들을 넣어두고 이를 모두 반환하는 임시코드
        sessionIDs.add(sessionId);
        messagingTemplate.convertAndSend("/topic/others/"+sessionId,sessionIDs);
        
        //상대 세션ID 리스트 (3개이상) 탐색 및 반환기능을 서비스 로직에 추가 필요
        //List<Member> members = redisService.get(lat,lng)
    }

    //클라이언트가 사전에 전달받은 상대 세션ID 리스트를 통해 mySessionId와 otherSessionId를 명시함으로써
    //WebRTC연결을 특정할 수 있다.
    @MessageMapping("/peer/offer/{mySessionId}/{otherSessionId}")
    @SendTo("/topic/peer/offer/{otherSessionId}")
    public String PeerHandleOffer(@Payload String offer, @DestinationVariable(value = "mySessionId") String mySessionId,
                                  @DestinationVariable(value = "otherSessionId") String otherSessionId) {
        log.info("[OFFER] {} : {}", mySessionId+" : "+otherSessionId, offer);

        return offer;
    }

    @MessageMapping("/peer/answer/{mySessionId}/{otherSessionId}")
    @SendTo("/topic/peer/answer/{otherSessionId}")
    public String PeerHandleAnswer(@Payload String answer, @DestinationVariable(value = "mySessionId") String mySessionId,
                                   @DestinationVariable(value = "otherSessionId") String otherSessionId) {
        log.info("[ANSWER] {} : {}", mySessionId+" : "+otherSessionId, answer);
        return answer;
    }

    @MessageMapping("/peer/iceCandidate/{mySessionId}/{otherSessionId}")
    @SendTo("/topic/peer/iceCandidate/{otherSessionId}")
    public String PeerHandleIceCandidate(@Payload String candidate, @DestinationVariable(value = "mySessionId") String mySessionId,
                                         @DestinationVariable(value = "otherSessionId") String otherSessionId) {
        log.info("[ICECANDIDATE] {} : {}", mySessionId+" : "+otherSessionId, candidate);

        return candidate;
    }

    //웹소켓 세션이 종료되었을때 해당 세션 종료에 대한 후처리 기능이 적용되어야함
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        sessionIDs.remove(sessionId);

        log.info("Disconnected: " + sessionId);
        log.info(sessionIDs.toString());
    }
}
