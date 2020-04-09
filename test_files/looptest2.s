.text
main:
	move $fp, $sp
	addi $t0, zero, 1
	li $t1, 5
loop:
	# print integer in $t0
	### Tiger-IR: 	call, puti, t0
	li $v0, 1	# print int
	# li $v0, 11	# print char
	move $a0, $t0
	syscall

	### Tiger-IR: 	call, putc, 10
	li $v0, 11	# print space
	li $a0, 10
	syscall
	
	beq $t0, $t1, done	# break when $t0 == 5
	bge $t0, $t1, done	# break when $t0 >= 5
	
	addi $t0, $t0, 1
	j loop
	#blt $t0, $t1, loop
	#beq $t0, $t1, loop
done:
	li $v0, 10	# exit
	syscall
