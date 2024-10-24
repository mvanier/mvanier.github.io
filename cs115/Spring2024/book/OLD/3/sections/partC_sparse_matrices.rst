Part C: Miniproject: Sparse Matrices
====================================

In this section, you will be implementing a Haskell module which
includes datatypes and functions to represent and compute with sparse
matrices. A sparse matrix is a matrix which contains a large number of
zero elements, which are not represented explicitly. Typically, the vast
majority of the elements in the matrix are zeros. Sparse matrices arise
naturally in a wide variety of applications.

Because of the large number of zero elements, computing with sparse
matrices requires different algorithms than with non-sparse ("dense")
matrices, or else the resulting code will be very inefficient. (Note
that here we use the term "matrix" to include not only square matrices
but matrices with any number of rows or columns.) In this module, you
will be implementing sparse matrix addition, subtraction,
multiplication, and negation. **While doing so, it is very important
that you take advantage of the sparseness of the matrices in your
implementation.** If you implement any of the functions by treating
sparse matrices as dense matrices, the code will be considered invalid
and you will get no credit on the problem!

In addition to the challenge of implementing efficient functions for
computing with sparse matrices, this miniproject has some other goals.
You will learn to work with some of the data structures from the Haskell
libraries, and you will learn to use Hoogle_ to search through the
APIs. The total amount of code you need to write for this section is not
large; our code is about 100 lines of non-comment code. (Note that we do
expect you to write a reasonable amount of comments in your code
explaining what your functions do.)


Sparse matrix representation
----------------------------

A particular element of each sparse matrix is identified by its row
index and its column index. Row and column indices start at 1 (not 0!).
Therefore, the basic representation for sparse matrices will be a map
between pairs of ``Integer``\s (don’t use ``Int``\s) and values, where
the integers must be at least 1 and values can be any Haskell type which
instantiates the ``Num`` type class. To represent the map you will use
the ``Data.Map`` module and the ``Map`` datatype. Browse through the
Hoogle_ documentation on ``Data.Map`` to learn how to use ``Map``\s.
Note that you don’t have to know how ``Map``\s are *implemented*,
though you can browse the source code if you’re curious; what’s
important here is that you know how to use them.

.. warning::

   If you use 0-based indexing you will get at most half marks on this
   section of the assignment.

.. note::

   Don’t confuse the ``Map`` type (which is a container type similar to
   a Python dictionary, but immutable) to the ``map`` higher-order
   function. There is no connection between them.

In addition to the map between pairs of ``Integer``\s and values, we
need some extra bookkeeping information in each sparse matrix.
Specifically, we have to store:

* The bounds of the matrix (number of rows and columns), as a pair of
  ``Integer``\s.

* A set of row indices, representing all rows that have nonzero
  elements.

* A set of column indices, representing all columns that have nonzero
  elements.

The row and column indices are stored to allow us to skip rows and
columns that contain only zeros. This is particularly important when
implementing multiplication. To implement sets, use the ``Set`` datatype
contained in the ``Data.Set`` module. Again, see Hoogle_ for details.

Here is the definition of the sparse matrix datatype you should use:

.. code-block:: haskell

   import qualified Data.Map as M
   import qualified Data.Set as S

   data SparseMatrix a =
     SM { bounds     :: (Integer, Integer),  -- number of rows, columns
          rowIndices :: S.Set Integer,       -- row indices with nonzeros
          colIndices :: S.Set Integer,       -- column indices with nonzeros
          vals       :: (M.Map (Integer, Integer) a) }  -- values
     deriving (Eq, Show)

Note that this is a polymorphic type where the type ``a`` must be an
instance of the ``Num`` type class. (We don’t include that as a datatype
context because Haskell doesn’t allow adding contexts to datatypes by
default, so this must be enforced by functions that act on the
datatype.) **You are not allowed to choose a different sparse matrix
representation.**

