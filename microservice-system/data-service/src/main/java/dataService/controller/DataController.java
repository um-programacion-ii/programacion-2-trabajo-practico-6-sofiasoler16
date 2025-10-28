package dataService.controller;

import dataService.service.CategoriaService;
import dataService.service.InventarioService;
import dataService.service.ProductoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}