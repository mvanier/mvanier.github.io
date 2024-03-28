Preamble
========

Overview
--------

This assignment’s theme is parsing and state-like monads. We will do
some exercises involving the ``State`` monad, derive the related (but
simpler) ``Reader`` monad, and then use parser combinators to parse
first a Scheme-like language and then a subset of XML.


Due date
--------

This assignment is due on |dueday|, |lab6due| at |duetime|.


Coverage
--------

This assignment covers the material up to lecture 21.


Submitting your assignment
--------------------------

Your code will consist of three files, corresponding to the three parts
of this assignment. The first file is called ``State.hs`` and contains
the part A and part B solutions. The second file is called ``Sexpr.hs``
and contains the part C solutions. The third file is called ``XML.hs``
and contains the part D solutions. These files should be submitted to
CodePost as ``Assignment 6``.


Writing and testing your code
-----------------------------

The code base for the assignment is available on Canvas as ``lab6.zip``.
There is a ``Makefile``, template code for all three sections, a test script,
and some input and expected output files.

.. note::

   We encourage you to test your code interactively,
   even if there are extensive tests.
   This is especially useful when developing the parsing code,
   as described below.

Part A
~~~~~~

Part A has a test script called ``Tests_Lab6a.hs``.
Compile this and run it by typing:

.. code-block:: text

   $ make lab6a_test

Testing functions which return ``IO`` values is tricky,
so we use the ``unsafePerformIO`` function (!) to do it.
(Don't worry -- it's safe in this context.)

Part B
~~~~~~

Part B is a derivation, so there are no tests.

Part C
~~~~~~

To test the S-expression parser in part C, compile and test it by typing

.. code-block:: text

   $ make sexpr
   $ make sexpr_test

``make sexpr`` will generate a standalone program called ``sexpr``
which can be called with a single filename as its argument:

.. code-block:: text

   $ ./sexpr test.scm

The tests compare the output of this to the expected output.
If it's not working yet, you can create your own (simpler)
file of S-expressions and see what the ``sexpr`` program converts it into.

Part D
~~~~~~

To test the XML parser in part D, compile and test it by typing

.. code-block:: text

   $ make xml
   $ make xml_test

This works exactly like the S-expression parser tests,
except for XML.  So the executable generated is called ``xml``
and the test file is ``test.xml``.

Running all tests
~~~~~~~~~~~~~~~~~

If you type:

.. code-block:: text

   $ make all

all code will be compiled and tested.  Naturally,

.. code-block:: text

   $ make clean

will remove all compilation and test artifacts.


General advice
~~~~~~~~~~~~~~

Don’t forget to use the ``parseTest`` function to test parsers
interactively. This was described in lecture 21. Import the module
``Text.Parsec`` to get access to this function. (Actually, if you load
up your code which imports this module, you will have access to it
already.) Look it up in Hoogle_ if you need more information about how
to use it.

Before you import ``Text.Parsec`` into ``ghci``, enter this line:

.. code-block:: text

   ghci> :set -package parsec

This will allow you to then enter:

.. code-block:: text

   ghci> import Text.Parsec

without errors. Similarly, before importing ``Control.Monad.State`` into
``ghci``, enter:

.. code-block:: text

   ghci> :set -package mtl

If you forget to do this, ``ghci`` will remind you.

.. note::

   These ``:set`` lines are *only* needed in ``ghci``; they shouldn’t be
   there in normal Haskell source code files.

