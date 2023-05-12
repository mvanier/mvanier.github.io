
Part A: The List Monad
======================

In this section you will do some exercises involving the list monad.


1. The Hardy-Ramanujan problem
------------------------------

[**2 marks**]

A classic problem in number theory
is to find positive integers
that can be expressed as the sum of two cubes in two different ways.
This problem dates back to the mathematicians Hardy and Ramanujan;
supposedly Ramanujan could tell at a glance
that 1729 was the smallest such integer.

Ben Bitfiddle has written a very elegant list comprehension
to generate all these numbers (up to infinity!):

.. code-block:: haskell

   hr_solutions :: [((Integer, Integer), (Integer, Integer), Integer)]
   hr_solutions =
     [((i, l), (j, k), i^3 + l^3) |
      i <- [1..],
      j <- [1..i-1],
      k <- [1..j-1],
      l <- [1..k-1],
      i^3 + l^3 == j^3 + k^3]

   -- Recall that (^) is the integer power operator.

Here’s an example of a run in ``ghci``:

.. code-block:: haskell

  ghci> take 5 hr_solutions
  [((12,1),(10,9),1729),((16,2),(15,9),4104),((24,2),(20,18),13832),((27,10),(24,19),20683),((32,4),(30,18),32832)]

Rewrite Ben’s code using the list monad instead of a list comprehension.
Use the ``do``-notation and the ``guard`` function
(in the module ``Control.Monad``) where appropriate.


2. Blast from the past
----------------------

[**2 marks**]

Redo problem A.6 of assignment 1 ("Fun with list comprehensions")
using the list monad instead of a list comprehension.
Write two different solutions:
one using the ``guard`` function
and one which doesn’t use ``guard``
but which does use ``mzero``
from the ``MonadPlus`` instance for lists
to filter out undesired values.
Don’t use the ``filter`` function.

The problem is:

.. pull-quote::

   Write an expression which computes the sum of the
   natural numbers below one thousand which are multiples of 3 or 5.

Your expression doesn’t have to fit on one line this time.


3. Project Euler problem #4
---------------------------

[**2 marks**]

Solve `Euler problem 4`_ using the list monad.

.. _Euler problem 4: https://projecteuler.net/index.php?section=problems&id=4

The problem description is as follows:

.. pull-quote::

   A palindromic number reads the same both ways. The largest palindrome
   made from the product of two 2-digit numbers is 9009 = 91 \* 99. Find
   the largest palindrome made from the product of two 3-digit numbers.

Write a one-line function called ``isPalindrome`` which takes an
``Integer`` and returns ``True`` if the integer’s decimal representation
is a palindrome. (*Hint:* Convert the integer to a string; note that
``Integer`` is an instance of the ``Show`` type class.) Then write a
function called ``largestPalindrome`` which computes the desired
quantity. Do not use a list comprehension or recursion, but do use the
list monad. The ``maximum`` function from the Haskell Prelude will
probably be useful to you.

Try to make your solution as efficient as possible. For instance, do you
need to multiply all pairs of three-digit numbers?

Write the solution to the problem in a comment.


4. A combinatorial puzzle
-------------------------

[**4 marks**]

.. NOTE: This is reminiscent of the "countdown problem" in
   Graham Hutton's work.

The list monad is a great tool for solving combinatorial problems.
Consider this puzzle:

.. pull-quote::

   Take the digits 1 to 9 in sequence. Put either a "+", a "-", or
   nothing between each digit to get an arithmetic expression. Print out
   all such expressions that evaluate to 100.

In this problem we will use the list monad to solve this problem.

We will start by defining some datatypes to represent expressions.

.. code-block:: haskell

   type Expr = [Item]

   data Item = N Int | O Op
     deriving Show

   data Op = Add | Sub | Cat
     deriving Show

In words: an expression is a list of items, an item is either a number
or an operator, and an operator is either addition, subtraction, or
concatenation. Note that these datatypes don’t enforce any reasonable
invariants; you can have an "expression" which is just operators, for
instance. We could design more clever datatypes to enforce such
invariants, but we won’t for simplicity. However, that means we’ll have
to detect invalid cases in our functions.

