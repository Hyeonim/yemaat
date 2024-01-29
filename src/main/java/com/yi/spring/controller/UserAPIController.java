package com.yi.spring.controller;


import com.yi.spring.entity.User;
import com.yi.spring.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserAPIController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // 추가
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        // @RequestBody User user 는 HTTP 요청의 본문에 있는 데이터를 User 객체로
        // 변환하고 이는 클라이언트에서 전달한 JSON 또는 폼 데이터 등을 자바객체로 매핑하는 역할을 함

        User saveduser = userService.createUser(user);

        return new ResponseEntity<>(saveduser, HttpStatus.CREATED);
    }

    // 수정
//    @PutMapping("{id}")
//    public ResponseEntity<User> updateUser(@PathVariable("id") Long userId, @RequestParam User user){
//        user.setId(userId);
//        User updatedUser = userService.updateUser(user);
//        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//    }
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long userId, @RequestBody User user) {
        user.setId(userId);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    // 삭제
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        // userServiceImpl 에서 구현한 deleteUser 메소드 기능을 통해 입력받은 userId 를 DB 에서 삭제함
        userService.deleteUser(userId);

        // 삭제되었다는걸 출력해줌
        return new ResponseEntity<>("삭제성공", HttpStatus.OK);
    }
}
