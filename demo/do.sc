{ i = j = 0. };
{
	do {
		println("i", i);
		i++.
	} on locking x; while (i < 10).
	
	do {
		println("j", j);
		j++.
	} on locking x; while (j < 10).
};

{ i = j = 0. };
{
	do {
		println("i", i);
		i++.
	}; while (i < 10) on locking x.
	
	do {
		println("j", j);
		j++.
	}; while (j < 10) on locking x.
}.