We’ll also define a list of all operators, for convenience:

.. code-block:: haskell

   ops :: [Item]
   ops = [O Add, O Sub, O Cat]

Here are the functions/values we want you to define.


a. ``exprs``
~~~~~~~~~~~~

Define a value called ``exprs`` which consists of a list of all possible
valid expressions from the puzzle description |ie| all possible
combinations of the digits 1 to 9 (in order) with one of the operators
from the ``Op`` datatype between each digit. **Use the list monad.**
(This is not optional.) Your function shouldn’t be more than about a
dozen lines long. There should be exactly ``3^8`` or ``6561`` possible
expressions in the list. (Please don’t write a brute-force expression
that is 6561 lines long! That would take way longer than solving the
problem would.)

.. note::

   The numbers can only be one thing at any position in the expression,
   but between each pair of adjacent numbers you can have any one of
   three operators.


b. ``normalize``
~~~~~~~~~~~~~~~~

Define a function called ``normalize`` that takes an expression and
removes all instances of the ``Cat`` operator by applying this
transformation: ``N i, Cat, N j --> N (ij)`` anywhere in the list.
So, for instance, ``N 1, Cat, N 2`` would become ``N 12`` (the digits
get concatenated). (Note that digits are represented as ``Int``\s, not
``String``\s, so you can’t use string concatenation.) This should work
for longer stretches of ``Cat``\s; for instance:
``N 1, Cat, N 2, Cat, N 3`` becomes ``N 123``. Anything that doesn’t
match the patterns described is just passed through unchanged, except
that illegitimate patterns (|eg| multiple operators/numbers in a row or
expressions that begin or end with operators) give rise to errors. This
function has the type ``Expr -> Expr`` and our implementation is five
lines long.

.. tip::

   Pattern match all valid subexpressions, and anything else becomes an
   error.


c. ``evaluate``
~~~~~~~~~~~~~~~

Define a function called ``evaluate`` that will take a normalized
expression (|ie| one with no ``Cat`` operators) and evaluate it to give
an ``Int``. Again, this function should only be a few lines long. One
thing to be careful about is that subtraction associates to the left, so
``a - b - c`` is ``(a - b) - c``, not ``a - (b - c)``. Therefore, start
evaluating from the beginning of the expression towards the end, not the
other way around.

.. note::

   Your ``evaluate`` function should signal an error if it is asked to
   evaluate an expression containing a ``Cat`` operator.

We are also giving you the functions ``find``, ``pprint`` and ``run``
which are defined as follows:

.. code-block:: haskell

   -- Pick out the expressions that evaluate to a particular number.
   find :: Int -> [Expr] -> [Expr]
   find n = filter (\e -> evaluate (normalize e) == n)

   -- Pretty-print an expression.
   pprint :: Expr -> String
   pprint [N i] = show i
   pprint (N i : O Add : es) = show i ++ " + " ++ pprint es
   pprint (N i : O Sub : es) = show i ++ " - " ++ pprint es
   pprint (N i : O Cat : es) = show i ++ pprint es
   pprint _ = error "pprint: invalid argument"

   -- Run the computation and print out the answers.
   run :: IO ()
   run = mapM_ putStrLn $ map pprint $ find 100 exprs

You should include these in your submitted code for testing purposes.

You can run your code for this problem by loading it into ``ghci`` and
typing ``run`` at the prompt. Your output should look something like
this (the vertical order may be different):

.. code-block:: text

   1 + 2 + 3 - 4 + 5 + 6 + 78 + 9
   1 + 2 + 34 - 5 + 67 - 8 + 9
   1 + 23 - 4 + 5 + 6 + 78 - 9
   1 + 23 - 4 + 56 + 7 + 8 + 9
   12 + 3 + 4 + 5 - 6 - 7 + 89
   12 + 3 - 4 + 5 + 67 + 8 + 9
   12 - 3 - 4 + 5 - 6 + 7 + 89
   123 + 4 - 5 + 67 - 89
   123 + 45 - 67 + 8 - 9
   123 - 4 - 5 - 6 - 7 + 8 - 9
   123 - 45 - 67 + 89

You can check that all of these expressions evaluate to 100.

|hline|


