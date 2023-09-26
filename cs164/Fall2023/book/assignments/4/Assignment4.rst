Assignment 4: Loops: the *Loop* language
========================================

Overview
--------

In this assignment, you will be extending the "Cond" language compiler from the
last assignment to create the "Loop" language.
This language will add the following features:

* loops (``while`` statements)
* mutable variables (``set!`` statements)
* sequence expressions (``begin`` statements)
* the ``Void`` type and the ``(void)`` expression


Textbook coverage
-----------------

This assignment is based on chapter 5 of *Essentials of Compilation*.


Due date
--------

This assignment is due on Friday, December 9th at 6 PM.


Starting code base
------------------

The starting code base is the zipfile ``ch5.zip``, which is posted on the
course Canvas site.
You should unzip this file in your Github repo, inside the ``src/`` directory.
It contains partial implementations of all the code for the assignment.

Inside the ``ch5`` directory will be the usual subdirectories:

* The ``tests/`` subdirectory contains the test programs for the compiler.

* The ``reference/`` subdirectory contains the output
  from the instructor's version of the compiler.

* The ``scripts/`` subdirectory contains scripts for testing your code.


Compiling and running the compiler
----------------------------------

To compile the compiler, ``cd`` into the ``src/ch5`` directory
and type ``make``.
This will compile the compiler (which is an executable file called ``compile``).
You should see a number of warnings when you compile the compiler;
that's expected.
(As you fill in the code for the compiler passes,
these warnings will go away).

The command-line interface to the ``compile`` program is identical to that
of the previous assignment, except that there is one new pass: "uncover get"
(abbreviated "ug").


Testing your compiler: the test scripts
---------------------------------------

The test scripts are essentially unchanged from the last assignment,
except for those changes that had to be made
to accommodate the new compiler pass.


New language features
---------------------

The new language features are described in the textbook and the lectures, but
in brief, they are:

* a ``Void`` type
* new expressions:

  * ``while`` (looping construct)
  * ``begin`` (sequencing expression)
  * ``set!``  (mutating (re-assigning) variables to new values)
* a ``print`` function has been added


Code to write: the compiler passes
----------------------------------

The compiler passes are described in chapter 5 of the textbook
and in the lectures,
but here they are again for completeness.
(You should still read the book and lectures for a more in-depth explanation!)
We will only include passes that you have to implement.
As before, the only files you should modify are the ``.ml`` files
corresponding to these compiler passes.


A. Passes that are unchanged from the previous assignment
.........................................................

These passes are identical except possibly for the names of imported
modules (`e.g.` ``Cif`` changing to ``Cloop``).

* remove unused blocks
* build interference graph
* graph coloring
* allocate registers
* remove jumps
* patch instructions
* add prelude and conclusion

.. note::

   If you used a topological sort to order the blocks in the
   "remove unused blocks" pass, you'll need to change that here,
   since the blocks don't have a topological ordering due to loops.


B. Passes that have minimal modifications from the previous assignment
......................................................................

These passes have to be extended to handle the new forms,
but the extensions should be straightforward.

* shrink

* uniquify

  * Note that ``set!`` requires the variable being set
    to have already been renamed. Otherwise, signal an error.


C. Passes that are new
......................

