package businessService.service;

import businessService.client.DataServiceClient;
import businessService.dto.CategoriaDTO;
import businessService.dto.InventarioDTO;
import businessService.dto.ProductoDTO;
import businessService.exception.MicroserviceCommunicationException;
import businessService.exception.ResourceNotFoundException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class InventarioBusinessService {
    private final DataServiceClient dataServiceClient;

    public InventarioBusinessService(DataServiceClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }

    public List<InventarioDTO> obtenerProductosConStockBajo() {
        try {
            return dataServiceClient.obtenerProductosConStockBajo();
        } catch (FeignException e) {
            log.error("Error al obtener Inventario productos", e);
            throw new ResourceNotFoundException("Error al encontrar productos con stock bajo");
        }
    }

    public BigDecimal valorTotalInventario() {
        try {
            return dataServiceClient.valorInventario();
        } catch (FeignException e) {
            log.error("Error consultando valor de inventario al data-service", e);
            throw new MicroserviceCommunicationException("No se pudo consultar el valor total de inventario");
        }
    }

    public List<InventarioDTO> productosConStockBajo() {
        try {
            return dataServiceClient.obtenerProductosConStockBajo();
        } catch (FeignException e) {
            log.error("Error consultando stock bajo al data-service", e);
            throw new MicroserviceCommunicationException("No se pudo consultar stock bajo en data-service");
        }
    }
}
