package com.ssafy.a505.Controller;

import com.ssafy.a505.Domain.User.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api-user")
@Tag(name = "UserController", description = "유저 CRUD")
@CrossOrigin("*")
public class UserController {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    // 로그인 요청 검증
    @Operation (summary = "로그인 요청 검증")
    @PostMapping ("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        // 서비스 -> 다오-> DB
        HttpStatus status = null;
        Map<String, Object> result = new HashMap<>();

        if(user.getUserId() == 1234 && user.getUserPassword() == "1234") {
            // 토큰 만들어서 넘김
            result.put("message", SUCCESS);
            // result.put("access-token", jwtUtil.createToken(user.getUserId()));
            // id 같이 보내면 덜 번거로움
            status = HttpStatus.ACCEPTED;
        }else {
            result.put("message", FAIL);
            status = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(result, status);
    }
}
