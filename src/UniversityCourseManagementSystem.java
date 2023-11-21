import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This class is responsible for orchestrating a university management system.
 * It manages the lifecycle of students, professors and courses.
 * The main method takes commands from terminal and calls corresponded actions from the university management system.
 *
 * @see UniversityCourseManagementSystem#main(String[] args)
 */
public class UniversityCourseManagementSystem {

    /**
     * This method is the entry point to the university management system.
     * It accepts a series of commands via terminal input. Supported commands are:
     * "course" - to create a new course
     * "student" - to register a new student
     * "professor" - to register a new professor
     * "enroll" - to enroll a student in a course
     * "drop" - to unregister a student from a course
     * "teach" - to assign a course to a professor
     * "exempt" - to remove a course assignment from a professor
     * If the command is unrecognized, or if any error occurs during execution of
     * a command, the program will terminate with a corresponding message.
     *
     * @see UniversityCourseManagementSystem#course(List, Course)
     * @see UniversityCourseManagementSystem#student(List, Student)
     * @see UniversityCourseManagementSystem#professor(List, Professor)
     * @see UniversityCourseManagementSystem#enroll(List, List, int, int)
     * @see UniversityCourseManagementSystem#drop(List, List, int, int)
     * @see UniversityCourseManagementSystem#teach(List, List, int, int)
     * @see UniversityCourseManagementSystem#exempt(List, List, int, int)
     * @param args commands and inputs needed for them.
     */
    public static void main(String[] args) {
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Professor> professors = new ArrayList<>();

        fillInitialData(courses, students, professors);
        Scanner sc = new Scanner(System.in);
        try {
            while (sc.hasNextLine()) {
                String command = sc.nextLine();
                switch (command) {
                    case ("course"):
                        String courseName = sc.nextLine().toLowerCase();
                        if (checkCourseName(courseName, courses)) {
                            System.out.println("Wrong inputs");
                            System.exit(0);
                        }
                        String level = sc.nextLine().toUpperCase();
                        Course currentCourse = new Course(courseName, CourseLevel.valueOf(level));
                        course(courses, currentCourse);
                        break;
                    case ("student"):
                        String studentName = sc.nextLine().toLowerCase();
                        if (checkName(studentName)) {
                            System.out.println("Wrong inputs");
                            System.exit(0);
                        }
                        Student currentStudent = new Student(studentName);
                        student(students, currentStudent);
                        break;
                    case ("professor"):
                        String professorName = sc.nextLine().toLowerCase();
                        if (checkName(professorName)) {
                            System.out.println("Wrong inputs");
                            System.exit(0);
                        }
                        Professor currentProfessor = new Professor(professorName);
                        professor(professors, currentProfessor);
                        break;
                    case ("enroll"):
                        int enrollMemberId = Integer.parseInt(sc.nextLine());
                        int enrollCourseId = Integer.parseInt(sc.nextLine());
                        enroll(courses, students, enrollMemberId, enrollCourseId);
                        break;
                    case ("drop"):
                        int dropMemberId = Integer.parseInt(sc.nextLine());
                        int dropCourseId = Integer.parseInt(sc.nextLine());
                        drop(courses, students, dropMemberId, dropCourseId);
                        break;
                    case ("teach"):
                        int teachMemberId = Integer.parseInt(sc.nextLine());
                        int teachCourseId = Integer.parseInt(sc.nextLine());
                        teach(courses, professors, teachMemberId, teachCourseId);
                        break;
                    case ("exempt"):
                        int exemptMemberId = Integer.parseInt(sc.nextLine());
                        int exemptCourseId = Integer.parseInt(sc.nextLine());
                        exempt(courses, professors, exemptMemberId, exemptCourseId);
                        break;
                    default:
                        System.out.println("Wrong inputs");
                        System.exit(0);
                }
            }
        } catch (Exception exception) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        System.exit(0);
    }

    /**
     * Add course to the courses list or exit the system
     * if the course already exists.
     *
     * @param courses List of courses
     * @param currentCourse Current course to be added
     */
    private static void course(List<Course> courses, Course currentCourse) {
        if (courses.contains(currentCourse)) {
            System.out.println("Course exists");
            System.exit(0);
        } else {
            courses.add(currentCourse);
            System.out.println("Added successfully");
        }
    }

    /**
     * Adds a student to the students list.
     *
     * @param students List of students
     * @param currentStudent Current student to be added
     */
    private static void student(List<Student> students, Student currentStudent) {
        students.add(currentStudent);
        System.out.println("Added successfully");
    }

