Part B: Simple standalone programs
==================================

In this section you will write two fairly simple standalone programs
that do input/output.

.. warning::

   Do not use ``unsafePerformIO`` at all in this assignment! If you
   think you need it, keep thinking.


1. ``reverse``
--------------

[**4 marks**]

Write a program called ``reverse`` that reads in all the lines of a file
and prints them, in reverse order, to standard output (|ie| to the
terminal).


Usage
~~~~~

Your program should be invoked as follows (``$`` is the prompt):

.. code-block:: text

   $ ./reverse filename

(for some file ``filename``). If there are too many or too few
command-line arguments, print out a usage message like this:

.. code-block:: text

   usage: reverse filename

and exit with a failure status. Upon successful completion, exit with a
success status (see the documentation in the ``System.Exit`` module for
more on exit status.)

.. note::

   You may assume that the file exists and is a text file (you don’t
   have to handle the error cases where this isn’t true). This applies
   to all the programs in this section.


Example
~~~~~~~

.. code-block:: text

   $ cat myfile
   This is my file.
   It has five lines.
   This is the third line.
   To be or not to be,
   that is the question.
   $ reverse myfile
   that is the question.
   To be or not to be,
   This is the third line.
   It has five lines.
   This is my file.


Useful modules/functions
~~~~~~~~~~~~~~~~~~~~~~~~

You may want to look the following modules/functions up in Hoogle_
before writing your program.

* ``Prelude`` module: ``reverse``, ``lines``, ``mapM_``, ``readFile``
* ``System.Environment`` module: ``getProgName``, ``getArgs``
* ``System.Exit`` module: ``exitFailure``, ``exitSuccess``


Writing and compiling the program
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Write the program in a file called ``Reverse.hs``. The program should
define a module called ``Main`` and there should be a function called
``main`` of type ``IO ()``. All functions must have explicit type
signatures! Compile the program by typing this line:

.. code-block:: text

   $ ghc -W -o reverse Reverse.hs -package base

(Without the ``$`` prompt, of course.) Any compiler warnings should be
considered as errors (with some obscure exceptions that you’re unlikely
to run into; if in doubt, email me).

All other standalone programs described below should be compiled in a
similar way.

.. note::

   This program only needs to be a few lines long.

Students don’t have to use ``hPutStrLn`` to print the usage message to
``stderr``. Regular ``putStrLn`` is OK too.


2. ``columns``
--------------

[**6 marks**]

Write a program called ``columns`` which will take some numbers and a
filename as command-line arguments, and output the corresponding columns
of the input to standard output. Column N of a line is defined to be the
Nth item in a list which is obtained by splitting the line on whitespace
(starting from 1 for the first item).


Usage
~~~~~

Your program should be invoked as follows:

.. code-block:: text

   $ ./columns n1 n2 ... filename

where ``n1``, ``n2``, |etc| are positive integers (there should be at
least one), and where ``filename`` is the name of the file to read from.
If ``filename`` is ``-`` (a single dash character), then read from
``stdin`` (standard input |ie| the terminal).

Given invalid inputs (numbers that aren’t numbers or are negative or 0),
print a usage message and exit as in the previous program. Your usage
message must indicate what the proper inputs need to be. Missing columns
are not an error; just ignore them. If all the requested columns are
missing in a line, output a blank line for that line.

Column numbers may occur in the command line out of order or duplicated;
print the correct value for each column number in the order that it
occurs. For instance, if the command line was ``columns 1 1 1 foobar``
then each output line will have three copies of the first column of that
line of the input file ``foobar``, and ``columns 3 2 1 foobar`` would
print the first three columns of the file ``foobar`` in reverse order.

Again, you may assume that the filename argument corresponds to a real
text file; you don’t have to check for nonexistent files or non-text
files.


Examples
~~~~~~~~

.. code-block:: text

   $ cat myfile
   a b c d
   foo bar baz
   now is the time for all good men
   these go to eleven
   $ ./columns 1 3 myfile
   a c
   foo baz
   now the
   these to
   $ cat myfile | ./columns 1 3 -
   a c
   foo baz
   now the
   these to
   $ ./columns 2 4 5 myfile
   b d
   bar
   is time for
   go eleven
   $ ./columns 2 1000 myfile
   b
   bar
   is
   go


Useful modules/functions
~~~~~~~~~~~~~~~~~~~~~~~~

In addition to the functions mentioned in the previous problem, you
might want to look at:

* ``Prelude:`` ``read``, ``words``, ``all``, ``mapM``, the ``!!`` operator
* ``Control.Monad``: ``guard``
* ``Data.Char``: ``isDigit``
* ``Data.List``: ``intercalate``, ``unwords``
* ``Data.Maybe``: ``mapMaybe``
* ``System.IO``: ``stdin``, ``hGetContents``

You won’t necessarily need to use all of these functions, but many of
them should be useful. Note particularly the ``read`` function, which
can parse arbitrary datatypes (as long as they are instance of the
``Read`` type class, which we haven’t discussed in class). For instance,
you can parse an integer this way (in ``ghci`` for illustration):

.. code-block:: haskell

   ghci> read "1286" :: Integer
   1286

Normally you don’t need to put the ``:: Integer`` into the code, because
the context requires the result to be of a particular type (here, an
``Integer``).


Writing and compiling the program
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

See the last problem for the coding guidelines.
Compile the program like this:

.. code-block:: text

   $ ghc -W -o columns Columns.hs -package base

