package core.Beans.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注扫描的包
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentScan {
    public String basePackage() default "";
    public String[] basePackages() default "";
    public Class basePackageClass() default ComponentScan.class;
    public Class[] basePackageClasses() default ComponentScan.class;
}
