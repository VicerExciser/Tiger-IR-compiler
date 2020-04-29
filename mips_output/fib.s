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
    # Calling subroutine 'fib'
    jal fib
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


    #  ---- { BLOCK  'fib_B0'  BEGIN } ---- 
fib: 
    # Start of prologue
    addi $sp, $sp, -32
    # Fetch arguments off of the stack
    lw $t9, 32($sp)
    sw $t9, 4($sp)
    # End of prologue
    li $t9, 0
    sw $t9, 12($sp)
    li $t9, 1
    sw $t9, 24($sp)
    lw $t9, 24($sp)
    move $t8, $t9
    sw $t8, 28($sp)
    lw $t9, 4($sp)
    li $t8, 1
    bgt $t9, $t8, if_label0_fib
    #  ---- { BLOCK  'fib_B0'  END } ---- 
    #  ---- { BLOCK  'fib_B1'  BEGIN } ---- 
    li $t9, 0
    sw $t9, 20($sp)
    lw $t9, 4($sp)
    move $t8, $t9
    sw $t8, 24($sp)
    lw $t9, 24($sp)
    move $t8, $t9
    sw $t8, 28($sp)
    j end_fib
    #  ---- { BLOCK  'fib_B1'  END } ---- 
    #  ---- { BLOCK  'fib_B2'  BEGIN } ---- 
if_label0_fib: 
    lw $t8, 4($sp)
    li $t7, 1
    sub $t9, $t8, $t7
    sw $t9, 4($sp)
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
    lw $t9, 52($sp)
    sw $t9, 0($sp)
    # Calling subroutine 'fib'
    jal fib
    addi $sp, $sp, 4
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
    sw $t9, 8($sp)
    lw $t1, 4($sp)
    li $t2, 1
    sub $t0, $t1, $t2
    sw $t0, -4($sp)
    lw $t1, 4($sp)
    li $t2, 1
    sub $t0, $t1, $t2
    sw $t0, 4($sp)
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
    lw $t0, 44($sp)
    sw $t0, 0($sp)
    # Calling subroutine 'fib'
    jal fib
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
    sw $t0, 0($sp)
    lw $t9, 12($sp)
    move $t8, $t9
    sw $t8, 20($sp)
    lw $t9, 12($sp)
    move $t8, $t9
    sw $t8, 24($sp)
    lw $t8, 8($sp)
    lw $t7, 0($sp)
    add $t9, $t8, $t7
    sw $t9, 24($sp)
    lw $t9, 24($sp)
    move $t8, $t9
    sw $t8, 28($sp)
    #  ---- { BLOCK  'fib_B2'  END } ---- 
    #  ---- { BLOCK  'fib_B3'  BEGIN } ---- 
end_fib: 
    # Store return value in $v0 and return
    lw $t9, 28($sp)
    move $v0, $t9
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 32
    # Return from subroutine 'fib'
    jr $ra
    # End of epilogue
    #  ---- { BLOCK  'fib_B3'  END } ---- 



