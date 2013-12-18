" Vim syntax file
" Language:	Small C
" Maintainer:	redraiment <redraiment@gmai.com>
" Last Change:	2010 Mar 19

" Quit when a (custom) syntax file was already loaded

"syn keyword	scConditional	if elsif else
"syn keyword	scReport	for do while
"syn keyword	scKeyword	function return on locking
"syn cluster	scStatement	contains=scConditional,scReport,scKeyword
syn keyword	scStatement	if elsif else for do while loop foreach function return on locking my local our

syn keyword	scTodo		TODO contained
syn region	scComment	start="/\*" end="\*/" contains=scTodo

syn region	scString	start=+L\="+ skip=+\\\\\|\\"+ end=+"+
syn match	scNumber	/\<\d\+\>/
syn match	scVariable	/\<[a-zA-Z]\w*\>/

hi link scStatement Statement
hi link scComment Comment
hi link scString String
hi link scNumber Number
hi link scVariable Identifier
