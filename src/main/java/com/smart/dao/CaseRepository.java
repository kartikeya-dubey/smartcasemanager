package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Case;
import com.smart.entities.User;

public interface CaseRepository extends JpaRepository<Case, Integer> {

	// Without pagination to get all cases of a user using his user ID
	/*
	 * @Query("from Case as c where c.user.id = :userId") public List<Case>
	 * findCasesByUser(@Param("userId") int userId);
	 */

	// pagination functionality to fetch all cases belonging to a caseID
	// current page-page
	// page size-5
	@Query("from Case as c where c.user.id = :userId")
	public Page<Case> findCasesByUser(@Param("userId") int userId, Pageable pageable);

	// Search case by client name keyword
	public List<Case> findByClientNameContainingAndUser(String keyword, User user);

	// Search case by case name keyword
	public List<Case> findByCaseNameContainingAndUser(String keyword, User user);

	// Search case by case name keyword
	public List<Case> findByCidAndUser(Integer keyword, User user);
}
