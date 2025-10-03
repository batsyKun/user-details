package com.userdetails.userdetails.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private Integer age;

    @NotBlank
    @Column(nullable = false)
    private String gender;

    @ElementCollection
    @CollectionTable(name = "user_languages", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "language")
    private List<String> communicationLanguages;

    @ElementCollection
    @CollectionTable(name = "user_documents", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "document_path")
    private List<String> documentPaths;

    @Column(name = "photo_path")
    private String photoPath;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Address> addresses;

    // Getters and setters

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) {
        this.dob = dob;
        this.age = calculateAge(dob);
    }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public List<String> getCommunicationLanguages() { return communicationLanguages; }
    public void setCommunicationLanguages(List<String> communicationLanguages) { this.communicationLanguages = communicationLanguages; }

    public List<String> getDocumentPaths() { return documentPaths; }
    public void setDocumentPaths(List<String> documentPaths) { this.documentPaths = documentPaths; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
        if (addresses != null) {
            for (Address addr : addresses) {
                addr.setUser(this);
            }
        }
    }

    // Utility method to calculate age from DOB
    public static int calculateAge(LocalDate dob) {
        if (dob == null) return 0;
        return Period.between(dob, LocalDate.now()).getYears();
    }

    @PrePersist
    @PreUpdate
    public void updateAge() {
        this.age = calculateAge(this.dob);
    }
}
