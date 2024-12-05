
package automata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class sintactico {

    boolean errorSintaxis = false;
    nodo p = null;
    variable primerVariable = null,variableN;
    int tipoDeDato;
    polish listaPolish = new polish();
    
    cuadruplo cuadruplo = new cuadruplo();
    int contadorApuntador = 1;
    boolean apuntadorEstructura = false;
    boolean doWhileBool = false;
    boolean doBool = false;
    boolean ifBool = false;
    boolean noElse = false;
    
       
    private ArrayList valorApuntador(){
        ArrayList<Integer> apuntador = new ArrayList<>();
        if(apuntadorEstructura){
            apuntador.add(contadorApuntador);
            contadorApuntador++;
            apuntadorEstructura = false;
            if (noElse) {
                apuntador.add(contadorApuntador);
                contadorApuntador++;  
                noElse = false;
            }
        }else{
            apuntador.add(null);
        }
        return apuntador;
    }
        
    public void sintaxis() {
        
        if (p!=null&&p.token == 236) { //inicio
            p = p.sig;

            if (p != null && (p.token == 227 )) { //Tipo de dato void

                p = p.sig;
                if (p != null && p.token == 239) { //Main

                    p = p.sig;
                    if (p != null && p.token == 113) {//Parentesis apertura (

                        p = p.sig;
                        if (p != null && p.token == 114) {//Parentesis cierre )

                            p = p.sig;
                            if (p != null && p.token == 122) {//Corchete apertura {
                                
                                p = p.sig;
                                if (p != null && validarSentencia()) {//bloque

                                    while (p != null && !errorSintaxis && p.token != 226) {
                                        bloque();
                                    }

                                    if (p != null && p.token == 226) { //Return

                                        p = p.sig;

                                            if (p != null && p.token == 117) {//Punto y coma ;

                                                p = p.sig;
                                                
                                                if (p != null && p.token == 123) {//Corchete cierre }
                                                                                                        
                                                    p = p.sig;
                                                    
                                                    listaPolish.insertarEnLista("<<Fin>>",valorApuntador());
                                                    listaPolish.imprimirLista();
                                                    System.out.println("");
                                                    
                                                    cuadruplo.recorrerListaPolish(listaPolish.primerNodo);
                                                    cuadruplo.imprimirLista();
                                                    System.out.println("");
                                                    
                                                    
                                                    ensamblador ensamblador = new ensamblador(variableN,cuadruplo);
                                                    
                                                    String archivo = new File("").getAbsolutePath() + "/src/automata/r.asm";
                                                    File file = new File(archivo);
                                                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                                                        writer.write(ensamblador.crearASM()); // Escribir el contenido
                                                        System.out.println("Texto agregado exitosamente al archivo.");
                                                    } catch (IOException e) {
                                                        System.err.println("Error al escribir en el archivo: " + e.getMessage());
                                                    }
                                                    
                                                } else {

                                                    imprimirError("corchete de cierre");
                                                }
                                            } else {

                                                imprimirError("punto y coma");
                                            }
                                        
                                    } else {
                                        
                                        if (!errorSintaxis) {

                                            imprimirError("return");
                                        }

                                    }
                                } else {

                                    imprimirError("sentencia valida (leer, escribir, declarar, inicializar, if, while, do while");
                                }

                            } else {

                                imprimirError("corchete de apertura");
                            }
                        } else {
                            imprimirError("parentesis de cierre");
                        }

                    } else {
                        imprimirError("parentesis de apertura");
                    }

                } else {

                    imprimirError("palabra main");
                }

            } else {
                imprimirError("palabra void");
            }

        } else {

            imprimirError("palabra inicio");
        }
    }
    //Semantico de aqui para abajo
    
    int sistemaDeTiposSuma[][]={
    // |   +   |  int  | real  | string |  char |  bool  |
    //         |   0   |   1   |   2   |    3   |    4   |
    /*0 int   */{  222 ,  233  ,  508  ,   508  ,   508  },
    /*1 real  */{  233 ,  233  ,  508  ,   508  ,   508  },
    /*2 string*/{  508 ,  508  ,  225  ,   225  ,   508  },
    /*3 char  */{  508 ,  508  ,  225  ,   225  ,   508  },
    /*4 bool  */{  508 ,  508  ,  508  ,   508  ,   508  }
    };
    
    int sistemaDeTiposRestaMutilplicacion[][]={
    // | -  *  |  int  | real  | string |  char |  bool  |
    //         |   0   |   1   |   2   |    3   |    4   |
    /*0 int   */{  222 ,  233  ,  508  ,   508  ,   508  },
    /*1 real  */{  233 ,  233  ,  508  ,   508  ,   508  },
    /*2 string*/{  508 ,  508  ,  508  ,   508  ,   508  },
    /*3 char  */{  508 ,  508  ,  508  ,   508  ,   508  },
    /*4 bool  */{  508 ,  508  ,  508  ,   508  ,   508  }
    };
    
    int sistemaDeTiposDivision[][]={
    // |   /   |  int  | real  | string |  char |  bool  |
    //         |   0   |   1   |   2   |    3   |    4   |
    /*0 int   */{  233 ,  233  ,  508  ,   508  ,   508  },
    /*1 real  */{  233 ,  233  ,  508  ,   508  ,   508  },
    /*2 string*/{  508 ,  508  ,  508  ,   508  ,   508  },
    /*3 char  */{  508 ,  508  ,  508  ,   508  ,   508  },
    /*4 bool  */{  508 ,  508  ,  508  ,   508  ,   508  }
    };
    
    int sistemaDeTiposComparativos[][]={ //>=, <=, <, >
    // |>= > <= <|  int  | real  | string |  char  | bool |
    //           |   0   |   1   |   2    |    3   |   4  |
    /*0 int   */ {  206  ,  206  ,  508   ,   508  ,   508},
    /*1 real  */ {  206  ,  206  ,  508   ,   508  ,   508},
    /*2 string*/ {  508  ,  508  ,  508   ,   508  ,   508},
    /*3 char  */ {  508  ,  508  ,  508   ,   508  ,   508},
    /*4 bool  */ {  508  ,  508  ,  508   ,   508  ,   508}
    };
    
    int sistemaDeTiposIgualdad[][]={
    // |== <>  |  int  | real  | string |  char  | bool  |
    //         |   0   |   1   |   2   |    3    |   4   |
    /*0 int   */{  206 ,  206  ,  508  ,   508  ,   508  },
    /*1 real  */{  206 ,  206  ,  508  ,   508  ,   508  },
    /*2 string*/{  508 ,  508  ,  206  ,   508  ,   508  },
    /*3 char  */{  508 ,  508  ,  508  ,   206  ,   508  },
    /*4 bool  */{  508 ,  508  ,  508  ,   508  ,   206  }
    };
    
    int sistemaDeTiposLogicos[][]={
    // |and or |  int  | real  | string |  char  | bool  |
    //         |   0   |   1   |   2   |    3    |    4  |
    /*0 int   */{  508 ,  508  ,  508  ,   508  ,   508  },
    /*1 real  */{  508 ,  508  ,  508  ,   508  ,   508  },
    /*2 string*/{  508 ,  508  ,  508  ,   508  ,   508  },
    /*3 char  */{  508 ,  508  ,  508  ,   508  ,   508  },
    /*4 bool  */{  508 ,  508  ,  508  ,   508  ,   206  }
    };
    
    int sistemaDeTiposNot[][]={
    // |  not  |  int  | real  | string |  char  | bool  |
    //         |   0   |   1   |   2   |    3   |    3   |
               {  508  ,  508  ,  508  ,   508  ,   206  }
    };
    int sistemaDeTiposAsignacion[][]={
    // |  =    |  int  | real  | string |  char  | bool  |
    //         |   0   |   1   |   2   |    3    |    4  |
    /*0 int   */{  222 ,  508  ,  508  ,   508  ,   508  },
    /*1 real  */{  508 ,  233  ,  508  ,   508  ,   508  },
    /*2 string*/{  508 ,  508  ,  225  ,   508  ,   508  },
    /*3 char  */{  508 ,  508  ,  508  ,   224  ,   508  },
    /*4 bool  */{  508 ,  508  ,  508  ,   508  ,   206  }
    };
    
    String erroresSemantica[][]={
        /*0*/ {"Variable ya declarada","506"},
        /*1*/ {"Variable no declarada","507"},
        /*2*/ {"Incompatibilidad de tipos","508"}};
        
    private boolean variableDeclarada(){
        boolean variableDeclarada = false;
        variable variableTemporal = variableN;
        if (variableN!=null) {
         
            do{
                if (p.lexema.equals(variableTemporal.lexema)) {
                    variableDeclarada = true;
                    break;
                }
                variableTemporal = variableTemporal.sig;
            }while(variableTemporal!=null);
        }
        return variableDeclarada;
    }

    private void variableInicializada(){
        variable variableTemporal = variableN;
        if (variableN!=null) {
         
            do{
                if (p.lexema.equals(variableTemporal.lexema)) {
                    variableTemporal.inicializada = true;
                }
                variableTemporal = variableTemporal.sig;
            }while(variableTemporal!=null);
        }
    }
    
    private boolean variableRepetida(){
        boolean variableRepetida = variableDeclarada();
        if (!variableRepetida) {
            insertarVariables();
        }
        
        return variableRepetida;
        
    }
    
    private void insertarVariables() {
        variable variable = new variable(p.lexema, p.token,tipoDeDato);
        if (primerVariable == null) {
            variableN = variable;
            primerVariable = variableN;
        } else {
            primerVariable.sig = variable;
            primerVariable = variable;
        }
    }
    
    private int buscarTipoDeDato(String lexema){
        variable variableTemporal = variableN;
        int dato=0;
        do{
            if (lexema.equals(variableTemporal.lexema)) {
                dato = variableTemporal.tipoDeDato;
                break;
            }
            variableTemporal = variableTemporal.sig;
        }while(variableTemporal!=null);
        
        return dato;
    }
    
    private int incopatibiidadDeTipos(int operador, int operando1, int operando2){
        int resultado;
        switch (operando1){
            case 101: //int
                operando1 = 0;
                break;
            case 222: //int
                operando1 = 0;
                break;
            case 233: //float
                operando1 = 1;
                break;
            case 102: //float
                operando1 = 1;
                break;
            case 121://string
                operando2= 2;
                break;
            case 225://string
                operando1= 2;
                break;
            case 127://char
                operando2= 3;
                break;
            case 224://char
                operando1= 3;
                break;
            case 206://bool
                operando1= 4;
                break;
            case 229://bool
                operando1= 4;
                break;
            case 230://bool
                operando1= 4;
                break;

        }
        
        switch (operando2){
            case 101: //int
                operando2 = 0;
                break;
            case 222: //int
                operando2 = 0;
                break;
            case 233: //float
                operando2 = 1;
                break;
            case 102: //float
                operando2 = 1;
                break;
            case 225://string
                operando2= 2;
                break;
            case 121://string
                operando2= 2;
                break;
            case 127://char
                operando2= 3;
                break;
            case 224://char
                operando2= 3;
                break;
            case 206://bool
                operando2= 4;
                break;
            case 229://bool
                operando2= 4;
                break;
            case 230://bool
                operando2= 4;
                break;
 
        }
        
        switch (operador){
            case 103://suma
                resultado = sistemaDeTiposSuma[operando1][operando2];
                break;
            case 104://-
                resultado = sistemaDeTiposRestaMutilplicacion[operando1][operando2];
                break;
            case 105://*
                resultado = sistemaDeTiposRestaMutilplicacion[operando1][operando2];
                break;
            case 106:///
                resultado = sistemaDeTiposDivision[operando1][operando2];
                break;
            case 107://<
                resultado = sistemaDeTiposComparativos[operando1][operando2];
                break;
            case 108://<=
                resultado = sistemaDeTiposComparativos[operando1][operando2];
                break;
            case 109://>
                resultado = sistemaDeTiposComparativos[operando1][operando2];
                break;
            case 110://>=
                resultado = sistemaDeTiposComparativos[operando1][operando2];
                break;
            case 111://==
                resultado = sistemaDeTiposIgualdad[operando1][operando2];
                break;
            case 112://<>
                resultado = sistemaDeTiposIgualdad[operando1][operando2];
                break;
            case 122://or
                resultado = sistemaDeTiposLogicos[operando1][operando2];
                break;
            case 123://and
                resultado = sistemaDeTiposLogicos[operando1][operando2];
                break;
            case 124://not
                resultado = sistemaDeTiposNot[0][operando1];
                break;
            case 120://=
                resultado = sistemaDeTiposAsignacion[operando1][operando2];
                break;
            default:
                resultado = 508;
                break;
        }
        return resultado;
    }
    
    private void errorSemantico(String errorSemantico){
        errorSintaxis = true;
        System.out.println(errorSemantico+". Linea: "+p.renglon);
    }
       
