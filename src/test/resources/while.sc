{ i = j = 0. };
{
	while (i < 10) {
		println("i", i);
		i++.
	} on locking x;.
	
	while (j < 10) {
		println("j", j);
		j++.
	} on locking x;.
};

{ i = j = 0. };
{
	while (i < 10) {
		println("i", i);
		i++.
	}; on locking x.
	
	while (j < 10) {
		println("j", j);
		j++.
	}; on locking x.
}.