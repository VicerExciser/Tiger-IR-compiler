.text
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
    addi $sp, $sp, -56
    # End of prologue
    li $t0, 1
    li $t1, 2
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
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    # Pushing function call args onto stack
    addi $sp, $sp, -4
    sw $t0, 0($sp)
    addi $sp, $sp, -4
    sw $t1, 0($sp)
    # Calling subroutine 'add_routine'
    jal add_routine
    addi $sp, $sp, 8
    lw $ra, 0($sp)
    addi $sp, $sp, 4
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
    move $t2, $v0
    # print_int
    li $v0, 1
    move $a0, $t2
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
    move $a0, $t0
    syscall
    # print_int
    li $v0, 1
    move $a0, $t1
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
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
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    # Pushing function call args onto stack
    addi $sp, $sp, -4
    sw $t0, 0($sp)
    addi $sp, $sp, -4
    sw $t1, 0($sp)
    # Calling subroutine 'mult_routine'
    jal mult_routine
    addi $sp, $sp, 8
    lw $ra, 0($sp)
    addi $sp, $sp, 4
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
    move $t2, $v0
    # print_int
    li $v0, 1
    move $a0, $t2
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
    move $a0, $t0
    syscall
    # print_int
    li $v0, 1
    move $a0, $t1
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
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
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    # Pushing function call args onto stack
    addi $sp, $sp, -4
    sw $t1, 0($sp)
    addi $sp, $sp, -4
    sw $t0, 0($sp)
    # Calling subroutine 'div_routine'
    jal div_routine
    addi $sp, $sp, 8
    lw $ra, 0($sp)
    addi $sp, $sp, 4
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
    move $t2, $v0
    # print_int
    li $v0, 1
    move $a0, $t2
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
    move $a0, $t0
    syscall
    # print_int
    li $v0, 1
    move $a0, $t1
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
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
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    # Pushing function call args onto stack
    addi $sp, $sp, -4
    sw $t0, 0($sp)
    addi $sp, $sp, -4
    sw $t1, 0($sp)
    # Calling subroutine 'add_routine'
    jal add_routine
    addi $sp, $sp, 8
    lw $ra, 0($sp)
    addi $sp, $sp, 4
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
    move $t2, $v0
    # print_int
    li $v0, 1
    move $a0, $t2
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
    move $a0, $t0
    syscall
    # print_int
    li $v0, 1
    move $a0, $t1
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    li $t3, 4
    div $t2, $t2, $t3
    # print_int
    li $v0, 1
    move $a0, $t2
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # Start of epilogue
    addi $sp, $sp, 56
    # End of epilogue
    # Program exit
    li $v0, 10
    syscall


add_routine: 
    move $fp, $sp
    # Start of prologue
    addi $sp, $sp, -56
    # Fetch arguments from stack & collapse
    lw $t1, 56($sp)
    lw $t2, 60($sp)
    # End of prologue
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
    move $a0, $t1
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    add $t0, $t2, $t1
    # Store return value in $v0 and return
    move $v0, $t0
    # Start of epilogue
    addi $sp, $sp, 56
    # End of epilogue
    # Return from subroutine add_routine
    jr $ra


mult_routine: 
    move $fp, $sp
    # Start of prologue
    addi $sp, $sp, -56
    # Fetch arguments from stack & collapse
    lw $t0, 56($sp)
    lw $t1, 60($sp)
    # End of prologue
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
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    mul $t2, $t1, $t0
    # Store return value in $v0 and return
    move $v0, $t2
    # Start of epilogue
    addi $sp, $sp, 56
    # End of epilogue
    # Return from subroutine mult_routine
    jr $ra


div_routine: 
    move $fp, $sp
    # Start of prologue
    addi $sp, $sp, -56
    # Fetch arguments from stack & collapse
    lw $t1, 56($sp)
    lw $t0, 60($sp)
    # End of prologue
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
    mul $t2, $t0, $t1
    # Store return value in $v0 and return
    move $v0, $t2
    # Start of epilogue
    addi $sp, $sp, 56
    # End of epilogue
    # Return from subroutine div_routine
    jr $ra



