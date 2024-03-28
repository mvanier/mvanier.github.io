Preamble
========

Due date
--------

This assignment is due on |dueday|, |lab2due| at |duetime|.


What to hand in
---------------

Write all your code in a single file called ``RedBlackTree.hs``. This
file should be submitted to CodePost as ``Assignment 2``.


Writing your code
-----------------

The file you will submit will be a Haskell module, which means it must
begin with a module declaration of the form:

.. code-block:: haskell

   module MODULENAME(EXPORTLIST) where

In this case, we are exporting everything from the module (mostly for
ease of testing), so the line should just be:

.. code-block:: haskell

   module RedBlackTree where


Testing your code
-----------------

For this week’s assignment's code base,
we are supplying you with a test script called ``Tests_Lab2.hs``,
as well as a ``Makefile``.
You can run the tests by putting both files into the same directory
and typing

.. code-block:: text

   $ make
   $ make test

(where ``$`` is the terminal prompt, of course).

This test script will run a number of tests on your code; any failure
indicates that you have some debugging to do. The test script relies on
the Haskell testing packages ``HUnit`` (which we installed and used last
week) and ``QuickCheck``. Install ``QuickCheck`` by starting up a
terminal and entering these commands:

.. code-block:: text

   $ cabal update
   $ cabal install QuickCheck --lib

``QuickCheck`` is a randomized testing framework
that tests that functions obey particular properties,
automatically synthesizing large numbers of random data values
to test the functions. [1]_
``HUnit`` is a more traditional unit testing framework where you supply
expected results for certain computations.

Some of the functions you will be writing in this assignment
are only used for testing;
if they are buggy, your tests may pass but your code may still be wrong.
We’ll be testing your code on our own test script, which is more stringent.
Moral: **don’t let test scripts lull you into a false sense of security!**
Just because code passes a test script doesn’t mean it’s automatically correct.

----

.. rubric:: Footnotes

.. [1] The ``QuickCheck`` library has been quite influential.
   A number of other languages have built ``QuickCheck`` clones
   to do the same kind of randomized testing.
