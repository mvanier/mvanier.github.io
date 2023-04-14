Part A: Exercises
=================

1. Defining new operators
-------------------------

Haskell lets you define your own operators. Here are a couple of simple
examples. (See lecture 2 for how to define new operators.)

a. ``+*`` (sum of squares)
~~~~~~~~~~~~~~~~~~~~~~~~~~

Write a definition for an operator called ``+*`` that computes the sum
of the squares of its arguments. Assume both arguments are
``Double``\ s. Make it left-associative and give it a precedence of 7.

.. note::

   Define this operator directly -- don’t just define a function and make
   the operator another name for the function. This applies for the next
   problem as well.

b. ``^||`` (exclusive-or)
~~~~~~~~~~~~~~~~~~~~~~~~~

Write a definition for an operator called ``^||`` that computes the
exclusive-OR of its two (boolean) arguments. Make it right-associative
with a precedence of 3. Your definition should be only two (very simple)
lines (not counting the type declaration) and shouldn’t use ``if``.

.. hint::

   Use pattern matching; if the first argument is ``False``,
   what must the answer be?


2. ``rangeProduct``
-------------------

Write a recursive function called ``rangeProduct`` that takes two
``Integers`` and computes the product of all the integers in the range
from one integer to the other (inclusive). For instance:

.. code-block:: haskell

  rangeProduct 10 20
  {-
  --> 10 * 11 * 12 * 13 * 14 * 15 * 16 * 17 * 18 * 19 * 20
  --> 6704425728000
  -}

If the first argument is greater than the second argument, signal an
error using the ``error`` function. If the two arguments are the same,
return the argument value (so ``rangeProduct 42 42`` is just ``42``).
Use pattern guards instead of explicit ``if`` expressions to test the
cases. Make sure all cases are exhaustive!


3. ``prod``
-----------

Use ``foldr`` to define a one-line point-free function called ``prod``
that returns the product of all the numbers in a list of ``Integer``\ s
(or ``1`` if the list is empty). Use this function to define a one-line
(not including the type signature) **non-recursive** definition for
``rangeProduct``, which you should call ``rangeProduct2``. (This will be
a two-line definition if you also include the line containing
``error``.)


4. ``map`` and variations
-------------------------

You learned about the ``map`` higher-order function in class. Here you
will write some variations on ``map``.


a. ``map2``
~~~~~~~~~~~

First, write a function ``map2`` which maps a two-argument function over
two lists. Write it as a recursive function (there is a trivial
definition using ``zipWith`` which you shouldn’t use). It could be used
like this:

.. code-block:: haskell

   ghci> map2 (*) [1, 2, 3] [4, 5, 6]
   [4,10,18]

Your solution should have a completely polymorphic type signature (|ie|
the most general type signature). Extra elements in either list should
be ignored.


b. ``map3``
~~~~~~~~~~~

Now, write a ``map3`` function that will work for functions of 3
arguments. For instance:

.. code-block:: haskell

   ghci> map3 (\x y z -> x + y + z) [1, 2, 3] [4, 5, 6] [7, 8, 9]
   [12,15,18]


5. Dot product
--------------

It’s easy to use ``map2`` along with the ``sum`` function to define a
function to compute the dot product of two lists of integers, as
follows:

.. code-block:: haskell

   dot :: [Integer] -> [Integer] -> Integer
   dot lst1 lst2 = sum (map2 (*) lst1 lst2)

or we can save some parentheses by writing it as:

.. code-block:: haskell

   dot :: [Integer] -> [Integer] -> Integer
   dot lst1 lst2 = sum $ map2 (*) lst1 lst2

You might think that you can write this as a point-free version using
the ``(.)`` function composition operator as follows:

.. code-block:: haskell

   dot :: [Integer] -> [Integer] -> Integer
   dot = sum . map2 (*)

However, this does not type check! The correct point-free version is:

.. code-block:: haskell

   dot :: [Integer] -> [Integer] -> Integer
   dot = (sum .) . map2 (*)

Write out a short (less than 10 lines) evaluation showing that
``dot lst1 lst2`` using the point-free definition is equivalent to the
explicit (point-wise) definition given above. Note that ``(sum .)`` is
an operator section equivalent to ``\x -> sum . x``.

.. hint::

   Don’t forget about currying!

.. note::

   Fun fact: there’s an obscure Haskell library called
   ``Data.Composition`` that contains a composition operator called
   ``.:`` which can compose a unary function with a binary one. Using
   this operator, ``dot`` can be written as:

   .. code-block:: haskell

      dot = sum .: map2 (*)

   The definition is simply:

   .. code-block:: haskell

      (.:) :: (c -> d) -> (a -> b -> c) -> a -> b -> d
      (f .: g) x y = f (g x y)

   The moral of the story is: if you think Haskell’s ``.`` composition
   operator isn’t good enough for a particular task, you can always
   define a new composition operator!


