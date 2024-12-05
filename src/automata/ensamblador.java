
package automata;
public class ensamblador {

    cuadruplo cuadruplo;
    variable variable;
    
    String blue1de5 = 
"INCLUDE macros.mac\n" +
"DOSSEG\n" +
".MODEL SMALL\n" +
".STACK 100h\n" +
"\n" +
".DATA\n" +
"            BUFFER      DB 8 DUP('$')  ;23h\n" +
"            BUFFERTEMP  DB 8 DUP('$')  ;23h\n" +
"            BLANCO  DB '#'\n" +
"            BLANCOS DB '$'\n" +
"            MENOS   DB '-$'\n" +
"            COUNT   DW 0\n" +
"            NEGATIVO    DB 0\n" +
"            ARREGLO DW 0\n" +
"            ARREGLO1    DW 0\n" +
"            ARREGLO2    DW 0\n" +
"            LISTAPAR    LABEL BYTE\n" +
"            LONGMAX DB 254\n" +
"            TOTCAR  DB ?\n" +
"            INTRODUCIDOS    DB 254 DUP ('$')\n" +
"            MULT10  DW 1\n" +
"            t1 dw ?\n" +
"            t2 dw ?\n" +
"            t3 dw ?\n" +
"            cin db \"Leyendo numero:\",\"$\"\n" +
"            BUF dw 10\n";
    
    String data2de5="";
    
    String blue3de5=".CODE\n" +
".386\n" +
"BEGIN:\n" +
"			MOV     AX, _DATA\n" +
"			MOV     DS, AX\n" +
"CALL  COMPI\n" +
"			MOV AX, 4C00H\n" +
"			INT 21H\n" +
"COMPI  PROC\n";
    
    String compiProc4de5="";
    
    String blue5de5="		ret\n" +
"COMPI  ENDP\n" +
"END BEGIN";
    
    String resultado="";
    
    ensamblador(variable variable, cuadruplo cuadruplo){
        this.cuadruplo = cuadruplo;
        this.variable = variable;
    }
    
    public void variables(){
        //			cadena db
        variable variableAuxiliar = variable;
        while(variableAuxiliar!=null&&variableAuxiliar.inicializada){
            data2de5+="			"+variableAuxiliar.lexema+" dw 0\n";
            variableAuxiliar=variableAuxiliar.sig;
        }
        
        cuadruplo cuadruploAuxiliar = cuadruplo.crearCopiaProfunda(cuadruplo);
        while(cuadruploAuxiliar.inicio!=null){

            if (cuadruploAuxiliar.inicio.resultado.startsWith("temp")) {
                data2de5+="			"+cuadruploAuxiliar.inicio.resultado+" dw 0\n";
            }
            cuadruploAuxiliar.inicio=cuadruploAuxiliar.inicio.siguiente;
        }

    }
    
    public void macros(){

        cuadruplo cuadruploAuxiliar = cuadruplo.crearCopiaProfunda(cuadruplo);
                
        while(cuadruploAuxiliar.inicio!=null){
            String macro="";
            
            if (!cuadruploAuxiliar.inicio.punteros.equals("[null]")) {
                String apuntador = cuadruploAuxiliar.inicio.punteros.substring(1, 2);
                macro+="SALTO"+apuntador+": \n";
            }
            
            switch(cuadruploAuxiliar.inicio.operador){
                case "-":

                    macro+="	RESTA   "+cuadruploAuxiliar.inicio.operando1+", "+cuadruploAuxiliar.inicio.operando2+", "+cuadruploAuxiliar.inicio.resultado+"\n";
                break;
            case "+":
                    macro+="	SUMAR   "+cuadruploAuxiliar.inicio.operando1+", "+cuadruploAuxiliar.inicio.operando2+", "+cuadruploAuxiliar.inicio.resultado+"\n";                    
                break;
            case "/":
                    macro+="	DIVIDE  "+cuadruploAuxiliar.inicio.operando1+", "+cuadruploAuxiliar.inicio.operando2+", "+cuadruploAuxiliar.inicio.resultado+"\n";

                break;

            case "*":
                    macro+="	MULTI   "+cuadruploAuxiliar.inicio.operando1+", "+cuadruploAuxiliar.inicio.operando2+", "+cuadruploAuxiliar.inicio.resultado+"\n";

                break;

            case "<":
                    macro+="	I_MENOR "+cuadruploAuxiliar.inicio.operando1+", "+cuadruploAuxiliar.inicio.operando2+", "+cuadruploAuxiliar.inicio.resultado+"\n";

                break;
            case ">":
                    macro+="	I_MAYOR "+cuadruploAuxiliar.inicio.operando1+", "+cuadruploAuxiliar.inicio.operando2+", "+cuadruploAuxiliar.inicio.resultado+"\n";

                break;
            case "<=":
                    macro+="	I_MENORIGUAL "+cuadruploAuxiliar.inicio.operando1+", "+cuadruploAuxiliar.inicio.operando2+", "+cuadruploAuxiliar.inicio.resultado+"\n";

                break;
            case ">=":
                    macro+="	I_MAYORIGUAL "+cuadruploAuxiliar.inicio.operando1+", "+cuadruploAuxiliar.inicio.operando2+", "+cuadruploAuxiliar.inicio.resultado+"\n";

                break;
            case "==":
                    macro+="	I_IGUAL "+cuadruploAuxiliar.inicio.operando1+", "+cuadruploAuxiliar.inicio.operando2+", "+cuadruploAuxiliar.inicio.resultado+"\n";

                break;

            case "<>":
                    macro+="	I_DIFERENTES    "+cuadruploAuxiliar.inicio.operando1+", "+cuadruploAuxiliar.inicio.operando2+", "+cuadruploAuxiliar.inicio.resultado+"\n";


                break;
                
            case "cout<<":
                
                    macro+="	ITOA   LISTAPAR"+", "+cuadruploAuxiliar.inicio.operando1+"\n"+"	WRITE   LISTAPAR\n    WRITELN\n";

                break;

                
            case "cin>>":
                
                macro+="    WRITE cin\n"+"    leernumero    "+cuadruploAuxiliar.inicio.resultado+"\n";
                break;

            case "BRF":
                    macro+="	JF  "+cuadruploAuxiliar.inicio.operando1+", SALTO"+cuadruploAuxiliar.inicio.resultado+"\n";

                break;

            case "BRV":
                    macro+="	JMAY    "+cuadruploAuxiliar.inicio.operando1+", SALTO"+cuadruploAuxiliar.inicio.resultado+"\n";

                break;

            case "BRI":
                    macro+="	JUMP   SALTO"+cuadruploAuxiliar.inicio.resultado+"\n";

                break;

            case "=":
                    macro+="	I_ASIGNAR   "+cuadruploAuxiliar.inicio.resultado+", "+cuadruploAuxiliar.inicio.operando1+"\n";
               
                break;
            default:
                break;
            }
            compiProc4de5+=macro;
            cuadruploAuxiliar.inicio=cuadruploAuxiliar.inicio.siguiente;
        }
    }
    
    public String crearASM(){
        variables();
        macros();
        resultado = blue1de5+data2de5+blue3de5+compiProc4de5+blue5de5;
        return resultado;
    }
}

















