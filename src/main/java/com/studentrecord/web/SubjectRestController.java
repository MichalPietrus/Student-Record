package com.studentrecord.web;

import com.studentrecord.model.SchoolClass;
import com.studentrecord.model.Subject;
import com.studentrecord.service.SchoolClassService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subject")
public class SubjectRestController {

    private final SchoolClassService schoolClassService;

    public SubjectRestController(SchoolClassService schoolClassService) {
        this.schoolClassService = schoolClassService;
    }

    @PostMapping("/{schoolClassName}")
    public List<String> getSubjects(@PathVariable String schoolClassName, Principal principal) {
        String activeUserEmail = principal.getName();
        String splitedSchoolClassName = schoolClassName.split(" ")[0];
        SchoolClass schoolClassList = schoolClassService.findByName(splitedSchoolClassName);
        List<Subject> subjectsList = new ArrayList<>();
        for (Subject subject : schoolClassList.getSubjects()) {
            if (subject.getTeacher().getEmail().equals(activeUserEmail)) {  // Checks if subject teacher email is the same as the logged in teacher
                if (!subjectsList.isEmpty()) {
                    for (int y = 0; y < subjectsList.size(); y++) { // Adds subject to subjectList if there is no subject with the same name in the list yet
                        if (subjectsList.stream()
                                .noneMatch(subject1 -> subject1.getName().equals(subject.getName())))
                            subjectsList.add(subject);
                    }
                } else
                    subjectsList.add(subject);
            }
        }
        return subjectsList.stream()
                .map(Subject::getName)
                .collect(Collectors.toList());
    }

}
