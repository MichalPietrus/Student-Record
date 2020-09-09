package com.studentrecord.web;

import com.studentrecord.model.*;
import com.studentrecord.service.GradeService;
import com.studentrecord.service.SchoolClassService;
import com.studentrecord.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/nauczyciel")
public class TeacherController {

    @Autowired
    private UserService userService;

    @Autowired
    private SchoolClassService schoolClassService;

    @Autowired
    private GradeService gradeService;

    private final List<String> categories = Arrays.asList("Sprawdzian", "Kartkówka",
            "Odpowiedź ustna", "Zadanie domowe", "Projekt", "inne", "bz", "np", "zw");
    private final List<Integer> ratings = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
    private final List<Integer> ratingWeights = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    @GetMapping("/panel")
    public String showTeacherPanel() {
        return "teacher/teacher-panel";
    }

    @GetMapping("/wybierz-klase")
    public String showChooseClassForm(Model model, Principal principal) {
        String activeUserEmail = principal.getName();
        List<SchoolClass> schoolClassList = schoolClassService.findAll();
        List<SchoolClass> schoolClasses = new ArrayList<>();
        List<Subject> subjectsList = new ArrayList<>();
        for (int i = 0; i < schoolClassList.size(); i++) {
            List<Subject> subjects = schoolClassList.get(i).getSubjects();
            for (int x = 0; x < subjects.size(); x++) {
                if (subjects.get(x).getTeacher().getEmail().equals(activeUserEmail)) {
                    if (!schoolClasses.contains(schoolClassList.get(i)))
                        schoolClasses.add(schoolClassList.get(i));
                    if (!subjectsList.isEmpty()) {
                        for (int y = 0; y < subjectsList.size(); y++) {
                            if (!subjectsList.get(y).equals(subjects.get(x)))
                                subjectsList.add(subjects.get(x));
                        }
                    } else
                        subjectsList.add(subjects.get(x));
                }
            }
        }
        model.addAttribute("schoolClasses", schoolClasses);
        model.addAttribute("subjects", subjectsList);
        return "teacher/choose-class";
    }

    @PostMapping("/wybierz-klase")
    public String choosedClass(@RequestParam(value = "schoolClass.id") Integer id,
                               @RequestParam(value = "subject.name") String subjectName,
                               RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("classId", id);
        redirectAttributes.addAttribute("subject-name", subjectName);
        return "redirect:/nauczyciel/uczniowie/{classId}/{subject-name}";
    }

    @GetMapping("/uczniowie/{classId}/{subject-name}")
    public String showUsersForm(Model model, @PathVariable("classId") Integer classId,
                                @PathVariable("subject-name") String subjectName) {
        SchoolClass schoolClass = schoolClassService.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schoolClass Id: " + classId));
        List<User> students = schoolClass.getUsers();
        students.sort(Comparator.comparing(User::getStudentNumber));
        model.addAttribute("schoolClass", schoolClass);
        model.addAttribute("subjectName", subjectName);
        model.addAttribute("users", students);
        return "teacher/students-list";
    }

