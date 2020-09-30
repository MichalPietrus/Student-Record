package com.studentrecord.web;

import com.studentrecord.model.*;
import com.studentrecord.service.GradeService;
import com.studentrecord.service.SchoolClassService;
import com.studentrecord.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/nauczyciel")
public class TeacherController {

    private final UserService userService;

    private final SchoolClassService schoolClassService;

    private final GradeService gradeService;

    private final List<String> categories = Arrays.asList("Sprawdzian", "Kartkówka",
            "Odpowiedź ustna", "Zadanie domowe", "Projekt", "inne", "bz", "np", "zw");
    private final List<Integer> ratings = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
    private final List<Integer> ratingWeights = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    public TeacherController(UserService userService, SchoolClassService schoolClassService, GradeService gradeService) {
        this.userService = userService;
        this.schoolClassService = schoolClassService;
        this.gradeService = gradeService;
    }

    @GetMapping("/wybierz-klase")
    public String showChooseClassForm(Model model, Principal principal) {
        String activeUserEmail = principal.getName();
        List<SchoolClass> schoolClassList = schoolClassService.findAll();
        List<SchoolClass> schoolClasses = new ArrayList<>();
        for (SchoolClass schoolClass : schoolClassList) {
            for (Subject subject : schoolClass.getSubjects()) {
                if (subject.getTeacher().getEmail().equals(activeUserEmail))   // Checks if subject teacher email is the same as the logged in teacher
                    if (!schoolClasses.contains(schoolClass))
                        schoolClasses.add(schoolClass); // If schoolClasses do not have previously downloaded class, it adds it to the class list
            }
        }
        model.addAttribute("schoolClasses", schoolClasses);
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
    public String showAddGradeForm(Model model,
                                   @PathVariable("userId") Long id,
                                   @PathVariable("subject-name") String subjectName,
                                   @PathVariable Integer semester) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        model.addAttribute("grade", new Grade());
        model.addAttribute("user", user);
        model.addAttribute("subjectName", subjectName);
        model.addAttribute("semester", semester);
        model.addAttribute("categories", categories);
        model.addAttribute("ratings", ratings);
        model.addAttribute("ratingWeights", ratingWeights);
        return "teacher/add-grade";
    }

    @PostMapping("/dodaj-ocene/{userId}/{subject-name}/{semester}")
    public String addGrade(@PathVariable("userId") Long id,
                           @PathVariable("subject-name") String subjectName,
                           @PathVariable("semester") Integer semester,
                           @ModelAttribute("grade") Grade grade,
                           RedirectAttributes redirectAttributes) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        gradeService.setGradeDetails(subjectName,user,grade,semester);
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
        String currentCategory = grade.getCategory();
        int currentRating = grade.getRating();
        int currentRatingWeight = grade.getRatingWeight();
        model.addAttribute("currentCategory", currentCategory);
        model.addAttribute("currentRating", currentRating);
        model.addAttribute("currentRatingWeight", currentRatingWeight);
        model.addAttribute("grade", grade);
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
                                 @PathVariable("gradeId") Integer gradeId,
                                 @ModelAttribute("grade") Grade grade,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        grade.setId(gradeId);
        gradeService.setGradeDetails(subjectName,user,grade,semester);
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
        gradeService.setGradeDetails(subjectName, user, grade,semester);
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
        userService.setTeacherDetails(user, userDetails, placeOfResident, userDb);
        if (userResult.hasErrors() || userDetailsResult.hasErrors() || placeOfResidentResult.hasErrors()) {
            return "teacher/user-details";
        }
        if (user.getPassword().equals(""))
            userService.saveWithoutEncoding(userDb);
        else {
            userDb.setPassword(user.getPassword());
            userService.saveAndEncode(userDb);
        }
        return "redirect:/";
    }

}


