package com.ifsul.WebII.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.ifsul.WebII.model.Curso;
import com.ifsul.WebII.repository.CursoRepository;

@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    CursoRepository repository;

    @GetMapping()
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("/index");
        List<Curso> lista = repository.findAll();
        mv.addObject("cursos", lista);
        return mv;
    }
}
