.text
main: 
    move $fp, $sp
    addi $t0, zero, 11
    addi $t0, $t0, 2
    add $t1, $t0, $t0
M1_main: 
    mul $t2, $t0, $t1
FIN_main: 
    li $v0, 1
    move $a0, $t2
    syscall
    li $v0, 10
    syscall



