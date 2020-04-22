# .data
# newline:
# .asciiz ".\n\0"

.text
main: 
    move $fp, $sp
    addi $t0, zero, 11
    addi $t0, $t0, 2
    # li $v0, 1
    # move $a0, $t0
    # syscall
    add $t1, $t0, $t0
    # li $v0, 1
    # move $a0, $t1
    # syscall
M1_main: 
    mul $t2, $t0, $t1
FIN_main: 
    li $v0, 1
    move $a0, $t2
    syscall
    # li $v0, 4
    # ls $a0, newline
    # syscall
    li $v0, 10
    syscall



