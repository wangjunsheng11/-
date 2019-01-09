package com.kakacl.product_service.domain;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 能力实体类
 * @date 2019-01-09
 */
public class Ability {

    private String id;
    private String name;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
