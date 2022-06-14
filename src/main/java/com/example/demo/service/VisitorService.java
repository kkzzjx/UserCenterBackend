package com.example.demo.service;

import com.example.demo.model.TrustedVisitor;
import com.example.demo.model.Visitor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86133
* @description 针对表【visitor】的数据库操作Service
* @createDate 2022-05-03 20:06:27
*/
public interface VisitorService extends IService<Visitor> {
    /**
     * 经过虚假属性辨别算法，进行属性的真实情况，然后在数据库中插入脱敏后的访客
     * @param visitor 输入的用户信息
     * @return 是否插入成功
     */
    Boolean uploadInformation(Visitor visitor);

    /**
     * 获得脱敏后的访客
     * @param visitor
     * @return 脱敏访客
     */
    Visitor getSafeVisitor(Visitor visitor);

    /**
     * 通第一遍：通过过输入的精确值信息获得真实身份范围
     * @param visitor 输入的身份信息
     * @return 可能的身份列表
     */
    List<TrustedVisitor> getSimilarVisitor(Visitor visitor);

    /**
     * 从上一步筛选的身份列表中，进一步通过模糊属性，筛选出唯一的真实身份
     * @param visitor
     * @param similarVisitorList
     * @return
     */
    TrustedVisitor getTrueVisitor(Visitor visitor,List<TrustedVisitor> similarVisitorList);


}
