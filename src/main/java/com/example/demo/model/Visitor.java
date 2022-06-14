package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName visitor
 */
@TableName(value ="visitor")
@Data
public class Visitor implements Serializable {
    /**
     * 访客ID
     */
    @TableId
    private String visID;

    /**
     * 访客姓名
     */
    private String visName;

    /**
     * 访客性别
     */
    private Integer visGender;

    /**
     * 访客年龄
     */
    private Integer visAge;

    /**
     * 访客电话
     */
    private String visTel;

    /**
     * 电话可信度
        待确认（default）-0
        确认可信 - 1
        确认不可信 - 2
     */
    private Integer telAttr;

    /**
     * 身份证号
     */
    private String visCardId;

    /**
     * 身份证号可信度
    待确认（default）-0
    确认可信 - 1
    确认不可信 - 2
     */
    private Integer cardIdAttr;

    /**
     * 访客身高
     */
    private String visHeight;

    /**
     * 访客发型
     */
    private String visHair;

    /**
     * 访客面容id
     */
    private String visFaceId;

    /**
     * 面容可信度
        待确认（default）-0
        确认可信 - 1
        确认不可信 - 2
     */
    private Integer faceIdAttr;

    /**
     * 访客指纹
     */
    private String visFingerprint;

    /**
     * 指纹可信度
        待确认（default）-0
        确认可信 - 1
        确认不可信 - 2
     */
    private Integer fingerPrintAttr;

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

    /**
     * 访客身份综合判断
        待确认（default）-0
        确认可信 - 1
        确认不可信 - 2
     */
    private Integer visRecord;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}