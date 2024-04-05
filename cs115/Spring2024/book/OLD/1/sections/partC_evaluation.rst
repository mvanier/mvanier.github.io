Part C: Evaluation
==================

In this section, you’ll have to write out the results of evaluating some
Haskell expressions step-by-step. These evaluations should look like
this:

.. code-block:: text

   double (3 + 4)
   --> (3 + 4) + (3 + 4)
   --> 7 + (3 + 4)
   --> 7 + 7
   --> 14

with one reduction step per line. Even more explicit would be the
following:

.. code-block:: text

   double (3 + 4)
   --> (3 + 4) + (3 + 4)  [expand from definition]
   [outermost redex is + operator]
   [+ is strict, needs both operands]
   --> 7 + (3 + 4)        [evaluate leftmost branch of + operator]
   --> 7 + 7              [evaluate rightmost branch of + operator]
   [outermost redex is + operator]
   --> 14                 [evaluate + application]

though we won’t require you to do this
(but you may find it helpful to do it this way).

Pay particular attention to the fact that Haskell is a lazy language.
You may assume that the result of the expression being evaluated is
going to be printed in its entirety after evaluation. You may also
assume that primitive arithmetic expressions evaluate strictly. If the
function evaluation does not terminate, compute enough of the evaluation
to make it clear that non-termination is the outcome.

.. note::

   Since Haskell uses lazy evaluation, it will evaluate copies of one
   subexpression at the same time, so the above example should really
   be:

   .. code-block:: text

      double (3 + 4)
      --> (3 + 4) + (3 + 4)
      --> 7 + 7    -- evaluate both (3 + 4)s together
      --> 14

   However, for the evaluations here, assume that all subexpressions are
   distinct |ie| each one should be evaluated separately.

Evaluating Haskell expressions by hand should use the following
guidelines:

#. Find the outermost redex (reducible expression) first. If there is
   more than one choice (which is very common), choose the leftmost of
   the outermost redexes.

#. If the redex is inside a lambda expression (|ie| an anonymous
   function), leave it alone. Otherwise, evaluate it.

#. Continue until you get the result you need. Don’t evaluate beyond
   that point.

#. When expanding a function application using its definition, keep the
   result in parentheses until it is fully evaluated so as to make clear
   what the subexpression is.

#. Note that an expression in parentheses can still be a redex.
   "Outermost" does not refer to parentheses but to the presence of
   other language constructs. For instance, an expression inside a data
   constructor or inside a lambda expression is not at the outermost
   level.


1. Fibonacci
------------

Consider this function to compute fibonacci numbers:

.. code-block:: haskell

   fib :: Integer -> Integer
   fib 0 = 0
   fib 1 = 1
   fib n = fib (n - 1) + fib (n - 2)

Evaluate ``fib 3``.


2. Factorial
------------

Consider this definition of the factorial function:

.. code-block:: haskell

   fact :: Integer -> Integer
   fact n = n * fact (n - 1)
   fact 0 = 1

Evaluate ``fact 3``. What is wrong with this definition?
Fix the definition, and then evaluate the fixed definition too.

Pay special attention to the consequences of lazy evaluation here. Assume that
the ``*`` operator applied to ``Integer``\s evaluates its arguments strictly
from left to right.


3. ``reverse``
--------------

We talked about the ``reverse`` function in lecture 3. One definition
was as follows:

.. code-block:: haskell

   reverse :: [a] -> [a]
   reverse xs = iter xs []
     where
       iter :: [a] -> [a] -> [a]
       iter [] ys = ys
       iter (x:xs) ys = iter xs (x:ys)

Evaluate the expression ``reverse [1,2,3]`` with this definition. What
is the asymptotic time complexity of this function as a function of the
length of the input list? Write your answer in a comment. Don’t just
give the answer, explain why it’s correct.


4. ``reverse`` again
--------------------

Another definition of ``reverse`` is as follows:

.. code-block:: haskell

   reverse :: [a] -> [a]
   reverse [] = []
   reverse (x:xs) = reverse xs ++ [x]

For reference: The definition of the ``(++)`` operator is:

.. code-block:: haskell

   (++) :: [a] -> [a] -> [a]
   (++) []     ys = ys
   (++) (x:xs) ys = x : (xs ++ ys)

and the ``++`` operator is also right-associative.

Ben Bitfiddle claims that this definition of ``reverse``
has an asymptotic time complexity which is linear
in the length of the input list,
giving this argument:
"Evaluating ``reverse [1, 2, 3]`` eventually results in
``[] ++ [3] ++ [2] ++ [1]`` after a linear number of steps,
and since appending a singleton list to another list
is an ``O(1)`` operation,
constructing the result list from this point is also linear."
What is wrong with this argument?
Write out a full evaluation of ``reverse [1, 2, 3]``
and explain where Ben made his mistake and what the mistake was.
What is the actual asymptotic time complexity of this version of ``reverse``?

.. warning::

   This is tricky! We want a full explanation, not just a couple of
   sentences.

.. hint::

   * The ``++`` operator is right-associative.

   * Parentheses are your friend.


5. Insertion sort
-----------------

An "insertion sort" is a particular way to sort lists. The first item in
the list is inserted at the right place in the result of insertion
sorting the rest of the list. For lists of integers, the code might look
like this:

.. code-block:: haskell

   isort :: [Integer] -> [Integer]
   isort [] = []
   isort (n:ns) = insert n (isort ns)
     where
       insert :: Integer -> [Integer] -> [Integer]
       insert n [] = [n]
       insert n m@(m1:_) | n < m1 = n : m
       insert n (m1:ms) = m1 : insert n ms

For reference, ``head`` is defined as:

.. code-block:: haskell

   head :: [a] -> a
   head [] = error "empty list"
   head (x:_) = x

Evaluate ``head (isort [3, 1, 2, 5, 4])``.
Don’t evaluate more than you need to!


6. ``foldr`` and ``foldl``
--------------------------

We discussed the ``foldr`` (fold right) and ``foldl`` (fold left)
higher-order functions in class. Assume that their definitions are as
follows:

.. code-block:: haskell

   foldr :: (a -> b -> b) -> b -> [a] -> b
   foldr _ init [] = init
   foldr f init (x:xs) = f x (foldr f init xs)

.. code-block:: haskell

   foldl :: (a -> b -> a) -> a -> [b] -> a
   foldl _ init [] = init
   foldl f init (x:xs) = foldl f (f init x) xs

(In fact, the actual definitions of these functions in GHC are slightly
different.)

Evaluate the following expressions:

* ``foldr max 0 [1, 5, 3, -2, 4]``
* ``foldl max 0 [1, 5, 3, -2, 4]``

where ``max`` gives the maximum of two values (which you can assume to
be ``Integers`` for this problem). What can you say about the space
complexity of ``foldr`` compared to ``foldl``?
*Hint:* Don’t forget about lazy evaluation!
