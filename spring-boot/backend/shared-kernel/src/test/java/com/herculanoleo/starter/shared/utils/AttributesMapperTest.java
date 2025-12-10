package com.herculanoleo.starter.shared.utils;

import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AttributesMapperTest {

    @Test
    void attributes_shouldReturnMapWithTipoAndRelacionadoId_whenRelacionadoIdIsNotBlank() {
        // Arrange
        final String relacionadoId = UUID.randomUUID().toString();
        final TipoAcesso tipoAcesso = TipoAcesso.USUARIO_SISTEMA;

        AttributesMapper mapper = new AttributesMapper() {
            @Override
            public String relacionadoId() {
                return relacionadoId;
            }

            @Override
            public TipoAcesso tipo() {
                return tipoAcesso;
            }
        };

        // Act
        Map<String, List<String>> result = mapper.attributes();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(TipoAcesso.APPLICATION_TYPE_KEY));
        assertEquals(List.of(tipoAcesso.getValue()), result.get(TipoAcesso.APPLICATION_TYPE_KEY));
        assertTrue(result.containsKey(TipoAcesso.APPLICATION_RELACIONADO_ID_KEY));
        assertEquals(List.of(relacionadoId), result.get(TipoAcesso.APPLICATION_RELACIONADO_ID_KEY));
    }

    @Test
    void attributes_shouldReturnMapWithOnlyTipo_whenRelacionadoIdIsBlank() {
        // Arrange
        final TipoAcesso tipoAcesso = TipoAcesso.USUARIO_SISTEMA;

        AttributesMapper mapper = new AttributesMapper() {
            @Override
            public String relacionadoId() {
                return null; // Testando com nulo
            }

            @Override
            public TipoAcesso tipo() {
                return tipoAcesso;
            }
        };

        // Act
        Map<String, List<String>> result = mapper.attributes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(TipoAcesso.APPLICATION_TYPE_KEY));
        assertEquals(List.of(tipoAcesso.getValue()), result.get(TipoAcesso.APPLICATION_TYPE_KEY));
        assertFalse(result.containsKey(TipoAcesso.APPLICATION_RELACIONADO_ID_KEY));
    }
}