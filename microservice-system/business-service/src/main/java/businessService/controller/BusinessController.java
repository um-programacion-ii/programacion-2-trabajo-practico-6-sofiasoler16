package businessService.controller;

import businessService.dto.InventarioDTO;
import businessService.dto.ProductoDTO;
import businessService.dto.ProductoRequest;
import businessService.service.CategoriaBusinessService;
import businessService.service.InventarioBusinessService;
import businessService.service.ProductoBusinessService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class BusinessController {

    private final ProductoBusinessService productoBusinessService;
    private final CategoriaBusinessService categoriaBusinessService;
    private final InventarioBusinessService inventarioBusinessService;

    public BusinessController(ProductoBusinessService productoBusinessService,
                              CategoriaBusinessService categoriaBusinessService,
                              InventarioBusinessService inventarioBusinessService) {
        this.productoBusinessService = productoBusinessService;
        this.categoriaBusinessService = categoriaBusinessService;
        this.inventarioBusinessService = inventarioBusinessService;
    }


    // Producto
    @GetMapping("/productos")
    public List<ProductoDTO> obtenerTodos() {
        return productoBusinessService.obtenerTodosLosProductos();
    }

    @GetMapping("/productos/{id}")
    public ProductoDTO porId(@PathVariable Long id) {
        return productoBusinessService.obtenerProductoPorId(id);
    }

    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDTO crear(@Valid @RequestBody ProductoRequest request) {
        return productoBusinessService.crearProducto(request);
    }

    @PutMapping("/productos/{id}")
    public ProductoDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productoBusinessService.actualizarProducto(id, request);
    }

    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        productoBusinessService.eliminarProducto(id);
    }

    @GetMapping("/productos/categoria/{nombre}")
    public List<ProductoDTO> porCategoria(@PathVariable String nombre) {
        return productoBusinessService.obtenerProductosPorCategoria(nombre);
    }

    // Reportes
    @GetMapping("/reportes/stock-bajo")
    public List<InventarioDTO> stockBajo() {
        return inventarioBusinessService.productosConStockBajo();
    }

    @GetMapping("/reportes/valor-inventario")
    public BigDecimal valorInventario() {
        return inventarioBusinessService.valorTotalInventario();
    }
}