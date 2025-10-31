package dataService.controller;

import dataService.dto.ProductoDTO;
import dataService.dto.ProductoRequest;
import dataService.entity.Categoria;
import dataService.entity.Inventario;
import dataService.service.CategoriaService;
import dataService.service.InventarioService;
import dataService.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/data")
@Validated
public class DataController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final InventarioService inventarioService;

    public DataController(ProductoService productoService,
                          CategoriaService categoriaService,
                          InventarioService inventarioService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.inventarioService = inventarioService;
    }

    // Producto
    @GetMapping("/productos")
    public List<ProductoDTO> getProductos() {
        return productoService.obtenerTodos();
    }

    @GetMapping("/productos/{id}")
    public ProductoDTO getProducto(@PathVariable Long id) {
        return productoService.porId(id);
    }

    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDTO crearProducto(@Valid @RequestBody ProductoRequest request) {
        return productoService.crear(request);
    }

    @PutMapping("/productos/{id}")
    public ProductoDTO actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productoService.actualizar(id, request);
    }

    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
    }

    @GetMapping("/productos/categoria/{nombre}")
    public List<ProductoDTO> getPorCategoriaNombre(@PathVariable String nombre) {
        return productoService.porCategoriaNombre(nombre);
    }

    // Categoria
    @GetMapping("/categorias")
    public List<Categoria> getCategorias() {
        return categoriaService.obtenerTodas();
    }

    @GetMapping("/categorias/{id}")
    public Categoria getCategoria(@PathVariable Long id) {
        return categoriaService.porId(id);
    }

    @PostMapping("/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria crearCategoria(@Valid @RequestBody Categoria c) {
        return categoriaService.crear(c);
    }

    @PutMapping("/categorias/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria c) {
        return categoriaService.actualizar(id, c);
    }

    @DeleteMapping("/categorias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
    }

    // Inventario
    @GetMapping("/inventario/stock-bajo")
    public List<Inventario> stockBajo() {
        return inventarioService.obtenerProductosConStockBajo();
    }

    @PutMapping("/inventario/{productoId}/stock")
    public Inventario actualizarStock(@PathVariable Long productoId,
                                      @RequestParam Integer cantidad) {
        return inventarioService.actualizarStock(productoId, cantidad);
    }

}