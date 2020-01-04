package core.Unit;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 *直接在“指针”层面上修改对象的属性的工具类
 */
public class UnsafeUnit {
    static Unsafe unsafe;
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe =(Unsafe) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /*设置Int类型*/
    public static<T> boolean setInt(T object,String FieldName,int InitialValue,int FinalValue) throws NoSuchFieldException {
        long statOffset = unsafe.objectFieldOffset(object.getClass().getDeclaredField(FieldName));
        return unsafe.compareAndSwapInt(object,statOffset,InitialValue,FinalValue);
    }

    /*设置Long类型*/
    public static<T> boolean setLong(T object,String FieldName,long InitialValue,long FinalValue) throws NoSuchFieldException {
        long statOffset = unsafe.objectFieldOffset(object.getClass().getDeclaredField(FieldName));
        return unsafe.compareAndSwapLong(object,statOffset,InitialValue,FinalValue);
    }

    /*设置Object类型*/
    public static<T> boolean setObject(T object,String FieldName,Object InitialValue,Object FinalValue) throws NoSuchFieldException{
        long statOffset = unsafe.objectFieldOffset(object.getClass().getDeclaredField(FieldName));
        return unsafe.compareAndSwapObject(object,statOffset,InitialValue,FieldName);
    }
}
