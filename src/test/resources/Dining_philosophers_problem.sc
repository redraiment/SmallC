/*
 * Dining philosophers problem
 *
 * Result:
 * The 0 philosopher was having dinner.
 * The 2 philosopher was having dinner.
 * The 4 philosopher was having dinner.
 * The 1 philosopher was having dinner.
 * The 3 philosopher was having dinner.
 */
for (local i = 0; i < 5; i++) {
	println("The " + i +  " philosopher was having dinner.\n").
} on locking fork[i], fork[(i+1)%5]..
