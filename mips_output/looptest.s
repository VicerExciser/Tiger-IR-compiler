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
    addi $sp, $sp, -8
    # End of prologue
    li $t0, 1
    sw $t0, 4($sp)
    li $t0, 5
    sw $t0, 0($sp)
    #  ---- { BLOCK  'main_B0'  END } ---- 
    #  ---- { BLOCK  'main_B1'  BEGIN } ---- 
loop_main: 
    # print_int
    li $v0, 1
    lw $t0, 4($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t0, 4($sp)
    lw $t1, 0($sp)
    bge $t0, $t1, done_main
    #  ---- { BLOCK  'main_B1'  END } ---- 
    #  ---- { BLOCK  'main_B2'  BEGIN } ---- 
    lw $t1, 4($sp)
    addi $t0, $t1, 1
    sw $t0, 4($sp)
    j loop_main
    #  ---- { BLOCK  'main_B2'  END } ---- 
    #  ---- { BLOCK  'main_B3'  BEGIN } ---- 
done_main: 
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 8
    # End of epilogue
    #  ---- { BLOCK  'main_B3'  END } ---- 
    # Program exit
    li $v0, 10
    syscall



