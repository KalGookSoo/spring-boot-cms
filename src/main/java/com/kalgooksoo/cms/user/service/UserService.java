package com.kalgooksoo.cms.user.service;

import com.kalgooksoo.cms.user.command.CreateUserCommand;
import com.kalgooksoo.cms.user.command.UpdateUserCommand;
import com.kalgooksoo.cms.user.entity.User;
import com.kalgooksoo.cms.user.model.UserPrincipal;
import com.kalgooksoo.cms.user.search.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * 계정 서비스
 */
public interface UserService {

    /**
     * 계정 생성
     *
     * @param command 계정 생성 커맨드
     * @return 생성된 계정
     */
    User createUser(@NonNull CreateUserCommand command);

    /**
     * 관리자 계정 생성
     *
     * @param command 계정 생성 커맨드
     * @return 생성된 계정
     */
    User createAdmin(@NonNull CreateUserCommand command);

    /**
     * 계정 수정
     *
     * @param id      계정 식별자
     * @param command 수정 명령
     * @return 수정된 계정
     */
    User update(@NonNull String id, @NonNull UpdateUserCommand command);

    /**
     * 계정 식별자로 계정 조회
     *
     * @param id 계정 식별자
     * @return 계정
     */
    Optional<User> findById(@NonNull String id);

    /**
     * 계정 목록 조회
     *
     * @param pageable 페이지 정보
     * @return 계정 목록
     */
    Page<User> findAll(@NonNull Pageable pageable);

    /**
     * 계정 목록 조회
     *
     * @param search   검색 조건
     * @param pageable 페이지 정보
     * @return 계정 목록
     */
    Page<User> findAll(@NonNull UserSearch search, @NonNull Pageable pageable);

    /**
     * 계정 삭제
     *
     * @param id 계정 식별자
     */
    void delete(@NonNull String id);

    /**
     * 패스워드 변경
     *
     * @param id             계정 식별자
     * @param originPassword 기존 패스워드
     * @param newPassword    새로운 패스워드
     */
    void updatePassword(@NonNull String id, @NonNull String originPassword, @NonNull String newPassword);

    /**
     * 계정 검증
     *
     * @param username 계정명
     * @param password 패스워드
     * @return 계정
     */
    UserPrincipal verify(@NonNull String username, @NonNull String password);

}
