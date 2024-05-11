Part C: Writing functions using arrays and locals
=================================================

A well-known typed imperative language with arrays and local variables
is C, so the functions you are going to write for this section are taken
from the CS 11 C track, assignment 3. You will write your
implementations of two functions in the (incomplete) test script called
``lab4.timp``. You can refer to the CS 11 description of the algorithm
to help you write your code. (You don't need to do the assertion stuff,
though.)

You will write two functions, each of which implements an imperative
in-place sorting algorithm on arrays. Each function will return a unit
value (``#u``).

.. note::

   The most recent version of CS 11 C track assignment 3 is located
   `here <http://users.cms.caltech.edu/~mvanier/CS11_C/labs/3/lab3.html>`_.


1. Minimum element sort
-----------------------

A minimum element sort starts with a nominal "minimum value" (the first
element of the array) and then successively compares it with each other
element in the array. When it finds a smaller element, it makes that the
smallest element and notes its index. Once the entire array has been
traversed, the index of the smallest element in the array is known. That
value is swapped with the value at the first index. Then the process is
repeated, starting from the next (second) element of the array, and so
on until the array is totally sorted.


2. Bubble sort
--------------

You should already know how a bubble sort works (and/or be willing to
look this up), but here goes anyway. With a bubble sort algorithm, you
compare adjacent values in an array, swapping them if they are
out-of-order. You move from one pair to the next adjacent pair (so the
first pair is indices 0 and 1, the next is indices 1 and 2, _etc._),
swapping as needed until you get to the end of the array. At this point,
the *last* element in the array should be the largest. Repeat the
process until the array is sorted. There are different ways to tell if
the array is sorted, but perhaps the simplest is to set a boolean
variable to ``#t`` at the beginning of an iteration and setting it to
``#f`` if a swap is ever made. If the entire array is traversed without
a swap, the array is sorted and you can stop!


General advice
--------------

Even though you might think these are trivial functions to write, be
aware that having to do them in the very limited *Typed Imp* language
will require care. All ``if`` expressions must have "else" clauses, and
the "then" and "else" clauses have to have the same type. Sometimes
adding a judicious ``#u`` can be very helpful. Almost all ``while``
expressions have a ``begin`` expression in the body, and don't forget to
update the loop counter! Make sure the sorting functions return ``#u``;
they are sorting in-place, after all.


