//Sorting HW
// Bubble Sort, Selection, Insertion Sort     

public void bubbleSort(){
	int out, in;
	for(out = nElems - 1, out > 1, out--){
		for(in = 0, in < 1, in++){
			if(a[in] > a[n+1]){
				swap(in, in + 1);
			} 
		}
	}
}

public void selectionSort() {
	int out, in, min;
	for(out=0; out<nElems-1; out++) {
		min = out;
		for(in=out+1; in<nElems; in++)
			if(a[in] < a[min]){ 
				min = in;}
		swap(out, min);
	} 
}

public void insertionSort() {
	int in, out;
	for(out=1; out<nElems; out++) {
		long temp = a[out];
		in = out;
	while(in>0 && a[in-1] >= temp){
		a[in] = a[in-1];
		--in;
		}
	a[in] = temp; 
	}
}

private void swap(int first, int second){
	long temp = a[one];
	a[one] = a[two];
	a[two] = temp;
}

public static void main(String[]args){

}