package top.meethigher.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 校验
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 18:57
 */
public class ValidateUtil {


    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private ValidateUtil() {
    }

    /**
     * 对{@link javax.validation}下的校验注解进行校验
     * 错误信息通过异常丢出。
     * 格式：msg1,msg2,msg3...
     *
     * @param t
     * @param <T>
     */
    public static <T> void validate(T t) {
        List<String> list = new LinkedList<>();
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        for (ConstraintViolation<T> violation : violations) {
            String name = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            list.add(name + message);
        }
        if (list.size() > 0) {
            throw new IllegalArgumentException(Arrays.toString(list.toArray()).replaceAll("\\]|\\[", ""));
        }
    }
}
