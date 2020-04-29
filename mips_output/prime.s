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
    li $t0, 0
    sw $t0, 36($sp)
    li $t0, 2
    sw $t0, 48($sp)
    li $t0, 3
    sw $t0, 44($sp)
    li $t0, 6
    sw $t0, 40($sp)
    li $t0, 0
    sw $t0, 4($sp)
    # read_int
    li $v0, 5
    syscall
    move $t0, $v0
    sw $t0, 32($sp)
    lw $t0, 32($sp)
    li $t1, 1
    bgt $t0, $t1, label0_main
    #  ---- { BLOCK  'main_B0'  END } ---- 
    #  ---- { BLOCK  'main_B1'  BEGIN } ---- 
    li $t0, 0
    sw $t0, 28($sp)
    lw $t0, 28($sp)
    move $t1, $t0
    sw $t1, 52($sp)
    j print_main
    #  ---- { BLOCK  'main_B1'  END } ---- 
    #  ---- { BLOCK  'main_B2'  BEGIN } ---- 
label0_main: 
    lw $t0, 32($sp)
    li $t1, 3
    bgt $t0, $t1, label1_main
    #  ---- { BLOCK  'main_B2'  END } ---- 
    #  ---- { BLOCK  'main_B3'  BEGIN } ---- 
    li $t0, 1
    sw $t0, 28($sp)
    lw $t0, 28($sp)
    move $t1, $t0
    sw $t1, 52($sp)
    j print_main
    #  ---- { BLOCK  'main_B3'  END } ---- 
    #  ---- { BLOCK  'main_B4'  BEGIN } ---- 
label1_main: 
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
    lw $t1, 100($sp)
    sw $t1, 0($sp)
    # Calling subroutine 'divisible'
    jal divisible
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
    sw $t0, 20($sp)
    lw $t9, 4($sp)
    move $t8, $t9
    sw $t8, 28($sp)
    lw $t9, 28($sp)
    move $t8, $t9
    sw $t8, 52($sp)
    lw $t9, 20($sp)
    li $t8, 1
    beq $t9, $t8, label2_main
    #  ---- { BLOCK  'main_B4'  END } ---- 
    #  ---- { BLOCK  'main_B5'  BEGIN } ---- 
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
    lw $t9, 80($sp)
    sw $t9, 0($sp)
    addi $sp, $sp, -4
    lw $t8, 96($sp)
    sw $t8, 0($sp)
    # Calling subroutine 'divisible'
    jal divisible
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
    sw $t9, 20($sp)
    lw $t0, 4($sp)
    move $t1, $t0
    sw $t1, 28($sp)
    lw $t0, 28($sp)
    move $t1, $t0
    sw $t1, 52($sp)
    lw $t0, 20($sp)
    li $t1, 1
    beq $t0, $t1, label2_main
    #  ---- { BLOCK  'main_B5'  END } ---- 
    #  ---- { BLOCK  'main_B6'  BEGIN } ---- 
    j label3_main
    #  ---- { BLOCK  'main_B6'  END } ---- 
    #  ---- { BLOCK  'main_B7'  BEGIN } ---- 
label2_main: 
    j print_main
    #  ---- { BLOCK  'main_B7'  END } ---- 
    #  ---- { BLOCK  'main_B8'  BEGIN } ---- 
label3_main: 
    li $t0, 5
    sw $t0, 36($sp)
    #  ---- { BLOCK  'main_B8'  END } ---- 
    #  ---- { BLOCK  'main_B9'  BEGIN } ---- 
