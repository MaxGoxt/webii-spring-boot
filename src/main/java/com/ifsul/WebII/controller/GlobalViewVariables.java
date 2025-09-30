package com.ifsul.WebII.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;

import com.ifsul.WebII.model.Categoria;
import com.ifsul.WebII.repository.CategoriaRepository;;

@ControllerAdvice
public class GlobalViewVariables {
    @Autowired
    CategoriaRepository categoriaRepository;

    @ModelAttribute("categorias")
    public List<Categoria> populateCategories() {
        return categoriaRepository.findAll();
    }
}
