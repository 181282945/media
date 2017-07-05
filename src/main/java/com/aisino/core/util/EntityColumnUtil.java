package com.aisino.core.util;

import com.aisino.core.entity.annotation.Transient;
import com.aisino.core.mybatis.specification.QueryLike;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import com.aisino.core.mybatis.specification.QueryLike.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
public class EntityColumnUtil {

    /**
     * 根据实体，反射动态查询条件
     */
    public static List<QueryLike> generateEntityQueryLike(Object entity) {
        return generateEntityQueryLike(entity, entity.getClass());
    }

    /**
     * 反射设置对象属性
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
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

    /**
     * 只针对实体模板方法使用
     * 根据实体值，赋值queryLikes
     */
    public static List<QueryLike> setEntityQueryLike(Object entity, List<QueryLike> queryLikes) {

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
    public static List<Field> getClassAllFields(Object entity, Class<?> cls) {

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

    private static List<QueryLike> generateEntityQueryLike(Object entity, Class<?> cls) {

        // 获取实体类的所有属性，返回Field数组
        Field[] declaredFields = cls.getDeclaredFields();
        List<Field> fields = Arrays.asList(declaredFields);
        return generateEntityQueryLike(entity, cls, fields);
    }

    private static List<QueryLike> generateEntityQueryLike(Object entity, Class<?> cls, List<Field> fields) {

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
                }
//                else{
//                    throw new RuntimeException("考虑数据库null值,所以只支持包装类型!不支持基本类型!");
//                }
            }

            list.addAll(generateEntityQueryLike(entity, cls.getSuperclass()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
