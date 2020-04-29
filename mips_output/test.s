.text
    #  ---- { BLOCK  'main_B0'  BEGIN } ---- 
main: 
    # Start of prologue
    move $fp, $sp
    li $t0, 0
    li $t1, 0
    li $t2, 0
    li $t3, 0
    li $t4, 0
    li $t5, 0
    li $t6, 0
    li $t7, 0
    li $t8, 0
    li $t9, 0
    addi $sp, $sp, -56
    # End of prologue
    li $t0, 11
    sw $t0, 12($sp)
    lw $t1, 12($sp)
    addi $t0, $t1, 2
    sw $t0, 12($sp)
    lw $t1, 12($sp)
    lw $t2, 12($sp)
    add $t0, $t1, $t2
    sw $t0, 8($sp)
    j M1_main
    #  ---- { BLOCK  'main_B0'  END } ---- 
    #  ---- { BLOCK  'main_B1'  BEGIN } ---- 
M1_main: 
    lw $t1, 12($sp)
    lw $t2, 8($sp)
    mul $t0, $t1, $t2
    sw $t0, 4($sp)
    # print_int
    li $v0, 1
    lw $t0, 12($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t0, 8($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t0, 4($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    #  ---- { BLOCK  'main_B1'  END } ---- 
    #  ---- { BLOCK  'main_B2'  BEGIN } ---- 
init_main: 
    lw $t0, 4($sp)
    move $t1, $t0
    sw $t1, 52($sp)
    lw $t1, 52($sp)
    addi $t0, $t1, 1
    sw $t0, 52($sp)
    lw $t1, 52($sp)
    addi $t0, $t1, 1
    sw $t0, 48($sp)
    lw $t1, 48($sp)
    addi $t0, $t1, 1
    sw $t0, 44($sp)
    lw $t1, 44($sp)
    addi $t0, $t1, 1
    sw $t0, 40($sp)
    lw $t1, 40($sp)
    addi $t0, $t1, 1
    sw $t0, 36($sp)
    lw $t1, 36($sp)
    addi $t0, $t1, 1
    sw $t0, 32($sp)
    lw $t1, 32($sp)
    addi $t0, $t1, 1
    sw $t0, 28($sp)
    lw $t1, 28($sp)
    addi $t0, $t1, 1
    sw $t0, 24($sp)
    lw $t1, 24($sp)
    addi $t0, $t1, 1
    sw $t0, 20($sp)
    lw $t1, 20($sp)
    addi $t0, $t1, 1
    sw $t0, 16($sp)
    #  ---- { BLOCK  'main_B2'  END } ---- 
    #  ---- { BLOCK  'main_B3'  BEGIN } ---- 
print_main: 
    # print_int
    li $v0, 1
    lw $t0, 52($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t0, 48($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t0, 44($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t0, 40($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t0, 36($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t0, 32($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t0, 28($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t0, 24($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t0, 20($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t0, 16($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    #  ---- { BLOCK  'main_B3'  END } ---- 
    #  ---- { BLOCK  'main_B4'  BEGIN } ---- 
FIN_main: 
    # Saving temporary regs
    addi $sp, $sp, -40
    sw $t0, 0($sp)
    sw $t1, 4($sp)
    sw $t2, 8($sp)
    sw $t3, 12($sp)
    sw $t4, 16($sp)
    sw $t5, 20($sp)
    sw $t6, 24($sp)
    sw $t7, 28($sp)
    sw $t8, 32($sp)
    sw $t9, 36($sp)
    move $s7, $fp
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    # Pushing function call args onto stack
    addi $sp, $sp, -4
    lw $t0, 80($sp)
    sw $t0, 0($sp)
    addi $sp, $sp, -4
    lw $t1, 80($sp)
    sw $t1, 0($sp)
    addi $sp, $sp, -4
    lw $t2, 80($sp)
    sw $t2, 0($sp)
    addi $sp, $sp, -4
    lw $t3, 80($sp)
    sw $t3, 0($sp)
    addi $sp, $sp, -4
    lw $t4, 80($sp)
    sw $t4, 0($sp)
    # Calling subroutine 'routine'
    jal routine
    addi $sp, $sp, 20
    lw $ra, 0($sp)
    addi $sp, $sp, 4
    move $fp, $s7
    # Restoring temporary regs
    lw $t9, 36($sp)
    lw $t8, 32($sp)
    lw $t7, 28($sp)
    lw $t6, 24($sp)
    lw $t5, 20($sp)
    lw $t4, 16($sp)
    lw $t3, 12($sp)
    lw $t2, 8($sp)
    lw $t1, 4($sp)
    lw $t0, 0($sp)
    addi $sp, $sp, 40
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t9, 4($sp)
    move $a0, $t9
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
    move $t9, $v0
    sw $t9, 4($sp)
    # print_int
    li $v0, 1
    lw $t9, 4($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 56
    # End of epilogue
    #  ---- { BLOCK  'main_B4'  END } ---- 
    # Program exit
    li $v0, 10
    syscall


    #  ---- { BLOCK  'routine_B0'  BEGIN } ---- 
routine: 
    # Start of prologue
    addi $sp, $sp, -24
    # Fetch arguments off of the stack
    lw $t9, 24($sp)
    lw $t8, 28($sp)
    lw $t7, 32($sp)
    lw $t6, 36($sp)
    lw $t5, 40($sp)
    sw $t8, 20($sp)
    sw $t7, 16($sp)
    sw $t9, 12($sp)
    sw $t6, 8($sp)
    sw $t5, 4($sp)
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
    lw $t9, 4($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t9, 8($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t9, 16($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t9, 20($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_int
    li $v0, 1
    lw $t9, 12($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 24
    # Return from subroutine 'routine'
    jr $ra
    # End of epilogue
    #  ---- { BLOCK  'routine_B0'  END } ---- 



