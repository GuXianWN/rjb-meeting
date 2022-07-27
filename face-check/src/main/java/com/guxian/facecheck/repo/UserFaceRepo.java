package com.guxian.facecheck.repo;

import com.guxian.facecheck.entity.UserFace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;


public interface UserFaceRepo extends PagingAndSortingRepository<UserFace, Integer> {

    Optional<UserFace> findByUserId(Long userId);


}
