package com.guxian.auth.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 人脸识别所需要的表，包含用户上传的脸图片URL
 * @TableName user_face
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class UserFaceDTO{
    private Integer id;
    private Long userId;
    private String faceUrl;
    private Date createTime;
}