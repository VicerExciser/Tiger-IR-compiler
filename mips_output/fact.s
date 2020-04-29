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
    # Calling subroutine 'fact'
    jal fact
    addi $sp, $sp, 4
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
    lw $t9, 4($sp)
    move $t8, $t9
    sw $t8, 12($sp)
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
    addi $sp, $sp, 16
    # End of epilogue
    #  ---- { BLOCK  'main_B0'  END } ---- 
    # Program exit
    li $v0, 10
    syscall


    #  ---- { BLOCK  'fact_B0'  BEGIN } ---- 
fact: 
    # Start of prologue
    addi $sp, $sp, -16
    # Fetch arguments off of the stack
    lw $t9, 16($sp)
    sw $t9, 4($sp)
    # End of prologue
    li $t9, 1
    sw $t9, 12($sp)
    lw $t9, 4($sp)
    move $t8, $t9
    sw $t8, 8($sp)
    #  ---- { BLOCK  'fact_B0'  END } ---- 
    #  ---- { BLOCK  'fact_B1'  BEGIN } ---- 
loop_fact: 
    lw $t9, 8($sp)
    li $t8, 1
    blt $t9, $t8, done_fact
    lw $t7, 8($sp)
    li $t6, 1
    beq $t7, $t6, done_fact
    #  ---- { BLOCK  'fact_B1'  END } ---- 
    #  ---- { BLOCK  'fact_B2'  BEGIN } ---- 
    lw $t8, 12($sp)
    lw $t7, 8($sp)
    mul $t9, $t8, $t7
    sw $t9, 12($sp)
    # print_int
    li $v0, 1
    lw $t9, 8($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 32
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
    lw $t8, 8($sp)
    li $t7, 1
    sub $t9, $t8, $t7
    sw $t9, 8($sp)
    j loop_fact
    #  ---- { BLOCK  'fact_B2'  END } ---- 
    #  ---- { BLOCK  'fact_B3'  BEGIN } ---- 
done_fact: 
    # Store return value in $v0 and return
    lw $t9, 12($sp)
    move $v0, $t9
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 16
    # Return from subroutine 'fact'
    jr $ra
    # End of epilogue
    #  ---- { BLOCK  'fact_B3'  END } ---- 



