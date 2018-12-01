package service.endpoint;


import service.model.Student;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Path("students")
public class StudentsResources {
    List<Student> students = new ArrayList<>();

    public StudentsResources() {
        students.add(new Student(1,"Emil"));
        students.add(new Student(2,"Gosho"));
        students.add(new Student(3,"Pesho"));
        students.add(new Student(4,"Tosho"));
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String connect() { return "Student Service is up and running!"; }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createStudentAccount(Student student) {
        if (findStudent(student.getId()) != null) {
            return Response.serverError().entity("\nWARNING!!!!\nStudent already exists with this number").build();
        } else {
            students.add(student);
            return Response.noContent().build();
        }
    }

    private Student findStudent(int id) {
        if (students != null) {
            for (Student student : students) {
                if (student.getId() == id)
                    return student;
            }
        }
        return null;
    }

    @Path("list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getStudents() {
        return students;
    }
}
