package dataService.service;


import dataService.entity.Categoria;
import dataService.exception.CategoriaNoEncontradaException;
import dataService.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    public List<Categoria> obtenerTodas() {
        return repo.findAll();
    }

    public Categoria porId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría no encontrada: " + id));
    }

    public Categoria crear(Categoria c) { return repo.save(c); }

    public Categoria actualizar(Long id, Categoria c) {
        Categoria actual = porId(id);
        actual.setNombre(c.getNombre());
        actual.setDescripcion(c.getDescripcion());
        return repo.save(actual);
    }

    public void eliminar(Long id) {
        if (!repo.existsById(id)) throw new CategoriaNoEncontradaException("Categoría no encontrada: " + id);
        repo.deleteById(id);
    }
}