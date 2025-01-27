package oy.interact.tira.student;

import java.util.Comparator;

public class Algorithms {

   private Algorithms() {
      // nada
   }

   ///////////////////////////////////////////
   // Insertion Sort for the whole array
   ///////////////////////////////////////////

   public static <T extends Comparable<T>> void insertionSort(T[] array) {
      insertionSort(array, 0, array.length);
   }

   ///////////////////////////////////////////
   // Insertion Sort for a slice of the array
   ///////////////////////////////////////////

   public static <T extends Comparable<T>> void insertionSort(T[] array, int fromIndex, int toIndex) {
      for(int i = fromIndex; i < toIndex; i++){
          T temp = array[i];
          int j = i - 1;
          while (j >= fromIndex && array[j].compareTo(temp) > 0){
              array[j + 1] = array[j];
              j--;
          }
          array[j + 1] = temp;
      }
  }

   //////////////////////////////////////////////////////////
   // Insertion Sort for the whole array using a Comparator
   //////////////////////////////////////////////////////////

   public static <T> void insertionSort(T[] array, Comparator<T> comparator) {      
      insertionSort(array, 0, array.length, comparator);
   }

   ////////////////////////////////////////////////////////////
   // Insertion Sort for slice of the array using a Comparator
   ////////////////////////////////////////////////////////////

   public static <T> void insertionSort(T[] array, int fromIndex, int toIndex, Comparator<T> comparator) {
      for (int i = fromIndex; i < toIndex; i++) {
         T temp = array[i];
         int j = i;
         while (j > fromIndex) {
             T temp2 = array[j - 1];
             if (comparator.compare(temp, temp2) >= 0) {
                 break;
             }
             array[j] = temp2;
             j--;
         }
         array[j] = temp;
     }
   }

   ///////////////////////////////////////////
   // Reversing an array
   ///////////////////////////////////////////

   public static <T> void reverse(T[] array) {
      reverse(array, 0, array.length);
   }

   ///////////////////////////////////////////
   // Reversing a slice of an array
   ///////////////////////////////////////////

   public static <T> void reverse(T[] array, int fromIndex, int toIndex) {
      T temp;
      for (int i = 0; i < toIndex / 2; i++){
         temp = array[i];
         array[i] = array[toIndex - i - 1];
         array[toIndex - i - 1] = temp;
      }
   }


   ///////////////////////////////////////////
   // Recursive binary search bw indices
   ///////////////////////////////////////////

   public enum BSearchImplementation {
      RECURSIVE,
      ITERATIVE
   }

   public static <T extends Comparable<T>> int binarySearch(T aValue, T[] fromArray, int fromIndex, int toIndex) {
      return binarySearchRecursive(aValue, fromArray, fromIndex, toIndex - 1);
   }

   public static <T extends Comparable<T>> int binarySearch(T aValue, T[] fromArray, int fromIndex, int toIndex, BSearchImplementation impl) {
      if (impl == BSearchImplementation.RECURSIVE) {
         return binarySearchRecursive(aValue, fromArray, fromIndex, toIndex - 1);
      }
      // TODO: IF implementing iterative binary search, call that here.
      return -1;
   }

   public static <T extends Comparable<T>> int binarySearchRecursive(T aValue, T[] fromArray, int fromIndex, int toIndex) {
      if (fromIndex > toIndex) {
         return -1;
      }
      int mid = (fromIndex + toIndex) >>> 1;
      T temp = fromArray[mid];
      if (temp.compareTo(aValue) == 0) {
          return mid;
      }
      if (aValue.compareTo(temp) < 0) {
          return binarySearchRecursive(aValue, fromArray, fromIndex, mid - 1);
      } else {
          return binarySearchRecursive(aValue, fromArray, mid + 1, toIndex);
      }

   }

   ///////////////////////////////////////////
   // Binary search using a Comparator
   ///////////////////////////////////////////

   public static <T> int binarySearch(T aValue, T[] fromArray, int fromIndex, int toIndex, Comparator<T> comparator) {
      return binarySearchRecursive(aValue, fromArray, fromIndex, toIndex , comparator);
   }

   public static <T> int binarySearch(T aValue, T[] fromArray, int fromIndex, int toIndex, Comparator<T> comparator, BSearchImplementation impl) {
      if (impl == BSearchImplementation.RECURSIVE) {
         return binarySearchRecursive(aValue, fromArray, fromIndex, toIndex, comparator);
      }
      // TODO: IF implementing iterative binary search, call that here.
      return -1;
   }

   public static <T> int binarySearchRecursive(T aValue, T[] fromArray, int fromIndex, int toIndex, Comparator<T> comparator) {
            int left = fromIndex;
            int right = toIndex - 1;
            while (left <= right){
               int mid = (left + right) >>> 1;
               T temp = fromArray[mid];
               if(comparator.compare(aValue, temp) == 0){
                  return mid;
               }
               if(comparator.compare(aValue, temp) < 0){
                  right = mid - 1;
               }
               else{
                  left = mid + 1;
               }
            }
            return -1;
   }

   public static <T> int binarySearchIterative(T aValue, T[] fromArray, int fromIndex, int toIndex, Comparator<T> comparator) {
      // TODO: Iterative implementation if grade interests you:
      return -1;
   }

   public enum FastSortAlgorithm {
      QUICKSORT,
      HEAPSORT,
      MERGESORT
   }

   public static <E> void fastSort(E[] array, Comparator<E> comparator) {
      fastSort(array, 0, array.length, comparator, FastSortAlgorithm.QUICKSORT);
   }

   public static <E extends Comparable<E>> void fastSort(E[] array, int fromIndex, int toIndex){
      fastSort(array, 0, array.length, Comparator.naturalOrder(), FastSortAlgorithm.QUICKSORT);
   }

   public static <E extends Comparable<E>> void fastSort(E[] array) {
      fastSort(array, 0, array.length, Comparator.naturalOrder(), FastSortAlgorithm.QUICKSORT);
   }

   public static <E> void fastSort(E[] array, int fromIndex, int toIndex, Comparator<E> comparator) {
      fastSort(array, fromIndex, toIndex, comparator, FastSortAlgorithm.QUICKSORT);
   }

   public static <E> void fastSort(E[] array, int fromIndex, int toIndex, Comparator<E> comparator, FastSortAlgorithm algorithm) {
      switch (algorithm) {
         case QUICKSORT:
            quickSort(array, fromIndex, toIndex - 1, comparator);
            break;
         case HEAPSORT:
            // TODO: IF implementing heapsort, call your algorithm here.
            break;
         case MERGESORT:
            // TODO: IF implementing mergesort, call your algorithm here.
            break;
         default:
            break;
      }
   }

   public static <E> void quickSort(E [] array, int fromIndex, int toIndex, Comparator<E> comparator) {
      if(fromIndex < toIndex){
         int pivot = partitionC(array, fromIndex, toIndex, comparator);
         quickSort(array, fromIndex, pivot-1, comparator);
         quickSort(array, pivot+1, toIndex, comparator);
      }
   }

   public static <E> int partitionC(E [] array, int begin, int end, Comparator<E> comparator){

      E pivot = array[end];
      int i = (begin - 1);

      for(int j = begin; j < end; j++){
         if(comparator.compare(array[j], pivot) <= 0){
            i++;
            E swap = array[i];
            array[i] = array[j];
            array[j] = swap;
         }
      }
      
      E swap = array[i+1];
      array[i+1] = array[end];
      array[end] = swap;

      return i+1;

   }

   

} // End of class Algorithms
