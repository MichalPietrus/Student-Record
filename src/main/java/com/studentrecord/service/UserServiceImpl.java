package com.studentrecord.service;

import com.studentrecord.model.*;
import com.studentrecord.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(new Role("ROLE_STUDENT")));
        userRepository.save(user);
    }

    @Override
    public void setStudentNumbers(String schoolClassName, User user, SchoolClass oldSchoolClass) {
        if (oldSchoolClass != null && !oldSchoolClass.getName().equals(schoolClassName)) {
            oldSchoolClass.getUsers().remove(user);
            List<User> oldClassStudents = oldSchoolClass.getUsers();
            oldClassStudents.sort(Comparator.comparing(User::getFirstName));
            for (int x = 0; x < oldClassStudents.size(); x++)
                oldClassStudents.get(x).setStudentNumber(x + 1);
        }
    }

    @Override
    public void saveAndEncode(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Override
    public Page<User> findAllPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findAllStudentsPageable(Pageable pageable) {
        return userRepository.findAllByRolesNameEquals("ROLE_STUDENT", pageable);
    }

    @Override
    public Page<User> findByKeywordPageable(String keyword, Pageable pageable) {
        return userRepository.findByKeywordPageable(keyword, pageable);
    }

    @Override
    public Page<User> findAllByKeywordAndRolePageable(String keyword, String role, Pageable pageable) {
        return userRepository.findAllByKeywordAndRolesNameEqualsPageable(keyword, role, pageable);
    }

    @Override
    public List<User> findAllByRoleName(String role) {
        return userRepository.findAllByRolesNameEquals(role);
    }

    @Override
    public void saveWithoutEncoding(User user) {
        userRepository.saveAndFlush(user);
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

    @Override
    public void setUserDetailsAndModels(Model model, User user) {
        UserDetailsDB userDetailsDB;
        PlaceOfResident placeOfResident;
        Parent parent;
        if (user.getUserDetailsDB() == null) {
            userDetailsDB = new UserDetailsDB();
            placeOfResident = new PlaceOfResident();
            parent = new Parent();
        } else {
            userDetailsDB = user.getUserDetailsDB();
            placeOfResident = user.getUserDetailsDB().getPlaceOfResident();
            parent = user.getUserDetailsDB().getParent();
        }
        model.addAttribute("userDetails", userDetailsDB);
        model.addAttribute("placeOfResident", placeOfResident);
        model.addAttribute("parent", parent);
        model.addAttribute("user", user);
    }

    @Override
    public void setUserDetails(User user, UserDetailsDB userDetails, Parent parent, PlaceOfResident placeOfResident, User userDB) {
        int userDetailsDbId = 0;
        int userDBParentId = 0;
        int userDBPlaceOfResidentId = 0;
        if (userDB.getUserDetailsDB() != null) {
            userDetailsDbId = userDB.getUserDetailsDB().getId();
            userDBParentId = userDB.getUserDetailsDB().getParent().getId();
            userDBPlaceOfResidentId = userDB.getUserDetailsDB().getPlaceOfResident().getId();
        }
        userDB.setFirstName(user.getFirstName());
        userDB.setLastName(user.getLastName());
        userDB.setEmail(user.getEmail());
        userDB.setPESEL(user.getPESEL());
        userDB.setUserDetailsDB(userDetails);
        userDB.getUserDetailsDB().setParent(parent);
        userDB.getUserDetailsDB().setPlaceOfResident(placeOfResident);
        if (userDetailsDbId != 0 && userDBParentId != 0 && userDBPlaceOfResidentId != 0) {
            userDB.getUserDetailsDB().setId(userDetailsDbId);
            userDB.getUserDetailsDB().getParent().setId(userDBParentId);
            userDB.getUserDetailsDB().getPlaceOfResident().setId(userDBPlaceOfResidentId);
        }
    }

    @Override
    public void extractTeachers(List<User> users, List<User> teachers) {
        for (User user : users) {
            Optional<Role> role = user.getRoles().stream()
                    .filter(u -> u.getName().equals("ROLE_TEACHER"))
                    .findAny();
            if (role.isPresent())
                teachers.add(user);
        }
    }

    @Override
    public void setTeacherDetails(User user, UserDetailsDB userDetails, PlaceOfResident placeOfResident, User userDB) {
        int userDetailsDbId = 0;
        int userDBPlaceOfResidentId = 0;
        if (userDB.getUserDetailsDB() != null) {
            userDetailsDbId = userDB.getUserDetailsDB().getId();
            userDBPlaceOfResidentId = userDB.getUserDetailsDB().getPlaceOfResident().getId();
        }
        userDB.setFirstName(user.getFirstName());
        userDB.setLastName(user.getLastName());
        userDB.setEmail(user.getEmail());
        userDB.setPESEL(user.getPESEL());
        userDB.setUserDetailsDB(userDetails);
        userDB.getUserDetailsDB().setPlaceOfResident(placeOfResident);
        if (userDetailsDbId != 0 && userDBPlaceOfResidentId != 0) {
            userDB.getUserDetailsDB().setId(userDetailsDbId);
            userDB.getUserDetailsDB().getPlaceOfResident().setId(userDBPlaceOfResidentId);
        }
    }

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
