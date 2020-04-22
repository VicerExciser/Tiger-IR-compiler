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
    addi $sp, $sp, -144
    # End of prologue
    li $t1, 7
    li $t2, 32
    la $t3, -128($fp)
    li $t4, 32
    li $t5, 10
AArrAssignLoop_main: 
    addi $t6, $t3, 0
    sw $t5, ($t6)
    addi $t3, $t3, 4
    addi $t4, $t4, -1
    bgt $t4, zero, AArrAssignLoop_main
printarr1_main: 
    lw $s7, -4($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -8($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -12($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -16($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -20($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -24($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -28($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -32($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -36($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -40($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -44($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -48($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -52($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -56($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -60($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -64($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -68($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -72($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -76($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -80($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -84($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -88($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -92($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -96($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -100($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -104($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -108($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -112($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -116($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -120($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -124($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -128($fp)
    li $v0, 1
    move $a0, $s7
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
arrstoretest_main: 
    la $t3, -128($fp)
    sll $t7, $t1, 2
    add $t3, $t3, $t7
    sw $t2, ($t3)
printarr2_main: 
    lw $s7, -4($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -8($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -12($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -16($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -20($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -24($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -28($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -32($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -36($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -40($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -44($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -48($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -52($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -56($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -60($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -64($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -68($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -72($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -76($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -80($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -84($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -88($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -92($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -96($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -100($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -104($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -108($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -112($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -116($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -120($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -124($fp)
    li $v0, 1
    move $a0, $s7
    syscall
    li $v0, 11
    li $a0, 10
    syscall
    lw $s7, -128($fp)
    li $v0, 1
    move $a0, $s7
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
arrloadtest_main: 
    la $t3, -128($fp)
    sll $t7, $t1, 2
    add $t3, $t3, $t7
    lw $t0, ($t3)
printarr3_main: 
    # print_int
    li $v0, 1
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
finish_main: 
    # Start of epilogue
    addi $sp, $sp, 144
    # End of epilogue
    # Program exit
    li $v0, 10
    syscall



