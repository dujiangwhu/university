insert into 
	PSTREENODE ( 
		SETID, 
		SETCNTRLVALUE, 
		TREE_NAME, 
		EFFDT, 
		TREE_NODE_NUM, 
		TREE_NODE, 
		TREE_BRANCH,
		TREE_NODE_NUM_END, 
		TREE_LEVEL_NUM, 
		TREE_NODE_TYPE, 
		PARENT_NODE_NUM, 
		PARENT_NODE_NAME, 
		OLD_TREE_NODE_NUM, 
		NODECOL_IMAGE, 
		NODEEXP_IMAGE
	) values (
		?,
		?,
		?,
		?, 
		?, 
		?, 
		?, 
		?, 
		?, 
		?, 
		?, 
		?, 
		?, 
		?, 
		?
	)