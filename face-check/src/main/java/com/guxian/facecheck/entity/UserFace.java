package com.guxian.facecheck.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * 人脸识别所需要的表，包含用户上传的脸图片URL
 * @TableName user_face
 */
@Table(name="user_face")
@Data
public class UserFace implements Serializable {
    /**
     * 
     */
    @Id
    private Integer id;

    /**
     * 上传者的user ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * face URL
     */
    @Column(name = "face_url")
    private String faceUrl;

    /**
     * 
     */
    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}