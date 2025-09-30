package com.ifsul.WebII.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifsul.WebII.model.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Integer>{
	List<Professor> findProfessorByNomeLike(String nome);
	List<Professor> findAll();
    Optional<Professor> findById(Long professorId);
}
