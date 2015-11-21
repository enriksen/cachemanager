package com.esr.app.cachemanager.db;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.esr.app.cachemanager.db.dao.User;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByName(String name);
    

    Page<User> findAll(Pageable p);
}