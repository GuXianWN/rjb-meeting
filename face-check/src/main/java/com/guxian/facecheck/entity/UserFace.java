package com.guxian.facecheck.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 人脸识别所需要的表，包含用户上传的脸图片URL
 * @TableName user_face
 */
@Table(name="user_face")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserFace implements Serializable {
    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}