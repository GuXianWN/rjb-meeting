package com.guxian.facecheck.service;

import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.facecheck.repo.UserFaceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaceCheckService {

    private final UserFaceRepo userFace;

    public FaceCheckService(@Autowired UserFaceRepo userFace) {
        this.userFace = userFace;
    }

    public boolean checkFace(String url) {
        var userId = CurrentUserSession.getUserSession().getUserId();
        var userFaceUrl = userFace.findByUserId(userId).orElseThrow(() ->
                new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST)).getFaceUrl();

        return userFaceUrl != null;
    }


    public boolean upload(String url) {
        return true;
    }
}
