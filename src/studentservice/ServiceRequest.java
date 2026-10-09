package studentservice;

import java.util.Objects;

public class ServiceRequest {
    private String requestId;
    private Student student;
    private String requestType;
    private String description;
    private String status;

    public ServiceRequest(String requestId, Student student, String requestType, String description) {
        setRequestId(requestId);
        setStudent(student);
        setRequestType(requestType);
        setDescription(description);
        this.status = "Pending";
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId == null ? "" : requestId.trim();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType == null ? "" : requestType.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }

        String trimmed = status.trim();
        if (!isValidStatus(trimmed)) {
            throw new IllegalArgumentException("Invalid status selected.");
        }

        this.status = trimmed;
    }

    public void updateStatus(String newStatus) {
        setStatus(newStatus);
    }

    public static boolean isValidStatus(String status) {
        if (status == null) {
            return false;
        }

        String trimmed = status.trim();
        return "Pending".equals(trimmed)
                || "In Progress".equals(trimmed)
                || "Resolved".equals(trimmed);
    }

    public void validateForSubmission() {
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("Request ID cannot be empty.");
        }

        if (student == null) {
            throw new IllegalArgumentException("Student information is required.");
        }

        if (requestType == null || requestType.isBlank()) {
            throw new IllegalArgumentException("Request type is required.");
        }

        if (requestType.length() > 60) {
            throw new IllegalArgumentException("Request type is too long.");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Request description is required.");
        }

        if (description.length() > 1000) {
            throw new IllegalArgumentException("Request description is too long. Maximum length is 1000 characters.");
        }

        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Request status is invalid.");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof ServiceRequest)) {
            return false;
        }

        ServiceRequest otherRequest = (ServiceRequest) other;
        return Objects.equals(requestId, otherRequest.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }

    @Override
    public String toString() {
        return requestId + " | " + student.getStudentId() + " | " + requestType + " | " + status;
    }
}
