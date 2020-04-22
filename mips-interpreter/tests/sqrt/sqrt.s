.text
main:
 move    $fp, $sp
 li      $v0, 7            # get double input
 syscall
 li.d    $f2, 0.0
 li.d    $f4, 1.0
 c.gt.d  $f0, $f4
 bc1f    main_body
 mov.d   $f4, $f0 
main_body:
 addi    $sp, $sp, -32
 s.d     $f2, 0($sp)
 s.d     $f4, 8($sp)
 s.d     $f0, 16($sp)
 jal     binsearch
main_done:
 addi    $sp, $sp, 32
 l.d     $f12, -8($sp)
 li      $v0, 3
 syscall                   # print answer
 li      $v0, 11           # print space
 li      $a0, 10
 syscall                 
 li      $v0, 10           # exit
 syscall

binsearch:
 move    $fp, $sp          # load, lo, hi bounds
 l.d     $f0, 0($sp)
 l.d     $f2, 8($sp) 
 add.d   $f4, $f0, $f2     # calculate mid*mid
 li.d    $f6, 2.0
 div.d   $f4, $f4, $f6
 mul.d   $f6, $f4, $f4
 l.d     $f8, 16($sp)      # load target
 sub.d   $f8, $f8, $f6
 addi    $sp, $sp, -8      # store ra, fp
 sw      $ra, 0($sp)
 sw      $fp, 4($sp)
 addi    $sp, $sp, -40     # store regs
 s.d     $f0, 0($sp)
 s.d     $f2, 8($sp)
 s.d     $f4, 16($sp)
 s.d     $f6, 24($sp)
 s.d     $f8, 32($sp)
 addi    $sp, $sp, -16     # push arg
 s.d     $f8, 0($sp)
 jal     abs               # call abs
 addi    $sp, $sp, 16
 l.d     $f10, -8($sp)     # load ret value  
 l.d     $f0, 0($sp)
 l.d     $f2, 8($sp)
 l.d     $f4, 16($sp)
 l.d     $f6, 24($sp)
 l.d     $f8, 32($sp)
 addi    $sp, $sp, 40   
 lw      $ra,  0($sp)
 lw      $fp, 4($sp)
 addi    $sp, $sp, 8
 li.d    $f12, 0.001
 c.lt.d  $f10, $f12
 bc1t    done
 li.d    $f6, 0.0
 c.gt.d  $f8, $f6
 bc1t    right
 bc1f    left
right:
 addi    $sp, $sp, -8
 sw      $ra, 0($sp)
 sw      $fp, 4($sp)
 addi    $sp, $sp, -32     # push arg
 s.d     $f4, 0($sp)
 s.d     $f2, 8($sp)
 l.d     $f8, 16($fp)
 s.d     $f8, 16($sp)   
 jal     binsearch
 addi    $sp, $sp, 32
 l.d     $f4, -8($sp)       # load ret val
 lw      $ra, 0($sp)
 lw      $fp, 4($sp)
 addi    $sp, $sp, 8
 j       done
left:
 addi    $sp, $sp, -8
 sw      $ra, 0($sp)
 sw      $fp, 4($sp)
 addi    $sp, $sp, -32     # push arg
 s.d     $f0, 0($sp)
 s.d     $f4, 8($sp)
 l.d     $f8, 16($fp)
 s.d     $f8, 16($sp)     
 j       binsearch
 addi    $sp, $sp, 32
 l.d     $f4, -8($sp)       # load ret val
 lw      $ra, 0($sp)
 lw      $fa, 4($sp)
 addi    $sp, $sp, 8
done:
 s.d     $f4, 24($sp)
 jr      $ra

abs:
 l.d     $f0, 0($sp)
 li.d    $f2, 0.0
 c.lt.d  $f0, $f2
 bc1f    abs_done
abs_neg:
 li.d    $f2, -1.0
 mul.d   $f0, $f0, $f2
abs_done:
 s.d     $f0, 8($sp)
 jr      $ra
