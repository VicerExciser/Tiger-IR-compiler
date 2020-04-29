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
    addi $sp, $sp, -16
    # End of prologue
    li $t0, 1
    sw $t0, 12($sp)
    li $t0, 2
    sw $t0, 8($sp)
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
    lw $t0, 60($sp)
    sw $t0, 0($sp)
    addi $sp, $sp, -4
    lw $t1, 60($sp)
    sw $t1, 0($sp)
    # Calling subroutine 'add_routine'
    jal add_routine
    addi $sp, $sp, 8
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
    # Load return value in $v0 into the destination register
    move $t0, $v0
    sw $t0, 4($sp)
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
    # print_int
    li $v0, 1
    lw $t9, 12($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    # print_int
    li $v0, 1
    lw $t9, 8($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # Saving temporary regs
    addi $sp, $sp, -40
    sw $t9, 36($sp)
    sw $t8, 32($sp)
    sw $t7, 28($sp)
    sw $t6, 24($sp)
    sw $t5, 20($sp)
    sw $t4, 16($sp)
    sw $t3, 12($sp)
    sw $t2, 8($sp)
    sw $t1, 4($sp)
    sw $t0, 0($sp)
    move $s7, $fp
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    # Pushing function call args onto stack
    addi $sp, $sp, -4
    lw $t9, 60($sp)
    sw $t9, 0($sp)
    addi $sp, $sp, -4
    lw $t8, 60($sp)
    sw $t8, 0($sp)
    # Calling subroutine 'mult_routine'
    jal mult_routine
    addi $sp, $sp, 8
    lw $ra, 0($sp)
    addi $sp, $sp, 4
    move $fp, $s7
    # Restoring temporary regs
    lw $t0, 0($sp)
    lw $t1, 4($sp)
    lw $t2, 8($sp)
    lw $t3, 12($sp)
    lw $t4, 16($sp)
    lw $t5, 20($sp)
    lw $t6, 24($sp)
    lw $t7, 28($sp)
    lw $t8, 32($sp)
    lw $t9, 36($sp)
    addi $sp, $sp, 40
    # Load return value in $v0 into the destination register
    move $t9, $v0
    sw $t9, 4($sp)
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
    # print_int
    li $v0, 1
    lw $t0, 12($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    # print_int
    li $v0, 1
    lw $t0, 8($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
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
    lw $t0, 56($sp)
    sw $t0, 0($sp)
    addi $sp, $sp, -4
    lw $t1, 64($sp)
    sw $t1, 0($sp)
    # Calling subroutine 'div_routine'
    jal div_routine
    addi $sp, $sp, 8
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
    # Load return value in $v0 into the destination register
    move $t0, $v0
    sw $t0, 4($sp)
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
    # print_int
    li $v0, 1
    lw $t9, 12($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    # print_int
    li $v0, 1
    lw $t9, 8($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # Saving temporary regs
    addi $sp, $sp, -40
    sw $t9, 36($sp)
    sw $t8, 32($sp)
    sw $t7, 28($sp)
    sw $t6, 24($sp)
    sw $t5, 20($sp)
    sw $t4, 16($sp)
    sw $t3, 12($sp)
    sw $t2, 8($sp)
    sw $t1, 4($sp)
    sw $t0, 0($sp)
    move $s7, $fp
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    # Pushing function call args onto stack
    addi $sp, $sp, -4
    lw $t9, 60($sp)
    sw $t9, 0($sp)
    addi $sp, $sp, -4
    lw $t8, 60($sp)
    sw $t8, 0($sp)
    # Calling subroutine 'add_routine'
    jal add_routine
    addi $sp, $sp, 8
    lw $ra, 0($sp)
    addi $sp, $sp, 4
    move $fp, $s7
    # Restoring temporary regs
    lw $t0, 0($sp)
    lw $t1, 4($sp)
    lw $t2, 8($sp)
    lw $t3, 12($sp)
    lw $t4, 16($sp)
    lw $t5, 20($sp)
    lw $t6, 24($sp)
    lw $t7, 28($sp)
    lw $t8, 32($sp)
    lw $t9, 36($sp)
    addi $sp, $sp, 40
    # Load return value in $v0 into the destination register
    move $t9, $v0
    sw $t9, 4($sp)
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
    # print_int
    li $v0, 1
    lw $t0, 12($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    # print_int
    li $v0, 1
    lw $t0, 8($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t1, 4($sp)
    li $t2, 1
    div $t0, $t1, $t2
    sw $t0, 4($sp)
    # print_int
    li $v0, 1
    lw $t0, 4($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 16
    # End of epilogue
    #  ---- { BLOCK  'main_B0'  END } ---- 
    # Program exit
    li $v0, 10
    syscall


    #  ---- { BLOCK  'add_routine_B0'  BEGIN } ---- 
add_routine: 
    # Start of prologue
    addi $sp, $sp, -16
    # Fetch arguments off of the stack
    lw $t0, 16($sp)
    lw $t1, 20($sp)
    sw $t0, 8($sp)
    sw $t1, 4($sp)
    # End of prologue
    # print_int
    li $v0, 1
    lw $t0, 4($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    # print_int
    li $v0, 1
    lw $t0, 8($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    lw $t1, 4($sp)
    lw $t2, 8($sp)
    add $t0, $t1, $t2
    sw $t0, 12($sp)
    # Store return value in $v0 and return
    lw $t0, 12($sp)
    move $v0, $t0
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 16
    # Return from subroutine 'add_routine'
    jr $ra
    # End of epilogue
    #  ---- { BLOCK  'add_routine_B0'  END } ---- 


    #  ---- { BLOCK  'mult_routine_B0'  BEGIN } ---- 
mult_routine: 
    # Start of prologue
    addi $sp, $sp, -16
    # Fetch arguments off of the stack
    lw $t0, 16($sp)
    lw $t1, 20($sp)
    sw $t0, 12($sp)
    sw $t1, -8($sp)
    # End of prologue
    # print_int
    li $v0, 1
    lw $t0, -8($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    # print_int
    li $v0, 1
    lw $t0, 12($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    lw $t1, -8($sp)
    lw $t2, 12($sp)
    mul $t0, $t1, $t2
    sw $t0, 4($sp)
    # Store return value in $v0 and return
    lw $t0, 4($sp)
    move $v0, $t0
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 16
    # Return from subroutine 'mult_routine'
    jr $ra
    # End of epilogue
    #  ---- { BLOCK  'mult_routine_B0'  END } ---- 


    #  ---- { BLOCK  'div_routine_B0'  BEGIN } ---- 
div_routine: 
    # Start of prologue
    addi $sp, $sp, -16
    # Fetch arguments off of the stack
    lw $t0, 16($sp)
    lw $t1, 20($sp)
    sw $t1, 12($sp)
    sw $t0, 8($sp)
    # End of prologue
    # print_int
    li $v0, 1
    lw $t0, 12($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    # print_int
    li $v0, 1
    lw $t0, 8($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
    syscall
    lw $t1, 12($sp)
    lw $t2, 8($sp)
    mul $t0, $t1, $t2
    sw $t0, 4($sp)
    # Store return value in $v0 and return
    lw $t0, 4($sp)
    move $v0, $t0
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 16
    # Return from subroutine 'div_routine'
    jr $ra
    # End of epilogue
    #  ---- { BLOCK  'div_routine_B0'  END } ---- 



