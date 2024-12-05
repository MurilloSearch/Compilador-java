package automata;

public class Automata {

    nodo p;

    public static void main(String[] args) {
        
        System.out.println("Comenzando analizador Lexico");
        lexico lexico = new lexico();
        if (!lexico.errorEncontrado) {
            System.out.println("Analizador Lexico Terminado\n");
            sintactico sintactico = new sintactico();
            sintactico.p = lexico.cabeza;
            System.out.println("Comenzando analizador Sintactico");
            sintactico.sintaxis();
            if (!sintactico.errorSintaxis) {
            System.out.println("Analizador Sintactico Terminado");
        }
        }

        
        
    }

}
