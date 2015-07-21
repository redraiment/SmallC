max = 2;
println("Outside:"); {
	for (i = 0; i < max; i++) {
		println("1", i);
	}; on locking i.
	for (i = 0; i < max; i++) {
		println("2", i);
	}; on locking i.
};
println("inner:"); {
	for (i = 0; i < max; i++) {
		println("1", i);
	} on locking i;.
	for (i = 0; i < max; i++) {
		println("2", i);
	} on locking i;.
}.
