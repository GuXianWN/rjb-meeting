package com.guxian.facecheck.service.provider;

import com.aliyun.facebody20191230.models.CompareFaceRequest;
import com.aliyun.teautil.models.RuntimeOptions;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.facecheck.config.AliServiceObject;
import com.guxian.facecheck.repo.UserFaceRepo;
import com.guxian.facecheck.service.FaceCompareService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;

@Service
public class AliFaceCompareService implements FaceCompareService {

    private final AliServiceObject aliServiceObject;

    private final UserFaceRepo userFaceRepo;

    public AliFaceCompareService(AliServiceObject aliServiceObject, UserFaceRepo userFaceRepo) {
        this.aliServiceObject = aliServiceObject;
        this.userFaceRepo = userFaceRepo;
    }

    @SneakyThrows
    @Override
    public double checkFaceSimilarRate(File faceFileA) {
        var client = aliServiceObject.getClient();
        CompareFaceRequest request = new CompareFaceRequest();
        request.setQualityScoreThreshold(28F);
        var user = userFaceRepo.findByUserId(CurrentUserSession.getUserSession().getUserId());

        try (var fileInputStream = new FileInputStream(faceFileA)) {
            request.setImageDataA(fileInputStream.readAllBytes());
        }
        var faceUrl = user.orElseThrow(() -> new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST))
                .getFaceUrl();

        if (!StringUtils.hasText(faceUrl)) {
            throw new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST);
        }

        request.setImageURLB(faceUrl);
        var runtime = new RuntimeOptions();
        return client.compareFaceWithOptions(request, runtime).getBody().getData().getConfidence();
    }
}
