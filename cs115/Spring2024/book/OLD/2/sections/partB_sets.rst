Part B: Implementing sets using red-black trees
===============================================

In this section, you will use the red-black tree implementation you
wrote in the last section to build a reasonably featureful set
implementation. All code for this section will still go into the file
``RedBlackTree.hs``. Put it after the code for the red-black trees.
(This isn’t necessary for it to work, but it will make the code easier
to read and grade.)

If you don’t know much about sets,
the `Wikipedia article <https://en.wikipedia.org/wiki/Set_(mathematics)>`_
contains everything you need to know for this section.  [1]_

This section of your code should start like this:

.. code-block:: haskell

   -- We define Set as a type synonym for Tree.
   type Set a = Tree a

   -- Empty set.
   empty :: Set a
   empty = Leaf

   -- Convert a list to a set.
   toSet :: Ord a => [a] -> Set a
   toSet = fromList

From this, we see that a ``Set`` is nothing more than an alias for a
red-black tree. The empty set is just a ``Leaf``, and the function
``toSet`` for converting a list to a set is just the ``fromList``
function on red-black trees.

Now write the following set functions, using the red-black tree
functions you defined above as needed. Every one of them can be written
in a single line of code, so you will be penalized if your functions are
much longer than this (pay attention to the hints!).

.. warning::

   Don't write any of these functions
   by converting both set arguments to lists
   and then just using list operations.
   (Doing that will get you no marks for the function.)
   Also, don't import the ``Data.List`` module.

   For each problem in this section,
   we will allow you to convert *one* of the set arguments to a list
   using ``toList`` if you need to (which you may not).


1. ``isSubset``
---------------

Write the ``isSubset`` function, which has this type signature:

.. code-block:: haskell

   isSubset :: Ord a => Set a -> Set a -> Bool

This function takes two sets as its arguments and returns ``True`` if
the first one is a subset of the second (it doesn’t have to be a proper
subset)

.. hint::

   Look up the ``all`` function on Hoogle_.


2. ``eqSet``
------------

Write the ``eqSet`` function, which has this type signature:

.. code-block:: haskell

   eqSet :: Ord a => Set a -> Set a -> Bool

This function takes two sets as its arguments and returns ``True`` if
the two sets are equal (|ie| if they have the same elements).

.. hint::

   Define this in terms of ``isSubset``.


3. ``union``
------------

Write the ``union`` function, which has this type signature:

.. code-block:: haskell

   union :: Ord a => Set a -> Set a -> Set a

This function takes two sets as its arguments and returns a new set
which is the union of the input sets.

.. hint::

   Convert one of the set arguments to a list and use ``foldr``.
   If it’s useful, write a skeleton for the function argument of ``foldr``
   as ``(\x r -> ...)``,
   where ``x`` is the current element of a list
   and ``r`` is the rest of the list after processing by ``foldr``.


4. ``intersection``
-------------------

Write the ``intersection`` function, which has this type signature:

.. code-block:: haskell

   intersection :: Ord a => Set a -> Set a -> Set a

This function takes two sets as its arguments and returns a new set
which is the intersection of the input sets. Fill in the following
skeleton code to get the solution:

.. code-block:: haskell

   intersection :: Ord a => Set a -> Set a -> Set a
   intersection s1 s2 = foldr (\x r -> if ... then ... else ...) ... (toList s2)


5. ``difference``
-----------------

Write the ``difference`` function, which has this type signature:

.. code-block:: haskell

   difference :: Ord a => Set a -> Set a -> Set a

This function takes two sets as its arguments and returns a new set
which is the set difference of the input sets, which means that it
contains all elements of the first set that are not in the second set.
Fill in the following skeleton code to get the solution:

.. code-block:: haskell

   difference :: Ord a => Set a -> Set a -> Set a
   difference s1 s2 = foldr (\x r -> if ... then ... else ...) ... (toList s1)

This is the same skeleton function we used above for ``intersection``,
and the contents of the function argument are also very similar.

----

.. rubric:: Footnotes

.. [1] Actually, it contains way more information than you need.
