package com.caffe.repository;

import com.caffe.entity.Category;
import com.caffe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    // Get all category at least 1 product
    @Query(value = "SELECT * FROM Category", nativeQuery = true)
    List<String> getAllCategory();
}
