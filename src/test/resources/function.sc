function outer {
	println("outer").
	function inner {
		println("inner").
	}
}

outer();
inner().