loop_main: 
    lw $t1, 36($sp)
    lw $t2, 36($sp)
    mul $t0, $t1, $t2
    sw $t0, 24($sp)
    lw $t0, 24($sp)
    lw $t1, 32($sp)
    bgt $t0, $t1, exit_main
    #  ---- { BLOCK  'main_B9'  END } ---- 
    #  ---- { BLOCK  'main_B10'  BEGIN } ---- 
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
    lw $t1, 88($sp)
    sw $t1, 0($sp)
    # Calling subroutine 'divisible'
    jal divisible
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
    sw $t0, 20($sp)
    lw $t9, 4($sp)
    move $t8, $t9
    sw $t8, 28($sp)
    li $t9, 0
    sw $t9, 12($sp)
    li $t9, 0
    sw $t9, 0($sp)
    lw $t9, 28($sp)
    move $t8, $t9
    sw $t8, 52($sp)
    lw $t9, 20($sp)
    li $t8, 1
    beq $t9, $t8, label2_main
    #  ---- { BLOCK  'main_B10'  END } ---- 
    #  ---- { BLOCK  'main_B11'  BEGIN } ---- 
    lw $t8, 36($sp)
    addi $t9, $t8, 2
    sw $t9, 16($sp)
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
    lw $t9, 80($sp)
    sw $t9, 0($sp)
    addi $sp, $sp, -4
    lw $t8, 68($sp)
    sw $t8, 0($sp)
    # Calling subroutine 'divisible'
    jal divisible
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
    sw $t9, 20($sp)
    lw $t0, 4($sp)
    move $t1, $t0
    sw $t1, 28($sp)
    lw $t0, 28($sp)
    move $t1, $t0
    sw $t1, 52($sp)
    lw $t0, 20($sp)
    li $t1, 1
    beq $t0, $t1, label2_main
    #  ---- { BLOCK  'main_B11'  END } ---- 
    #  ---- { BLOCK  'main_B12'  BEGIN } ---- 
    lw $t1, 36($sp)
    addi $t0, $t1, 6
    sw $t0, 36($sp)
    j loop_main
    #  ---- { BLOCK  'main_B12'  END } ---- 
    #  ---- { BLOCK  'main_B13'  BEGIN } ---- 
exit_main: 
    lw $t0, 12($sp)
    move $t1, $t0
    sw $t1, 8($sp)
    lw $t0, 0($sp)
    move $t1, $t0
    sw $t1, 28($sp)
    li $t0, 1
    sw $t0, 28($sp)
    lw $t0, 28($sp)
    move $t1, $t0
    sw $t1, 52($sp)
    #  ---- { BLOCK  'main_B13'  END } ---- 
    #  ---- { BLOCK  'main_B14'  BEGIN } ---- 
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
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 56
    # End of epilogue
    #  ---- { BLOCK  'main_B14'  END } ---- 
    # Program exit
    li $v0, 10
    syscall


    #  ---- { BLOCK  'divisible_B0'  BEGIN } ---- 
divisible: 
    # Start of prologue
    addi $sp, $sp, -16
    # Fetch arguments off of the stack
    lw $t0, 16($sp)
    lw $t1, 20($sp)
    sw $t1, 8($sp)
    sw $t0, 4($sp)
    # End of prologue
    lw $t1, 8($sp)
    lw $t2, 4($sp)
    div $t0, $t1, $t2
    sw $t0, 12($sp)
    lw $t1, 12($sp)
    lw $t2, 4($sp)
    mul $t0, $t1, $t2
    sw $t0, 12($sp)
    lw $t0, 8($sp)
    lw $t1, 12($sp)
    bne $t0, $t1, label0_divisible
    #  ---- { BLOCK  'divisible_B0'  END } ---- 
    #  ---- { BLOCK  'divisible_B1'  BEGIN } ---- 
    # Store return value in $v0 and return
    addi $sp, $sp, -4
    lw $t0, 0($sp)
    move $v0, $t0
    #  ---- { BLOCK  'divisible_B1'  END } ---- 
    #  ---- { BLOCK  'divisible_B2'  BEGIN } ---- 
label0_divisible: 
    # Store return value in $v0 and return
    addi $sp, $sp, -4
    lw $t0, 0($sp)
    move $v0, $t0
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 16
    # Return from subroutine 'divisible'
    jr $ra
    # End of epilogue
    #  ---- { BLOCK  'divisible_B2'  END } ---- 



