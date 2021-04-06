package com.etoak.bean;

import lombok.Data;

/** sys_user表 */
@Data
public class User {

    private Integer id;
    private String name;
    private String password;
    private String email;
    private Integer age;
    private Integer state;
    private String createTime;
}
