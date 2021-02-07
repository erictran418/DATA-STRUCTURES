package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if(poly1==null || poly2==null) {
			Node head = null;
			return head;
		}
		
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		
		ptr1 = checkzero(ptr1);
		ptr2 = checkzero(ptr2);
		
		//Inefficient but lazy
		Node poly1holderhead = new Node(ptr1.term.coeff, ptr1.term.degree, null);
		Node poly1prev = poly1holderhead;
		while(ptr1 != null) {
			Node poly1temp = new Node (ptr1.term.coeff, ptr1.term.degree, null);
			poly1prev.next = poly1temp;
			poly1prev = poly1temp;
			ptr1 = ptr1.next;
			
		}
		poly1holderhead = poly1holderhead.next;
		
		Node poly2holderhead = new Node(ptr2.term.coeff, ptr2.term.degree, null);
		Node poly2prev = poly2holderhead;
		while(ptr2 != null) {
			Node poly2temp = new Node (ptr2.term.coeff, ptr2.term.degree, null);
			poly2prev.next = poly2temp;
			poly2prev = poly2temp;
			ptr2 = ptr2.next;
			
		}
		poly2holderhead=poly2holderhead.next;
		//Inefficient but lazy
		
		Node head = null;
		int choice = 0;

		//determines where head will be depending on the first terms of each linked lists
		if(poly1holderhead.term.degree == poly2holderhead.term.degree) {
			float a = poly1holderhead.term.coeff + poly2holderhead.term.coeff;
			head = new Node(a, poly1holderhead.term.degree, null);
			choice = 1;
		}
		
		if(poly1holderhead.term.degree > poly2holderhead.term.degree) {
			head = new Node(poly2holderhead.term.coeff, poly2holderhead.term.degree, null);
			choice = 2;
		} else if(poly1holderhead.term.degree < poly2holderhead.term.degree){
			head = new Node(poly1holderhead.term.coeff, poly1holderhead.term.degree, null);
			choice = 3;
		}
		
		Node prev = head;
		
		switch(choice) {
		case 1: poly1holderhead = poly1holderhead.next; poly2holderhead = poly2holderhead.next; break;
		case 2: poly2holderhead = poly2holderhead.next; break;
		case 3: poly1holderhead = poly1holderhead.next; break;
		}
		
		while(poly1holderhead != null) {
			
			if(poly1holderhead.next == null && poly2holderhead.next != null) {
				while (poly2holderhead != null) {
					if (poly1holderhead.term.degree < poly2holderhead.term.degree) {
						Node temp = new Node(poly1holderhead.term.coeff,poly1holderhead.term.degree,null);
						prev.next = temp;
						prev = temp;
						prev.next = poly2holderhead;
						head = checkzero(head);
						return head;
					}
					
					if (poly2holderhead.next == null) {
						Node temp = new Node (poly2holderhead.term.coeff, poly2holderhead.term.degree,null);
						prev.next = temp;
						prev=temp;
						Node another = new Node(poly1holderhead.term.coeff, poly1holderhead.term.degree,null);
						prev.next = another;
						prev = another;
						head = checkzero(head);
						return head;
						
					}
					Node temp = new Node(poly2holderhead.term.coeff,poly2holderhead.term.degree,null);
					prev.next = temp;
					prev = temp;
					poly2holderhead = poly2holderhead.next;
				}
			}
			
			if(poly2holderhead.next == null && poly1holderhead.next != null) {
				while (poly1holderhead != null) {
					if (poly2holderhead.term.degree < poly1holderhead.term.degree) {
						Node temp = new Node(poly2holderhead.term.coeff,poly2holderhead.term.degree,null);
						prev.next = temp;
						prev = temp;
						prev.next = poly1holderhead;
						head = checkzero(head);
						return head;
					}
					
					if (poly1holderhead.next == null) {
						Node temp = new Node (poly1holderhead.term.coeff, poly1holderhead.term.degree,null);
						prev.next = temp;
						prev=temp;
						Node another = new Node(poly2holderhead.term.coeff, poly2holderhead.term.degree,null);
						prev.next = another;
						prev = another;
						head = checkzero(head);
						return head;
						
					}
					Node temp = new Node(poly1holderhead.term.coeff,poly1holderhead.term.degree,null);
					prev.next = temp;
					prev = temp;
					poly1holderhead = poly1holderhead.next;
				}
				
			}
			
			if(poly1holderhead.term.degree == poly2holderhead.term.degree) {
				float a = poly1holderhead.term.coeff + poly2holderhead.term.coeff; 
				Node temp = new Node(a, poly1holderhead.term.degree, null);
				prev.next = temp;
				prev = temp;
			}
			if(poly1holderhead.term.degree > poly2holderhead.term.degree) {
				Node temp = new Node (poly2holderhead.term.coeff, poly2holderhead.term.degree, null);
				prev.next = temp;
				prev = temp;
				Node another = new Node(poly1holderhead.term.coeff, poly1holderhead.term.degree, null);
				prev.next = another;
				prev = another;
				
			}
			if(poly2holderhead.term.degree > poly1holderhead.term.degree) {
				Node temp = new Node (poly1holderhead.term.coeff, poly1holderhead.term.degree, null);
				prev.next = temp;
				prev = temp;
				Node another = new Node(poly2holderhead.term.coeff, poly2holderhead.term.degree, null);
				prev.next = another;
				prev = another;
			}
			
			
			poly1holderhead = poly1holderhead.next;
			poly2holderhead = poly2holderhead.next;
		}
		head = checkzero(head);
		
		return head;
}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if(poly1 == null || poly2 == null) {
        	Node head = null;
            return head;
        }
		
		Node ptr1 = poly1;
		Node ptr2 = poly2;
        
        
        
        ptr1 = checkzero(ptr1);
        ptr2 = checkzero(ptr2);
        
        
        float cProd;
        int dSum;
        
        
        Node head = new Node(ptr1.term.coeff*ptr2.term.coeff, ptr1.term.degree+ptr2.term.degree,null);
        Node prev = head;
        
        
        while(ptr1 != null) {
            while (ptr2 != null) {
                cProd = ptr1.term.coeff * ptr2.term.coeff;
                dSum = ptr1.term.degree + ptr2.term.degree;
                Node temp = new Node(cProd, dSum, null);
                prev.next = temp;
                prev =temp;
                ptr2 = ptr2.next;
            }
            ptr1 = ptr1.next;
            ptr2 = poly2;
        }
        head = head.next;
        
        //finds max degree for combing like term operation
        Node maxDegreefinder = head;
        int maxDegree;
        while(maxDegreefinder.next != null) {
            maxDegreefinder = maxDegreefinder.next;
        }
        maxDegree = maxDegreefinder.term.degree;
        
        //Combines like terms
       Node fTemp = head;
       Node fHead = new Node(head.term.coeff, head.term.degree, null);
       Node fPrev = fHead;
      
       
       for(int i = 0; i<=maxDegree; i++) {
    	   fTemp = head;
    	   float fSum = 0;
    	   
    	   while(fTemp!= null) {
    		   if(fTemp.term.degree == i) {
    			   fSum+=fTemp.term.coeff;
    		   }
    		   fTemp = fTemp.next;
    	   }
    	   
    	   if(fSum != 0) {
    		   Node finalNode = new Node(fSum, i,null);
    		   fPrev.next = finalNode;
    		   fPrev = finalNode;
    	   }
       }
       //trim final excess Node
       fHead = fHead.next;
       
        
     		return fHead;

	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (poly == null) {
			return 0;
		}
		Node head = poly;
		
		head = checkzero(head);
	    
	    float counter = 0;
	    
	    while(head != null) {
	    	if(head.term.degree == 0) {
	    	counter += head.term.coeff;
	    	head = head.next;
	    	}
	    	double a = head.term.coeff;
	    	double b = head.term.degree;
	    	double c = Math.pow(x, b);
	    	counter += a*c;
	    	head = head.next;
	    }
		
		return counter;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}
	
	private static Node checkzero(Node head) {
		//ChecksList for excessive zeros
				Node checkzeroHead = head;
				Node checkzeroCurr = checkzeroHead;
				Node checkzeroPrev = null;
				
				//when head and rest of list is zero
				while(checkzeroHead.term.coeff == 0 && checkzeroHead !=null ) {
				if(checkzeroHead.term.coeff == 0) {
					checkzeroHead = checkzeroHead.next;
					checkzeroCurr = checkzeroHead;
				}
					if(checkzeroHead.term.coeff == 0 && checkzeroHead.next == null) {
					return null;
					}
				}
				
				while(checkzeroCurr.next != null) {
					checkzeroPrev = checkzeroCurr;
					checkzeroCurr = checkzeroCurr.next;
					
					if(checkzeroCurr.term.coeff == 0) {
						checkzeroCurr = checkzeroCurr.next;
						checkzeroPrev.next = checkzeroCurr;
					}
				}
				if(checkzeroCurr.next == null && checkzeroCurr.term.coeff ==0) {
					checkzeroPrev.next = null;
				}
				
				return checkzeroHead;
	}
}
