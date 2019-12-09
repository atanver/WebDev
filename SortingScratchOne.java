import java.util.Random;
public class Sorting {
	private static void swap(int a[], int one, int two){
		int temp = a[one];
		a[one] = a[two];
		a[two] = temp;
	}
	public static void bubbleSort(int a[], String direction){
		for(int i = a.length - 1; i > 0; i--){
			for(int j = 0; j < i; j++){
				if (direction.equals("ascend")) {
					if(a[j] > a[j + 1]){
						swap(a, j, j + 1);
					}
				} else if (direction.equals("descend")) {
					if(a[j] < a[j + 1]){
						swap(a, j, j + 1);
					}
				}
			}
		}
	}
	public static void selectionSort(int a[], String direction){
		for(int i = 0; i < a.length; i++) {
			int minIndex = i;
			for (int j = i; j < a.length; j++) {
				if(direction.equals("ascend")){
					if (a[j] < a[minIndex]) {
						minIndex = j;
					}
				}else if (direction.equals("descend")) {
					if (a[j] > a[minIndex]) {
						minIndex = j;
					}
				}
			}
			swap(a, i, minIndex);
		}
}
	public static void insertionSort(int a[], String direction){
		for(int sortedLine = 1; sortedLine < a.length; sortedLine++) {
			int temp = a[sortedLine];
			int newPos = sortedLine;
			if(direction.equals("ascend")){
				while ((newPos > 0) && (a[newPos - 1] > temp)) {
					newPos--;
				}
			}else if (direction.equals("descend")) {
				while ((newPos > 0) && (a[newPos - 1] < temp)) {
					newPos--;
				}
			}
			for (int movePos = sortedLine; movePos > newPos; movePos--) {
				a[movePos] = a[movePos - 1];
			}
			a[newPos] = temp;
		}
	}
	public static boolean isSorted(int[] a, String direction){
		if (direction.equalsIgnoreCase("ascend")){
			for (int i = 0; i < a.length - 1; i++)
				if (a[i] > a[i + 1])
					return false;
		}
		else if (direction.equalsIgnoreCase("descend")){
			for (int i = 0; i < a.length - 1; i++)
				if (a[i] < a[i + 1])
					return false;
		}
	else{
		System.out.println("Invalid sorting direction!");
		return false;
	}
	 return true;
	}
	
	public static void display(int a[]) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
		System.out.println();
	}
	public static void main(String[]args){
		Random rand = new Random();
		int arrSize = 10;
		int arr1[] = new int[arrSize];
		int arr2[] = new int[arrSize];
		int arr3[] = new int[arrSize];
		for(int i = 0; i < arr1.length; i++){
			arr1[i] = rand.nextInt() % arr1.length;
			arr2[i] = arr1[i];
			arr3[i] = arr1[i];
		}
		display(arr1);
		bubbleSort(arr1, "ascend");
		display(arr1);
		System.out.println("Bubble Sort Ascending result: " + isSorted(arr1, "ascend"));
		bubbleSort(arr1, "descend");
		display(arr1);
		System.out.println("Bubble Sort Descending result: " + isSorted(arr1, "descend"));
		selectionSort(arr2, "ascend");
		display(arr2);
		System.out.println("Selection Sort Ascending result: " + isSorted(arr2, "ascend"));
		selectionSort(arr2, "descend");
		display(arr2);
		System.out.println("Selection Sort Descending result: " + isSorted(arr2, "descend"));
		insertionSort(arr3, "ascend");
		display(arr3);
		System.out.println("Insertion Sort Ascending result: " + isSorted(arr3, "ascend"));
		insertionSort(arr3, "descend");
		display(arr3);
		System.out.println("Insertion Sort Descending result: " + isSorted(arr3, "descend"));
	}
}
