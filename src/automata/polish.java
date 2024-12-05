package automata;
import java.util.ArrayList;

public class polish {

    public static class nodo {

        String lexema;
        ArrayList<Integer> apuntador;
        nodo siguiente;
        nodo anterior;

        public nodo(String lexema, ArrayList apuntador) {
            this.lexema = lexema;
            this.apuntador = apuntador;
        }
        
    }
    nodo primerNodo = null;
    nodo ultimoNodo = null;
    
    class stack {

        private nodoPila top; // Nodo superior de la pila

        // Clase interna para representar un nodo de la pila
        private static class nodoPila {

            private String dato;
            private int jerarquia;
            private nodoPila next;

            public nodoPila(String dato, int jerarquia) {
                this.dato = dato;
                this.jerarquia = jerarquia;
            }
        }

        // Constructor de la pila
        public stack() {
            this.top = null;
            
        }
        
        public void distriminarOperador(String dato){
            switch(dato){
                case "=":
                    push(dato,1);
                    break;
                case "+":
                    push(dato,2);
                    break;
                case "-":
                    push(dato,2);
                    break;
                case "*":
                    push(dato,3);
                    break;
                case "/":
                    push(dato,3);
                    break;
                
            }
        }

        // Método para añadir un elemento a la pila (push)
        public void push(String dato, int jerarquia) {
            nodoPila auxiliar = new nodoPila(dato, jerarquia);
            
            if(pilaVacia()){
                auxiliar.next = top; 
                top = auxiliar;
            }
            else if (top.jerarquia<auxiliar.jerarquia) {
                auxiliar.next = top; // Enlaza el nodo nuevo con el nodo superior actual
                top = auxiliar;      // Actualiza el nodo superior
            }else{
                pop();
                auxiliar.next = top; // Enlaza el nodo nuevo con el nodo superior actual
                top = auxiliar;      // Actualiza el nodo superior
            }            
        }

        // Método para retirar el elemento superior de la pila (pop)
        public void pop() {
            if (pilaVacia()) {
                System.out.println("Pila vacia");
            }else{
            nodo salida = new nodo(top.dato,null);// Obtiene el dato del nodo superior
            insertarEnLista(salida.lexema,salida.apuntador);
            top = top.next;     // Mueve el puntero al siguiente nodo
            }
        }

        // Método para retirar el elemento superior de la pila (pop)
        public void popAll() {
            while (!pilaVacia()) {

                nodo salida = new nodo(top.dato,null);// Obtiene el dato del nodo superior
                insertarEnLista(salida.lexema,salida.apuntador);
                top = top.next;     // Mueve el puntero al siguiente nodo
            }
        }

        // Método para verificar si la pila está vacía
        public boolean pilaVacia() {
            return top == null;
        }

        // Método para imprimir los elementos de la pila (opcional)
        public void imprimirPila() {
            nodoPila apuntador = top;
            while (apuntador != null) {
                System.out.print(apuntador.dato + " ");
                apuntador = apuntador.next;
            }
            System.out.println();
        }
    }

    stack pila = new stack();

    polish() {
        this.primerNodo = null;
        this.ultimoNodo = null;
    }

    public void insertarEnLista(String lexema, ArrayList apuntador) {
        nodo auxiliar = new nodo(lexema, apuntador);

        if (primerNodo == null) {
            primerNodo = auxiliar;
            ultimoNodo = auxiliar;
            primerNodo.anterior = null;
            ultimoNodo.siguiente = null;
            
        } else {
            ultimoNodo.siguiente = auxiliar;
            auxiliar.anterior = ultimoNodo;
            ultimoNodo = auxiliar;
            ultimoNodo.siguiente = null;

        }
    }

    public void operandoUoperador(String lexema, ArrayList apuntador) {
        if (lexema.equals("=")||lexema.equals("+")||lexema.equals("-")||lexema.equals("*")||lexema.equals("/")) {
            pila.distriminarOperador(lexema);
        } else {
            insertarEnLista(lexema, apuntador);
        }
    }
    
    public void imprimirLista(){
        nodo auxiliar = primerNodo;
        System.out.println("Posfijo: ");
        System.out.printf("%-10s %-12s%n","Nodo","Apuntadores");
        while(auxiliar!=null){
            System.out.printf("%-10s %-12s%n",auxiliar.lexema,auxiliar.apuntador);
            auxiliar = auxiliar.siguiente;
        }
        
    }
   

}
