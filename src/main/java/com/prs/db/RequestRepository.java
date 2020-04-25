package com.prs.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.business.Request;


public interface RequestRepository extends JpaRepository<Request, Integer> {
	Optional<Request> setStatusForReview(String request);
}
