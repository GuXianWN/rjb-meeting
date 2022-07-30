package com.guxian.facecheck.repo;

import com.guxian.facecheck.entity.FaceCount;
import com.guxian.facecheck.entity.UserFace;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface FaceCountRepo extends PagingAndSortingRepository<FaceCount, Long> {

}
