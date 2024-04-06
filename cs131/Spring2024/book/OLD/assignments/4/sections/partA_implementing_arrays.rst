Part A: Implementing arrays
===========================

Implementing arrays naturally divides into several steps, which we
describe below.


Array forms
-----------

Here are the array forms you’ll be implementing:

* ``make-array``

   This takes two arguments: an integer (representing the length of the
   resulting array) and a value, and creates an array of the given
   length where each element equals the value. *Note*: if the value
   being replicated into the array is itself an array, you are not
   required to make unique copies of the value; it’s OK to just do a
   reference copy. Making unique copies is desirable but tricky. If you
   do want to attempt it, you may find the ``Array.init`` function to be
   helpful, and we suggest that you define a ``deepcopy`` function that
   computes a deep copy of a ``value``.

* ``array-at``

   This takes two arguments: an array and an integer index. It returns
   the element at the index. If the index is out of range, an
   ``IndexError`` is raised using the ``index_err`` function you will
   define below.

* ``array-put``

   This takes three arguments: an array, an integer index, and a value.
   It sets the element at the given index of the array to the value. If
   the index is out of range, an ``IndexError`` is raised using the
   ``index_err`` function you will define below.
   ``array-put`` returns a unit value (``#u``).

* ``array-size``

   This takes one argument (an array), and returns the length of the
   array.

.. note::

   We call these "forms" instead of "functions" because they are
   language constructs, even though they are effectively built-in
   functions. They can’t be defined as functions in the language because
   of the limitations of the |typedimp| type system, as we've described
   (all of these are *polymorphic* operations, which means they can act
   on more than one type).


1. Adding syntactic support for arrays
--------------------------------------

Files to edit:

* ``ast.ml``
* ``ast.mli``

You will need to modify the abstract syntax types ``imp_type`` and
``exp`` to incorporate the array forms. Here are the modifications:

* ``imp_type`` gets another constructor ``ArrayType of imp_type`` which
  represents all array types.

* ``exp`` gets new constructors
  ``ArrayMake``, ``ArrayAt``, ``ArrayPut``, and ``ArraySize``,
  corresponding to the expressions
  ``make-array``, ``array-at``, ``array-put``, and ``array-size``.
  Each constructor gets a ``loc`` field and
  however many ``exp`` fields are needed.
  Note that the non-``loc`` arguments of the form are all expressions,
  and not |eg| integers,
  even if the expression needs to evaluate to an integer.

While you’re at it, make sure you update the ``loc_of_exp`` function so
it works for array forms too. (The compiler will remind you if you
forget.)

Then you will have to modify the code that converts from S-expressions
to AST forms. Here are the steps:

* Add the names of the array forms (``make-array``, ``array-at``,
  ``array-put``, and ``array-size``) to the ``keywords`` list. Also add
  the type constructor name ``array`` to the ``reserved_ids`` list.

* Modify the ``parse_expr`` function to correctly parse all four array
  forms. Note that an array form with the wrong number of
  subexpressions should result in a syntax error. Use the other forms
  handled by the function as models.

* Modify the ``parse_type`` function to correctly parse array types
  (|eg| ``(array int)`` or ``(array (array bool))``).


2. Adding array values
----------------------

Files to edit:

* ``value.ml``
* ``value.mli``

Modify the ``value`` type by adding the array constructor:
``Array of value array``. Also modify the ``string_of_value`` function
so that it works for arrays as well. The string representation for an
array puts the array values inside a pair of square brackets. For
instance:

.. code-block:: text

   $ ./typed_imp
   >>> (make-array 10 0)
   [0 0 0 0 0 0 0 0 0 0] : (array int)

Here, the string representation of the array is
``[0 0 0 0 0 0 0 0 0 0]``. The ``(array int)`` part is the string
representation of the array type, which we’ll get to below.

Also modify the function ``equal_values`` so that it works for arrays as
well. The algorithm for array equality is simple: two arrays are equal
if they have the same length and if each of the corresponding elements
are equal in the ``equal_values`` sense. You will probably want to make
the ``equal_values`` function recursive.

.. warning::

   Do not implement the ``equal_values`` function just by using the
   ``=`` operator even though equality is polymorphic in OCaml.
   ``equal_values`` needs to check that the types of the two values are
   the same, even though the type checker should catch it if they
   aren’t. So if ``equal_values`` is ever called with two arguments
   which aren’t of the same type, there is a bug in the type checker and
   the ``BugInTypeChecker`` error must be raised (using the
   ``Error.bug_in_type_checker`` function).


3. Adding array-related error conditions
----------------------------------------

Files to edit:

* ``error.ml``
* ``error.mli``

When your programming language supports arrays, there is always the
chance that a program will try to access or modify array elements whose
index is outside the range of allowable indices. Our arrays have indices
that start at 0 and go up to one less than the length of the array;
anything outside that range can’t be accessed. Modify the ``error_tag``
type in ``error.ml`` by adding this constructor: ``IndexError of int``.
This represents an invalid access/modification, and the argument is the
invalid integer index. Since we don’t want other modules to have to use
this type directly, add a new function ``index_err`` which is modeled
after the other ``XXX_err`` functions in the ``Error`` module. This
function takes a source code location and an ``int`` and raises an
``Imp_err`` exception corresponding to an ``IndexError``. Also modify
the ``print_err`` function so it also works on ``IndexError``\ s.


4. Adding type checking code for array forms
--------------------------------------------

File to edit:

* ``typecheck.ml``

First, do a couple of simple things. Modify the ``string_of_type``
function so it converts array types to strings too. Modify the ``ty_eq``
function so it works on array types too.

Now modify the type-checking code in ``typecheck_expr`` to work on all
four array forms. Use the operational semantics from lecture 13 as your
guide. This code is very straightforward to write. Make sure all
arguments to each form are typechecked, and make sure that arguments
with expected types (|eg| ``int``) do in fact have those types. Again,
use the existing code as a model. Make sure each form returns the
correct type.

.. DELETE THIS NEXT COMMENT NEXT YEAR:

.. note::

   The return type of ``array-put`` was the same as the type of the
   array elements in previous versions of this course, but has been 
   changed to be ``unit``, which is more reasonable.




5. Adding evaluator code for array forms
----------------------------------------

File to edit:

* ``eval.ml``

First of all, note that the arguments to the array forms are often
required to be either arrays or integers. If type checking has been
successful, the expressions in these positions will always have the
right types. However, if the type checker has bugs, they may not. Even
if they do have the right types, you still have to extract the array or
integer from a ``value``. Write two functions called ``to_int`` and
``to_array`` which each take two arguments (a code location and a
``value``) and return an ``int`` or an array, respectively. If they
can’t convert the argument to the right type, they should raise an
exception using the ``Error.bug_in_type_checker`` function.

Once this is done, extend the ``eval_expr`` function with cases
for the array constructors
(``ArrayMake``, ``ArrayAt``, ``ArrayPut``, ``ArraySize``),
so that they implement the semantics described above.
When an argument needs to be an array or an integer,
use the ``to_array`` or ``to_int`` function, respectively.
Make sure to update environments after evaluating each subexpression;
remember that a subexpression can contain ``set`` expressions
that mutate the environment.
Catch array-out-of-bounds errors when they can occur
and use ``Error.index_err`` to raise the corresponding exception.
You’ll find all the functions you need to implement array functionality
in the OCaml ``Array`` module.
Make sure the return value of ``ArrayPut`` is ``#u``.

Once all of these features have been implemented, all of the test
scripts except for ``locals`` and ``lab4`` should work. Implementing
local variables is the topic of the next section.

