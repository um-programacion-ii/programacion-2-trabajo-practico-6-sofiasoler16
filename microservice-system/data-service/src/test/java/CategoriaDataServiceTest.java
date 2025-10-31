import dataService.entity.Categoria;
import dataService.exception.CategoriaNoEncontradaException;
import dataService.repository.CategoriaRepository;
import dataService.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaDataServiceTest {

    @Mock private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void obtenerTodas_ok() {
        var c1 = new Categoria(); c1.setId(1L); c1.setNombre("A");
        var c2 = new Categoria(); c2.setId(2L); c2.setNombre("B");
        when(categoriaRepository.findAll()).thenReturn(List.of(c1, c2));

        var lista = categoriaService.obtenerTodas();
        assertEquals(2, lista.size());
    }

    @Test
    void porId_ok() {
        var c = new Categoria(); c.setId(5L); c.setNombre("X");
        when(categoriaRepository.findById(5L)).thenReturn(Optional.of(c));

        var cat = categoriaService.porId(5L);
        assertEquals("X", cat.getNombre());
    }

    @Test
    void porId_falla_noExiste() {
        when(categoriaRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.porId(9L));
    }

    @Test
    void crear_ok() {
        var c = new Categoria(); c.setNombre("Nueva");
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(i -> {
            Categoria in = i.getArgument(0); in.setId(100L); return in;
        });
        var res = categoriaService.crear(c);
        assertNotNull(res.getId());
        assertEquals("Nueva", res.getNombre());
    }

    @Test
    void actualizar_ok() {
        var existente = new Categoria(); existente.setId(3L); existente.setNombre("Old");
        when(categoriaRepository.findById(3L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(i -> i.getArgument(0));

        var cambios = new Categoria(); cambios.setNombre("New");
        var res = categoriaService.actualizar(3L, cambios);
        assertEquals("New", res.getNombre());
    }

    @Test
    void eliminar_ok() {
        when(categoriaRepository.existsById(4L)).thenReturn(true);
        categoriaService.eliminar(4L);
        verify(categoriaRepository).deleteById(4L);
    }

    @Test
    void eliminar_falla_noExiste() {
        when(categoriaRepository.existsById(4L)).thenReturn(false);
        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.eliminar(4L));
    }
}
