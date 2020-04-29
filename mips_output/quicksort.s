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
    addi $sp, $sp, -416
    # End of prologue
    li $t0, 0
    sw $t0, 12($sp)
    # read_int
    li $v0, 5
    syscall
    move $t0, $v0
    sw $t0, 4($sp)
    lw $t0, 4($sp)
    li $t1, 100
    bgt $t0, $t1, return_main
    #  ---- { BLOCK  'main_B0'  END } ---- 
    #  ---- { BLOCK  'main_B1'  BEGIN } ---- 
    lw $t1, 4($sp)
    li $t2, 1
    sub $t0, $t1, $t2
    sw $t0, 4($sp)
    li $t0, 0
    sw $t0, 8($sp)
    #  ---- { BLOCK  'main_B1'  END } ---- 
    #  ---- { BLOCK  'main_B2'  BEGIN } ---- 
loop0_main: 
    lw $t0, 8($sp)
    lw $t1, 4($sp)
    bgt $t0, $t1, exit0_main
    #  ---- { BLOCK  'main_B2'  END } ---- 
    #  ---- { BLOCK  'main_B3'  BEGIN } ---- 
    # read_int
    li $v0, 5
    syscall
    move $t0, $v0
    sw $t0, 12($sp)
    la $t0, -400($fp)
    lw $t1, 8($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, 12($sp)
    sw $t3, ($t0)
    lw $t1, 8($sp)
    addi $t0, $t1, 1
    sw $t0, 8($sp)
    j loop0_main
    #  ---- { BLOCK  'main_B3'  END } ---- 
    #  ---- { BLOCK  'main_B4'  BEGIN } ---- 
exit0_main: 
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
    addi $sp, $sp, -4
    lw $t0, 0($sp)
    sw $t0, 0($sp)
    addi $sp, $sp, -4
    sw $zero, 0($sp)
    addi $sp, $sp, -4
    lw $t1, 64($sp)
    sw $t1, 0($sp)
    # Calling subroutine 'quicksort'
    jal quicksort
    addi $sp, $sp, 12
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
    li $t9, 0
    sw $t9, 12($sp)
    #  ---- { BLOCK  'main_B4'  END } ---- 
    #  ---- { BLOCK  'main_B5'  BEGIN } ---- 
loop1_main: 
    lw $t9, 12($sp)
    lw $t8, 8($sp)
    bgt $t9, $t8, exit1_main
    #  ---- { BLOCK  'main_B5'  END } ---- 
    #  ---- { BLOCK  'main_B6'  BEGIN } ---- 
    la $t9, -400($fp)
    lw $t8, 12($sp)
    sll $t7, $t8, 2
    add $t9, $t9, $t7
    lw $t6, ($t9)
    sw $t6, 16($sp)
    # print_int
    li $v0, 1
    lw $t9, 16($sp)
    move $a0, $t9
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t8, 12($sp)
    addi $t9, $t8, 1
    sw $t9, 12($sp)
    j loop1_main
    #  ---- { BLOCK  'main_B6'  END } ---- 
    #  ---- { BLOCK  'main_B7'  BEGIN } ---- 
exit1_main: 
return_main: 
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 416
    # End of epilogue
    #  ---- { BLOCK  'main_B7'  END } ---- 
    # Program exit
    li $v0, 10
    syscall


    #  ---- { BLOCK  'quicksort_B0'  BEGIN } ---- 
quicksort: 
    # Start of prologue
    addi $sp, $sp, -440
    # Fetch arguments off of the stack
    lw $t9, 440($sp)
    lw $t8, 444($sp)
    lw $t7, 448($sp)
    sw $t9, 36($sp)
    sw $t7, -464($sp)
    sw $t8, 32($sp)
    # End of prologue
    li $t9, 0
    sw $t9, -404($sp)
    li $t9, 0
    sw $t9, 0($sp)
    lw $t9, 32($sp)
    lw $t8, 36($sp)
    bge $t9, $t8, end_quicksort
    #  ---- { BLOCK  'quicksort_B0'  END } ---- 
    #  ---- { BLOCK  'quicksort_B1'  BEGIN } ---- 
    lw $t8, 32($sp)
    lw $t7, 36($sp)
    add $t9, $t8, $t7
    sw $t9, 12($sp)
    lw $t8, 12($sp)
    li $t7, 2
    div $t9, $t8, $t7
    sw $t9, 12($sp)
    la $t9, -400($fp)
    lw $t8, 12($sp)
    sll $t7, $t8, 2
    add $t9, $t9, $t7
    lw $t6, ($t9)
    sw $t6, 8($sp)
    lw $t8, 32($sp)
    li $t7, 1
    sub $t9, $t8, $t7
    sw $t9, -404($sp)
    lw $t8, 36($sp)
    addi $t9, $t8, 1
    sw $t9, 0($sp)
    #  ---- { BLOCK  'quicksort_B1'  END } ---- 
    #  ---- { BLOCK  'quicksort_B2'  BEGIN } ---- 
loop0_quicksort: 
loop1_quicksort: 
    lw $t8, -404($sp)
    addi $t9, $t8, 1
    sw $t9, -404($sp)
    la $t9, -400($fp)
    lw $t8, -404($sp)
    sll $t7, $t8, 2
    add $t9, $t9, $t7
    lw $t6, ($t9)
    sw $t6, 16($sp)
    lw $t9, 16($sp)
    move $t8, $t9
    sw $t8, 28($sp)
    lw $t9, 28($sp)
    lw $t8, 8($sp)
    blt $t9, $t8, loop1_quicksort
    #  ---- { BLOCK  'quicksort_B2'  END } ---- 
    #  ---- { BLOCK  'quicksort_B3'  BEGIN } ---- 
loop2_quicksort: 
    lw $t8, 0($sp)
    li $t7, 1
    sub $t9, $t8, $t7
    sw $t9, 0($sp)
    la $t9, -400($fp)
    lw $t8, 0($sp)
    sll $t7, $t8, 2
    add $t9, $t9, $t7
    lw $t6, ($t9)
    sw $t6, 16($sp)
    lw $t9, 16($sp)
    move $t8, $t9
    sw $t8, 24($sp)
    lw $t9, 24($sp)
    lw $t8, 8($sp)
    bgt $t9, $t8, loop2_quicksort
    #  ---- { BLOCK  'quicksort_B3'  END } ---- 
    #  ---- { BLOCK  'quicksort_B4'  BEGIN } ---- 
    lw $t9, -404($sp)
    lw $t8, 0($sp)
    bge $t9, $t8, exit0_quicksort
    #  ---- { BLOCK  'quicksort_B4'  END } ---- 
    #  ---- { BLOCK  'quicksort_B5'  BEGIN } ---- 
    la $t9, -400($fp)
    lw $t8, 0($sp)
    sll $t7, $t8, 2
    add $t9, $t9, $t7
    lw $t6, 28($sp)
    sw $t6, ($t9)
    la $t9, -400($fp)
    lw $t8, -404($sp)
    sll $t7, $t8, 2
    add $t9, $t9, $t7
    lw $t6, 24($sp)
    sw $t6, ($t9)
    j loop0_quicksort
    #  ---- { BLOCK  'quicksort_B5'  END } ---- 
    #  ---- { BLOCK  'quicksort_B6'  BEGIN } ---- 
exit0_quicksort: 
    lw $t8, 0($sp)
    addi $t9, $t8, 1
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
    lw $t9, -416($sp)
    sw $t9, 0($sp)
    addi $sp, $sp, -4
    lw $t8, 84($sp)
    sw $t8, 0($sp)
    addi $sp, $sp, -4
    lw $t7, 56($sp)
    sw $t7, 0($sp)
    # Calling subroutine 'quicksort'
    jal quicksort
    addi $sp, $sp, 12
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
    lw $t1, 0($sp)
    addi $t0, $t1, 1
    sw $t0, 0($sp)
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
    lw $t0, -416($sp)
    sw $t0, 0($sp)
    addi $sp, $sp, -4
    lw $t1, 52($sp)
    sw $t1, 0($sp)
    addi $sp, $sp, -4
    lw $t2, 92($sp)
    sw $t2, 0($sp)
    # Calling subroutine 'quicksort'
    jal quicksort
    addi $sp, $sp, 12
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
    #  ---- { BLOCK  'quicksort_B6'  END } ---- 
    #  ---- { BLOCK  'quicksort_B7'  BEGIN } ---- 
end_quicksort: 
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 440
    # Return from subroutine 'quicksort'
    jr $ra
    # End of epilogue
    #  ---- { BLOCK  'quicksort_B7'  END } ---- 



