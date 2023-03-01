package com.caffe.repository;

import com.caffe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query(value = "SELECT * FROM User WHERE user_role='user'", nativeQuery = true)
    List<User> getAllUser();
}
