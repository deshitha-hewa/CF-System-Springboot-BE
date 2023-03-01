package com.caffe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.sql.Timestamp;

// ########################## USER MODEL ##########################

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "User name must no be null")
    @Size(min = 3, message = "User name at least 3 characters")
    private String name;

    @NotBlank(message = "Contact Number must no be null")
    @Size(min = 10, message = "Contact Number at least 10 characters")
    private String contactNumber;

    @NotBlank(message = "Email must no be null")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Password must not be null")
    @JsonIgnore
    private String password;

    @NotNull(message = "User status must no be null")
    private Boolean status = false;

    @Column(name = "user_role")
    private String role = "user";

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
}
