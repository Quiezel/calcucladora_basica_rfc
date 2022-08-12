package dev;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import dev.quiezel.CalculadoraBasicaRFC;

public class CalculadoraBasicaRFCTest {
    private CalculadoraBasicaRFC calculadora;

    @Test
    public void calcularRFCTest() {
        LocalDate fecha = LocalDate.of(1956, 12, 31);
        calculadora = new CalculadoraBasicaRFC("Emma", "Gómez", "Díaz", fecha);
        assertEquals("GODE561231GR8", calculadora.getRFC());
    }

}
