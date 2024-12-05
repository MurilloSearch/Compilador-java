package automata;

public class cuadruplo {

    polish listaPolish;
    String operador;
    String operando1;
    String operando2;
    String resultado;
    String punteros;

    int numeroTemporal = 1;
    boolean temporal = false;
    String temp = "temp";

    nodo inicio;
    nodo fin;

    class nodo {

        String operador;
        String operando1;
        String operando2;
        String resultado;
        String punteros;
        nodo siguiente;

        public nodo(String punteros, String operador, String operando1, String operando2, String resultado) {
            this.punteros = punteros;
            this.operador = operador;
            this.operando1 = operando1;
            this.operando2 = operando2;
            this.resultado = resultado;
        }

        public nodo() {
            this.punteros = null;
            this.operador = null;
            this.operando1 = null;
            this.operando2 = null;
            this.resultado = null;
        }
    }

    class stack {

        private polish.nodo top; // Nodo superior de la pila

        // Constructor de la pila
        public stack() {
            this.top = null;

        }

        // Método para añadir un elemento a la pila (push)
        public void push(polish.nodo nodo) {
            polish.nodo auxiliar = new polish.nodo(nodo.lexema, nodo.apuntador);

            if (pilaVacia()) {
                auxiliar.siguiente = top;
                top = auxiliar;
            } else {
                auxiliar.siguiente = top; // Enlaza el nodo nuevo con el nodo superior actual
                top = auxiliar;      // Actualiza el nodo superior

            }
        }

        // Método para retirar el elemento superior de la pila (pop)
        public void pop() {
            if (pilaVacia()) {
                System.out.println("Pila vacia");
            } else {
                top = top.siguiente;     // Mueve el puntero al siguiente nodo
            }
        }

        // Método para retirar el elemento superior de la pila (pop)
        public void popAll() {
            while (!pilaVacia()) {

                top = top.siguiente;     // Mueve el puntero al siguiente nodo
            }
        }

        // Método para verificar si la pila está vacía
        public boolean pilaVacia() {
            return top == null;
        }

        // Método para imprimir los elementos de la pila (opcional)
        public void imprimirPila() {
            polish.nodo apuntador = top;
            while (apuntador != null) {
                System.out.print(apuntador.lexema + " ");
                apuntador = apuntador.siguiente;
            }
            System.out.println();
        }
    }

    stack pila = new stack();

    public void insertarEnLista(String punteros, String operador, String operando1, String operando2, String resultado) {

        nodo auxiliar = new nodo(punteros, operador, operando1, operando2, resultado);

        if (inicio == null) {
            inicio = auxiliar;
            fin = auxiliar;
        } else {
            fin.siguiente = auxiliar;
            fin = auxiliar;
        }
    }

    public void imprimirLista() {
        nodo auxiliar = inicio;
        System.out.println("Cuadruplo: ");
        System.out.printf("%-12s %-10s %-10s %-10s %-10s%n", "Apuntadores", "Operador", "Operando 1", "Operando 2", "Resultado");
        while (auxiliar != null) {
            System.out.printf("%-12s %-10s %-10s %-10s %-10s%n", auxiliar.punteros, auxiliar.operador, auxiliar.operando1, auxiliar.operando2, auxiliar.resultado);
            auxiliar = auxiliar.siguiente;
        }
    }

    public void recorrerListaPolish(polish.nodo nodo) {
        polish.nodo auxiliar = nodo;

        while (auxiliar != null) {
            operandoUoperador(auxiliar);
            auxiliar = auxiliar.siguiente;
        }
    }

    public void elseAritmetico() {
        operando2 = pila.top.lexema;
        pila.pop();
        punteros = pila.top.apuntador.toString();
        operando1 = pila.top.lexema;
        pila.pop();
        resultado = temp + numeroTemporal;
     
    }

    public void sumaResta() {
        if (temporal) {

            if (fin.operador.equals("*") || fin.operador.equals("/")) {
                operando2 = resultado;
                operando1 = pila.top.lexema;
            } else {
                operando1 = resultado;
                operando2 = pila.top.lexema;
            }
            punteros = pila.top.apuntador.toString();
            resultado = temp + numeroTemporal;
            pila.pop();

        } else {

            elseAritmetico();

        }

        insertarEnLista(punteros, operador, operando1, operando2, resultado);

        numeroTemporal++;
        temporal = true;
    }

    public void divMulti() {
        if (temporal) {

            punteros = pila.top.apuntador.toString();
            operando1 = resultado;
            operando2 = pila.top.lexema;
            resultado = temp + numeroTemporal;
            pila.pop();

        } else {

            elseAritmetico();
        }

        insertarEnLista(punteros, operador, operando1, operando2, resultado);

        numeroTemporal++;
        temporal = true;

    }

