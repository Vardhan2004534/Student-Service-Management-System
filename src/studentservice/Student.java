package studentservice;

import java.util.Objects;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String contactNumber;

    public Student(String studentId, String name, String email, String contactNumber) {
        setStudentId(studentId);
        setName(name);
        setEmail(email);
        setContactNumber(contactNumber);
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = normaliseText(studentId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = normaliseText(name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = normaliseText(email);
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = normaliseText(contactNumber);
    }

    public void validateForSubmission() {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student ID is required.");
        }

        if (studentId.length() > 20) {
            throw new IllegalArgumentException("Student ID is too long. Maximum length is 20 characters.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Student name is required.");
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException("Student name is too long. Maximum length is 100 characters.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email address is required.");
        }

        if (email.length() > 100) {
            throw new IllegalArgumentException("Email address is too long. Maximum length is 100 characters.");
        }

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }

        if (contactNumber == null || contactNumber.isBlank()) {
            throw new IllegalArgumentException("Contact number is required.");
        }

        if (contactNumber.length() > 20) {
            throw new IllegalArgumentException("Contact number is too long. Maximum length is 20 characters.");
        }
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        String trimmed = email.trim();
        if (trimmed.isEmpty() || trimmed.length() > 100) {
            return false;
        }

        int atIndex = trimmed.indexOf('@');
        int dotIndex = trimmed.lastIndexOf('.');

        return atIndex > 0
                && dotIndex > atIndex + 1
                && dotIndex < trimmed.length() - 1;
    }

    private static String normaliseText(String value) {
        return value == null ? "" : value.trim();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Student)) {
            return false;
        }

        Student otherStudent = (Student) other;
        return Objects.equals(studentId, otherStudent.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return studentId + " - " + name;
    }
}