//Sintactico de aqui para abajo

    private boolean validarSentencia() {
        if (p.token == 237/*cin*/ || p.token == 238/*cout*/ || p.token == 222 || p.token == 224
                || p.token == 206 || p.token == 233 || p.token == 225/*tipos de datos*/
                || p.token == 100/*variable*/ || p.token == 213/*if*/
                || p.token == 231/*while*/ || p.token == 207/*dowhile*/) {
            return true;
        } else {
            return false;
        }
    }

    private void bloque() {
        if (validarSentencia()) {

            switch (p.token) {
                case 237:
                    System.out.println("    Se inicio lectura en renglon "+ p.renglon);
                    p = p.sig;
                    leer();
                    break;
                case 238:
                    System.out.println("    Se inicio impresion en renglon "+ p.renglon);
                    p = p.sig;
                    escribir();
                    break;
                case 222://int
                    tipoDeDato=222;
                    p = p.sig;
                    declarar();
                    break;
                case 224://char
                    tipoDeDato=224;
                    p = p.sig;
                    declarar();
                    break;
                case 206://bool
                    tipoDeDato=206;
                    p = p.sig;
                    declarar();
                    break;
                case 233://float
                    tipoDeDato=233;
                    p = p.sig;
                    declarar();
                    break;
                case 225://string
                    tipoDeDato=225;
                    p = p.sig;
                    declarar();
                    break;
                case 100:
                    inicializar();
                    break;
                case 213:
                    System.out.println("    Se inicio if en renglon "+ p.renglon);
                    p = p.sig;
                    condicional();
                    break;
                case 231:
                    System.out.println("    Se inicio while en renglon "+ p.renglon);
                    p = p.sig;
                    mientras();
                    break;
                case 207:
                    System.out.println("    Se inicio do while en renglon "+ p.renglon);
                    p = p.sig;
                    hacerMientras();
                    break;
            }
        } else {
            System.out.println(p.lexema+p.token);
            imprimirError("sentencia valida (leer, escribir, declarar, inicializar, if, while, do while");
        }
    }

    private void leer() {
        if (p != null && p.token == 126) {//Flujo de entrada >>
            
            listaPolish.pila.push("cin>>", 1);
            p = p.sig;
            if (p != null && p.token == 100) { //variable
                
                listaPolish.insertarEnLista(p.lexema, valorApuntador());
                p = p.sig;
                if (p != null && p.token == 126) {

                    leer();
                } else if (p != null && p.token == 117) {
                    listaPolish.pila.pop();
                    p = p.sig;
                } else {

                    imprimirError("punto y coma o flujo de entrada (>>)");
                }
            } else {

                imprimirError("nombre de la variable a leer");
            }
        } else {

            imprimirError("flujo de entrada (>>)");
        }
    }

    private void escribir() {

        if (p != null && p.token == 125) {//Flujo de salida <<
            listaPolish.pila.push("cout<<", 1);
            p = p.sig;
            if (p != null && (p.token == 121 || p.token == 100)) { //Cadena o variable
                listaPolish.insertarEnLista(p.lexema, valorApuntador());
                listaPolish.pila.pop();
                p = p.sig;
                if (p != null && p.token == 125) {
                    escribir();
                } else if (p != null && p.token == 117) {

                    p = p.sig;
                } else {

                    imprimirError("punto y coma o flujo de salida(<<)");
                }

            } else {

                imprimirError("cadena, variable o endl");
            }
        } else {

            imprimirError("flujo de salida (<<)");
        }
    }

    private void declarar() {

        if (p != null && p.token == 100) {
            if (!variableRepetida()) {
                System.out.println("    Se declaro variable "+p.lexema+" en renglon "+ p.renglon);
                p = p.sig;
                if (p != null && (p.token == 117 || p.token == 116)) {

                    if (p != null && p.token == 116) {

                        p = p.sig;
                        declarar();
                    } else if (p != null && p.token == 117) {
                        p = p.sig;
                        } else {

                            imprimirError("punto y coma");
                        }
                } else {

                    imprimirError("coma para declarar mas variable o punto y coma para terminar");
                }
            }else{
                errorSemantico("    Error "+erroresSemantica[0][1]+" "+erroresSemantica[0][0]);
            }
        } else {

            imprimirError("nombre de variable");
        }
    }

    private void inicializar() {
        
        if (variableDeclarada()) {
            System.out.println("    Se inicializo variable "+ p.lexema +" en renglon "+ p.renglon);
            int operando1 = buscarTipoDeDato(p.lexema);
            variableInicializada();
            listaPolish.operandoUoperador(p.lexema, valorApuntador());
            
            p = p.sig;
            if (p != null && p.token == 120) {//=
                operaciones(operando1);
            } else {

                imprimirError("simbolo igual para inicializar variable");
            }
        }else{
            errorSemantico("    Error "+erroresSemantica[1][1]+" "+erroresSemantica[1][0]);
        }
    }

    private void operaciones(int operando1) {
        int operador = p.token;
        int operando2;
        
        listaPolish.operandoUoperador(p.lexema, valorApuntador());
        
        p = p.sig;
        if (p != null && (p.token == 121 ||/*string*/p.token == 127 ||/*char*/ p.token == 101/*numero*/
                || p.token == 102/*decimal*/ || p.token == 229/*true*/
                || p.token == 230/*false*/ || p.token == 100)) {

            if (p.token==100&&!variableDeclarada()) {
                        errorSemantico("    Error "+erroresSemantica[1][1]+" "+erroresSemantica[1][0]);
                    }else{
                        if (p.token==100) {
                            listaPolish.operandoUoperador(p.lexema, valorApuntador());
                            operando2 = buscarTipoDeDato(p.lexema);
                        }else{
                            listaPolish.operandoUoperador(p.lexema, valorApuntador());
                            operando2= p.token;
                        }
                        operando1 = incopatibiidadDeTipos(operador, operando1, operando2);
                        if (operando1==508) {
                            errorSemantico("    Error "+erroresSemantica[2][1]+" "+erroresSemantica[2][0]);
                        }else{
                            p = p.sig;
                            if (p != null && (p.token == 103/*+*/ || p.token == 104/*-*/
                            || p.token == 105/***/ || p.token == 106/*/*/)) {
                                operaciones(operando1);
                                
                            } else if (p != null && p.token == 117) { //;
                                listaPolish.pila.popAll();
                                p = p.sig;
                            } else {

                                imprimirError("punto y coma");
                            }                        
                        }
                        
                    }

        } else {

            imprimirError("variable o valor");
        }
    }

    private void comparacion(){
        int operando1,operando2 = 0,operador;
        if (p != null && p.token == 113/*(*/) {
            p = p.sig;
            if (p != null && (p.token == 100/*variable*/ || p.token == 101/*num*/
                            || p.token == 102/*decimal*/ || p.token == 121/*string*/
                            ||p.token == 127/*char*/
                            || p.token == 229/*true*/ || p.token == 230/*false*/)) {
                
                if (p.token==100&&!variableDeclarada()) {
                   errorSemantico("    Error "+erroresSemantica[1][1]+" "+erroresSemantica[1][0]);
                }else{
                    if (p.token==100) {
                        operando1 = buscarTipoDeDato(p.lexema);
                    }else{
                        operando1 = p.token;
                    }
                    listaPolish.insertarEnLista(p.lexema, valorApuntador());
                    p = p.sig;
                    if (p != null && (p.token == 107/*<*/ || p.token == 108/*<=*/ || p.token == 109/*>*/
                        || p.token == 110/*>=*/ || p.token == 111/*==*/ || p.token == 112/*<>*/)) {
                        operador = p.token;
                        listaPolish.pila.push(p.lexema, 0);
                        p = p.sig;
                        if (p != null && (p.token == 100/*variable*/ || p.token == 101/*num*/
                            || p.token == 102/*decimal*/ || p.token == 121/*string*/||p.token == 127/*char*/
                            
                            || p.token == 229/*true*/ || p.token == 230/*false*/)) {
                            
                            if (p.token==100&&!variableDeclarada()) {
                                errorSemantico("    Error "+erroresSemantica[1][1]+" "+erroresSemantica[1][0]);
                            }else{
                                if (p.token==100) {
                                    operando2 = buscarTipoDeDato(p.lexema);
                                }else{
                                    operando2 = p.token;
                                }
                            }
                            if (incopatibiidadDeTipos(operador, operando1, operando2)==508) {
                                errorSemantico("    Error "+erroresSemantica[2][1]+" "+erroresSemantica[2][0]);

                            }else{
                                listaPolish.insertarEnLista(p.lexema, valorApuntador());
                                listaPolish.pila.pop();
                                if (doWhileBool) {
                                    listaPolish.insertarEnLista("BRV "+(contadorApuntador-1), valorApuntador());
                                    doWhileBool = false;
                                }
                                if (doBool) {
                                    listaPolish.insertarEnLista("BRF "+(contadorApuntador), valorApuntador());
                                    doBool = false;
                                }
                                if (ifBool) {
                                    listaPolish.insertarEnLista("BRF "+(contadorApuntador), valorApuntador());
                                    ifBool = false;
                                }
                                p = p.sig;
                                if (p != null && p.token == 114/*)*/) {

                                    p = p.sig;
                            
                                } else {

                                    imprimirError("parentesis de cierre");
                                }
                            
                            }
                            
                        } else {

                        imprimirError("variable a comparar");
                        }
                    } else {

                        imprimirError("operador relaciona");
                    }
                }
                
                
            } else {

                imprimirError("condición");
            }
        } else {

            imprimirError("parentesis de apertura");
        }
        
    }

    private void condicional() {
        ifBool=true;
        comparacion();
        if(!errorSintaxis){
            if (p != null && p.token == 122/*{*/) {
                                p = p.sig;
                                if (p != null && validarSentencia()) {
                                    while(p.token!=123){
                                        bloque();
                                    }
                                    if (p != null && p.token == 123/*}*/) {
                                        p = p.sig;
                                        listaPolish.insertarEnLista("BRI "+(contadorApuntador+1), valorApuntador());
                                        apuntadorEstructura = true;
                                        if (p != null && p.token == 218) {
                                            p = p.sig;
                                            otraCosa();
                                        }else{
                                            noElse = true;
                                        }
                                    } else {

                                        imprimirError("corchete de cierre");
                                    }
                                } else {

                                    imprimirError("sentencia valida");
                                }

                            } else {

                                imprimirError("corchete de apertura");
                            }
        }
                            
    }

    private void otraCosa() {
        if (p != null && p.token == 122/*{*/) {

            p = p.sig;
            if (p != null && validarSentencia()) {
                
                while(p.token!=123){
                    bloque();
                }
                if (p != null && p.token == 123/*}*/) {

                    p = p.sig;
                    apuntadorEstructura = true;
                } else {

                    if (!errorSintaxis) {

                        System.out.println("    Se espera corchete de cierre");
                    }
                    errorSintaxis = true;
                }

            } else {

                imprimirError("sentencia valida");
            }

        } else {
            
            imprimirError("corchete de apertura");
        }
    }

    private void mientras() {

        apuntadorEstructura= true;
        doBool = true;
        comparacion();
        if (!errorSintaxis) {
            if (p != null && p.token == 122/*{*/) {

                                p = p.sig;
                                if (p != null && validarSentencia()) {
                                    while(p.token!=123){
                                        bloque();
                                    }
                                    if (p != null && p.token == 123/*}*/) {
                                        p = p.sig;
                                        listaPolish.insertarEnLista("BRI "+ (contadorApuntador-1), valorApuntador());
                                        apuntadorEstructura = true;

                                    } else {
                                        
                                        imprimirError("corchete de cierre");
                                    }
                                } else {

                                    imprimirError("sentencia valida");
                                }

                            } else {

                                imprimirError("corchete de apertura");
                            }
        }
                            
    }

    private void hacerMientras() {
        if (p != null && p.token == 122/*{*/) {

            p = p.sig;
            apuntadorEstructura= true;
            if (p != null && validarSentencia()) {
                
                
                while(p.token!=123){
                    bloque();
                }
                if (p != null && p.token == 123/*}*/) {

                    p = p.sig;
                    if (p != null && p.token == 231/*while*/) {
                        p = p.sig;
                        doWhileBool=true;
                        comparacion();
                        if (!errorSintaxis) {
                            if (p != null && p.token == 117/*;*/) {
                                p = p.sig;
                            } else {

                                imprimirError("punto y coma");
                            }
                        }
                                        
                    } else {

                        imprimirError("while");
                    }
                } else {

                    if (!errorSintaxis) {

                        System.out.println("    Se espera corchete de cierre");
                    }
                    errorSintaxis = true;
                }
            } else {

                imprimirError("sentencia valida");
            }
        } else {

            imprimirError("corchete de apertura");
        }
    }
     
    private void imprimirError(String valorOmitido){
        
        errorSintaxis = true;
        if (p==null) {
            System.out.println("    Se espera "+ valorOmitido + " al final del documento");
        }else{
            System.out.println("    Se espera "+ valorOmitido + " en renglon " + p.renglon);
        }
    }
    
      
}

