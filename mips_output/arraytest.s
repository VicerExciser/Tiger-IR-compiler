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
    addi $sp, $sp, -144
    # End of prologue
    li $t0, 3
    sw $t0, 8($sp)
    li $t0, 32
    sw $t0, 4($sp)
    la $t0, -128($fp)
    li $t1, 32
    li $t2, 10
AArrAssignLoopmain: 
    addi $t3, $t0, 0
    sw $t2, ($t3)
    addi $t0, $t0, 4
    addi $t1, $t1, -1
    bgt $t1, $zero, AArrAssignLoopmain
    #  ---- { BLOCK  'main_B0'  END } ---- 
    #  ---- { BLOCK  'main_B1'  BEGIN } ---- 
printarr1_main: 
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    #  ---- { BLOCK  'main_B1'  END } ---- 
    #  ---- { BLOCK  'main_B2'  BEGIN } ---- 
arrstoretest_main: 
    la $t0, -128($fp)
    lw $t1, 8($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, 4($sp)
    sw $t3, ($t0)
    #  ---- { BLOCK  'main_B2'  END } ---- 
    #  ---- { BLOCK  'main_B3'  BEGIN } ---- 
printarr2_main: 
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    #  ---- { BLOCK  'main_B3'  END } ---- 
    #  ---- { BLOCK  'main_B4'  BEGIN } ---- 
arrloadtest_main: 
    la $t0, -128($fp)
    lw $t1, 8($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, ($t0)
    sw $t3, 12($sp)
    #  ---- { BLOCK  'main_B4'  END } ---- 
    #  ---- { BLOCK  'main_B5'  BEGIN } ---- 
printarr3_main: 
    # print_int
    li $v0, 1
    lw $t0, 12($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    #  ---- { BLOCK  'main_B5'  END } ---- 
    #  ---- { BLOCK  'main_B6'  BEGIN } ---- 
finish_main: 
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 144
    # End of epilogue
    #  ---- { BLOCK  'main_B6'  END } ---- 
    # Program exit
    li $v0, 10
    syscall



