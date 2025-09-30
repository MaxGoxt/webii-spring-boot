package com.ifsul.WebII.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifsul.WebII.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{
	List<Categoria> findCategoriaByNomeLike(String nome);
	List<Categoria> findAll();
	Optional<Categoria> findById(Long categoriaId);
}