Uncover ``get!`` (``uncover_get.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This pass is discussed in lecture 14 and in the textbook.
The idea is to annotate any variable expression that corresponds
to a variable which is reassigned by ``set!``.
Such variables are converted to ``get!`` expressions,
so ``Var x`` becomes ``GetBang x``.
This is used in the "remove complex operands" pass,
as described in lecture 14.

Implementing this pass is quite simple.  You go through the code twice.
The first time, you collect a set of all the variables that are assigned to
in a ``set!`` expression.  The second time,
you convert any variable reference to one of these variables
to a ``get!`` (``GetBang``) form.


D. Passes with significant modifications from the previous assignment
.....................................................................

1. Remove complex operands (``remove_complex.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In this pass, the ``set!``, ``begin``, and ``while`` forms
are all considered to be complex operands.
In addition the subexpressions of ``set!``, ``begin``, and ``while``
are allowed to be complex.
Also, ``Void`` has to be added as an additional case.

The ``get!`` (``GetBang``) form added in the "uncover get" pass
is converted back to a regular variable (``Var``) in this pass.
In ``rco_atom``, the variable is bound to a new fresh variable name
which becomes the atom returned from the function.
In ``rco_exp``, the variable is simply the returned expression.

It's legal to pass ``set!`` and ``while`` expressions to the
``rco_atom`` function, since they return ``Void`` values.
These would normally not be function arguments,
but we handle this case for completeness.
In such cases, ``rco_atom`` returns the ``Void`` atom,
and binds the expression to the dummy variable ``$_``,
which will never be accessed.


2. Explicate control (``explicate_control.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The following changes need to be made to the "expicate control" pass.

* New forms need to be handled in ``convert_atom`` and ``convert_exp``.
  This should be straightforward.

* The new ``while``, ``begin``, and ``set!`` forms need to be handled
  in ``explicate_assign``.  Both the ``while`` and ``begin`` forms
  have side effects; these are handled in the new ``explicate_effect``
  function described below, and that function is called from
  ``explicate_assign``.  The return value of ``begin`` is the return value
  of the last expression; all others are evaluated for side effects.
  The return values of ``while`` and ``set!`` are ``Void``.
  Make sure to accumulate the subexpressions of ``begin`` in the correct order.
  (Hint: the ``List.fold_right`` function will be useful.)

* The ``explicate_pred`` function has to be extended to deal with
  the ``while``, ``begin``, and ``set!`` forms.
  Since ``while`` and ``set!`` can only return ``Void``, not booleans,
  handling them is easy: it's an error.
  A ``begin`` can return a boolean, but the subexpressions need to 
  be handled first, and in the correct order.

* The ``explicate_tail`` function has to be extended to deal with
  the ``while``, ``begin``, and ``set!`` forms.
  This will involve calls to ``explicate_assign`` and/or ``explicate_effect``
  as needed.

* The new function ``explicate_effect`` has been added to deal with forms
  that have side effects.  Note that any form can be in a side-effecting
  position (`e.g.` inside a ``begin`` expression), even ones that
  have no side effects.  These pure expressions are discarded
  because they have no effect.
  Effectful primitives (``read`` and ``print``) can be converted using
  the ``PrimS`` constructor.
  Converting the rest of the forms should be straightforward,
  with the exception of the ``if`` and ``while`` forms.

  The form ``(if <test> <then> <else>)``, when evaluated for its effects,
  is going to either evaluate the ``<then>`` clause or the ``<else>``
  clause for its effects.  That means that those clauses will have to
  be processed by ``explicate_effect`` as well.  When doing this,
  be careful not to duplicate the original tail passed to the
  ``explicate_effect`` function; to avoid that, make it into a block
  and use a ``Goto`` to that block.

  The form ``(while <test> <body>)`` followed by a tail ``tail``
  should be converted to the equivalent of this code:

  .. code-block:: text

    loop:
      if test then
        body
        goto loop
      else
        tail

  You will need to generate the loop label (marked as ``loop`` here).
  Use the ``fresh`` function as you did in the ``uniquify`` pass
  with the base ``loop`` and the separator ``_``
  so you get ``loop_N`` for some number ``N``.
  The loop label will correspond to a tail returned from
  the ``explicate_pred`` function; together these form a basic block,
  and this has to be added to the ``basic_blocks`` variable,
  and a ``Goto`` to this block's label is returned.
  (Don't call ``create_block`` here, since that function will
  assign its own label.)

  See section 5.6 in the book for further discussion of this.


3. Select instructions (``select_instructions.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The following changes need to be made to the "select instructions" pass.

* A ``Void`` expression becomes the integer ``0``.

* A ``Read`` can be a stand-alone statement, as well as an expression.
  (This is almost completely useless, but it is permitted.)
  Stand-alone statements use the new ``PrimS`` constructor
  of the "Cloop" intermediate language.

* The ``Print`` function we added can also be a stand-alone statement.
  (This makes sense.)  (Note that ``Print`` is our addition;
  it's not in the textbook.)
  Its return value (``Void``) can also be assigned to a variable,
  and it can be a tail expression.
  Note that ``Print`` takes one argument, which must be placed into
  the ``%rdi`` register before calling the function.

Note that just as ``read`` in the source language becomes ``read_int``
in assembly language, ``print`` in the source language
becomes ``print_int`` in assembly language.


4. Uncover live (``uncover_live.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The only parts of the "uncover live" pass that have changed are:

* The ``order_labels`` function has been replaced
  with the ``compute_liveness`` function,
  which uses the dataflow analysis described
  in sections 5.2 and 5.8 of the textbook,
  and also in lecture 15.
  This function iteratively computes the live-before sets of all blocks,
  which is necessary because the control flow graph
  is no longer a directed acyclic graph
  in the presence of looping constructs,
  so we can't use a topological sort as we did in the previous compiler.

* The ``uncover_live`` function has been modified to use the
  ``compute_liveness`` function.
  Note that this function is supplied to you in its entirety
  (which wasn't the case in the previous assignment's compiler),
  because the steps are straightforward.

Therefore, your job for this pass
is to implement the ``compute_liveness`` function.
The algorithm is described in some detail
in the comment preceding the function.
This algorithm is not extremely difficult,
but we recommend that you use the ``_debug`` variable
and print out various important data structures
if debugging is enabled.
For one thing, you should check that the algorithm
really does converge to a final state.

The ``_debug`` variable (which is a ``ref``, but doesn't really need to be)
is for your benefit.  If you want to put in debugging code that can be
switched off, you can write something like this:

.. code-block:: text

   if !_debug then
      (* print a debug message *)

If the ``_debug`` variable is set to ``true``,
the debug messages will be printed.
If it's set to ``false``, they won't be.
If you change it, you have to recompile your code for it to take effect.


"Submitting" your assignment
----------------------------

The assignment will be graded in your Github repository as usual,
in the ``ch5`` directory.

----

..
   ..rubric:: Footnotes