The ``import qualified ... as ...`` lines indicate that you should
import the indicated modules and give a one-letter prefix to names in
those modules. (It doesn’t have to be one-letter, but that’s what we’re
doing here.) So the ``Set`` datatype in ``Data.Set`` becomes ``S.Set``
instead of ``Data.Set.Set``, and the ``Map`` datatype in ``Data.Map``
becomes ``M.Map`` instead of ``Data.Map.Map``. This allows you to keep
different namespaces separate without having to do too much extra
typing. This is important here because the ``Set`` and ``Map`` modules
contain a number of functions with the same names.

One important point about the data representation is that after any
sparse matrix operation (including the function to construct a sparse
matrix!), you must make sure that:

* there are no zeros stored in the matrix (|ie| in the ``vals`` map)

* the ``rowIndices`` and ``colIndices`` fields have been updated so
  that they contain all (and only!) the rows/columns with nonzero
  elements.


Structure of the code
---------------------

The Haskell code for the sparse matrix module will be called
``SparseMatrix.hs`` and will implement the ``SparseMatrix`` module. The
first few lines will be as follows (add comments as you see fit):

.. code-block:: haskell

   module SparseMatrix where

   import qualified Data.Map as M
   import qualified Data.Set as S

   data SparseMatrix a =
     SM { bounds     :: (Integer, Integer),  -- number of rows, columns
          rowIndices :: S.Set Integer,       -- row indices with nonzeros
          colIndices :: S.Set Integer,       -- column indices with nonzeros
          vals       :: (M.Map (Integer, Integer) a) }  -- values
     deriving (Eq, Show)

Following this, write the code for your function/operator definitions,
along with any helper functions you may want to write.


Useful library functions
------------------------

Here are the names of some library functions you may find useful. Look
them up in Hoogle_ to find out how they work. You aren’t required to
use them if you don’t need to. You may also find other functions in
these modules to be useful; use whichever ones you like. Don’t use other
modules.


From ``Data.Map``
~~~~~~~~~~~~~~~~~

* ``elems``
* ``filter``
* ``filterWithKey``
* ``findWithDefault``
* ``foldr``
* ``fromList``
* ``intersectionWith``
* ``keys``
* ``lookup``
* ``map``
* ``mapKeys``
* ``toList``
* ``unionWith``

Note that the functions called ``map`` and ``filter`` in this module are
not the same as those called ``map`` and ``filter`` in the Prelude (|ie|
that work on lists). This is another reason why we use
``import qualified`` when importing this module.


From ``Data.Set``
~~~~~~~~~~~~~~~~~

* ``fromList``
* ``toList``

Note that these two functions are not the same as the functions with the
same name from the ``Data.Map`` module. (This is why we use
``import qualified`` when importing these modules.)

.. warning::

   Avoid converting ``Set``\s to or from lists unless you have to.
   (For one thing, this can make your functions much less efficient.)
   If a ``Set`` function can do the same thing as a similar list function,
   use the ``Set`` version.

   Similarly, avoid converting ``Map``\s to or from lists unless you have to.
   Check to see if one of the ``Map`` functions referred to above
   can do what you need before doing a conversion to lists.


From ``Data.List``
~~~~~~~~~~~~~~~~~~

* ``all``

Here are the functions you have to implement.


1. ``sparseMatrix``
-------------------

[**2 marks**]

Implement a function called ``sparseMatrix`` which creates a sparse
matrix from a list of index/value pairs and the array bounds.

The type signature of this function will be:

.. code-block:: haskell

   sparseMatrix :: (Eq a, Num a) =>
     [((Integer, Integer), a)] -> (Integer, Integer) -> SparseMatrix a
   -- sparseMatrix <list of index/element pairs> <bounds> -> sparse matrix

Note that the indices in the index/value pairs are themselves pairs of
integers. The function must check that the given bounds are valid (|ie|
at least 1 each), and that all the index pairs given are within those
bounds. If not, an error must be signalled.

The function must not put zero values into the sparse matrix, even if
some of the values in the input index/value pair list are zeros.
Similarly, the function must store the row indices and column indices of
only the rows/columns that have nonzero values.