    @GetMapping("/dodaj-ocene/{userId}/{subject-name}/{semester}")
    public String showAddGradeForm(Model model, @PathVariable("userId") Long id, @PathVariable("subject-name") String subjectName, @PathVariable Integer semester) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        Grade grade = new Grade();
        model.addAttribute("grade", grade);
        model.addAttribute("user", user);
        model.addAttribute("subjectName", subjectName);
        model.addAttribute("categories", categories);
        model.addAttribute("ratings", ratings);
        model.addAttribute("ratingWeights", ratingWeights);
        model.addAttribute("semester", semester);
        return "teacher/add-grade";
    }

    @PostMapping("/dodaj-ocene/{userId}/{subject-name}/{semester}")
    public String addGrade(@PathVariable("userId") Long id,
                           @PathVariable("subject-name") String subjectName,
                           @PathVariable("semester") Integer semester,
                           @RequestParam(value = "category") String category,
                           @RequestParam(value = "rating") Integer rating,
                           @RequestParam(value = "ratingWeight") Integer ratingWeight,
                           @ModelAttribute("grade") Grade grade,
                           RedirectAttributes redirectAttributes) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        ControllersHelper.setGradeDetails(subjectName, semester, category, rating, ratingWeight, user, grade);
        user.addGrade(grade);
        userService.saveWithoutEncoding(user);
        redirectAttributes.addAttribute("classId", user.getSchoolClass().getId());
        redirectAttributes.addAttribute("subject-name", subjectName);
        return "redirect:/nauczyciel/uczniowie/{classId}/{subject-name}";
    }

    @GetMapping("/edytuj-ocene/{userId}/{subject-name}/{semester}/{gradeId}")
    public String showEditGradeForm(Model model,
                                    @PathVariable("userId") Long id,
                                    @PathVariable("subject-name") String subjectName,
                                    @PathVariable Integer semester,
                                    @PathVariable Integer gradeId) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        Grade grade = gradeService.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid grade Id: " + gradeId));
        model.addAttribute("currentCategory", grade.getCategory());
        model.addAttribute("currentRating", grade.getRating());
        model.addAttribute("currentRatingWeight", grade.getRatingWeight());
        model.addAttribute("grade", gradeService.findById(gradeId));
        model.addAttribute("user", user);
        model.addAttribute("subjectName", subjectName);
        model.addAttribute("categories", categories);
        model.addAttribute("ratings", ratings);
        model.addAttribute("ratingWeights", ratingWeights);
        model.addAttribute("semester", semester);
        return "teacher/edit-grade";
    }

    @PostMapping("/edytuj-ocene/{userId}/{subject-name}/{semester}/{gradeId}")
    public String editGradeModel(@PathVariable("userId") Long id,
                                 @PathVariable("subject-name") String subjectName,
                                 @PathVariable("semester") Integer semester,
                                 @RequestParam(value = "category") String category,
                                 @RequestParam(value = "rating") Integer rating,
                                 @RequestParam(value = "ratingWeight") Integer ratingWeight,
                                 @RequestParam(value = "comment") String comment,
                                 @PathVariable("gradeId") Integer gradeId,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        Grade grade = gradeService.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid grade Id: " + gradeId));
        ControllersHelper.setGradeDetails(subjectName, semester, category, rating, ratingWeight, user, grade);
        grade.setComment(comment);
        grade.setStudent(user);
        userService.saveWithoutEncoding(user);
        redirectAttributes.addAttribute("classId", user.getSchoolClass().getId());
        redirectAttributes.addAttribute("subject-name", subjectName);
        return "redirect:/nauczyciel/uczniowie/{classId}/{subject-name}";
    }

    @PostMapping("/usun-ocene/{userId}/{subject-name}/{semester}/{gradeId}")
    public String deleteGrade(@PathVariable("userId") Long id,
                              @PathVariable("subject-name") String subjectName,
                              @PathVariable("gradeId") Integer gradeId,
                              @PathVariable("semester") Integer semester,
                              RedirectAttributes redirectAttributes) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        gradeService.deleteById(gradeId);
        redirectAttributes.addAttribute("classId", user.getSchoolClass().getId());
        redirectAttributes.addAttribute("subject-name", subjectName);
        return "redirect:/nauczyciel/uczniowie/{classId}/{subject-name}";
    }

    @GetMapping("/dodaj-ocene-koncowa/{userId}/{subject-name}/{semester}/{isFinal}/{gradeId}")
    public String showFinalGradeForm(Model model,
                                     @PathVariable("userId") Long id,
                                     @PathVariable("subject-name") String subjectName,
                                     @PathVariable("semester") Integer semester,
                                     @PathVariable("gradeId") Integer gradeId,
                                     @PathVariable("isFinal") String isFinal) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        Grade grade;
        if (gradeId == 0)
            grade = new Grade();
        else
            grade = gradeService.findById(gradeId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid grade Id: " + gradeId));
        model.addAttribute("grade", grade);
        model.addAttribute("user", user);
        model.addAttribute("ratings", ratings);
        model.addAttribute("subjectName", subjectName);
        model.addAttribute("semester", semester);
        model.addAttribute("isFinal", isFinal);
        return "teacher/add-final-grade";
    }

    @PostMapping("/dodaj-ocene-koncowa/{userId}/{subject-name}/{semester}/{isFinal}/{gradeId}")
    public String saveFinalGrade(@PathVariable("userId") Long id,
                                 @PathVariable("subject-name") String subjectName,
                                 @PathVariable("semester") Integer semester,
                                 @PathVariable("isFinal") String isFinal,
                                 @PathVariable("gradeId") Integer gradeId,
                                 @RequestParam("rating") Integer rating,
                                 @ModelAttribute("grade") Grade grade,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        grade.setRating(rating);
        grade.setCategory(isFinal);
        grade.setIsFinal(isFinal);
        grade.setSemester(semester);
        ControllersHelper.setSubjectForGrade(subjectName, user, grade);
        grade.setTimestamp(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()));
        user.addGrade(grade);
        userService.saveWithoutEncoding(user);
        redirectAttributes.addAttribute("classId", user.getSchoolClass().getId());
        redirectAttributes.addAttribute("subject-name", subjectName);
        return "redirect:/nauczyciel/uczniowie/{classId}/{subject-name}";
    }

    @GetMapping("/szczegoly-uzytkownika")
    public String showUserDetailsForm(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        UserDetailsDB userDetailsDB;
        PlaceOfResident placeOfResident;
        if (user.getUserDetailsDB() == null) {
            userDetailsDB = new UserDetailsDB();
            placeOfResident = new PlaceOfResident();
        } else {
            userDetailsDB = user.getUserDetailsDB();
            placeOfResident = user.getUserDetailsDB().getPlaceOfResident();
        }
        model.addAttribute("userDetails", userDetailsDB);
        model.addAttribute("placeOfResident", placeOfResident);
        model.addAttribute("user", user);
        return "teacher/user-details";
    }

    @PostMapping("/zapisz-szczegoly-uzytkownika")
    public String saveUserDetails(@ModelAttribute("user") @Valid User user, BindingResult userResult,
                                  @ModelAttribute("userDetails") @Valid UserDetailsDB userDetails, BindingResult userDetailsResult,
                                  @ModelAttribute("placeOfResident") @Valid PlaceOfResident placeOfResident, BindingResult placeOfResidentResult,
                                  Principal principal) {
        User userDb = userService.findByEmail(principal.getName());
        ControllersHelper.setTeacherDetails(user, userDetails, placeOfResident, userDb);
        if (userResult.hasErrors() || userDetailsResult.hasErrors() || placeOfResidentResult.hasErrors()) {
            return "teacher/user-details";
        }
        if (user.getPassword().equals(""))
            userService.saveWithoutEncoding(userDb);
        else {
            userDb.setPassword(user.getPassword());
            userService.saveAndEncode(userDb);
        }
        return "redirect:/nauczyciel/panel";
    }

}


