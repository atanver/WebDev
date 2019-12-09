package aasimah.queue;

import java.util.Random;

public class PriorityQueue {
	
	private class Node {
		
		private final int value;
		
		private Node next;

		public Node(int value) {
			this.value = value;
			this.next = null;
		}

		public Node(int value, Node nextNode) {
			this.value = value;
			this.next = nextNode;
		}
		
		public int getValue() {
			return value;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}
	}
	
	private Node headNode;
	
	public PriorityQueue() {
		this.headNode = null;
	}
	
	public void enque(int value) {
		if (headNode == null) {
			headNode = new Node(value);
		} else if (headNode.getValue() > value){
			Node newNode = new Node(value, headNode);
			headNode = newNode;
		} else {
			Node prevNode = headNode;
			while ((prevNode.getNext() != null) && (prevNode.getNext().getValue() <= value)) {
				prevNode = prevNode.getNext();
			}
			Node newNode = new Node(value, prevNode.getNext());
			prevNode.setNext(newNode);
		}
	}
	
	public void printQueue() {
		Node currentNode = headNode;
		while (currentNode != null) {
			System.out.print(currentNode.getValue() + " ");
			currentNode = currentNode.getNext();
		}
		System.out.println();
	}
	
	public Integer deque() {
		Integer value = null;
		if (headNode != null) {
			value = headNode.getValue();
			headNode = headNode.getNext();
		}
		return value;
	}

	public static void main(String[] args) {
		PriorityQueue queue = new PriorityQueue();
		int size = 10;
		Random rand = new Random();
		for (int i = 0; i < size; i++) {
			queue.enque(rand.nextInt() % size);
			queue.printQueue();
		}
		for (int i = 0; i < size + 2; i++) {
			System.out.println(queue.deque());
			queue.printQueue();
		}
	}

}
