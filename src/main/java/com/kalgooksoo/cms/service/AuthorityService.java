package com.kalgooksoo.cms.service;

import com.kalgooksoo.cms.command.AuthoritySaveCommand;
import com.kalgooksoo.cms.entity.Authority;

import java.util.List;

public interface AuthorityService {

    List<Authority> findAll();

    void saveAll(List<AuthoritySaveCommand> commands);

    void deleteAll(List<String> ids);

}
