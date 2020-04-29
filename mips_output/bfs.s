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
    addi $sp, $sp, -5256
    # End of prologue
    la $t0, -5240($fp)
    li $t1, 100
    li $t2, 0
vArrAssignLoopmain: 
    addi $t3, $t0, 0
    sw $t2, ($t3)
    addi $t0, $t0, 4
    addi $t1, $t1, -1
    bgt $t1, $zero, vArrAssignLoopmain
    li $t0, 10
    sw $t0, 828($sp)
    li $t0, 0
    sw $t0, 820($sp)
    # read_int
    li $v0, 5
    syscall
    move $t0, $v0
    sw $t0, 824($sp)
    li $t0, 0
    sw $t0, 840($sp)
    #  ---- { BLOCK  'main_B0'  END } ---- 
    #  ---- { BLOCK  'main_B1'  BEGIN } ---- 
L0_main: 
    lw $t0, 840($sp)
    lw $t1, 824($sp)
    bge $t0, $t1, EOI_main
    #  ---- { BLOCK  'main_B1'  END } ---- 
    #  ---- { BLOCK  'main_B2'  BEGIN } ---- 
    # read_int
    li $v0, 5
    syscall
    move $t0, $v0
    sw $t0, 5244($sp)
    la $t0, -4412($fp)
    lw $t1, 840($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, 5244($sp)
    sw $t3, ($t0)
    li $t0, 0
    sw $t0, 836($sp)
    #  ---- { BLOCK  'main_B2'  END } ---- 
    #  ---- { BLOCK  'main_B3'  BEGIN } ---- 
L1_main: 
    lw $t0, 836($sp)
    lw $t1, 5244($sp)
    bge $t0, $t1, L2_main
    #  ---- { BLOCK  'main_B3'  END } ---- 
    #  ---- { BLOCK  'main_B4'  BEGIN } ---- 
    # read_int
    li $v0, 5
    syscall
    move $t0, $v0
    sw $t0, 8($sp)
    lw $t1, 828($sp)
    lw $t2, 840($sp)
    mul $t0, $t1, $t2
    sw $t0, 416($sp)
    lw $t1, 416($sp)
    lw $t2, 836($sp)
    add $t0, $t1, $t2
    sw $t0, 416($sp)
    la $t0, -4012($fp)
    lw $t1, 416($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, 8($sp)
    sw $t3, ($t0)
    lw $t1, 836($sp)
    addi $t0, $t1, 1
    sw $t0, 836($sp)
    j L1_main
    #  ---- { BLOCK  'main_B4'  END } ---- 
    #  ---- { BLOCK  'main_B5'  BEGIN } ---- 
L2_main: 
    lw $t1, 840($sp)
    addi $t0, $t1, 1
    sw $t0, 840($sp)
    j L0_main
    #  ---- { BLOCK  'main_B5'  END } ---- 
    #  ---- { BLOCK  'main_B6'  BEGIN } ---- 
EOI_main: 
    la $t0, -5240($fp)
    li $t1, 0
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    li $t3, 1
    sw $t3, ($t0)
    la $t0, -4836($fp)
    li $t1, 0
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    li $t3, 0
    sw $t3, ($t0)
    la $t0, -5240($fp)
    li $t1, 0
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, ($t0)
    sw $t3, 4($sp)
    li $t0, 0
    sw $t0, 5252($sp)
    lw $t0, 5252($sp)
    move $t1, $t0
    sw $t1, 840($sp)
    li $t0, 1
    sw $t0, 836($sp)
    #  ---- { BLOCK  'main_B6'  END } ---- 
    #  ---- { BLOCK  'main_B7'  BEGIN } ---- 
L3_main: 
    lw $t0, 840($sp)
    lw $t1, 836($sp)
    beq $t0, $t1, FIN_main
    #  ---- { BLOCK  'main_B7'  END } ---- 
    #  ---- { BLOCK  'main_B8'  BEGIN } ---- 
    la $t0, -4836($fp)
    lw $t1, 840($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, ($t0)
    sw $t3, 12($sp)
    lw $t1, 840($sp)
    addi $t0, $t1, 1
    sw $t0, 5252($sp)
    lw $t0, 5252($sp)
    move $t1, $t0
    sw $t1, 840($sp)
    la $t0, -4412($fp)
    lw $t1, 12($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, ($t0)
    sw $t3, 5244($sp)
    lw $t0, 840($sp)
    move $t1, $t0
    sw $t1, 832($sp)
    li $t0, 0
    sw $t0, 832($sp)
    #  ---- { BLOCK  'main_B8'  END } ---- 
    #  ---- { BLOCK  'main_B9'  BEGIN } ---- 
L4_main: 
    lw $t0, 832($sp)
    lw $t1, 5244($sp)
    bge $t0, $t1, L5_main
    #  ---- { BLOCK  'main_B9'  END } ---- 
    #  ---- { BLOCK  'main_B10'  BEGIN } ---- 
    lw $t0, 832($sp)
    move $t1, $t0
    sw $t1, 416($sp)
    lw $t1, 828($sp)
    lw $t2, 12($sp)
    mul $t0, $t1, $t2
    sw $t0, 416($sp)
    lw $t1, 416($sp)
    lw $t2, 832($sp)
    add $t0, $t1, $t2
    sw $t0, 416($sp)
    la $t0, -4012($fp)
    lw $t1, 416($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, ($t0)
    sw $t3, 8($sp)
    la $t0, -5240($fp)
    lw $t1, 8($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, ($t0)
    sw $t3, 5248($sp)
    lw $t0, 5248($sp)
    li $t1, 1
    beq $t0, $t1, L6_main
    #  ---- { BLOCK  'main_B10'  END } ---- 
    #  ---- { BLOCK  'main_B11'  BEGIN } ---- 
    la $t0, -5240($fp)
    lw $t1, 8($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    li $t3, 1
    sw $t3, ($t0)
    la $t0, -4836($fp)
    lw $t1, 836($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, 8($sp)
    sw $t3, ($t0)
    la $t0, -5240($fp)
    lw $t1, 8($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, ($t0)
    sw $t3, 4($sp)
    lw $t1, 836($sp)
    addi $t0, $t1, 1
    sw $t0, 836($sp)
    #  ---- { BLOCK  'main_B11'  END } ---- 
    #  ---- { BLOCK  'main_B12'  BEGIN } ---- 
L6_main: 
    lw $t1, 832($sp)
    addi $t0, $t1, 1
    sw $t0, 832($sp)
    j L4_main
    #  ---- { BLOCK  'main_B12'  END } ---- 
    #  ---- { BLOCK  'main_B13'  BEGIN } ---- 
L5_main: 
    j L3_main
    #  ---- { BLOCK  'main_B13'  END } ---- 
    #  ---- { BLOCK  'main_B14'  BEGIN } ---- 
FIN_main: 
    li $t0, 0
    sw $t0, 5252($sp)
    lw $t0, 5252($sp)
    move $t1, $t0
    sw $t1, 840($sp)
    #  ---- { BLOCK  'main_B14'  END } ---- 
    #  ---- { BLOCK  'main_B15'  BEGIN } ---- 
L7_main: 
    lw $t0, 840($sp)
    lw $t1, 836($sp)
    beq $t0, $t1, L8_main
    #  ---- { BLOCK  'main_B15'  END } ---- 
    #  ---- { BLOCK  'main_B16'  BEGIN } ---- 
    la $t0, -4836($fp)
    lw $t1, 840($sp)
    sll $t2, $t1, 2
    add $t0, $t0, $t2
    lw $t3, ($t0)
    sw $t3, 8($sp)
    # print_int
    li $v0, 1
    lw $t0, 8($sp)
    move $a0, $t0
    syscall
    # print_char
    li $v0, 11
    li $a0, 10
    syscall
    lw $t1, 840($sp)
    addi $t0, $t1, 1
    sw $t0, 5252($sp)
    lw $t0, 5252($sp)
    move $t1, $t0
    sw $t1, 840($sp)
    j L7_main
    #  ---- { BLOCK  'main_B16'  END } ---- 
    #  ---- { BLOCK  'main_B17'  BEGIN } ---- 
L8_main: 
    # Start of epilogue -- Collapse the stack
    addi $sp, $sp, 5256
    # End of epilogue
    #  ---- { BLOCK  'main_B17'  END } ---- 
    # Program exit
    li $v0, 10
    syscall



