package com.example.scheduleviewer;

import java.util.ArrayList;

public class Course {
    int courseNumber;
    String name;
    String teacher;
    int period;
    String classroom;
    char term;
    int section;

    static ArrayList<Course> courseList = new ArrayList<>();

    public Course(int courseNumber, String name, String teacher, int period, String classroom, char term, int section) {
        this.courseNumber = courseNumber;
        this.name = name;
        this.teacher = teacher;
        this.period = period;
        this.classroom = classroom;
        this.term = term;
        this.section = section;
    }

    public Course(String name, String teacher, int period, String classroom) {
        this.name = name;
        this.teacher = teacher;
        this.period = period;
        this.classroom = classroom;
    }

    public Course() {
    }

    public static Course findCourse(String combined){
        String[] splited = combined.split(" ");
        String temp = "";
        Course result = new Course();
        for (int i = 0; i < splited.length; i++){
            try{
                int sec = Integer.parseInt(splited[i]);
                try {
                    Integer.parseInt(splited[i + 1]);
                }
                catch (Exception e){
                    for (Course course : courseList){
                        if (course.getName().equals(temp.substring(1)) && sec == course.getSection()){
                            result = course;
                            break;
                        }
                    }
                    break;
                }
            }
            catch (Exception e){}
            temp = temp + " " + splited[i];
        }
        return result;
    }

    public String combine(){
        return section == 0 ? (name + " " + teacher) : (name + " " + section + " " + teacher);
    }

    public String toString(){
        return ("Course Name: " + name + ", Classroom: " + classroom + ", Course Number: " + courseNumber + ", Period: " + period + ", Section: " + section + ", Teacher: " + teacher);
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getPeriod() {
        return period;
    }

    public String getClassroom() {
        return classroom;
    }

    public char getTerm() {
        return term;
    }

    public int getSection() {
        return section;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public void setTerm(char term) {
        this.term = term;
    }

    public void setSection(int section) {
        this.section = section;
    }
}