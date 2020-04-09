.text
main: 
    move $fp, $sp
    li $t0, 1
    li $t1, 5
loop_main: 
    addi $sp, $sp, -8
    sw $ra, ($sp)
    li $v0, 1
    move $a0, $t0
    syscall
    addi $sp, $sp, -8
    sw $ra, ($sp)
    li $v0, 11
    li $a0, 10
    syscall
    bge $t0, $t1, done_main
    addi $t0, $t0, 1
    j loop_main
done_main: 
    li $v0, 10
    syscall



