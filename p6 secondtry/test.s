	.text
	_add:
	sw    $ra, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 16
	subu  $sp, $sp, 0
	lw    $t0, 0($fp)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	lw    $t0, -4($fp)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	move  $t0, $a0
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	add   $a0, $a0, $t0
	sw    $a0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	#POP
	addu  $sp, $sp, 4
	j     _add_Exit
_add_Exit:
	lw    $ra, -8($fp)
	move  $t0, $fp
	lw    $fp, -12($fp)
	move  $sp, $t0
	jr    $ra
	.text
	.globl main
	main:
	__start:
	sw    $ra, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 8
	subu  $sp, $sp, 8
	li    $t0, 1
	sw    $t0, ($sp)
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	sw    $a0, -8($fp)
	li    $t0, 0
	sw    $t0, ($sp)
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	sw    $a0, -12($fp)
	lw    $t0, -8($fp)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	move  $t0, $a0
	beqz  $t0, .L0
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	j     .L1
.L0:
	lw    $t0, -12($fp)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	or    $a0, $a0, $t0
	sw    $a0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
.L1:
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	beqz  $a0, .L2
	.data
.L4:	.asciiz "worked"
	.text
	la    $t0, .L4
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall
	j     .L3
.L2:
.L3:
	li    $t0, 0
	sw    $t0, ($sp)
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	sw    $a0, -8($fp)
	lw    $t0, -8($fp)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	move  $t0, $a0
	beqz  $t0, .L5
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	j     .L6
.L5:
	lw    $t0, -12($fp)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	or    $a0, $a0, $t0
	sw    $a0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
.L6:
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	beqz  $a0, .L7
	j     .L8
.L7:
	.data
.L9:	.asciiz "worked"
	.text
	la    $t0, .L9
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall
.L8:
_main_Exit:
	lw    $ra, -0($fp)
	move  $t0, $fp
	lw    $fp, -4($fp)
	li    $v0, 10
	syscall
