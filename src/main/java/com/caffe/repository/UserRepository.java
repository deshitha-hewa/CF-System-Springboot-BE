package com.caffe.repository;

import com.caffe.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// ########################## USER CUSTOM QUERIES ##########################

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // User findByEmailId(@Param("email") String email);
    @Query(value = "SELECT * FROM User WHERE email=?1", nativeQuery = true)
    User findByEmailId(@Param("email") String email);

    // Get all users where the user role is user
    @Query(value = "SELECT * FROM User WHERE user_role='user'", nativeQuery = true)
    List<User> getAllUser();

    // Update user status for admin
    @Transactional
    @Modifying
    @Query(value = "UPDATE User SET status=?1 WHERE id=?2", nativeQuery = true)
    void updateStatus(@Param("status") Boolean status, @Param("id") Long id);


    // Get all admin
    @Query(value = "SELECT email FROM User WHERE user_role='admin'", nativeQuery = true)
    List<String> getAllAdmin();

    // Find Email
    User findByEmail(String email);
}
