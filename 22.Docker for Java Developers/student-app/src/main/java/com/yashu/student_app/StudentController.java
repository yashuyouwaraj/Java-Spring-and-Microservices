package com.yashu.student_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {
    @Autowired
    StudentRepo repo;

    @RequestMapping("/getStudents")
    public List<Student> getStudents(){
        return repo.findAll();
    }

    @RequestMapping("/addStudents")
    public void addStudent(){
        Student s = new Student("Raj",26);
                repo.save(s);
    }
}
