Part B: Pitfalls
================

In this section we’ll see some examples of bad code and ask you to fix
them. By "bad code" we don’t mean "the code doesn’t work" but rather
"the code isn’t optimal in some way".  By "optimal" we don't necessarily
always mean "inefficient"; there are other kinds of non-optimality too.

Write both the corrected code and (in a Haskell comment) the reason why the
original code is bad.


1. ``sumList``
--------------

The following recursive definition has poor Haskell style. How would you
improve the style by making a simple change (|ie| keep it a recursive
definition, but with better style)?

.. code-block:: haskell

   sumList :: [Integer] -> Integer
   sumList [] = 0
   sumList lst = head lst + sumList (tail lst)


2. ``largest``
--------------

What is wrong with this recursive definition? How would you fix it,
while keeping it a recursive definition? (The function works, but it is
still not "good" in some sense.)

.. code-block:: haskell

   -- Return the largest value in a list of integers.
   largest :: [Integer] -> Integer
   largest xs | length xs == 0 = error "empty list"
   largest xs | length xs == 1 = head xs
   largest xs = max (head xs) (largest (tail xs))
