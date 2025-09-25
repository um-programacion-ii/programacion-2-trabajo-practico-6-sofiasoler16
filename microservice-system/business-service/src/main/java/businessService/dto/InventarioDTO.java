package businessService.dto;

import java.time.LocalDateTime;

public class InventarioDTO {
    private Long id;
    private ProductoDTO producto;
    private Integer cantidad;
    private Integer stockMinimo;
    private LocalDateTime fechaActualizacion;
}