You can assume that the list of index/value pairs will not have
duplicated indices. If there are duplicated indices, you can do whatever
is most convenient.

.. note::

   The number ``0`` is a valid value of any type that is an instance of
   the ``Num`` type class.


2. ``addSM``
------------

[**1 mark**]

Implement a function called ``addSM`` which adds two compatible sparse
matrices.

The type signature of this function will be:

.. code-block:: haskell

   addSM :: (Eq a, Num a) => SparseMatrix a -> SparseMatrix a -> SparseMatrix a

If the two sparse matrices cannot be added (|ie| they don’t have the
same number of rows or columns) an error must be signalled. Be sure to
adjust the ``rowIndices`` and ``colIndices`` after adding, and make sure
that there are no zero elements in the matrix after adding.


3. ``negateSM``
---------------

[**0.5 marks**]

Implement a function called ``negateSM`` which negates a sparse matrix.


4. ``subSM``
------------

[**0.5 marks**]

Implement a function called ``subSM`` which subtracts two compatible
sparse matrices.

.. tip::

   This function can trivially be defined in terms of the previous two.


5. ``mulSM``
------------

[**3 marks**]

Implement a function called ``mulSM`` which multiplies two compatible sparse
matrices.

Recall that for two matrices to be compatible for multiplication, the
number of columns in the first matrix must equal the number of rows in
the second matrix. Your function must check for this and signal an error
if the two matrices cannot be multiplied.

.. warning::

   This function is by far the trickiest one to implement of all the
   sparse matrix functions in this miniproject. We recommend that you
   write a helper function to multiply a row of one matrix (a row
   vector) by a column of the other (a column vector), and use that to
   construct the product matrix. Use the ``rowIndices`` and
   ``colIndices`` fields of the matrices to avoid multiplying empty rows
   or columns.

   As always, make sure that none of the stored entries of the sparse
   matrix are zeros.


6. Accessors
------------

[**1 mark**]

Define three accessor functions for sparse matrices: ``getSM``, which
retrieves a value from a sparse matrix given the row and column,
``rowsSM``, which returns the number of rows in a sparse matrix, and
``colsSM``, which returns the number of columns in a sparse matrix.

.. note::

   ``rowsSM``/``colsSM`` return the *total* number of rows/columns in
   the sparse matrix, *not* the number of nonzero rows/columns.

Here are the type signatures:

.. code-block:: haskell

   getSM :: Num a => SparseMatrix a -> (Integer, Integer) -> a
   rowsSM :: Num a => SparseMatrix a -> Integer
   colsSM :: Num a => SparseMatrix a -> Integer

``getSM`` retrieves the value at the index represented by the pair of
integers. If this row/column location is invalid (out of bounds of the
matrix), signal an error. If it’s in bounds but there is no value stored
in the matrix at that location, return a zero.


7. Operators
------------

[**1 mark**]

Define operator shortcuts for the ``addSM``, ``subSM``, ``mulSM``, and
``getSM`` functions defined previously.

Use ``<|+|>`` as the operator for ``addSM``, ``<|-|>`` for ``subSM``,
``<|*|>`` for ``mulSM`` and ``<!>`` for ``getSM``. The type signatures
of these operators are identical to those of the corresponding functions
(but you should write them out anyway).


8. ``Num`` instance of ``SparseMatrix``?
----------------------------------------

[**1 mark**]

Why doesn’t it make sense to define the ``SparseMatrix`` datatype as an
instance of the ``Num`` type class?  Write your answer in a comment.


Things to watch out for
-----------------------

By far the biggest pitfall when writing sparse matrix code is writing
code that inadvertently breaks the sparse matrix invariants |ie| where
explicit zero elements are stored in the sparse matrix, or where the
row/column indices stored are invalid (|eg| by containing indices for
rows with no nonzero elements). Pay special attention to this when
writing the code, especially the ``sparseMatrix`` function and the
addition and multiplication functions.

.. tip::

   Don’t forget to run the test suite on your code (see above).

