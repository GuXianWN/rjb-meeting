package com.guxian.facecheck.repo;

import com.guxian.facecheck.entity.UserFace;
import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserFaceRepo extends CrudRepository<UserFace, Integer> {

    Optional<UserFace> findByUserId(Long userId);
}
