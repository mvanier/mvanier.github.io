Preamble
========

Overview
--------

The theme of this assignment is working with the ``IO`` monad. We will
do so in various ways: with some simple exercises, some simple
stand-alone programs, and a more complicated program. When you’ve
finished this assignment you should be very comfortable with the ``IO``
monad and with the ``do`` notation for monads.


Due date
--------

This assignment is due on |dueday|, |lab4due| at |duetime|.


Coverage
--------

This assignment covers the material up to lecture 13.


What to hand in
---------------

You will hand in four files for this assignment. The exercises will be
in a file called ``Lab4a.hs``. The three standalone programs should each
be in separate files called ``Reverse.hs``, ``Columns.hs`` and
``Sudoku.hs``. These files should be submitted to CodePost as
``Assignment 4``.


Writing and testing your code
-----------------------------

The code base for the assignment is available on Canvas as ``lab4.zip``.

We don't have automated tests for this assignment,
since most of the assignment is creating and running standalone programs.
We do expect that all of your standalone programs should compile successfully
(without warnings) and run correctly on inputs of varying kinds.
You should test these manually to convince yourself of this.
The writeup contains some suggested inputs for you to try.

.. note::

   Although automated tests are awesome,
   many students get far too dependent on them,
   to the point where they never actually run their programs themselves.
   This assignment will hopefully help you to step out of that mindset!

The last part of the assignment (the Sudoku solver) 
has both a template file called ``Sudoku.hs``
and a directory containing sample input boards.
The solver should successfully solve all the sample boards
in a reasonable amount of time
(certainly no more than 30 seconds for any given board).

