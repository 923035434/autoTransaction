package com.ll.autotransaction.dao.mode;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("system_config")
public class SystemConfigDo {


    private Integer id;

    private String key;

    private String value;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
