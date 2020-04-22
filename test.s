.text
main: 
    move $fp, $sp
    # Start of prologue
    li $t10main, 0
    li $t11main, 0
    li $t12main, 0
    addi $sp, $sp, -96
    # End of prologue
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
    sw $t0, -4($fp)
    sw $t1, -8($fp)
    sw $t2, -12($fp)
    sw $t3, -16($fp)
    sw $t4, -20($fp)
    sw $t5, -24($fp)
    sw $t6, -28($fp)
    sw $t7, -32($fp)
    sw $t8, -36($fp)
    sw $t9, -40($fp)
    # Save return address $ra in stack
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    # Pushing function call args onto stack
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
    # Calling subroutine 'routine'
    jal routine
    addi $sp, $sp, 20
    lw $ra, 0($sp)
    addi $sp, $sp, 4
    # Restoring temporary regs
    lw $t9, -40($fp)
    lw $t8, -36($fp)
    lw $t7, -32($fp)
    lw $t6, -28($fp)
    lw $t5, -24($fp)
    lw $t4, -20($fp)
    lw $t3, -16($fp)
    lw $t2, -12($fp)
    lw $t1, -8($fp)
    lw $t0, -4($fp)
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
    # read_int
    li $v0, 5
    syscall
    move $t12main, $v0
    # print_int
    li $v0, 1
    move $a0, $t12main
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # Start of epilogue
    addi $sp, $sp, 96
    # End of epilogue
    # Program exit
    li $v0, 10
    syscall


routine: 
    move $fp, $sp
    # Start of prologue
    addi $sp, $sp, -64
    # Fetch arguments from stack & collapse
    lw $t7, 64($sp)
    lw $t9, 68($sp)
    lw $t8, 72($sp)
    lw $t6, 76($sp)
    lw $t5, 80($sp)
    # End of prologue
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
    # print_int
    li $v0, 1
    move $a0, $t7
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # Start of epilogue
    addi $sp, $sp, 64
    # End of epilogue
    # Return from subroutine routine
    jr $ra



