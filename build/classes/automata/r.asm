INCLUDE macros.mac
DOSSEG
.MODEL SMALL
.STACK 100h

.DATA
            BUFFER      DB 8 DUP('$')  ;23h
            BUFFERTEMP  DB 8 DUP('$')  ;23h
            BLANCO  DB '#'
            BLANCOS DB '$'
            MENOS   DB '-$'
            COUNT   DW 0
            NEGATIVO    DB 0
            ARREGLO DW 0
            ARREGLO1    DW 0
            ARREGLO2    DW 0
            LISTAPAR    LABEL BYTE
            LONGMAX DB 254
            TOTCAR  DB ?
            INTRODUCIDOS    DB 254 DUP ('$')
            MULT10  DW 1
            t1 dw ?
            t2 dw ?
            t3 dw ?
            cin db "Leyendo numero:","$"
            BUF dw 10
			x dw 0
			y dw 0
			z dw 0
			temp1 dw 0
			temp2 dw 0
			temp3 dw 0
			temp4 dw 0
			temp5 dw 0
.CODE
.386
BEGIN:
			MOV     AX, _DATA
			MOV     DS, AX
CALL  COMPI
			MOV AX, 4C00H
			INT 21H
COMPI  PROC
	I_ASIGNAR   x, 10
	I_ASIGNAR   y, 0
	I_MAYOR x, 10, temp1
	JF  temp1, SALTO1
	I_ASIGNAR   x, 20
	JUMP   SALTO2
SALTO1: 
	ITOA   LISTAPAR, x
	WRITE   LISTAPAR
    WRITELN
SALTO2: 
	I_MENOR y, 10, temp2
	JF  temp2, SALTO3
	SUMAR   y, 5, temp3
	I_ASIGNAR   y, temp3
	JUMP   SALTO2
SALTO3: 
    WRITE cin
    leernumero    z
	ITOA   LISTAPAR, x
	WRITE   LISTAPAR
    WRITELN
	ITOA   LISTAPAR, y
	WRITE   LISTAPAR
    WRITELN
	SUMAR   z, x, temp4
	SUMAR   temp4, y, temp5
	I_ASIGNAR   z, temp5
	ITOA   LISTAPAR, z
	WRITE   LISTAPAR
    WRITELN
		ret
COMPI  ENDP
END BEGIN