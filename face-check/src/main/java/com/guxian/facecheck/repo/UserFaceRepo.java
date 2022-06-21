package com.guxian.facecheck.repo;

import com.guxian.facecheck.entity.UserFace;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface UserFaceRepo extends PagingAndSortingRepository<UserFace, Integer> {
    Optional<UserFace>
    findByUserId(Long userId);

    Optional<UserFace> removeByUserId(Long userId);


}
