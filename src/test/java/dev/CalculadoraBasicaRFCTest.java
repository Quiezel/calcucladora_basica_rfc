package dev;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import dev.quiezel.CalculadoraBasicaRFC;

public class CalculadoraBasicaRFCTest {
    private CalculadoraBasicaRFC calculadora;

    @Test
    public void calcularRFCTest() {
        LocalDate fecha = LocalDate.of(1970, 12, 13);
        calculadora = new CalculadoraBasicaRFC("Juan", "Barrios", "Fernández", fecha);
        assertEquals("BAFJ701213SBA", calculadora.getRFC());
    }

}
