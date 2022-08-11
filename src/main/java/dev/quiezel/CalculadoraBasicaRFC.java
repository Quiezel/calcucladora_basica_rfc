/*
 * Basado en informacion de la pagina web : https://solucionfactible.com/sfic/capitulos/timbrado/rfc-persona-fisica.jsp
 */
package dev.quiezel;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalculadoraBasicaRFC {
    

    private final String nombre;
    private final String primerApellido;
    private final String segundoApellido;
    private final LocalDate fechaNacimiento;
    private final Map<Character, String> anexo1 = new HashMap<>();
    private final Map<Integer, Character> anexo2 = new HashMap<>();
    private final Map<Character, Integer> anexo3 = new HashMap<>();
    private final List<String> anexo4 = new ArrayList<>();
    private final List<String> anexo5 = new ArrayList<>();

    public CalculadoraBasicaRFC(String nombre, String primerApellido, String segundoApellido, LocalDate fechaNacimiento) {
        this.nombre = Normalizer.normalize(nombre.toUpperCase(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9Ñ& ]", "");
        String Apellido1 = Normalizer.normalize(primerApellido.toUpperCase(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9Ñ& ]", "");
        String Apellido2 = Normalizer.normalize(segundoApellido.toUpperCase(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9Ñ& ]", "");
        this.primerApellido = Apellido1.length() < 1 ? Apellido2 : Apellido1;
        this.segundoApellido = Apellido1.length() < 1 ? "" : Apellido2;
        this.fechaNacimiento = fechaNacimiento;
        //LLenamos anexos con información de las tablas
        llenarAnexo1();
        llenarAnexo2();
        llenarAnexo3();
        llenarAnexo4();
        llenarAnexo5();
    }

    public String getRFC() {
        return stringIniciales() + stringFecha() + calcularClaveHomonimiaRFC() + calcularVerificadorRFC();
    }

    public String stringIniciales() {
        StringBuilder iniciales = new StringBuilder();
        String apellido1 = eliminarSiAnexo5(primerApellido);
        String apellido2 = eliminarSiAnexo5(segundoApellido);
        String[] nombres = nombre.split(" ");
        String nombre1 = nombres.length > 1 && new ArrayList<>(Arrays.asList("JOSE,MARIA,JO,MA,J,M".split(","))).contains(nombres[0]) ? nombres[1] : nombres[0];
        iniciales.append(apellido1.charAt(0));
        iniciales.append(getPrimerVocal(apellido1.substring(1)));
        iniciales.append(apellido2.length() > 0 ? apellido2.charAt(0) : "");
        iniciales.append(nombre1.charAt(0));
        int faltante = 4 - iniciales.length();
        for (int i = 0; i < faltante; i++) {
            iniciales.append(nombre1.charAt(i + 1));
        }
        if (anexo4.contains(iniciales.toString())) {
            iniciales.replace(iniciales.length() - 1, iniciales.length(), "X");
        }
        return iniciales.toString();
    }

    public String stringFecha() {
        return fechaNacimiento.format(DateTimeFormatter.ofPattern("yyMMdd"));
    }

    public String calcularClaveHomonimiaRFC() {
        String persona = String.join(" ", primerApellido, segundoApellido, nombre);
        StringBuilder cadenaValores = new StringBuilder("0");
        for (char c : persona.toCharArray()) {
            cadenaValores.append(anexo1.get(c));
        }
        String valores = cadenaValores.toString();
        Integer suma = 0;
        for (int i = 0; i < valores.length() - 1; i++) {
            suma += numeroString(valores, i, i + 2) * numeroString(valores, i + 1);
        }
        String stringSuma = suma.toString();
        Integer ultimosTres = toInt(stringSuma.substring(stringSuma.length() - 3));
        Integer cociente = ultimosTres / 34;
        Integer residuo = ultimosTres % 34;
        return anexo2.get(cociente).toString() + anexo2.get(residuo);
    }

    private Integer toInt(String substring) {
        Integer numero;
        try {
            numero = Integer.parseInt(substring);
        } catch (NumberFormatException e) {
            numero = 0;
        }
        return numero;
    }

    public String calcularVerificadorRFC() {
        String RFCSinVerificar = String.join("", stringIniciales(), stringFecha(), calcularClaveHomonimiaRFC());
        Integer suma = 0;
        for (int i = 0; i < RFCSinVerificar.length(); i++) {
            suma += anexo3.getOrDefault(RFCSinVerificar.charAt(i), 0) * (13 - i);
        }
        Integer residuo = suma % 11;
        return residuo == 0 ? "0" : residuo == 1 ? "A" : (11 - residuo) + "";
    }

    public String getPrimerVocal(String texto) {
        int i = 0;
        for (; i < texto.length(); i++) {
            if (esVocal(texto.charAt(i))) {
                break;
            }
        }
        return String.valueOf(i < texto.length() ? texto.charAt(i) : "");
    }

    public static boolean esVocal(char c) {
        Character letra = Character.toLowerCase(c);
        return (letra == 'a') || (letra == 'e') || (letra == 'i') || (letra == 'o') || (letra == 'u');
    }

    public static boolean esConsonante(char c) {
        return !esVocal(c) && Character.isLetter(c);
    }

    private Integer numeroString(String texto, int inicio) {
        return numeroString(texto, inicio, inicio + 1);
    }

    private Integer numeroString(String texto, int inicio, int fin) {
        return toInt(texto.substring(inicio, fin));
    }

    private void llenarAnexo1() {
        anexo1.put(' ', "00");
        anexo1.put('0', "00");
        anexo1.put('1', "01");
        anexo1.put('2', "02");
        anexo1.put('3', "03");
        anexo1.put('4', "04");
        anexo1.put('5', "05");
        anexo1.put('6', "06");
        anexo1.put('7', "07");
        anexo1.put('8', "08");
        anexo1.put('9', "09");
        anexo1.put('%', "10");
        anexo1.put('A', "11");
        anexo1.put('B', "12");
        anexo1.put('C', "13");
        anexo1.put('D', "14");
        anexo1.put('E', "15");
        anexo1.put('F', "16");
        anexo1.put('G', "17");
        anexo1.put('H', "18");
        anexo1.put('I', "19");
        anexo1.put('J', "21");
        anexo1.put('K', "22");
        anexo1.put('L', "23");
        anexo1.put('M', "24");
        anexo1.put('N', "25");
        anexo1.put('O', "26");
        anexo1.put('P', "27");
        anexo1.put('Q', "28");
        anexo1.put('R', "29");
        anexo1.put('S', "32");
        anexo1.put('T', "33");
        anexo1.put('U', "34");
        anexo1.put('V', "35");
        anexo1.put('W', "36");
        anexo1.put('X', "37");
        anexo1.put('Y', "38");
        anexo1.put('Z', "39");
        anexo1.put('Ñ', "40");
    }

    private void llenarAnexo2() {
        anexo2.put(0, '1');
        anexo2.put(1, '2');
        anexo2.put(2, '3');
        anexo2.put(3, '4');
        anexo2.put(4, '5');
        anexo2.put(5, '6');
        anexo2.put(6, '7');
        anexo2.put(7, '8');
        anexo2.put(8, '9');
        anexo2.put(9, 'A');
        anexo2.put(10, 'B');
        anexo2.put(11, 'C');
        anexo2.put(12, 'D');
        anexo2.put(13, 'E');
        anexo2.put(14, 'F');
        anexo2.put(15, 'G');
        anexo2.put(16, 'H');
        anexo2.put(17, 'I');
        anexo2.put(18, 'J');
        anexo2.put(19, 'K');
        anexo2.put(20, 'L');
        anexo2.put(21, 'M');
        anexo2.put(22, 'N');
        anexo2.put(23, 'P');
        anexo2.put(24, 'Q');
        anexo2.put(25, 'R');
        anexo2.put(26, 'S');
        anexo2.put(27, 'T');
        anexo2.put(28, 'U');
        anexo2.put(29, 'V');
        anexo2.put(30, 'W');
        anexo2.put(31, 'X');
        anexo2.put(32, 'Y');
        anexo2.put(33, 'Z');
    }

    private void llenarAnexo3() {
        anexo3.put('0', 0);
        anexo3.put('1', 1);
        anexo3.put('2', 2);
        anexo3.put('3', 3);
        anexo3.put('4', 4);
        anexo3.put('5', 5);
        anexo3.put('6', 6);
        anexo3.put('7', 7);
        anexo3.put('8', 8);
        anexo3.put('9', 9);
        anexo3.put('A', 10);
        anexo3.put('B', 11);
        anexo3.put('C', 12);
        anexo3.put('D', 13);
        anexo3.put('E', 14);
        anexo3.put('F', 15);
        anexo3.put('G', 16);
        anexo3.put('H', 17);
        anexo3.put('I', 18);
        anexo3.put('J', 19);
        anexo3.put('K', 20);
        anexo3.put('L', 21);
        anexo3.put('M', 22);
        anexo3.put('N', 23);
        anexo3.put('&', 24);
        anexo3.put('O', 25);
        anexo3.put('P', 26);
        anexo3.put('Q', 27);
        anexo3.put('R', 28);
        anexo3.put('S', 29);
        anexo3.put('T', 30);
        anexo3.put('U', 31);
        anexo3.put('V', 32);
        anexo3.put('W', 33);
        anexo3.put('X', 34);
        anexo3.put('Y', 35);
        anexo3.put('Z', 36);
        anexo3.put(' ', 37);
        anexo3.put('Ñ', 38);
    }

    private void llenarAnexo4() {
        anexo4.add("BUEI");
        anexo4.add("BUEY");
        anexo4.add("CACA");
        anexo4.add("CACO");
        anexo4.add("CAGA");
        anexo4.add("CAGO");
        anexo4.add("CAKA");
        anexo4.add("CAKO");
        anexo4.add("COGE");
        anexo4.add("COJA");
        anexo4.add("COJE");
        anexo4.add("COJI");
        anexo4.add("COJO");
        anexo4.add("CULO");
        anexo4.add("FETO");
        anexo4.add("GUEY");
        anexo4.add("JOTO");
        anexo4.add("KACA");
        anexo4.add("KACO");
        anexo4.add("KAGA");
        anexo4.add("KAGO");
        anexo4.add("KOGE");
        anexo4.add("KOJO");
        anexo4.add("KAKA");
        anexo4.add("KULO");
        anexo4.add("MAME");
        anexo4.add("MAMO");
        anexo4.add("MEAR");
        anexo4.add("MEAS");
        anexo4.add("MEON");
        anexo4.add("MION");
        anexo4.add("MOCO");
        anexo4.add("MULA");
        anexo4.add("PEDA");
        anexo4.add("PEDO");
        anexo4.add("PENE");
        anexo4.add("PUTA");
        anexo4.add("PUTO");
        anexo4.add("QULO");
        anexo4.add("RATA");
        anexo4.add("RUIN");
    }

    private void llenarAnexo5() {
        String palabrasNoUsadas = "EL,LA,DE,S DE RL,SA DE CV,LA,DE,LOS,LAS,LAS,"
                + "Y,MC,DEL,SA,VON,COMPAÑÍA,CIA,DEL,SOCIEDAD,SOC,LOS,COOPERATIVA,"
                + "COOP,Y,S EN C POR A,A EN P,MAC,S EN NC,S EN C,VAN,PARA,EN,MI,"
                + "POR,CON,AL,SUS,E,SC,SCL,SCS,SNC,THE,OF,AND,COMPANY,CO,MC,MAC,"
                + "VON,VAN,MI,A,SRL CV,SA DE CV MI,SA MI,COMPA&ÍA,SRL CV MI,SRL MI";
        Arrays.stream(palabrasNoUsadas.split(","))
                .filter(p -> p.trim().length() > 0)
                .forEach(anexo5::add);
    }

    private String eliminarSiAnexo5(String persona) {
        return Arrays.stream(persona.split(" "))
                .filter(p -> p.trim().length() > 0)
                .filter(p -> !anexo5.contains(p))
                .collect(Collectors.joining(" "));
    }
}
