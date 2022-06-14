package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName trusted_visitor
 */
@TableName(value ="trusted_visitor")
@Data
public class TrustedVisitor implements Serializable {
    /**
     * 可信访客ID
     */
    @TableId
    private String trustedID;

    /**
     * 可信访客姓名
     */
    private String trustedName;

    /**
     * 可信访客性别
     */
    private Integer trustedGender;

    /**
     * 可信访客年龄
     */
    private Integer trustedAge;

    /**
     * 可信访客电话
     */
    private String trustedTel;

    /**
     * 可信访客身份证号
     */
    private String trustedCardId;

    /**
     * 可信访客身高
     */
    private String trustedHeight;

    /**
     * 可信访客发型
     */
    private String trustedHair;

    /**
     * 可信访客面容id
     */
    private String trustedFaceId;

    /**
     * 可信访客指纹
     */
    private String trustedFingerprint;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}