    public void operandoUoperador(polish.nodo nodo) {

        if (nodo.lexema.startsWith("BR")) {
            String tipoBR = nodo.lexema.substring(0, 3);
            resultado = nodo.lexema.substring(4, 5);
            nodo.lexema = tipoBR;
        }

        switch (nodo.lexema) {
            case "-":

                operador = "-";
                sumaResta();
                break;
            case "+":

                operador = "+";
                sumaResta();
                break;
            case "/":

                operador = "/";
                divMulti();
                break;

            case "*":

                operador = "*";
                divMulti();
                break;

            case "<":

                operador = "<";
                
                elseAritmetico();

                insertarEnLista(punteros, operador, operando1, operando2, resultado);

                numeroTemporal++;
                temporal = true;

                break;
            case ">":

                operador = ">";
                
                elseAritmetico();
                insertarEnLista(punteros, operador, operando1, operando2, resultado);

                numeroTemporal++;
                temporal = true;

                break;
            case "<=":

                operador = "<=";
                
                elseAritmetico();

                insertarEnLista(punteros, operador, operando1, operando2, resultado);

                numeroTemporal++;
                temporal = true;

                break;
            case ">=":

                operador = ">=";
                
                elseAritmetico();

                insertarEnLista(punteros, operador, operando1, operando2, resultado);

                numeroTemporal++;
                temporal = true;

                break;
            case "==":

                operador = "==";
                
                elseAritmetico();

                insertarEnLista(punteros, operador, operando1, operando2, resultado);

                numeroTemporal++;
                temporal = true;

                break;

            case "<>":

                operador = "<>";
                
                elseAritmetico();

                insertarEnLista(punteros, operador, operando1, operando2, resultado);

                numeroTemporal++;
                temporal = true;

                break;

            case "cout<<":

                punteros = pila.top.apuntador.toString();
                operador = "cout<<";
                operando1 = pila.top.lexema;
                operando2 = "";
                resultado = "";

                insertarEnLista(punteros, operador, operando1, operando2, resultado);
                pila.pop();
                break;

            case "cin>>":

                punteros = pila.top.apuntador.toString();
                operador = "cin>>";
                operando1 = "";
                operando2 = "";
                resultado = pila.top.lexema;

                insertarEnLista(punteros, operador, operando1, operando2, resultado);
                pila.pop();
                break;

            case "BRF":
                punteros = nodo.apuntador.toString();
                operador = "BRF";
                operando1 = fin.resultado;
                operando2 = "";

                insertarEnLista(punteros, operador, operando1, operando2, resultado);

                temporal = false;
                break;

            case "BRV":
                punteros = nodo.apuntador.toString();
                operador = "BRV";
                operando1 = fin.resultado;
                operando2 = "";

                insertarEnLista(punteros, operador, operando1, operando2, resultado);

                temporal = false;
                break;

            case "BRI":
                punteros = nodo.apuntador.toString();
                operador = "BRI";
                operando1 = "";
                operando2 = "";

                insertarEnLista(punteros, operador, operando1, operando2, resultado);

                temporal = false;
                break;

            case "=":

                if (temporal) {

                    punteros = pila.top.apuntador.toString();
                    operador = "=";
                    operando1 = resultado;
                    operando2 = "";
                    resultado = pila.top.lexema;
                    pila.pop();
                    temporal = false;

                } else {
                    
                    operador = "=";
                    operando1 = pila.top.lexema;
                    operando2 = "";                    
                    pila.pop();
                    punteros = pila.top.apuntador.toString();
                    resultado = pila.top.lexema;
                    pila.pop();
                    temporal = false;

                }

                insertarEnLista(punteros, operador, operando1, operando2, resultado);

                temporal = false;

                break;

            case "<<Fin>>":
                if (!nodo.apuntador.toString().equals("[null]")) {
                    punteros = nodo.apuntador.toString();
                    operador = "";
                    operando1 = "";
                    operando2 = "";
                    resultado = "";
                    insertarEnLista(punteros, operador, operando1, operando2, resultado);

                }
                break;

            default:
                pila.push(nodo);
                break;
        }
    }

    public cuadruplo crearCopiaProfunda(cuadruplo original) {
        cuadruplo copia = new cuadruplo();
        nodo actual = original.inicio;
        nodo nuevo = null, ultimo = null;
        while (actual != null) {
            nuevo = new nodo();
            nuevo = actual;
            if (copia.inicio == null) {
                copia.inicio = nuevo;
            } else {
                ultimo.siguiente = nuevo;
            }
            ultimo = nuevo;
            actual = actual.siguiente;
        }
        return copia;
    }

}
