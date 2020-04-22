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
    addi $sp, $sp, -136
    # End of prologue
    li $t0, 7
    li $t1, 32
    la $t2, -128($fp)
    li $t3, 32
    li $t4, 10
AArrAssignLoop_main: 
    addi $t5, $t2, 0
    sw $t4, ($t5)
    addi $t2, $t2, 4
    addi $t3, $t3, -1
    bgt $t3, zero, AArrAssignLoop_main
printarr1_main: 
    lw $t9, -4($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -8($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -12($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -16($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -20($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -24($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -28($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -32($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -36($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -40($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -44($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -48($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -52($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -56($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -60($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -64($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -68($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -72($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -76($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -80($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -84($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -88($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -92($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -96($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -100($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -104($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -108($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -112($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -116($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -120($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -124($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -128($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    li $v0, 11
    li $a0, 10
    syscall
arrstoretest_main: 
    la $t2, -128($fp)
    sll $t0, $t0, 2
    add $t2, $t2, $t0
    sw $t1, ($t2)
printarr2_main: 
    lw $t9, -4($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -8($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -12($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -16($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -20($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -24($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -28($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -32($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -36($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -40($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -44($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -48($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -52($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -56($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -60($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -64($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -68($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -72($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -76($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -80($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -84($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -88($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -92($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -96($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -100($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -104($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -108($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -112($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -116($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -120($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -124($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $t9, -128($fp)
    li $v0, 1
    move $a0, $t9
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
finish_main: 
    # Start of epilogue
    addi $sp, $sp, 136
    # End of epilogue
    # Program exit
    li $v0, 10
    syscall



