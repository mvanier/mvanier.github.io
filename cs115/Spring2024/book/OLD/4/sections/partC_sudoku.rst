Part C: Miniproject: Sudoku
===========================

[**10 marks**]

Most of you probably already know how to play Sudoku. If by chance you
don’t, `here <https://en.wikipedia.org/wiki/Sudoku>`_ is a link to
teach you the rules. Sudoku puzzles range from quite easy to extremely
difficult. However, computers have a much easier time solving Sudoku
puzzles than humans do, and in this section you’ll be writing a Haskell
program to do just that. Your program should be able to solve the sample
Sudoku puzzles we’ll supply you with in no more than about 30 seconds
per puzzle, though it may take longer on other Sudoku puzzles.


Program description
-------------------

The program will read in a file containing a representation of a Sudoku
puzzle, compute the solution to the puzzle, and print the solution to
standard output (the terminal).


Solution algorithm
------------------

**We want you to solve this problem imperatively.**
It is possible to write very concise and elegant Sudoku solvers
in a purely functional manner (which is a good exercise)
but that’s not how we want you to do it here.
Instead, the program will read the input board’s contents into an array
(specifically, an ``IOArray``). From there, it will mutate the contents
of the array until a solution is reached or until it is determined that
there is no solution.

The solution algorithm works as follows.

* Iterate over the 81 locations in the board row-by-row
  and column-by-column within rows.

* If the location is already filled (contains a number between 1 and 9),
  leave it alone and keep going.

* If it’s empty (contains a 0), you have to pick a number
  to put into the location.
  Compute all the possible numbers that could be in this location
  (|ie| numbers that don’t conflict with other numbers
  in the same row, column, or 3x3 box).
  For each of these numbers:

  * Set the location to contain that number.
  * Try to (recursively) solve the board starting from the next location.

  If this works, you have your solution, so return ``True``
  (the board will have mutated to the solution value).

  If not, unmake the move (write a zero back into the location)
  and try the next move.
  
  .. note::

     **Unmaking the move is very important!**
     If you forget to do this your program will not work!

* If you are at the last location
  and can’t find a suitable number to put there, return ``False``.


Inputs and outputs
------------------

The compiled program will be called ``sudoku`` and will take a single
argument, which is the name of a file containing a representation of a
board. An example board looks like this:

.. code-block:: text

   .........
   ....1..92
   .86....4.
   ..156....
   .....362.
   ......5.7
   .3.....8.
   .9.8.2...
   ..7..43..

The (``.``)s represent empty squares, and numbers from 1 to 9 mean that
this location in the board has that (fixed) number. The file must
contain nothing else but these characters (not even whitespace at the
ends of lines, other than the necessary newline character).

The internal representation of the board is as a two-dimensional
``IOArray`` (indexed by (``Int``, ``Int``) pairs) where an empty square
is represented by a zero and a digit from 1 to 9 is represented by the
corresponding ``Int`` value.

The job of the program is to fill in the locations marked with (``.``)s
so that the board is solved. In this case, the program would output the
following (unique) solution to the Sudoku puzzle:

.. code-block:: text

   412985763
   753416892
   986327145
   271568934
   549173628
   368249517
   634751289
   195832476
   827694351

This output is simply printed to standard output (the terminal).


Template file
-------------

To get you started,
in the ``lab4.zip`` file on Canvas,
we are supplying you with a template file called ``Sudoku.hs``
that contains some of the less interesting function definitions
pre-written for you.
This will allow you to concentrate on the interesting part
(the board solving code).
You are not absolutely required to use our code
(if you think you can do things better all on your own, you may),
but using it will probably significantly cut down
on the time requirement for this problem.
If you do use our code as a template,
you should substitute your own code
in the location marked ``{- TODO -}``.
Please remove the ``{- TODO -}`` comment as well!

We are also including the type signatures and comments of all the helper
functions we used in the ``solveSudoku`` function. You may use these to
guide you in your solution, or write different helper functions as you
see fit. **Note, though, that the solution has to be imperative, not
functional.** Nevertheless, it will be recursive, as described above.

Note that one thing you are not allowed to change is the representation
of the Sudoku board |ie| the type definition:

.. code-block:: haskell

   type Sudoku = IOArray (Int, Int) Int

Since this problem is an exercise in doing imperative programming in Haskell,
we want you to solve this program using the ``IO`` monad and ``IOArray``\s.

Another thing you are absolutely not allowed to do, of course, is to
copy Sudoku-solving code written in Haskell you find somewhere on the
internet or elsewhere.


Sample input boards and running the program
-------------------------------------------

In the ``lab4.zip`` file,
we are including a directory called ``boards`` containing ten input boards.
You should use these board files to test your program.
None of them should take your program more than 30 seconds to solve,
and most should take much less time than that.

Test a particular board by running it like this:

.. code-block:: text

   $ ./sudoku boards/board1
   412985763
   753416892
   986327145
   271568934
   549173628
   368249517
   634751289
   195832476
   827694351

If you want to time it, this should work:

.. code-block:: text

   $ /usr/bin/time ./sudoku boards/board1
   412985763
   753416892
   986327145
   271568934
   549173628
   368249517
   634751289
   195832476
   827694351
           4.74 real         4.68 user         0.04 sys

The ``user`` time gives the actual elapsed time for the run in seconds.

