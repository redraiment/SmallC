/*
 * 有时是 True，有时是 False
 * 看谁先获得锁
 */
if (i) {
	println("True");
} else {
	println("False");
} on locking i.
i = 1 on locking i.
