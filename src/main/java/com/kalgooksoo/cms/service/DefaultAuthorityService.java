package com.kalgooksoo.cms.service;

import com.kalgooksoo.cms.command.AuthoritySaveCommand;
import com.kalgooksoo.cms.entity.Authority;
import com.kalgooksoo.cms.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class DefaultAuthorityService implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }

    @Override
    public void saveAll(List<AuthoritySaveCommand> commands) {
        // 생성 목록
        List<Authority> authorities = new ArrayList<>();
        commands.stream()
                .filter(command -> command.getId() == null)
                .map(Authority::create)
                .forEach(authorities::add);

        // 수정 목록
        List<AuthoritySaveCommand> updateCommands = commands.stream()
                .filter(command -> command.getId() != null)
                .toList();
        List<String> ids = updateCommands.stream()
                .map(AuthoritySaveCommand::getId)
                .toList();
        Map<String, Authority> foundAuthorities = authorityRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(Authority::getId, Function.identity()));
        for (AuthoritySaveCommand updateCommand : updateCommands) {
            Authority authority = foundAuthorities.get(updateCommand.getId());
            authority.update(updateCommand);
            authorities.add(authority);
        }

        authorityRepository.saveAll(authorities);
    }

    @Override
    public void deleteAll(List<String> ids) {
//        authorityRepository.deleteAllById(ids);
        authorityRepository.deleteAllByIdInBatch(ids);
    }
}
