package studentservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentServiceSystem {
    private final List<Student> students;
    private final List<ServiceRequest> requests;
    private int nextRequestNumber;

    public StudentServiceSystem() {
        this.students = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.nextRequestNumber = 1;
    }

    public Student registerStudent(String studentId, String name, String email, String contactNumber) {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student ID is required.");
        }

        if (containsStudentId(studentId.trim())) {
            throw new IllegalArgumentException("A student with this ID already exists. Student IDs must be unique.");
        }

        Student student = new Student(studentId, name, email, contactNumber);
        student.validateForSubmission();
        students.add(student);
        return student;
    }

    public Student findStudentById(String studentId) {
        if (studentId == null || studentId.isBlank()) {
            return null;
        }

        String normalisedId = studentId.trim();
        for (Student student : students) {
            if (student.getStudentId().equalsIgnoreCase(normalisedId)) {
                return student;
            }
        }
        return null;
    }

    public boolean containsStudentId(String studentId) {
        return findStudentById(studentId) != null;
    }

    public ServiceRequest submitRequest(
            String studentId,
            String studentName,
            String email,
            String contactNumber,
            String requestType,
            String description) {

        validateStudentData(studentId, studentName, email, contactNumber);
        validateRequestData(requestType, description);

        Student existingStudent = findStudentById(studentId);
        if (existingStudent == null) {
            existingStudent = registerStudent(studentId, studentName, email, contactNumber);
        } else {
            if (!existingStudent.getName().equalsIgnoreCase(studentName.trim())
                    || !existingStudent.getEmail().equalsIgnoreCase(email.trim())
                    || !existingStudent.getContactNumber().equalsIgnoreCase(contactNumber.trim())) {
                throw new IllegalArgumentException("This student ID already exists with different details. Please use the existing student record.");
            }
        }

        String requestId = generateUniqueRequestId();
        ServiceRequest request = new ServiceRequest(requestId, existingStudent, requestType, description);
        request.validateForSubmission();
        requests.add(request);
        return request;
    }

    public String generateUniqueRequestId() {
        String requestId = String.format("REQ%04d", nextRequestNumber);
        nextRequestNumber++;
        return requestId;
    }

    public List<ServiceRequest> getAllRequests() {
        return new ArrayList<>(requests);
    }

    public List<ServiceRequest> searchByStudentId(String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student ID search value is required.");
        }

        String searchValue = studentId.trim();
        List<ServiceRequest> results = new ArrayList<>();
        for (ServiceRequest request : requests) {
            if (request.getStudent().getStudentId().equalsIgnoreCase(searchValue)) {
                results.add(request);
            }
        }
        return results;
    }

    public ServiceRequest searchByRequestId(String requestId) {
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("Request ID search value is required.");
        }

        String searchValue = requestId.trim();
        for (ServiceRequest request : requests) {
            if (request.getRequestId().equalsIgnoreCase(searchValue)) {
                return request;
            }
        }
        return null;
    }

    public void updateRequestStatus(String requestId, String newStatus) {
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("Please select a request before updating status.");
        }

        ServiceRequest request = searchByRequestId(requestId);
        if (request == null) {
            throw new IllegalArgumentException("The selected request does not exist.");
        }

        request.updateStatus(newStatus);
    }

    public static List<String> getValidRequestTypes() {
        List<String> validTypes = new ArrayList<>();
        validTypes.add("Academic Enquiry");
        validTypes.add("IT Support");
        validTypes.add("Assessment Support");
        validTypes.add("General Enquiry");
        return validTypes;
    }

    public static List<String> getValidStatuses() {
        List<String> statuses = new ArrayList<>();
        statuses.add("Pending");
        statuses.add("In Progress");
        statuses.add("Resolved");
        return statuses;
    }

    public void validateStudentData(String studentId, String studentName, String email, String contactNumber) {
        Student student = new Student(studentId, studentName, email, contactNumber);
        student.validateForSubmission();
    }

    public void validateRequestData(String requestType, String description) {
        if (requestType == null || requestType.isBlank()) {
            throw new IllegalArgumentException("Please select a request type.");
        }

        if (!getValidRequestTypes().contains(requestType.trim())) {
            throw new IllegalArgumentException("Selected request type is invalid.");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Request description is required.");
        }

        if (description.trim().length() > 1000) {
            throw new IllegalArgumentException("Description is too long. Maximum length is 1000 characters.");
        }
    }

    public boolean isEmpty() {
        return requests.isEmpty();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof StudentServiceSystem)) {
            return false;
        }

        StudentServiceSystem otherSystem = (StudentServiceSystem) other;
        return Objects.equals(students, otherSystem.students)
                && Objects.equals(requests, otherSystem.requests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(students, requests, nextRequestNumber);
    }
}
