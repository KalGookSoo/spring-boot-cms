package com.kalgooksoo.cms.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "권한 저장 커맨드")
@Data
public class AuthoritySaveCommand {

    private String id;

    private String name;

    private String alias;

}
