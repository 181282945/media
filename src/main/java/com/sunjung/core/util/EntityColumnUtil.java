package com.sunjung.core.util;

import com.sunjung.core.mybatis.specification.QueryLike;
import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.Transient;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import com.sunjung.core.mybatis.specification.QueryLike.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
public class EntityColumnUtil {

    /**
     * 根据实体，反射动态查询条件
     */
    public static List<QueryLike> generateEntityQueryLike(BaseEntity entity) {
        return generateEntityQueryLike(entity, entity.getClass());
    }

//    /**
//     * 根据反射 ManyToOne 实体
//     * Pair<属性名, Class>
//     */
//    public static List<Pair<String, Class>> generateEntityManyToOne(Class cls) {
//
//        List<Pair<String, Class>> list = new ArrayList<>();
//        try {
//            // 获取实体类的所有属性，返回Field数组
//            Field[] fields = cls.getDeclaredFields();
//            for (Field field : fields) {
//
//                if (0 == field.getGenericType().toString().indexOf("class")) {
//                    Class tempcls = Class.forName(field.getGenericType().toString().substring(6));
//                    if (BaseEntity.class.isAssignableFrom(tempcls)) {
//                        list.add(new Pair<>(field.getName(), tempcls));
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        return list;
//    }

    /**
     * 反射设置对象属性
     * @return
     */
    public static Annotation getEntityPropertyAnnotation(Class entityClass, String propertyName, Class annotationClass) {

        try {
            // 获取实体类的所有属性，返回Field数组
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {

                if (propertyName.equals(field.getName())) {
                    return field.getAnnotation(annotationClass);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;

    }

//    /**
//     * 反射设置对象属性
//     */
//    public static void setEntityProperty(BaseEntity entity, String propertyName, Object value) {
//
//        try {
//            // 获取实体类的所有属性，返回Field数组
//            Field[] fields = entity.getClass().getDeclaredFields();
//            for (Field field : fields) {
//
//                if (propertyName.equals(field.getName())) {
//                    Method method = entity.getClass().getDeclaredMethod("set" + getMethodName(field.getName()), value.getClass());
//                    method.invoke(entity, value);// 调用getter方法获取属性值
//                }
//
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    /**
     * 只针对实体模板方法使用
     * 根据实体值，赋值queryLikes
     */
    public static List<QueryLike> setEntityQueryLike(BaseEntity entity, List<QueryLike> queryLikes) {

        // <属性名, QueryLike>
        Map<String, QueryLike> map = new HashMap<>();
        for (QueryLike queryLike : queryLikes) {
            map.put(queryLike.getColumnName(), queryLike);
        }

        List<Field> declaredFields = getClassAllFields(entity, entity.getClass());
        List<Field> queryLikeFields = new ArrayList<>();
        for (Field field : declaredFields) {
            if (map.containsKey(field.getName())) {
                queryLikeFields.add(field);
            }
        }

        List<QueryLike> queryLikeValues = generateEntityQueryLike(entity, entity.getClass(), queryLikeFields);
        Map<String, QueryLike> resultMap = new HashMap<String, QueryLike>();
        for (QueryLike queryLike : queryLikeValues) {
            resultMap.put(queryLike.getColumnName(), queryLike);
        }
        for (QueryLike queryLike : queryLikes) {
//            if (!resultMap.containsKey(queryLike.getColumnName()) || !ValidateUtil.isEmpty("value", queryLike)) {
//                continue;
//            }
            if (!resultMap.containsKey(queryLike.getColumnName()) || !(queryLike==null || StringUtils.isBlank(queryLike.getValue()))) {
                continue;
            }
            QueryLike temp = resultMap.get(queryLike.getColumnName());
            queryLike.setColumnType(temp.getColumnType());
            queryLike.setValue(temp.getValue());
            //实体必须有两个字段,开始值-结束值,并且结束值以end_+开始值命名
            if (QueryLike.LikeMode.Between.getCode().equals(queryLike.getLikeMode().getCode())) {
                //开始值
                queryLike.setValue(temp.getValue());
//                queryLike.setValue2(ValidateUtil.format("end_"+temp.getColumnName(), entity));
                //结束值

                String value2 = null;
                try {
                    value2 = entity.getClass().getDeclaredField("end_"+temp.getColumnName()).get(entity).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                queryLike.setValue2(value2);
            }
        }

        return queryLikes;
    }

    // 获取实体所有Field，排除静态变量
    private static List<Field> getClassAllFields(BaseEntity entity, Class cls) {

        if (Object.class.getName().equals(cls.getName())) {
            return new ArrayList<>();
        }

        Field[] declaredFields = cls.getDeclaredFields();
        List<Field> list = new ArrayList<>();
        for (Field field : declaredFields) {
            // 排除静态变量
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            if (isStatic) {
                continue;
            }

            list.add(field);
        }

        list.addAll(getClassAllFields(entity, cls.getSuperclass()));
        return list;
    }

    private static List<QueryLike> generateEntityQueryLike(BaseEntity entity, Class cls) {

        // 获取实体类的所有属性，返回Field数组
        Field[] declaredFields = cls.getDeclaredFields();
        List<Field> fields = Arrays.asList(declaredFields);
        return generateEntityQueryLike(entity, cls, fields);
    }

    private static List<QueryLike> generateEntityQueryLike(BaseEntity entity, Class cls, List<Field> fields) {

        if (Object.class.getName().equals(cls.getName())) {
            return new ArrayList<>();
        }

        List<QueryLike> list = new ArrayList<>();
        try {

            for (Field field : fields) {

                // 排除静态变量
                boolean isStatic = Modifier.isStatic(field.getModifiers());
                if (isStatic) {
                    continue;
                }

                Transient transientTemp = field.getAnnotation(Transient.class);
                boolean isTransient = false;
                if (null != transientTemp) {
                    isTransient = true;
                }

                field.setAccessible(true);

                // 如果type是类类型，则前面包含"class "，后面跟类名
                // String类型
                if (field.getGenericType().toString().equals("class java.lang.String")) {
                    Object value = field.get(entity);
                    if (value != null) {
                        QueryLike queryLike = new QueryLike(field.getName(), value.toString());
                        queryLike.setIsTransient(isTransient);
                        list.add(queryLike);
                    }
                    // 如果类型是Integer
                }else if (field.getGenericType().toString().equals("class java.lang.Integer")) {
                    Object value = field.get(entity);
                    if (value != null) {
                        QueryLike queryLike = new QueryLike(field.getName(), LikeMode.Eq, ColumnType.Integer, value.toString());
                        queryLike.setIsTransient(isTransient);
                        list.add(queryLike);
                    }
                    // 如果类型是Double
                }else if (field.getGenericType().toString().equals("class java.lang.Double")) {
                    Object value = field.get(entity);
                    if (value != null) {
                        QueryLike queryLike = new QueryLike(field.getName(), LikeMode.Eq, ColumnType.Double, value.toString());
                        queryLike.setIsTransient(isTransient);
                        list.add(queryLike);
                    }
                    // 如果类型是Boolean 是封装类
                }else if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
                    Object value = field.get(entity);
                    if (value != null) {
                        QueryLike queryLike = new QueryLike(field.getName(), LikeMode.Eq, ColumnType.Boolean,Boolean.parseBoolean(value.toString()) ? "1" : "0");
                        queryLike.setIsTransient(isTransient);
                        list.add(queryLike);
                    }
                    // 如果类型是Date
                }else if (field.getGenericType().toString().equals("class java.util.Date")) {
                      Object value = field.get(entity);
                    if (value != null) {
                        QueryLike queryLike = new QueryLike(field.getName(), LikeMode.Eq, ColumnType.Date, DateUtil.format((Date)value, DateUtil.C_TIME_PATTON_DEFAULT));
                        queryLike.setIsTransient(isTransient);
                        list.add(queryLike);
                    }
                    // 如果类型是Short
                }else if (field.getGenericType().toString().equals("class java.lang.Short")) {
                    Object value = field.get(entity);
                    if (value != null) {
                        QueryLike queryLike = new QueryLike(field.getName(), LikeMode.Eq, ColumnType.Integer, value.toString());
                        queryLike.setIsTransient(isTransient);
                        list.add(queryLike);
                    }
                }else{
                    throw new RuntimeException("考虑数据库null值,所以只支持包装类型!不支持基本类型!");
                }
            }

            list.addAll(generateEntityQueryLike(entity, cls.getSuperclass()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // 把一个字符串的第一个字母大写、效率是最高的
    private static String getMethodName(String fildeName) throws Exception {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

}
