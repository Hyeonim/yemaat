package com.yi.spring.service;

import com.yi.spring.entity.User;
import com.yi.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Override
    public User createUser(User user) {

        // save 는 새로운 사용자 객체를 DB에 삽입하거나, 이미 존재하는 사용자 객체의 경우 업데이트를 수행 함.
        // 즉 새로운 사용자 객체가 있으면 DB에 삽입 함.
        return userRepository.save(user);
        // userRepository.save(user)를 호출함으로써 DB에 사용자를 저장하고,
        // 저장된 사용자 객체를 반환하는 것이 createUser 메소드의 주요 동작
    }

    @Override
    public User getUserById(Long userId) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {

        // findAll() 은 userRepository 에 정의된 엔티티(User) 의 모든 레코드를 조회함
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {


        // 업데이트를 수행 할 사용자를 찾아 DB 에서 조회함, userRepository.findById를 통해 사용자 ID를 찾고 get() 메소드로 Optional 객체에서
        // 실제 사용자 객체를 가져 옴
        User existingUser = userRepository.findById(Math.toIntExact(user.getId())).get();

        // 새로운 사용자 정보로 기존 사용자의 필드를 업데이트 함 - user 객체로 업데이트.
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setEmail(user.getEmail());

        // 업데이트 된 사용자 객체를 DB 에 저장함. save 메소드는 새로운 객체를 생성하지 않고, 주어진 객체의 상태를
        // DB 에 반영함
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        // (Math.toIntExact(userId)) 는 userId 는 Long 타입 이지만 deleteById 는 Integer 형태의 ID를 기대함.
        // 그래서 Math.toIntExact 메소드를 사용해서 userId를 int 로 변환함
        userRepository.deleteById(Math.toIntExact(userId));
    }

    @Override
    public Optional<User> getUserEmail(String email){
        // findByEmail 는 JpaRepository 에서 자동생성된 쿼리 메소드임. 주어진 이메일에 해당하는 사용자를 찾아주는 역할을 함
        // 반환 타입은 Optional<User> .
        return userRepository.findByEmail(email);
    }



}
