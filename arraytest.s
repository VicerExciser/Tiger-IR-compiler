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
    addi $sp, $sp, -128
    # End of prologue
    la $t0, -128($fp)
    li $t1, 32
    li $t2, 10
AArrAssignLoop_main: 
    addi $t3, $t0, 0
    sw $t2, ($t3)
    addi $t0, $t0, 4
    addi $t1, $t1, -1
    bgt $t1, zero, AArrAssignLoop_main
finish_main: 
    lw $t4, -4($fp)
    # print_int
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -8($fp)
    lw $t4, -12($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -16($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -20($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -24($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -28($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -32($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -36($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -40($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -44($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -48($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -52($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -56($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -60($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -64($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -68($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -72($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -76($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -80($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -84($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -88($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -92($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -96($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -100($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -104($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -108($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -112($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -116($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -120($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -124($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t4, -128($fp)
    li $v0, 1
    move $a0, $t4
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    # Start of epilogue
    addi $sp, $sp, 128
    # End of epilogue
    # Program exit
    li $v0, 10
    syscall



