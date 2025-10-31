import dataService.DataServiceApplication;
import dataService.dto.ProductoDTO;
import dataService.dto.ProductoRequest;
import dataService.entity.Categoria;
import dataService.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = DataServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

@ActiveProfiles("test")
class DataControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void cuandoCrearProducto_entoncesSePersisteCorrectamente() {

        Categoria cat = new Categoria();
        cat.setNombre("Cat Test");
        cat.setDescripcion("Desc cat");
        cat = categoriaRepository.save(cat);


        ProductoRequest req = new ProductoRequest();
        req.setNombre("Producto Test");
        req.setDescripcion("Descripción de prueba");
        req.setPrecio(BigDecimal.valueOf(100.50));
        req.setCategoriaId(cat.getId());
        req.setStock(10);
        req.setStockMinimo(3);

        ResponseEntity<ProductoDTO> resp = restTemplate.postForEntity(
                "/data/productos", req, ProductoDTO.class
        );

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().getId());
        assertEquals("Producto Test", resp.getBody().getNombre());
        assertEquals("Cat Test", resp.getBody().getCategoriaNombre());
        assertEquals(10, resp.getBody().getStock());
        assertFalse(resp.getBody().getStockBajo());
    }

    @Test
    void cuandoBuscarProductoInexistente_entoncesRetorna404() {
        ResponseEntity<String> resp = restTemplate.getForEntity(
                "/data/productos/999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }
}