    /**
     * Adds a professor to the professors list.
     *
     * @param professors List of professors
     * @param currentProfessor Current professor to be added
     */
    private static void professor(List<Professor> professors, Professor currentProfessor) {
        professors.add(currentProfessor);
        System.out.println("Added successfully");
    }

    /**
     * Enroll student in a course or display error upon invalid
     * operation.
     *
     * @param courses List of courses
     * @param students List of students
     * @param enrollMemberId ID of the student to be enrolled
     * @param enrollCourseId ID of the course to enroll the student in
     */
    private static void enroll(List<Course> courses, List<Student> students, int enrollMemberId, int enrollCourseId) {
        int enrollId = enrollMemberId;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getMemberId() == enrollMemberId) {
                enrollMemberId = i;
            }
        }
        if (enrollMemberId == enrollId) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        if (courses.get(enrollCourseId - 1).getEnrolledStudents().contains(students.get(enrollMemberId))) {
            System.out.println("Student is already enrolled in this course");
            System.exit(0);
        } else {
            if (students.get(enrollMemberId).getEnrolledCourses().size()
                    >= students.get(enrollMemberId).getMaxEnrolment()) {
                System.out.println("Maximum enrollment is reached for the student");
                System.exit(0);
            } else {
                if (courses.get(enrollCourseId - 1).isFull()) {
                    System.out.println("Course is full");
                    System.exit(0);
                } else {
                    if (students.get(enrollMemberId).enroll(courses.get(enrollCourseId - 1))) {
                        System.out.println("Enrolled successfully");
                    } else {
                        System.out.println("Wrong inputs");
                        System.exit(0);
                    }
                }
            }
        }
    }

    /**
     * Drop a student from a course or display an error upon
     * invalid operation.
     *
     * @param courses List of courses
     * @param students List of students
     * @param dropMemberId ID of the student to be dropped
     * @param dropCourseId ID of the corresponding course
     */
    private static void drop(List<Course> courses, List<Student> students, int dropMemberId, int dropCourseId) {
        int dropId = dropMemberId;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getMemberId() == dropMemberId) {
                dropMemberId = i;
            }
        }
        if (dropMemberId == dropId) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        if (!courses.get(dropCourseId - 1).getEnrolledStudents().contains(students.get(dropMemberId))) {
            System.out.println("Student is not enrolled in this course");
            System.exit(0);
        } else {
            if (students.get(dropMemberId).drop(courses.get(dropCourseId - 1))) {
                System.out.println("Dropped successfully");
            } else {
                System.out.println("Wrong inputs");
                System.exit(0);
            }
        }
    }

    /**
     * Assign a professor to teach a course or display an
     * error upon invalid operation.
     *
     * @param courses List of courses
     * @param professors List of professors
     * @param teachMemberId ID of the professor to teach the course
     * @param teachCourseId ID of the corresponding course
     */
    private static void teach(List<Course> courses, List<Professor> professors, int teachMemberId, int teachCourseId) {
        int teachId = teachMemberId;
        for (int i = 0; i < professors.size(); i++) {
            if (professors.get(i).getMemberId() == teachMemberId) {
                teachMemberId = i;
            }
        }
        if (teachMemberId == teachId) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        if (professors.get(teachMemberId).getAssignedCourses().size() >= professors.get(teachMemberId).getMaxLoad()) {
            System.out.println("Professor's load is complete");
            System.exit(0);
        } else {
            if (professors.get(teachMemberId).getAssignedCourses().contains(courses.get(teachCourseId - 1))) {
                System.out.println("Professor is already teaching this course");
                System.exit(0);
            } else {
                if (professors.get(teachMemberId).teach(courses.get(teachCourseId - 1))) {
                    System.out.println("Professor is successfully assigned to teach this course");
                } else {
                    System.out.println("Wrong inputs");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Exempt a professor from a course or display an error
     * upon invalid operation.
     *
     * @param courses List of courses
     * @param professors List of professors
     * @param memberId ID of the professor to be exempted
     * @param courseId ID of the corresponding course
     */
    private static void exempt(List<Course> courses, List<Professor> professors, int memberId, int courseId) {
        int exemptCourseId = courseId;
        int exemptMemberId = memberId;
        int exemptId = exemptMemberId;
        for (int i = 0; i < professors.size(); i++) {
            if (professors.get(i).getMemberId() == exemptMemberId) {
                exemptMemberId = i;
            }
        }
        if (exemptMemberId == exemptId) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        if (!professors.get(exemptMemberId).getAssignedCourses().contains(courses.get(exemptCourseId - 1))) {
            System.out.println("Professor is not teaching this course");
            System.exit(0);
        } else {
            if (professors.get(exemptMemberId).exempt(courses.get(exemptCourseId - 1))) {
                System.out.println("Professor is exempted");
            } else {
                System.out.println("Wrong inputs");
                System.exit(0);
            }
        }
    }


    /**
     * Fill the lists of courses, students, and professors
     * with initial data.
     *
     * @param courses A list to hold courses data
     * @param students A list to hold students data
     * @param professors A list to hold professors data
     */
    public static void fillInitialData(List<Course> courses, List<Student> students, List<Professor> professors) {
        courses.add(new Course("java_beginner", CourseLevel.BACHELOR));
        courses.add(new Course("java_intermediate", CourseLevel.BACHELOR));
        courses.add(new Course("python_basics", CourseLevel.BACHELOR));
        courses.add(new Course("algorithms", CourseLevel.MASTER));
        courses.add(new Course("advanced_programming", CourseLevel.MASTER));
        courses.add(new Course("mathematical_analysis", CourseLevel.MASTER));
        courses.add(new Course("computer_vision", CourseLevel.MASTER));

        Student student = new Student("Alice");
        students.add(student);
        students.get(0).enroll(courses.get(0));
        students.get(0).enroll(courses.get(1));
        students.get(0).enroll(courses.get(2));

        student = new Student("Bob");
        students.add(student);
        students.get(1).enroll(courses.get(0));
        students.get(1).enroll(courses.get(3));

        student = new Student("Alex");
        students.add(student);
        students.get(2).enroll(courses.get(4));

        Professor professor = new Professor("Ali");
        professors.add(professor);
        professors.get(0).teach(courses.get(0));
        professors.get(0).teach(courses.get(1));

        professor = new Professor("Ahmed");
        professors.add(professor);
        professors.get(1).teach(courses.get(2));
        professors.get(1).teach(courses.get(4));

        professor = new Professor("Andrey");
        professors.add(professor);
        professors.get(2).teach(courses.get(5));
    }

    /**
     * Checks if the name is not equivalent to any commands
     * and contains only alphabetical characters.
     *
     * @param name The name to be checked
     * @return boolean Return false if name is acceptable
     */
    public static boolean checkName(String name) {
        ArrayList<String> commands = new ArrayList<>();
        commands.addAll(Arrays.asList("student", "course", "professor", "enroll", "teach", "exempt", "drop"));
        return commands.contains(name) || !name.matches("[a-zA-Z]+");
    }

    /**
     * Checks if the course name is not equivalent to any
     * commands and does not exist in the course list.
     *
     * @param name The name to be checked
     * @param courses List of courses
     * @return boolean Return false if name is acceptable
     */
    public static boolean checkCourseName(String name, ArrayList<Course> courses) {
        for (Course course : courses) {
            if (course.getCourseName().equals(name)) {
                System.out.println("Course exists");
                System.exit(0);
            }
        }
        ArrayList<String> commands = new ArrayList<>();
        commands.add("student");
        commands.add("course");
        commands.add("professor");
        commands.add("enroll");
        commands.add("teach");
        commands.add("exempt");
        commands.add("drop");
        commands.add("master");
        commands.add("bachelor");
        return commands.contains(name) || !name.matches("[a-zA-Z]+(_[a-zA-Z]+)*");
    }
}

/**
 * Student class extends UniversityMember and implements Enrollable interface.
 * A student can enroll in courses, and drop from them.
 */
class Student extends UniversityMember implements Enrollable {

    /**
     * Maximum number of courses a student can enroll.
     */
    private static final int MAX_ENROLMENT = 3;

    /**
     * A list of courses the student is enrolled in.
     */
    private final List<Course> enrolledCourses = new ArrayList<>();

    /**
     * Constructor of the Student class.
     *
     * @param memberName Name of the student
     */
    Student(String memberName) {
        super(++numberOfMembers, memberName);
    }

    /**
     * Removes a student from a specific course.
     * If the process is successful, it returns true.
     * Otherwise, it returns false.
     *
     * @param course Course from which the student will be deregistered
     * @return A boolean of the success of the drop operation
     */
    public boolean drop(Course course) {
        try {
            course.getEnrolledStudents().remove(this);
            this.enrolledCourses.remove(course);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Registers a student to a specific course. If the process
     * is successful, it returns true.
     * Otherwise, it returns false.
     *
     * @param course Course in which the student will be registered
     * @return A boolean of the success of the enroll operation
     */
    public boolean enroll(Course course) {
        try {
            course.getEnrolledStudents().add(this);
            this.enrolledCourses.add(course);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * getter to the max enrollment for student.
     * @return MAX_ENROLLMENT
     */
    public int getMaxEnrolment() {
        return MAX_ENROLMENT;
    }

    /**
     * Getter for the list of enrolled courses for student.
     * @return List <b>enrolledStudents</b>
     */
    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

}

/**
 * Course Class, holds students in enrolledStudents array.
 */
class Course {

    /**
     * The capacity of each course.
     */
    private final static int CAPACITY = 3;

    /**
     * Number of collected courses.
     */
    private static int numberOfCourses = 0;

    /**
     * A unique identity number for course.
     */
    private final int courseID;

    /**
     * The name of course.
     */
    private String courseName;

    /**
     * List of students who have enrolled in the course.
     */
    private final List<Student> enrolledStudents = new ArrayList<>();

    /**
     * Level of the course.
     */
    private final CourseLevel courseLevel;

    /**
     * Setter to the name of the course.
     * @param name
     */
    public void setCourseName(String name) {
        this.courseName = name;
    }

    /**
     * Constructor of the Course class. Sets the course name and level
     * and increments the number of created courses.
     *
     * @param name Name of the course
     * @param level Level of the course: BACHELOR or MASTER
     */
    Course(String name, CourseLevel level) {
        setCourseName(name);
        this.courseLevel = level;
        this.courseID = ++numberOfCourses;
    }

    /**
     * Returns the list of students enrolled in the course.
     *
     * @return List of students enrolled in the course.
     */
    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    /**
     * Checks if the course is full.
     *
     * @return true if course is full or false if not.
     */
    public boolean isFull() {
        return this.enrolledStudents.size() == CAPACITY;
    }

    /**
     * Getter for level of the course.
     * @return courseLevel
     */
    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    /**
     * Getter for the Id of the course.
     * @return courseID
     */
    public int getCourseID() {
        return courseID;
    }

    /**
     * Getter for the name of the course.
     * @return courseName
     */
    public String getCourseName() {
        return this.courseName;
    }
}

/**
 * Professor Class extends UniversityMember.
 * A professor can teach, and be exempted from, courses.
 */
class Professor extends UniversityMember {

    /**
     * Maximum number of courses a professor can teach.
     */
    private final static int MAX_LOAD = 2;

    /**
     * A List of courses assigned to a professor.
     */
    private final List<Course> assignedCourses = new ArrayList<>();

    /**
     * Constructor of the Professor class.
     *
     * @param memberName Name of the professor
     */
    Professor(String memberName) {
        super(++numberOfMembers, memberName);
    }

    /**
     * Assigns a course to the professor. If the process is successful,
     * returns true, otherwise it returns false.
     *
     * @param course Course to be assigned
     * @return A boolean of the success of the teach operation
     */
    public boolean teach(Course course) {
        try {
            this.assignedCourses.add(course);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Removes a course from professor's list. If the process is successful,
     * returns true, otherwise it returns false.
     *
     * @param course Course to be removed
     * @return A boolean of the success of the exempt operation
     */
    public boolean exempt(Course course) {
        try {
            this.assignedCourses.remove(course);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * Getter of the value for max number of loading for one professor.
     * @return MAX_LOAD
     */
    public int getMaxLoad() {
        return MAX_LOAD;
    }

    /**
     * Getter of courses assigned by professor.
     * @return List <b>assignedCourses</b>
     */
    public List<Course> getAssignedCourses() {
        return assignedCourses;
    }
}

/**
 * UniversityMember is an abstract class that forms the base for
 * the Student and Professor classes.
 */
abstract class UniversityMember {

    /**
     * Variable meant to track the number of members (incrementing for each new member).
     */
    protected static int numberOfMembers = 0;

    /**
     * Unique identity number for a University Member.
     */
    private final int memberId;

    /**
     * A String variable that contains the member name.
     */
    private final String memberName;

    /**
     * Getter for the name of the member of university.
     * @return memberName
     */
    public String getMemberName() {
        return memberName;
    }

    /**
     * Getter for Id of the member of the university.
     * @return memberId
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * Constructor for UniversityMember.
     *
     * @param id Unique ID for the university member
     * @param name Name of the university member
     */
    UniversityMember(int id, String name) {
        this.memberId = id;
        this.memberName = name;

    }


}

/**
 * Enum representing Course levels: BACHELOR, MASTER.
 */
enum CourseLevel {

    /**
     * Represents a Bachelor's course level.
     */
    BACHELOR,

    /**
     * Represents a Master's course level.
     */
    MASTER;
}

/**
 * The interface for classes capable of enrollment activities.
 */
interface Enrollable {

    /**
     * Drops the student from a specified course.
     *
     * @param course the course to be dropped
     * @return boolean indicating the success of the operation
     */
    boolean drop(Course course);

    /**
     * Enrolls the student in a specified course.
     *
     * @param course the course to enroll in
     * @return boolean indicating the success of the operation
     */
    boolean enroll(Course course);
}
