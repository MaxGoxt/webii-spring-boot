package com.ifsul.WebII.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifsul.WebII.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer>{
	List<Curso> findCursoByNomeLike(String nome);
	List<Curso> findAll();
	Optional<Curso> findById(Long id);
}
