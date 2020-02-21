import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Solution
 */
public class Solution {

    public static void main(String[] args) {
        int B,L,D;
        Scanner sc = new Scanner(System.in);
        B=sc.nextInt(); L=sc.nextInt(); D=sc.nextInt();
        int[] score = new int[B];
        HashMap<Integer, List<Library>> bookToLib = new HashMap<>();
        for(int i=0;i<B;i++) {
            score[i] = sc.nextInt();
            bookToLib.put(i, new ArrayList<Library>());
        }
        Book.B = score;
        TreeSet<Library> libraries = new TreeSet<>();
        
        for(int i=0;i<L;i++){
            Library library = new Library(i, sc.nextInt(), sc.nextInt(), sc.nextInt());
            for (int j=0;j<library.numOfBooks;j++) {
                int id=sc.nextInt();
                library.addBook(id);
                bookToLib.get(id).add(library);
            }

            libraries.add(library);
        }
        
        Set<Integer> scannedBooks = new HashSet<>();
        
        int usableLibrary = 0;
        int today = 0;
        int totalDay =D;
        String output = "";
        for (int i=0;i<L;i++) {
            Library library = libraries.pollFirst();
            today += library.signupDays;
            totalDay = D-today;
            if(totalDay<=0) break;
            int count = 0;
            List<Integer> scan = new ArrayList<>();
            for(Integer id: library.books) {
                if (count >= totalDay *library.booksPerDay) break;
                scannedBooks.add(id);
                scan.add(id);
                count++;

                for(Library lib: bookToLib.get(id)){
                    if(lib.id == library.id || lib.used){
                        continue;
                    }
                    libraries.remove(lib);
                    lib.removeBook(id);
                    libraries.add(lib);
                }
            }
            library.used = true;
            if (count > 0) {
                usableLibrary++;
                output += String.format("%d %d\n", library.id, count);
                for (Integer b: scan) {
                    output += String.format("%d ", b);
                }
                output += "\n";
            }
        }
        System.out.println(usableLibrary);
        System.out.print(output);
    }
}

class Book {
    static int[] B;
}

class Library implements Comparable<Library> {
    int id;
    int numOfBooks;
    int signupDays;
    int booksPerDay;
    TreeSet<Integer> books = new TreeSet<>();
    int booksSum;
    boolean used=false;

    public Library(int id, int numOfBooks, int signupDays, int booksPerDay) {
        this.id = id;
        this.numOfBooks = numOfBooks;
        this.booksPerDay = booksPerDay;
        this.signupDays = signupDays;
    }

    public void addBook(int index) {
        books.add(index);
        booksSum+=Book.B[index];
    }

    public void removeBook(int index) {
        if(books.remove(index)){
            booksSum-=Book.B[index];
        }
    }

    public int getScore() {
        return booksSum - signupDays*549;
    }

	@Override
	public int compareTo(Library o) {
		if(o.getScore() != this.getScore()) {
            return o.getScore() - this.getScore();
        } else if (this.signupDays != o.signupDays) {
            return this.signupDays - o.signupDays;
        } else {
            return this.id - o.id;
        }
	}

}