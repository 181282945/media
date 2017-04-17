package com.sunjung.common.dto.jqgrid;

import com.sunjung.common.annotation.jqgrid.JqgridColumn;
import com.sunjung.core.entity.BaseEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 为 on 2017-4-17.
 * 获取实体字段再jqgrid
 */
public class JqgridColumnUtil {

    public static List<JqgridFormat> getFormat(Class<? extends BaseEntity> clz){
        List<JqgridFormat> list = new ArrayList<>();
        Field [] fields = clz.getDeclaredFields();
        for(Field field : fields){
            JqgridColumn jqgridColumn = field.getAnnotation(JqgridColumn.class);
            if(jqgridColumn != null ){
                JqgridFormat jqgridFormat = new JqgridFormat(jqgridColumn.displayName(),field.getName(),field.getName(),jqgridColumn.editable(),jqgridColumn.search(),jqgridColumn.hidden());
                list.add(jqgridFormat);
            }
        }
        return list;
    }
}
