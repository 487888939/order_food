package com.zh.order_food.Entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private static final Long serialVersionUID=2L;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private String email;
    private String sex;
    private String idNumber;
    private String avatar;
    private Integer status;
}
