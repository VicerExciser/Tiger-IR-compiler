.text
    #  ---- { BLOCK  'main_B0'  BEGIN } ---- 
main: 
    move $fp, $sp
    # Start of prologue
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
    # read_int
    li $v0, 5
    syscall
    move $t2, $v0
    li $t3, 100
    bgt $t2, $t3, return_main
    #  ---- { BLOCK  'main_B0'  END } ---- 
    #  ---- { BLOCK  'main_B1'  BEGIN } ---- 
    li $t3, 1
    sub $t2, $t2, $t3
    li $t1, 0
    #  ---- { BLOCK  'main_B1'  END } ---- 
    #  ---- { BLOCK  'main_B2'  BEGIN } ---- 
loop0_main: 
    bgt $t1, $t2, exit0_main
    #  ---- { BLOCK  'main_B2'  END } ---- 
    #  ---- { BLOCK  'main_B3'  BEGIN } ---- 
    # read_int
    li $v0, 5
    syscall
    move $t0, $v0
    la $t4, -400($fp)
    sll $t5, $t1, 2
    add $t4, $t4, $t5
    sw $t0, ($t4)
    addi $t1, $t1, 1
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
    move $s6, $fp
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    # Pushing function call args onto stack
    addi $sp, $sp, -4
    sw $t6, 0($sp)
    addi $sp, $sp, -4
    sw zero, 0($sp)
    addi $sp, $sp, -4
    sw $t2, 0($sp)
    # Calling subroutine 'quicksort'
    jal quicksort
    addi $sp, $sp, 12
    lw $ra, 0($sp)
    addi $sp, $sp, 4
    move $fp, $s6
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
    li $t1, 0
    #  ---- { BLOCK  'main_B4'  END } ---- 
    #  ---- { BLOCK  'main_B5'  BEGIN } ---- 
loop1_main: 
    bgt $t1, $t2, exit1_main
    #  ---- { BLOCK  'main_B5'  END } ---- 
    #  ---- { BLOCK  'main_B6'  BEGIN } ---- 
    la $t4, -400($fp)
    sll $t5, $t1, 2
    add $t4, $t4, $t5
    lw $t0, ($t4)
    # print_int
    li $v0, 1
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    addi $t1, $t1, 1
    j loop1_main
    #  ---- { BLOCK  'main_B6'  END } ---- 
    #  ---- { BLOCK  'main_B7'  BEGIN } ---- 
exit1_main: 
return_main: 
    # Start of epilogue
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
    # Fetch arguments from stack & collapse
    lw $t9, 440($sp)
    lw $t8, 444($sp)
    lw $a0, 448($sp)
    # End of prologue
    li $t1, 0
    li $t0, 0
    bge $t8, $t9, end_quicksort
    #  ---- { BLOCK  'quicksort_B0'  END } ---- 
    #  ---- { BLOCK  'quicksort_B1'  BEGIN } ---- 
    add $t3, $t8, $t9
    li $t10quicksort, 0
    li $t10quicksort, 2
    div $t3, $t3, $t10quicksort
    li $t11quicksort, 0
    li $t12quicksort, 0
    la $t11quicksort, -400($fp)
    sll $t12quicksort, $t3, 2
    add $t11quicksort, $t11quicksort, $t12quicksort
    lw $t2, ($t11quicksort)
    li $t10quicksort, 1
    sub $t1, $t8, $t10quicksort
    addi $t0, $t9, 1
    #  ---- { BLOCK  'quicksort_B1'  END } ---- 
    #  ---- { BLOCK  'quicksort_B2'  BEGIN } ---- 
loop0_quicksort: 
loop1_quicksort: 
    addi $t1, $t1, 1
    li $t13quicksort, 0
    la $t11quicksort, -400($fp)
    sll $t13quicksort, $t1, 2
    add $t11quicksort, $t11quicksort, $t13quicksort
    lw $t4, ($t11quicksort)
    move $t7, $t4
    blt $t7, $t2, loop1_quicksort
    #  ---- { BLOCK  'quicksort_B2'  END } ---- 
    #  ---- { BLOCK  'quicksort_B3'  BEGIN } ---- 
loop2_quicksort: 
    li $t10quicksort, 1
    sub $t0, $t0, $t10quicksort
    li $t14quicksort, 0
    la $t11quicksort, -400($fp)
    sll $t14quicksort, $t0, 2
    add $t11quicksort, $t11quicksort, $t14quicksort
    lw $t4, ($t11quicksort)
    move $t6, $t4
    bgt $t6, $t2, loop2_quicksort
    #  ---- { BLOCK  'quicksort_B3'  END } ---- 
    #  ---- { BLOCK  'quicksort_B4'  BEGIN } ---- 
    bge $t1, $t0, exit0_quicksort
    #  ---- { BLOCK  'quicksort_B4'  END } ---- 
    #  ---- { BLOCK  'quicksort_B5'  BEGIN } ---- 
    la $t11quicksort, -400($fp)
    sll $t14quicksort, $t0, 2
    add $t11quicksort, $t11quicksort, $t14quicksort
    sw $t7, ($t11quicksort)
    la $t11quicksort, -400($fp)
    sll $t13quicksort, $t1, 2
    add $t11quicksort, $t11quicksort, $t13quicksort
    sw $t6, ($t11quicksort)
    j loop0_quicksort
    #  ---- { BLOCK  'quicksort_B5'  END } ---- 
    #  ---- { BLOCK  'quicksort_B6'  BEGIN } ---- 
exit0_quicksort: 
    addi $t5, $t0, 1
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
    move $s6, $fp
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    # Pushing function call args onto stack
    addi $sp, $sp, -4
    sw $a0, 0($sp)
    addi $sp, $sp, -4
    sw $t8, 0($sp)
    addi $sp, $sp, -4
    sw $t0, 0($sp)
    # Calling subroutine 'quicksort'
    jal quicksort
    addi $sp, $sp, 12
    lw $ra, 0($sp)
    addi $sp, $sp, 4
    move $fp, $s6
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
    addi $t0, $t0, 1
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
    move $s6, $fp
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    # Pushing function call args onto stack
    addi $sp, $sp, -4
    sw $a0, 0($sp)
    addi $sp, $sp, -4
    sw $t0, 0($sp)
    addi $sp, $sp, -4
    sw $t9, 0($sp)
    # Calling subroutine 'quicksort'
    jal quicksort
    addi $sp, $sp, 12
    lw $ra, 0($sp)
    addi $sp, $sp, 4
    move $fp, $s6
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
    # Start of epilogue
    addi $sp, $sp, 440
    # Return from subroutine 'quicksort'
    jr $ra
    # End of epilogue
    #  ---- { BLOCK  'quicksort_B7'  END } ---- 



