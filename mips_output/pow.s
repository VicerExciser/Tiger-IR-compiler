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
    # read_int
    li $v0, 5
    syscall
    move $t0, $v0
    sw $t0, 12($sp)
    # read_int
    li $v0, 5
    syscall
    move $t0, $v0
    sw $t0, 8($sp)
    lw $t0, 8($sp)
    li $t1, 0
    blt $t0, $t1, END_main
    #  ---- { BLOCK  'main_B0'  END } ---- 
    #  ---- { BLOCK  'main_B1'  BEGIN } ---- 
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
    # Calling subroutine 'pow'
    jal pow
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
    #  ---- { BLOCK  'main_B1'  END } ---- 
    #  ---- { BLOCK  'main_B2'  BEGIN } ---- 
END_main: 
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 16
    # End of epilogue
    #  ---- { BLOCK  'main_B2'  END } ---- 
    # Program exit
    li $v0, 10
    syscall


    #  ---- { BLOCK  'pow_B0'  BEGIN } ---- 
pow: 
    # Start of prologue
    addi $sp, $sp, -32
    # Fetch arguments off of the stack
    lw $t9, 32($sp)
    lw $t8, 36($sp)
    sw $t8, 16($sp)
    sw $t9, 4($sp)
    # End of prologue
    li $t9, 0
    sw $t9, 0($sp)
    li $t9, 1
    sw $t9, 24($sp)
    lw $t9, 4($sp)
    li $t8, 0
    bne $t9, $t8, LABEL0_pow
    #  ---- { BLOCK  'pow_B0'  END } ---- 
    #  ---- { BLOCK  'pow_B1'  BEGIN } ---- 
    li $t9, 0
    sw $t9, 8($sp)
    lw $t9, 0($sp)
    move $t8, $t9
    sw $t8, 28($sp)
    lw $t9, 24($sp)
    move $t8, $t9
    sw $t8, 28($sp)
    j RET_pow
    #  ---- { BLOCK  'pow_B1'  END } ---- 
    #  ---- { BLOCK  'pow_B2'  BEGIN } ---- 
LABEL0_pow: 
    li $t9, 0
    sw $t9, 12($sp)
    lw $t9, 12($sp)
    move $t8, $t9
    sw $t8, 8($sp)
    lw $t8, 4($sp)
    li $t7, 2
    div $t9, $t8, $t7
    sw $t9, 20($sp)
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
    lw $t9, 64($sp)
    sw $t9, 0($sp)
    addi $sp, $sp, -4
    lw $t8, 72($sp)
    sw $t8, 0($sp)
    # Calling subroutine 'pow'
    jal pow
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
    sw $t9, 24($sp)
    lw $t1, 24($sp)
    lw $t2, 24($sp)
    mul $t0, $t1, $t2
    sw $t0, 24($sp)
    lw $t0, 24($sp)
    move $t1, $t0
    sw $t1, 28($sp)
    lw $t1, 20($sp)
    li $t2, 2
    mul $t0, $t1, $t2
    sw $t0, 20($sp)
    lw $t0, 20($sp)
    lw $t1, 4($sp)
    beq $t0, $t1, RET_pow
    #  ---- { BLOCK  'pow_B2'  END } ---- 
    #  ---- { BLOCK  'pow_B3'  BEGIN } ---- 
    lw $t1, 24($sp)
    lw $t2, 16($sp)
    mul $t0, $t1, $t2
    sw $t0, 24($sp)
    lw $t0, 0($sp)
    move $t1, $t0
    sw $t1, 28($sp)
    lw $t0, 24($sp)
    move $t1, $t0
    sw $t1, 28($sp)
    #  ---- { BLOCK  'pow_B3'  END } ---- 
    #  ---- { BLOCK  'pow_B4'  BEGIN } ---- 
RET_pow: 
    # Store return value in $v0 and return
    lw $t0, 28($sp)
    move $v0, $t0
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 32
    # Return from subroutine 'pow'
    jr $ra
    # End of epilogue
    #  ---- { BLOCK  'pow_B4'  END } ---- 



