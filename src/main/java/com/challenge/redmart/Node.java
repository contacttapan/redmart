package com.challenge.redmart;

public class Node {
	int colPos;
	int rowPos;
	int value;
	Node south;
	Node north;
	Node east;
	Node west;
	public Node(int rowPos,int colPos,int value){
		this.rowPos=rowPos;
		this.colPos=colPos;
		this.value=value;
		//if matrix a[i][j]
		
	}
	public void initNodes(int[][] input,int rowsize,int colsize){
		if(this.rowPos-1>0){
			// north a[i-1][j]
			if(input[this.rowPos-1][colPos]<value)
			north= new Node(this.rowPos-1,this.colPos,input[this.rowPos-1][colPos]);
		}
		if(this.rowPos+1<rowsize){
			// south a[i][j+1]
				if(input[this.rowPos+1][colPos]<value)
					south= new Node(this.rowPos+1,this.colPos,input[this.rowPos+1][colPos]);
		}
	    if(this.colPos+1<colsize){
	    	if(input[this.rowPos][this.colPos+1]<value)
		// east  a[i][j+1]
	    	east=new Node(this.rowPos,colPos+1,input[this.rowPos][this.colPos+1]);
	    }
		if(this.colPos-1>0){
		 	if(input[this.rowPos][this.colPos-1]<value)
	    // west  a[i][j-1]
			west=new Node(this.rowPos,this.colPos-1,input[this.rowPos][this.colPos-1]);
		}
	}
	@Override
	public String toString() {
		return "Node [colPos=" + colPos + ", rowPos=" + rowPos + ", value=" + value + ", south=" + south + ", north="
				+ north + ", east=" + east + ", west=" + west + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colPos;
		result = prime * result + ((east == null) ? 0 : east.hashCode());
		result = prime * result + ((north == null) ? 0 : north.hashCode());
		result = prime * result + rowPos;
		result = prime * result + ((south == null) ? 0 : south.hashCode());
		result = prime * result + value;
		result = prime * result + ((west == null) ? 0 : west.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		Node target=(Node)obj; 
		if(this.colPos==target.colPos&&
			this.rowPos==target.rowPos&& this.value==target.value) return true;
		else
			return false;
	}
	

	
}
