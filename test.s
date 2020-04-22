.text
main: 
    move $fp, $sp
    li $t10main, 0
    li $t11main, 0
    li $t12main, 0
    li $t10main, 11
    addi $t10main, $t10main, 2
    add $t11main, $t10main, $t10main
    j M1_main
    sub $t11main, $t11main, $t10main
M1_main: 
    mul $t12main, $t10main, $t11main
    # print_int
    li $v0, 1
    move $a0, $t10main
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t11main
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t12main
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
init_main: 
    move $t0, $t12main
    addi $t0, $t0, 1
    addi $t1, $t0, 1
    addi $t2, $t1, 1
    addi $t3, $t2, 1
    addi $t4, $t3, 1
    addi $t5, $t4, 1
    addi $t6, $t5, 1
    addi $t7, $t6, 1
    addi $t8, $t7, 1
    addi $t9, $t8, 1
print_main: 
    # print_int
    li $v0, 1
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t1
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t2
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t3
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t5
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t6
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t7
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t8
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
FIN_main: 
    # Saving temporary regs
    addi $sp, $sp, -4
    sw $t0, 0($sp)
    addi $sp, $sp, -4
    sw $t1, 0($sp)
    addi $sp, $sp, -4
    sw $t2, 0($sp)
    addi $sp, $sp, -4
    sw $t3, 0($sp)
    addi $sp, $sp, -4
    sw $t4, 0($sp)
    addi $sp, $sp, -4
    sw $t5, 0($sp)
    addi $sp, $sp, -4
    sw $t6, 0($sp)
    addi $sp, $sp, -4
    sw $t7, 0($sp)
    addi $sp, $sp, -4
    sw $t8, 0($sp)
    addi $sp, $sp, -4
    sw $t9, 0($sp)
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    addi $sp, $sp, -4
    sw $t5, 0($sp)
    addi $sp, $sp, -4
    sw $t6, 0($sp)
    addi $sp, $sp, -4
    sw $t7, 0($sp)
    addi $sp, $sp, -4
    sw $t8, 0($sp)
    addi $sp, $sp, -4
    sw $t9, 0($sp)
    jal routine
    lw $ra, 0($sp)
    addi $sp, $sp, 4
    # Restoring temporary regs
    lw $t9, 0($sp)
    addi $sp, $sp, 4
    lw $t8, 0($sp)
    addi $sp, $sp, 4
    lw $t7, 0($sp)
    addi $sp, $sp, 4
    lw $t6, 0($sp)
    addi $sp, $sp, 4
    lw $t5, 0($sp)
    addi $sp, $sp, 4
    lw $t4, 0($sp)
    addi $sp, $sp, 4
    lw $t3, 0($sp)
    addi $sp, $sp, 4
    lw $t2, 0($sp)
    addi $sp, $sp, 4
    lw $t1, 0($sp)
    addi $sp, $sp, 4
    lw $t0, 0($sp)
    addi $sp, $sp, 4
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t12main
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # Program exit
    li $v0, 10
    syscall


routine: 
    move $fp, $sp
    # Fetch arguments from stack
    lw $t9, 0($sp)
    addi $sp, $sp, 4
    lw $t8, 0($sp)
    addi $sp, $sp, 4
    lw $t7, 0($sp)
    addi $sp, $sp, 4
    lw $t6, 0($sp)
    addi $sp, $sp, 4
    lw $t5, 0($sp)
    addi $sp, $sp, 4
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t5
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t6
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t7
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t8
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # Return from subroutine routine
    jr $ra



