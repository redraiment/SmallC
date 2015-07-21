function r {
	println(i);
	i++;
	if (i < 10) {
		r().
	}.
}

i = 0;
r().