6. Fun with list comprehensions
-------------------------------

Using a list comprehension, write a one-line expression which computes
the sum of the natural numbers below one thousand which are multiples of
3 or 5. Write the result in a comment. You may use the Haskell ``sum``
function to do the actual sum.


7. Sum of primes
----------------

Calculate the sum of all the prime numbers below 10000.

Do this as follows:

#. First, generate an infinite list of prime numbers using the "Sieve of
   Eratosthenes" algorithm. This consists of generating all positive
   integers and removing all multiples of successive prime numbers.
   Write a function called ``sieve`` which takes a list of
   integers, retains the first one, removes all multiples of the first
   one from the rest, and then sieves the rest. Name the infinite list
   of primes ``primes``. Note that ``sieve`` is a two-line definition
   (not including type declaration) and ``primes`` is trivial given
   ``sieve``. Make sure your code doesn’t generate any compiler
   warnings.

#. Then, use the ``takeWhile`` function on the primes list to take the
   appropriate prime numbers and compute their sum.

#. Write the answer as a Haskell comment.


8. Balanced parentheses
-----------------------

For this problem we are going to write functions that take in a string
and return a boolean value. This returned value will be ``True`` if the
parentheses in the string are "balanced" and ``False`` otherwise. By
"balanced" we mean:

* there are an equal number of open and close parentheses (they pair
  up),

* and a close parenthesis never occurs before its matching open
  parenthesis.

Here are some strings that have balanced parentheses:

.. code-block:: haskell

   "",
   "foo",
   "()",
   "(foo)",
   "()()()",
   "(()())",
   "(((())))",
   "(((()())()))"

Here are some strings with unbalanced parentheses:

.. code-block:: haskell

   "("
   ")"
   ")("
   ")a("
   "()()("
   "(()))(()"

Note that characters other than ``'('`` or ``')'`` are irrelevant to
determining if parentheses are balanced or not.

You have to write three different functions to determine if a string has
balanced parentheses. We will use this to contrast writing a function
using explicit recursion *vs.* using higher-order functions.

.. note::

   Remember that strings in Haskell are (by default) just lists of
   characters |ie| lists of ``Char`` values.
   So you can use list functions on strings as you would on any list.


a. ``balancedParentheses``
~~~~~~~~~~~~~~~~~~~~~~~~~~

Write a function called ``balancedParentheses`` that takes a ``String``
and returns a ``Bool`` which is ``True`` if the string has balanced
parentheses or ``False`` otherwise. Use a recursive helper function
which traverses the string one character at a time and keeps a running
count of the "parenthesis imbalance". This can be done by starting the
count at zero, adding 1 every time a left parenthesis is encountered,
and subtracting 1 every time a right parenthesis is encountered. At the
end of the string, the imbalance count will be 0 if the string has
balanced parentheses. In addition, the count should never go negative;
if it does, the string doesn’t have balanced parentheses
(there are too many close parentheses at that point in the string),
so ``False`` should immediately be returned.

Put the helper function in a ``where`` clause of the
``balancedParentheses`` function.


b. ``balancedParentheses2``
~~~~~~~~~~~~~~~~~~~~~~~~~~~

In general, Haskell programmers prefer to use higher-order functions
instead of writing explicitly recursive functions. Rewrite
``balancedParentheses`` into a new version called
``balancedParentheses2`` which is *not* recursive but which uses the
``foldl`` (left fold) higher-order function. (Don’t use ``foldr``, even
though you could make it work that way.) The function being folded has
the type ``Int -> Char -> Int``. It takes the existing imbalance count
at some location in the string and the current character in the string,
and returns the new count (1 higher if the character is a ``'('``, 1
lower if it’s a ``')'``, or the same otherwise). The fold as a whole
will return the final imbalance count of the string; if it’s 0, the
string has balanced parentheses.

Some hints and advice:

#. This function should be shorter than ``balancedParentheses``.

#. If the imbalance count ever goes negative, it should stay negative
   through the entire fold. Basically, once the count is negative, then
   the string has unbalanced parentheses, and you can just propagate the
   negative value through to the end of the fold. This can be done
   simply by writing the folded function a particular way. If the fold
   returns a negative number you know that the string has unbalanced
   parentheses.

