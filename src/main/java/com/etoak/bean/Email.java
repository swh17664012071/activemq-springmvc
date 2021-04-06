package com.etoak.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    // 接收人
    private String receiver;

    // 邮件主题
    private String subject;

    // 邮件内容
    private String content;
}
