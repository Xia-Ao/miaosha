package com.xiaao.miaosha.validate;
/**
 * @author: Xia-ao
 * @create: 2019-02-08 22:37
 **/

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @className: ValidatorImpl
 * @description: NULL
 * @author: Xia-ao
 * @create: 2019-02-08 22:37
 **/
// 实现初始化的bean接口
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    // 在bean 初始化之后嗲用这个方法
    @Override
    public void afterPropertiesSet() throws Exception {

        //将hibernate validator通过工厂的初始化方法使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    // 实现校验方法并返回校验结果

    public ValidationResult validate(Object bean) {
        // 创建自定义的验证结果对象，用来存储是否错误和错误信息
        ValidationResult result = new ValidationResult();

        // 调用验证器的验证方法，返回一个Set
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if (constraintViolationSet.size() > 0) {
            // bean中有错误
            result.setHasErrors(true);
            // 遍历set，将错误存入到result
            constraintViolationSet.forEach(constraintViolation -> {
                // 获取bean属性上注解定义的错误信息
                String errMsg = constraintViolation.getMessage();
                // 获取哪个信息有错误
                String protertyName = constraintViolation.getPropertyPath().toString();
                // 将错误信息和对应的属性放入到错误map中
                result.getErrorMsgMap().put(errMsg, protertyName);
            });
        }
        // 将这个map返回给用户
        return result;

    }
}
