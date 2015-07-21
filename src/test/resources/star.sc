a = 18;
b = 12;
while (a % b) {
	t = a % b;
	a = b;
	b = t;
};;
println(b);
