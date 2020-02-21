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

/**
 * Solution
 */
public class Solution {

    private static double h(double[] stat, Library l, int bookValueSum) {
        double averageSignupDay = stat[0];
        double stdevSignupDay = stat[1];
        double averageBookValueSum = stat[2];
        double stdevBookValueSum = stat[3];
        double signupDayScore = ((double) l.signupDays - averageSignupDay) / stdevSignupDay;
        double bookValueSumScore = ((double) bookValueSum - averageBookValueSum) / stdevBookValueSum;
        return bookValueSumScore - signupDayScore;
    }

    private static double[] getStat(Library[] libraries, int[] bookValueSum) {
        int totalSignupDay = 0;
        int totalBookValueSum = 0;
        double stdevSignupDay = 0.0;
        double stdevBookValueSum = 0.0;
        for(int i=0;i<libraries.length;i++) {
            totalSignupDay += libraries[i].signupDays;
            totalBookValueSum += bookValueSum[i];
        }
        double averageSignupDay = (double) totalSignupDay / libraries.length;
        double averageBookValueSum = (double) totalBookValueSum / libraries.length;
        for(int i=0;i<libraries.length;i++) {
            stdevSignupDay += Math.pow(libraries[i].signupDays-averageSignupDay,2);
            stdevBookValueSum += Math.pow(bookValueSum[i]-averageBookValueSum,2);
        }
        stdevSignupDay = Math.sqrt(stdevSignupDay/libraries.length);
        stdevBookValueSum = Math.sqrt(stdevBookValueSum/libraries.length);
        double[] res = new double[4];
        res[0] = averageSignupDay;
        res[1] = stdevSignupDay;
        res[2] = averageBookValueSum;
        res[3] = stdevBookValueSum;
        return res;
    }

    public static void main(String[] args) {
        int B,L,D;
        Scanner sc = new Scanner(System.in);
        B=sc.nextInt(); L=sc.nextInt(); D=sc.nextInt();
        int[] score = new int[B];
        for(int i=0;i<B;i++) {
            score[i] = sc.nextInt();
        }
        Library[] libraries = new Library[L];
        int[] bookValueSum = new int[L];
        for(int i=0;i<L;i++){
            libraries[i] = new Library(i, sc.nextInt(), sc.nextInt(), sc.nextInt(), null);
            List<Integer> books = new ArrayList<>();
            libraries[i].setBooks(books);
            bookValueSum[i] = 0;
            for (int j=0;j<libraries[i].numOfBooks;j++) {
                int curb = sc.nextInt();
                books.add(curb);
                bookValueSum[i] += score[curb];
            }
            Collections.sort(books, (first, second) -> {
                return score[second] - score[first];
            });
        }
        double[] stat = getStat(libraries, bookValueSum);
        Arrays.sort(libraries, (l1, l2) -> {
            return h(stat,l1,l1.id)-h(stat,l2,l2.id) > 0 ? -1 : 1;
        });

        Set<Integer> scannedBooks = new HashSet<>();
        int lastLibrary = 0;
        int dayCounter = 0;
        for (int i=0;i<L;i++) {
            dayCounter += libraries[i].signupDays;
            if (dayCounter <= D) {
                lastLibrary = i;
            } else {
                break;
            }
        }
        int usableLibrary = 0;
        int[] totalDay = new int[lastLibrary+1];
        int today = 0;
        for (int i=0;i<=lastLibrary;i++) {
            today += libraries[i].signupDays;
            totalDay[i] = D-today;
        }
        String output = "";
        for (int i=0;i<=lastLibrary;i++) {
            int count = 0;
            List<Integer> scan = new ArrayList<>();
            for(Integer id: libraries[i].books) {
                if (count >= totalDay[i] *libraries[i].booksPerDay) break;
                if (!scannedBooks.contains(id)) {
                    scannedBooks.add(id);
                    scan.add(id);
                    count++;
                }
            }
            if (count > 0) {
                usableLibrary++;
                output += String.format("%d %d\n", libraries[i].id, count);
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
    int id;
    int value;
    public Book(int id, int value) {
        this.id = id;
        this.value = value;
    }
}

class Library {
    int id;
    int numOfBooks;
    int signupDays;
    int booksPerDay;
    List<Integer> books;

    public Library(int id, int numOfBooks, int signupDays, int booksPerDay, List<Integer> books) {
        this.id = id;
        this.numOfBooks = numOfBooks;
        this.booksPerDay = booksPerDay;
        this.signupDays = signupDays;
        this.books = books;
    }

    public void setBooks(List<Integer> books) {
        this.books = books;
    }

}

class LibraryComparator implements Comparator<Library> {
    @Override
    public int compare(Library o1, Library o2) {
        return o1.signupDays - o2.signupDays;
    }
}