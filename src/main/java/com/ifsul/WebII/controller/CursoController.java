package com.ifsul.WebII.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsul.WebII.dto.CursoDTO;
import com.ifsul.WebII.model.Categoria;
import com.ifsul.WebII.model.Curso;
import com.ifsul.WebII.model.Professor;
import com.ifsul.WebII.repository.CategoriaRepository;
import com.ifsul.WebII.repository.CursoRepository;
import com.ifsul.WebII.repository.ProfessorRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/curso")
public class CursoController {
	@Autowired
	CursoRepository repository;

	@Autowired
	ProfessorRepository professorRepository;

	@Autowired
	CategoriaRepository categoriaRepository;

	@GetMapping("/inserir")
	public String inserir(Model profModel, Model catModel) {
		profModel.addAttribute("professores", professorRepository.findAll());
		catModel.addAttribute("categorias", categoriaRepository.findAll());
		return "curso/inserir";
	}

	@PostMapping("/inserir")
	public String inserido(
			@ModelAttribute @Valid CursoDTO dto,
			BindingResult result,
			RedirectAttributes msg,
			@RequestParam("file") MultipartFile imagem,
			@RequestParam("professor_ID") Long professorId,
			@RequestParam("categoria_ID") Long categoriaId) {

		if (result.hasErrors()) {
			msg.addFlashAttribute("erro", "Erro ao inserir!");
			System.out.println(result.getAllErrors().toString());
			return "redirect:/curso/inserir";
		}

		var curso = new Curso();
		BeanUtils.copyProperties(dto, curso);

		Optional<Professor> professor = professorRepository.findById(professorId);
		Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);

		if (professor.isEmpty() || categoria.isEmpty()) {
			msg.addFlashAttribute("erro", "Professor ou Categoria inválidos.");

			return "redirect:/curso/inserir";
		}

		curso.setProfessor(professor.get());
		curso.setCategoria(categoria.get());

		try {
			if (!imagem.isEmpty()) {
				byte[] bytes = imagem.getBytes();

				Path caminho = Paths.get(
						"./src/main/resources/static/img/" + imagem.getOriginalFilename());

				Files.write(caminho, bytes);
				curso.setImg(imagem.getOriginalFilename());
			}
		} catch (IOException e) {
			System.out.println("Erro ao salvar imagem: " + e.getMessage());
		}

		repository.save(curso);
		msg.addFlashAttribute("ok", "Curso inserido com sucesso!");
		return "redirect:/curso/listar";
	}

	@GetMapping("/imagem/{imagem}")
	@ResponseBody
	public byte[] mostraImagem(@PathVariable("imagem") String imagem)
			throws IOException {
		File nomeArquivo = new File("./src/main/resources/static/img/" + imagem);
		if (imagem != null || imagem.trim().length() > 0) {
			return Files.readAllBytes(nomeArquivo.toPath());
		}
		return null;
	}

	@GetMapping("/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("/curso/listar");
		List<Curso> lista = repository.findAll();
		mv.addObject("cursos", lista);
		return mv;
	}

	@PostMapping("/listar")
	public ModelAndView listarcursosFind(@RequestParam("busca") String buscar) {
		ModelAndView mv = new ModelAndView("curso/listar");
		List<Curso> lista = repository.findCursoByNomeLike("%" + buscar + "%");
		mv.addObject("cursos", lista);
		return mv;
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable(value = "id") int id) {
		Optional<Curso> curso = repository.findById(id);
		if (curso.isEmpty()) {
			return "redirect:/curso/listar";
		}
		repository.deleteById(id);
		return "redirect:/curso/listar";
	}

	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable(value = "id") int id) {
		ModelAndView mv = new ModelAndView("/curso/editar");
		Optional<Curso> curso = repository.findById(id);
		mv.addObject("id", curso.get().getId());
		mv.addObject("nome", curso.get().getNome());
		mv.addObject("descricao", curso.get().getDescricao());
		mv.addObject("data_inicio", curso.get().getData_inicio());
		mv.addObject("data_final", curso.get().getData_final());
		mv.addObject("professor", curso.get().getProfessor());
		mv.addObject("categoria", curso.get().getCategoria());
		mv.addObject("professores", professorRepository.findAll());
		mv.addObject("categorias", categoriaRepository.findAll());
		mv.addObject("img", curso.get().getImg());
		return mv;
	}

	@PostMapping("/editar/{id}")
	public String editado(
			@ModelAttribute @Valid CursoDTO dto,
			BindingResult result,
			RedirectAttributes msg,
			@PathVariable int id,
			@RequestParam("file") MultipartFile imagem) {
		if (result.hasErrors()) {
			msg.addFlashAttribute("erro", "Erro ao editar!");
			return "redirect:/curso/editar/" + id;
		}

		Optional<Curso> cursoOptional = repository.findById(id);
		if (cursoOptional.isEmpty()) {
			msg.addFlashAttribute("erro", "Curso não encontrado");
			return "redirect:/curso/listar";
		}

		Curso curso = cursoOptional.get();

		BeanUtils.copyProperties(dto, curso, "img", "professor", "categoria");

		Optional<Professor> professorOpt = professorRepository.findById(dto.professor_ID());
		Optional<Categoria> categoriaOpt = categoriaRepository.findById(dto.categoria_ID());

		if (professorOpt.isEmpty() || categoriaOpt.isEmpty()) {
			msg.addFlashAttribute("erro", "Professor ou Categoria inválidos.");
			return "redirect:/curso/editar/" + id;
		}
		curso.setProfessor(professorOpt.get());
		curso.setCategoria(categoriaOpt.get());

		// Se houver nova imagem, salva ela e atualiza o nome
		try {
			if (imagem != null && !imagem.isEmpty()) {
				byte[] bytes = imagem.getBytes();
				Path caminho = Paths.get("./src/main/resources/static/img/" + imagem.getOriginalFilename());
				Files.write(caminho, bytes);
				curso.setImg(imagem.getOriginalFilename());
			}
			// Se não houver nova imagem, mantém a imagem atual (não faz nada)
		} catch (IOException e) {
			msg.addFlashAttribute("erro", "Erro ao salvar imagem: " + e.getMessage());
			return "redirect:/curso/editar/" + id;
		}

		repository.save(curso);
		msg.addFlashAttribute("sucesso", "Curso editado com sucesso!");
		return "redirect:/curso/listar";
	}

}
