package com.studentrecord.service;

import java.util.*;
import java.util.stream.Collectors;

import com.studentrecord.model.Role;
import com.studentrecord.model.User;
import com.studentrecord.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(new Role("ROLE_STUDENT")));
        return userRepository.save(user);
    }

    @Override
    public List<User> findByKeyword(String keyword) {
        return userRepository.findByKeyword(keyword);
    }

    public User saveAndEncode(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public Page<User> findAllPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findAllStudentsPageable(Pageable pageable) {
        return userRepository.findAllByRolesNameEquals("ROLE_STUDENT",pageable);
    }

    @Override
    public Page<User> findByKeywordPageable(String keyword, Pageable pageable) {
        return userRepository.findByKeywordPageable(keyword, pageable);
    }

    @Override
    public Page<User> findAllStudentsByKeywordPageable(String keyword, Pageable pageable) {
        return userRepository.findAllByKeywordAndRolesNameEquals(keyword,"ROLE_STUDENT",pageable);
    }

    @Override
    public User saveWithoutEncoding(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public List<User> findAllStudents() {
        List<User> users = userRepository.findAll();
        List<User> students = new ArrayList<>();
        extractStudents(users, students);
        return students;
    }

    @Override
    public List<User> findStudentsByKeyword(String keyword) {
        List<User> users = userRepository.findByKeyword(keyword);
        List<User> students = new ArrayList<>();
        extractStudents(users, students);
        return students;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Extracts Students (Users with Student Role) from the users list.
     */
    private void extractStudents(List<User> users, List<User> students) {
        for (User user : users) {
            Optional<Role> role = user.getRoles().stream()
                    .filter(u -> u.getName().equals("ROLE_STUDENT"))
                    .findAny();
            if (role.isPresent())
                students.add(user);
        }
    }
}
