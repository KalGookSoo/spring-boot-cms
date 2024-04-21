package com.kalgooksoo.cms.user.service;

import com.kalgooksoo.cms.user.command.CreateUserCommand;
import com.kalgooksoo.cms.user.command.UpdateUserCommand;
import com.kalgooksoo.cms.user.entity.Authority;
import com.kalgooksoo.cms.user.entity.ContactNumber;
import com.kalgooksoo.cms.user.entity.Email;
import com.kalgooksoo.cms.user.entity.User;
import com.kalgooksoo.cms.user.model.UserPrincipal;
import com.kalgooksoo.cms.user.repository.UserRepository;
import com.kalgooksoo.cms.user.search.UserSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @see UserService
 */
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * @see UserService#createUser(CreateUserCommand)
     */
    @Override
    public User createUser(@NonNull CreateUserCommand command) {
        Email email = new Email(command.emailId(), command.emailDomain());
        ContactNumber contactNumber = new ContactNumber(command.firstContactNumber(), command.middleContactNumber(), command.lastContactNumber());
        User user = User.create(command.username(), command.password(), command.name(), email, contactNumber);
        user.changePassword(passwordEncoder.encode(user.getPassword()));
        Authority authority = Authority.create("ROLE_USER", user);
        user.addAuthority(authority);
        return userRepository.save(user);
    }

    /**
     * @see UserService#createAdmin(CreateUserCommand)
     */
    @Override
    public User createAdmin(@NonNull CreateUserCommand command) {
        Email email = new Email(command.emailId(), command.emailDomain());
        ContactNumber contactNumber = new ContactNumber(command.firstContactNumber(), command.middleContactNumber(), command.lastContactNumber());
        User user = User.create(command.username(), command.password(), command.name(), email, contactNumber);
        user.changePassword(passwordEncoder.encode(user.getPassword()));
        Authority authority = Authority.create("ROLE_ADMIN", user);
        user.addAuthority(authority);
        return userRepository.save(user);
    }

    /**
     * @see UserService#update(String, UpdateUserCommand)
     */
    @Override
    public User update(@NonNull String id, @NonNull UpdateUserCommand command) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("계정을 찾을 수 없습니다."));
        String name = command.name();
        Email email = new Email(command.emailId(), command.emailDomain());
        ContactNumber contactNumber = new ContactNumber(command.firstContactNumber(), command.middleContactNumber(), command.lastContactNumber());
        user.update(name, email, contactNumber);
        return userRepository.save(user);
    }

    /**
     * @see UserService#findById(String)
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(@NonNull String id) {
        return userRepository.findById(id);
    }

    /**
     * @see UserService#findAll(Pageable)
     */
    @Transactional(readOnly = true)
    @Override
    public Page<User> findAll(@NonNull Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * @see UserService#findAll(UserSearch, Pageable)
     */
    @Transactional(readOnly = true)
    @Override
    public Page<User> findAll(@NonNull UserSearch search, @NonNull Pageable pageable) {
        return userRepository.searchAll(search, pageable);
    }

    /**
     * @see UserService#delete(String)
     * 삭제 연산은 권한을 가진자에게만 허용할 예정이라 노출해도 무방할 것 같다고 판단하여 예외 처리를 하였습니다.
     */
    @Override
    public void delete(@NonNull String id) {
        User user = userRepository.getReferenceById(id);
        user.removeAuthorities();
        userRepository.deleteById(id);
    }

    /**
     * @see UserService#updatePassword(String, String, String)
     */
    @Override
    public void updatePassword(@NonNull String id, @NonNull String originPassword, @NonNull String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("계정을 찾을 수 없습니다."));
        if (!passwordEncoder.matches(originPassword, user.getPassword())) {
            throw new IllegalArgumentException("계정 정보가 일치하지 않습니다.");
        }
        user.changePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * 계정 정보를 검증합니다.
     *
     * @param username 계정명
     * @param password 패스워드
     * @return 계정 정보가 일치하면 계정 정보를 반환합니다.
     * @throws IllegalArgumentException 보안상 이유로 계정 정보가 일치하지 않는 경우, 계정을 찾지 못한 경우 모두 같은 예외를 발생시킵니다.
     * @see UserService#verify(String, String)
     */
    @Override
    public UserPrincipal verify(@NonNull String username, @NonNull String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("계정 정보가 일치하지 않습니다."));
        if (passwordEncoder.matches(password, user.getPassword())) {
            Set<String> authorityNames = user.getAuthorities()
                    .stream()
                    .map(Authority::getName)
                    .collect(Collectors.toSet());
            return new UserPrincipal(user.getUsername(), user.getPassword(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired(), authorityNames);
        }
        throw new IllegalArgumentException("계정 정보가 일치하지 않습니다.");
    }

}
