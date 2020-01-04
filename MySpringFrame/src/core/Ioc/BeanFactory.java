package core.Ioc;

public interface BeanFactory<T> {
    public T getBean(String name);
    public T getBean(Class beanClass);
}