#. We’ve seen in class that some functions in Haskell can lead to large
   expressions being generated because of Haskell’s lazy evaluation
   rule. Careless use of ``foldl`` can easily do this, and this function
   is no exception. To avoid a "space leak" (where a function that
   should run in constant space instead runs in |eg| O(N) space),
   use ``foldl'`` (called "fold-ell-prime") instead of ``foldl``,
   which makes the function being folded evaluate strictly.  [1]_ Other
   than the single ``'`` character, ``foldl'`` is used the exact same
   way as ``foldl``.

#. In order to use ``foldl'``, you will need to import the ``Data.List``
   library. Put this line at the top of your code (after the
   ``module Lab1 where`` line):

   .. code-block:: haskell 

      import Data.List

   .. note::

      Even using ``foldl'`` may not be enough to guarantee strict
      evaluation. There are more tricks for ensuring strict evaluation
      that we will teach you later in the course, but don’t worry too
      much about this now.

c. ``balancedParentheses3``
~~~~~~~~~~~~~~~~~~~~~~~~~~~

One problem with ``balancedParentheses2`` is that using ``foldl'``
doesn’t exit early if the imbalance count goes negative. This means that
``balancedParentheses2`` is less efficient than ``balancedParentheses``.
It seems a shame that converting from a crude recursive definition to an
elegant definition using higher-order functions costs us in efficiency.
It would be nice to have the best of both worlds.

Write a function called ``balancedParentheses3`` which solves the
balanced parentheses problem using this algorithm:

#. Write a function called ``ctoi`` which converts ``Char`` values
   (characters) to ``Int`` values. This should return ``1`` if the input
   character is ``'('``, ``-1`` if it’s ``')'``, and ``0`` otherwise.

#. Use ``map`` to convert each character in the string to an ``Int``
   using ``ctoi``. This will return a list of ``Int``\ s.

#. The sum of all the ``Int`` values will be ``0`` for a string with
   balanced parentheses. However, we also want to make sure that for
   each prefix of the string (|ie| each string that starts on the first
   character of the original string and ends before the last character),
   the imbalance count isn’t negative (which would imply unbalanced
   parentheses for the entire string). For this, we can use the
   ``scanl`` function. ``scanl`` works a lot like ``foldl`` but collects
   up the results for all prefixes of the list (string) argument. For
   instance:

   .. code-block:: haskell

      foldl (+) 0 [1..5] --> 0 + 1 + 2 + 3 + 4 + 5 = 15
      scanl (+) 0 [1..5]
        --> [0, 0 + 1, 0 + 1 + 2, 0 + 1 + 2 + 3, 0 + 1 + 2 + 3 + 4, 0 + 1 + 2 + 3 + 4 + 5]
        --> [0, 1, 3, 6, 10, 15]

   Using ``scanl`` on the results of the ``map`` gives us the list of
   partial imbalance counts of the string. (To avoid problems with
   laziness, use the strict version ``scanl'`` instead of
   just ``scanl`` itself.)

#. Now that we have a list of the imbalance counts at each character, we
   can test if this corresponds to a string with balanced parentheses.
   If so, either the list is empty (corresponding to an empty string),
   or its last element is ``0`` and none of its elements are negative.
   Write a function called ``test`` that takes a list of ``Int``\ s and
   returns a ``Bool`` which is ``True`` if the list has the desired
   properties. (The ``all`` and ``last`` functions will probably be
   useful to you; look them up in Hoogle_.) You can write this as a
   recursive function if you must, but try not to.
   (*Hint*: use ``all`` and ``last``.)  Also, the function ``null``
   returns ``True`` if its argument is an empty list.

#. Assemble the ``test``, ``scanl'``, and ``map`` functions in a
   (short!) single line to write the ``balancedParentheses3`` function.
   Put the ``test`` and ``ctoi`` functions as helper functions in a
   ``where`` clause. (They can be more than one line long.)

If you do this right, you will have an elegant function to compute
balanced parentheses which will quit at the first character which
guarantees unbalanced parentheses.

.. note::

   Haskell’s lazy evaluation is key to understanding why this function
   will exit when the imbalance count first goes negative. Basically,
   the ``scanl'`` call will generate one prefix count at a time, and the
   ``test`` function will look at each of these and return ``False``
   immediately if a negative count is ever seen.

   Also, the fact that you use ``scanl'`` instead of ``scanl`` *doesn’t*
   mean that the entire list of partial counts will be generated
   strictly (this would ruin the efficiency of the function). It just
   means that the function which is being folded over the list is
   applied strictly, which is what you want.

   If this seems like a lot of mental effort to write a simple function,
   be aware that this is the nature of Haskell: you write much less
   code, but you think more about the code you do write. (Some people
   like this trade off, and some don’t.) The good news is that the more
   you do it, the less thinking it will require.

----

.. rubric:: Footnotes

.. [1] Technically, it makes it evaluate *more* strictly,
   as we will see later in the course.
