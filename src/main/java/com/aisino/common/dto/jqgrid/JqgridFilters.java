package com.aisino.common.dto.jqgrid;


import com.aisino.core.mybatis.specification.QueryLike;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 为 on 2017-4-12.
 */
public class JqgridFilters{
    private String groupOp = QueryLike.OPERATOR_AND;

    private List<Rule> rules = new ArrayList<>();

    public JqgridFilters(String groupOp, List<Rule> rules) {
        this.groupOp = groupOp;
        this.rules = rules;
    }

    public JqgridFilters() {
    }


    //-----------------------------------getter and setter--------------------------------------------

    public String getGroupOp() {
        return groupOp;
    }

    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public static class Rule{

        public Rule(String field,String op,String data){
            this.field = field;
            this.op = op;
            this.data = data;
        }

        /**
         * 属性名,数据库字段名
         */
        private String field;
        /**
         * 操作符
         */
        private String op;
        /**
         * 参考数据
         */
        private String data;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getOp() {
            return op;
        }

        public void setOp(String op) {
            this.op = op;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
