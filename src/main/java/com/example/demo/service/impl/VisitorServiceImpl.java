package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.TrustedVisitor;
import com.example.demo.model.Visitor;
import com.example.demo.service.VisitorService;
import generator.mapper.TrustedVisitorMapper;
import generator.mapper.VisitorMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 86133
* @description 针对表【visitor】的数据库操作Service实现
* @createDate 2022-05-03 20:06:27
*/
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor>
    implements VisitorService{

    private static final String SALT="writeByZjx";

    @Resource
    VisitorMapper visitorMapper;

    @Resource
    TrustedVisitorMapper trustedVisitorMapper;

    /**
     * 插入脱敏后的用户
     * @param visitor 输入的用户信息
     * @return 是否插入成功
     */
    @Override
    public Boolean uploadInformation(Visitor visitor) {
        if(visitor==null) return false;
        if(StringUtils.isAnyBlank(visitor.getVisCardId(),visitor.getVisFaceId(),
                visitor.getVisFingerprint(),visitor.getVisHeight(),visitor.getVisTel(),visitor.getVisHair())){
            return false;
        }
        Visitor safeVisitor=getSafeVisitor(visitor);

        int result= visitorMapper.insert(safeVisitor);
        if(result<0) return false;
        else return true;

    }

    /**
     * 给定一个身高的偏差值
     */
    private static final Integer HEIGHT_OFFSET=7;

    /**
     * 通第一遍：通过过输入的精确值信息获得真实身份范围
     * @param visitor 输入的身份信息
     * @return 可能的身份列表
     */

    @Override
    public List<TrustedVisitor> getSimilarVisitor(Visitor visitor){
        // 一共有3个精确属性，其中有1个因为是照相识别，可认为是可信属性；tel和cardId是手动填写，需要判断是否可信
        String tel=visitor.getVisTel();
        Integer isTrustedTel=visitor.getTelAttr();

        String cardId=visitor.getVisCardId();
        String protectedCardId=DigestUtils.md5DigestAsHex((SALT + cardId).getBytes());
        Integer isTrustedCardId=visitor.getCardIdAttr();
        String height=visitor.getVisHeight();
        Integer h = Integer.valueOf(height);
        Integer leftH=h-HEIGHT_OFFSET;
        Integer rightH=h+HEIGHT_OFFSET;
        QueryWrapper<TrustedVisitor> trustedVisitorQueryWrapper = new QueryWrapper<>();
        if(h>100&&h<200){
            trustedVisitorQueryWrapper.between("trustedHeight",Integer.toString(leftH),Integer.toString(rightH));
        }
        else{//输入参数错误
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if(isTrustedCardId==1){
            trustedVisitorQueryWrapper.eq("trustedCardId",protectedCardId);//注意这里要传入的是脱敏后的身份证号码
        }
        if (isTrustedTel==1){
            trustedVisitorQueryWrapper.eq("trustedTel",tel);
        }
        return trustedVisitorMapper.selectList(trustedVisitorQueryWrapper);

    }

    private static final Double PIC_OFFSET=0.3;

    /**
     * 从上一步筛选的身份列表中，进一步通过模糊属性，筛选出唯一的真实身份
     * @param visitor
     * @param similarVisitorList
     * @return
     */
    @Override
    public TrustedVisitor getTrueVisitor(Visitor visitor,List<TrustedVisitor> similarVisitorList) {
        String visFaceId = visitor.getVisFaceId();
        Integer faceIdAttr = visitor.getFaceIdAttr();
        String visFingerprint = visitor.getVisFingerprint();
        Integer fingerPrintAttr = visitor.getFingerPrintAttr();
        if (faceIdAttr == 1 && fingerPrintAttr == 1) {
            for (TrustedVisitor trustedVisitor : similarVisitorList) {
                if (picSimilarity(visFaceId, trustedVisitor.getTrustedFaceId()) <= PIC_OFFSET && picSimilarity(visFingerprint, trustedVisitor.getTrustedFingerprint()) <= PIC_OFFSET) {
                    return trustedVisitor;
                }
            }
        }
        else{
            //alert 发现虚假属性 发出警报
            return null;
        }

        return null;//没有找到
    }



    /**
     * 获得脱敏后的访客
     * @param visitor
     * @return 脱敏访客
     */

    @Override
    public Visitor getSafeVisitor(Visitor visitor){
        Visitor safeVisitor = new Visitor();

        safeVisitor.setVisID(visitor.getVisID());
        safeVisitor.setVisName(visitor.getVisName());
        safeVisitor.setVisGender(visitor.getVisGender());
        safeVisitor.setVisAge(visitor.getVisAge());
        safeVisitor.setVisTel(visitor.getVisTel());
        safeVisitor.setTelAttr(visitor.getTelAttr());
        String protectedCardId = DigestUtils.md5DigestAsHex((SALT + visitor.getVisCardId()).getBytes());
        safeVisitor.setVisCardId(protectedCardId);
        safeVisitor.setCardIdAttr(visitor.getCardIdAttr());
        safeVisitor.setVisHeight(visitor.getVisHeight());
        safeVisitor.setVisHair(visitor.getVisHair());
        // faceid、figerprint不能加密 因为比的是相似度 只有精准值需要加密
        safeVisitor.setVisFaceId(visitor.getVisFaceId());
        safeVisitor.setFaceIdAttr(visitor.getFaceIdAttr());
        safeVisitor.setVisFingerprint(visitor.getVisFingerprint());
        safeVisitor.setFingerPrintAttr(visitor.getFingerPrintAttr());
        safeVisitor.setCreateTime(visitor.getCreateTime());
        safeVisitor.setUpdateTime(visitor.getUpdateTime());
        safeVisitor.setIsDelete(visitor.getIsDelete());
        safeVisitor.setVisRecord(visitor.getVisRecord());
        return safeVisitor;

    }

    /**
     * 给算法留的接口
     * 0.0~1.0 1表示完全相同
     */
    public Double picSimilarity(String pic,String truePic){

        return 1.0;

    }

}




