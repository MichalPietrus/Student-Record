# Electronic Student Record

My main goal while creating this project was to learn spring, thymeleaf, html and MySQL as well as to consolidate my knowledge in Java.
Languages and frameworks used in the project:

- Java
- Spring boot, Spring Data, Spring MVC, Spring Security, Thymeleaf
- MySQL, Hibernate
- HTML, Bootstrap
- Maven

Things I would have done differently:
- The appearance of the page and dynamic change of the page content - e.g. filtering the table content.
- Security is failing in some places, unfortunately I was unable to deal with this problem.

TODO List:
- Absences
- Lesson plan
- Expansion of the administration and moderation panel
- Scale up the application so that it can run in multiple schools, not just one
- Unit tests

# Project Presentation:

## Sign in page :

![Sign in](src/main/resources/static/readmeImages/sign_in.png)

## Sign up page :

![Sign up](src/main/resources/static/readmeImages/sign_up.png)

## Index: The content of this page varies depending on the permissions of the logged in user.
#### - The administrator has access to the moderator and administrator panel
#### - The moderator has access to the moderator's panel
#### - The teacher has access to the teacher's panel
#### - The student has access to the student panel

![index](src/main/resources/static/readmeImages/index.png)

## Add class: Allows you to add a new class with the appropriate year and specialization. Each field has a corresponding validation

![add class](src/main/resources/static/readmeImages/add_class.png)

## Add subject: Adds a subject to a given class and also assigns a teacher (User with the role of teacher) to the subject

![add subject](src/main/resources/static/readmeImages/add_subject.png)

## User list: The main admin panel that allows you to edit user data, change their roles and also delete them. Also have a search button

![users list](src/main/resources/static/readmeImages/users_list.png)

## User details: Allows you to set the user details of the content validation in the fields:
#### - PESEL
#### - Email
#### - Postal Code
#### - Phone number

![user details](src/main/resources/static/readmeImages/user_details.png)

## Change user role : allows you to change user role

![change role](src/main/resources/static/readmeImages/change_role.png)

## List of Students in the moderator's panel with a search button.

![students list](src/main/resources/static/readmeImages/students_list.png)

## Allows to change the class of the students, it is worth adding that when changing the class, the students numbers in the class are set and changed automatically

![change_class](src/main/resources/static/readmeImages/change_class.png)

## Teacher have 2 panels one is user details which is similar to user details from admin panel and the other is add grades panel

![select class subject](src/main/resources/static/readmeImages/select_class_subject.png)

## After selecting your class and subject, you will see this panel :
## Here you can add partial grades, predicted final grades and final grades, you can also edit these grades, The average grade is displayed and calculated automatically

![teacher panel](src/main/resources/static/readmeImages/teacher_panel.png)

## Here you can add a new grade

![add grade](src/main/resources/static/readmeImages/add_grade.png)

## And here you can edit it when you add or edit a rating, the date of its issuance is set automatically, previously selected rating settings are displayed automatically

![edit grade](src/main/resources/static/readmeImages/edit_grade.png)

## Adding Final Predicted grade and Final grade looks the same

![add final grade](src/main/resources/static/readmeImages/add_final_grade.png)

## User have only 2 panels one is user details which is similar to the admin user details and also show grades panel.
## The student can view his grades here

![show grade](src/main/resources/static/readmeImages/show_grades.png)

## When a student clicks on a grade, then he can see the grade details

![grade details](src/main/resources/static/readmeImages/grade_details.png)