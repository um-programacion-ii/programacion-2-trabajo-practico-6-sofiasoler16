package dataService.service;

import dataService.dto.ProductoDTO;
import dataService.dto.ProductoRequest;
import dataService.entity.Categoria;
import dataService.entity.Inventario;
import dataService.entity.Producto;
import dataService.exception.CategoriaNoEncontradaException;
import dataService.exception.ProductoNoEncontradoException;
import dataService.repository.CategoriaRepository;
import dataService.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream().map(this::toDto).toList();
    }

    public ProductoDTO porId(Long id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado: " + id));
        return toDto(p);
    }

    public ProductoDTO crear(ProductoRequest r) {
        Categoria cat = categoriaRepository.findById(r.getCategoriaId())
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría inexistente: " + r.getCategoriaId()));

        Producto p = new Producto();
        p.setNombre(r.getNombre());
        p.setDescripcion(r.getDescripcion());
        p.setPrecio(r.getPrecio());
        p.setCategoria(cat);

        Inventario inv = new Inventario();
        inv.setProducto(p);
        inv.setCantidad(r.getStock());
        inv.setStockMinimo(r.getStockMinimo());
        inv.setFechaActualizacion(LocalDateTime.now());
        p.setInventario(inv);

        return toDto(productoRepository.save(p));
    }

    public ProductoDTO actualizar(Long id, ProductoRequest r) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado: " + id));

        Categoria cat = categoriaRepository.findById(r.getCategoriaId())
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría inexistente: " + r.getCategoriaId()));

        p.setNombre(r.getNombre());
        p.setDescripcion(r.getDescripcion());
        p.setPrecio(r.getPrecio());
        p.setCategoria(cat);

        Inventario inv = p.getInventario();
        inv.setCantidad(r.getStock());
        inv.setStockMinimo(r.getStockMinimo());
        inv.setFechaActualizacion(LocalDateTime.now());

        return toDto(productoRepository.save(p));
    }

    public List<ProductoDTO> porCategoriaNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .map(cat -> productoRepository.findByCategoriaId(cat.getId()).stream().map(this::toDto).toList())
                .orElse(Collections.emptyList());
    }

    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNoEncontradoException("Producto no encontrado: " + id);
        }
        productoRepository.deleteById(id);
    }

    private ProductoDTO toDto(Producto p) {
        var i = p.getInventario();
        return new ProductoDTO(
                p.getId(), p.getNombre(), p.getDescripcion(), p.getPrecio(),
                p.getCategoria().getNombre(), i.getCantidad(),
                i.getCantidad() <= i.getStockMinimo()
        );
    }
}


