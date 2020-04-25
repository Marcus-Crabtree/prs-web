package com.prs.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.business.Request;


public interface RequestRepository extends JpaRepository<Request, Integer> {
	List<Request> findAllByUserIdNot(int userId);
}
