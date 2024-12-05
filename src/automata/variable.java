
package automata;

public class variable {
    String lexema;
    int token;
    int tipoDeDato;
    boolean inicializada; 
    variable sig = null;
    
    variable(String lexema,int token,int tipoDeDato){
        this.lexema = lexema;
        this.token = token;
        this.tipoDeDato = tipoDeDato;
        this.inicializada = false;
    }
}
