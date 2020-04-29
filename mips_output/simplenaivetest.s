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
    li $t0, 1
    sw $t0, 12($sp)
    lw $t0, 12($sp)
    move $t1, $t0
    sw $t1, 8($sp)
    lw $t1, 12($sp)
    lw $t2, 8($sp)
    add $t0, $t1, $t2
    sw $t0, 4($sp)
    # print_int
    li $v0, 1
    lw $t0, 4($sp)
    move $a0, $t0
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



