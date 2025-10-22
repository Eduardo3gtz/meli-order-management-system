package com.meli.ordermanagementsystem.service;

import com.meli.ordermanagementsystem.model.Order;
import com.meli.ordermanagementsystem.repository.OrderRepository;
import com.meli.ordermanagementsystem.exception.ResourceNotFoundException; // Asegúrate de que esta clase de excepción exista
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections; // Para listas vacías
import java.util.List;
import java.util.Optional; // Para Optional

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong; // Para ID de tipo Long
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase OrderServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateOrder_DeberiaAsignarFechaYGuardar() {
        // Arrange
        Order ordenEntrada = new Order();
        ordenEntrada.setCustomerName("Cliente de Prueba");
        ordenEntrada.setStatus("Pendiente");
        ordenEntrada.setTotalAmount(100.0);

        Order ordenGuardada = new Order();
        ordenGuardada.setId(1L);
        ordenGuardada.setCustomerName(ordenEntrada.getCustomerName());
        ordenGuardada.setStatus(ordenEntrada.getStatus());
        ordenGuardada.setTotalAmount(ordenEntrada.getTotalAmount());
        ordenGuardada.setOrderDate(LocalDateTime.now()); // Simulamos que la fecha se asignó

        when(orderRepository.save(any(Order.class))).thenReturn(ordenGuardada);

        // Act
        Order ordenResultado = orderService.createOrder(ordenEntrada);

        // Assert
        assertNotNull(ordenResultado.getId());
        assertNotNull(ordenResultado.getOrderDate());
        assertEquals("Cliente de Prueba", ordenResultado.getCustomerName());
        assertEquals(1L, ordenResultado.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testGetAllOrders_DeberiaDevolverListaDeOrdenes() {
        // Arrange
        Order orden1 = new Order();
        orden1.setId(1L);
        orden1.setCustomerName("Cliente 1");
        orden1.setStatus("Completado");
        orden1.setTotalAmount(50.0);

        Order orden2 = new Order();
        orden2.setId(2L);
        orden2.setCustomerName("Cliente 2");
        orden2.setStatus("Pendiente");
        orden2.setTotalAmount(75.0);

        List<Order> listaDeOrdenes = List.of(orden1, orden2); // Usamos List.of para una lista inmutable

        when(orderRepository.findAll()).thenReturn(listaDeOrdenes);

        // Act
        List<Order> resultado = orderService.getAllOrders();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Cliente 1", resultado.get(0).getCustomerName());
        assertEquals("Cliente 2", resultado.get(1).getCustomerName());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetAllOrders_DeberiaDevolverListaVaciaCuandoNoHayOrdenes() {
        // Arrange
        List<Order> listaVacia = Collections.emptyList(); // Lista vacía

        when(orderRepository.findAll()).thenReturn(listaVacia);

        // Act
        List<Order> resultado = orderService.getAllOrders();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById_DeberiaDevolverOrdenCuandoExiste() {
        // Arrange
        Long ordenId = 1L;
        Order ordenExistente = new Order();
        ordenExistente.setId(ordenId);
        ordenExistente.setCustomerName("Cliente Existente");
        ordenExistente.setOrderDate(LocalDateTime.now());

        when(orderRepository.findById(ordenId)).thenReturn(Optional.of(ordenExistente));

        // Act
        Order resultado = orderService.getOrderById(ordenId);

        // Assert
        assertNotNull(resultado);
        assertEquals(ordenId, resultado.getId());
        assertEquals("Cliente Existente", resultado.getCustomerName());
        verify(orderRepository, times(1)).findById(ordenId);
    }

    @Test
    void testGetOrderById_DeberiaLanzarExcepcionCuandoNoExiste() {
        // Arrange
        Long ordenId = 99L; // Un ID que no existe
        when(orderRepository.findById(ordenId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(ordenId));
        verify(orderRepository, times(1)).findById(ordenId);
    }

    @Test
    void testUpdateOrder_DeberiaActualizarOrdenCuandoExiste() {
        // Arrange
        Long ordenId = 1L;
        Order ordenExistente = new Order();
        ordenExistente.setId(ordenId);
        ordenExistente.setCustomerName("Cliente Original");
        ordenExistente.setStatus("Pendiente");
        ordenExistente.setTotalAmount(100.0);
        ordenExistente.setOrderDate(LocalDateTime.now());

        Order ordenActualizadaData = new Order();
        ordenActualizadaData.setCustomerName("Cliente Actualizado");
        ordenActualizadaData.setStatus("Completado");
        ordenActualizadaData.setTotalAmount(150.0);

        Order ordenGuardada = new Order(); // Lo que esperamos que save devuelva
        ordenGuardada.setId(ordenId);
        ordenGuardada.setCustomerName(ordenActualizadaData.getCustomerName());
        ordenGuardada.setStatus(ordenActualizadaData.getStatus());
        ordenGuardada.setTotalAmount(ordenActualizadaData.getTotalAmount());
        ordenGuardada.setOrderDate(ordenExistente.getOrderDate()); // La fecha no debería cambiar

        when(orderRepository.findById(ordenId)).thenReturn(Optional.of(ordenExistente));
        when(orderRepository.save(any(Order.class))).thenReturn(ordenGuardada);

        // Act
        Order resultado = orderService.updateOrder(ordenId, ordenActualizadaData);

        // Assert
        assertNotNull(resultado);
        assertEquals(ordenId, resultado.getId());
        assertEquals("Cliente Actualizado", resultado.getCustomerName());
        assertEquals("Completado", resultado.getStatus());
        assertEquals(150.0, resultado.getTotalAmount());
        // Verifica que la fecha original se mantuvo, no se creó una nueva
        assertEquals(ordenExistente.getOrderDate(), resultado.getOrderDate());

        verify(orderRepository, times(1)).findById(ordenId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateOrder_DeberiaLanzarExcepcionCuandoOrdenNoExiste() {
        // Arrange
        Long ordenId = 99L;
        Order ordenActualizadaData = new Order();
        ordenActualizadaData.setCustomerName("Cliente Actualizado");

        when(orderRepository.findById(ordenId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(ordenId, ordenActualizadaData));
        verify(orderRepository, times(1)).findById(ordenId);
        verify(orderRepository, never()).save(any(Order.class)); // Verifica que save NO fue llamado
    }

    @Test
    void testDeleteOrder_DeberiaEliminarOrdenCuandoExiste() {
        // Arrange
        Long ordenId = 1L;
        Order ordenExistente = new Order();
        ordenExistente.setId(ordenId);
        // No necesitamos muchos más detalles para la eliminación

        when(orderRepository.findById(ordenId)).thenReturn(Optional.of(ordenExistente));
        doNothing().when(orderRepository).delete(any(Order.class)); // Mockea el método delete

        // Act
        assertDoesNotThrow(() -> orderService.deleteOrder(ordenId)); // Verifica que no lanza excepción

        // Assert & Verify
        verify(orderRepository, times(1)).findById(ordenId);
        verify(orderRepository, times(1)).delete(ordenExistente); // Verifica que se llamó a delete con la orden correcta
    }

    @Test
    void testDeleteOrder_DeberiaLanzarExcepcionCuandoOrdenNoExiste() {
        // Arrange
        Long ordenId = 99L;
        when(orderRepository.findById(ordenId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(ordenId));
        verify(orderRepository, times(1)).findById(ordenId);
        verify(orderRepository, never()).delete(any(Order.class)); // Verifica que delete NO fue llamado
    }
}