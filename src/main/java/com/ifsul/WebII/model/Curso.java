package com.ifsul.WebII.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="curso")
public class Curso implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    private LocalDateTime data_inicio;
    private LocalDateTime data_final;

    @NotBlank
    private String img;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

	

    public int getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getData_inicio() { return data_inicio; }
    public void setData_inicio(LocalDateTime data_inicio) { this.data_inicio = data_inicio; }

    public LocalDateTime getData_final() { return data_final; }
    public void setData_final(LocalDateTime data_final) { this.data_final = data_final; }

    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
