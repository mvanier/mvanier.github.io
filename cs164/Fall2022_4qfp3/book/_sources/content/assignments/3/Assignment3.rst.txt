Assignment 3: Conditionals: the *Cond* language
===============================================

Overview
--------

In this assignment, you will be extending the "Var" language compiler from the
last assignment and adding the following features:

* conditionals (``if`` statements)
* boolean values (true or false, written as ``#t`` and ``#f``)
* type checking (although you don't have to write the type checking code)


Textbook coverage
-----------------

This assignment is based on chapter 4 of *Essentials of Compilation*.


Due date
--------

This assignment is due on Monday, November 21st at 2 AM
(so, effectively very late Sunday night).


Starting code base
------------------

The starting code base is the zipfile ``ch4.zip``, which is posted on the
course Canvas site.
You should unzip this file in your Github repo, inside the ``src/`` directory.
It contains partial implementations of all the code for the assignment.

.. note::

   We've made additional small changes in the ``support`` library code,
   so you should also download the file ``support.zip``
   and use it to replace your existing ``support`` directory as follows
   (from your ``src`` directory):

   .. code-block:: text

      $ git rm -rf support
      $ unzip support.zip
      $ rm support.zip
      $ git add support
      $ git commit support

   **Be very careful** when typing ``git rm -rf support``.
   Make sure you type that exact command; if you do
   ``git rm -rf *`` you could destroy most of your repository.

Inside the ``ch4`` directory will be these subdirectories:

* The ``tests/`` subdirectory contains the test programs for the compiler.

* The ``reference/`` subdirectory contains the output
  from the instructor's version of the compiler.

* The ``scripts/`` subdirectory contains scripts for testing your code.


Compiling and running the compiler
----------------------------------

To compile the compiler, ``cd`` into the ``src/ch4`` directory
and type ``make``.
This will compile the compiler (which is an executable file called ``compile``).
You should see a number of warnings when you compile the compiler;
that's expected.
(As you fill in the code for the compiler passes,
these warnings will go away).

The command-line interface to the ``compile`` program is identical to that
of the previous assignment, except that there are three new passes:

* "shrink" (abbreviated ``sh``)
* "remove unused blocks" (abbreviated ``ru``)
* "remove jumps" (abbreviated ``rj``)


Testing your compiler: the test scripts
---------------------------------------

The test scripts are essentially unchanged from the last assignment,
except for those changes that had to be made
to accommodate the new compiler passes.


New language features
---------------------

The new language features are described in the textbook and the lectures, but
in brief, they are:

* a ``Boolean`` type
* boolean values: true (``#t``) and false (``#f``)
* relational operators (operators that return boolean values):
  ``eq?``, ``<``, ``<=``, ``>``, and ``>=``
* new expressions:

  * ``and`` (boolean AND)
  * ``or``  (boolean OR)
  * ``not`` (boolean NOT)
  * ``if``  (conditionals)

Note that ``and``, ``or``, and ``not`` are implemented as operators.
The relational operators ``<``, ``<=``, ``>``, and ``>=``
all take two integer operands and return a boolean.
The ``eq?`` (equality comparison) operator takes either
two integers or two booleans, and returns a boolean.

Operators don't have separate intermediate language constructors
like ``Add`` and ``Negate`` from the *Var* language compiler;
all of these have been combined together into the ``Prim`` constructor,
which can take any number of arguments.
Checking that each operator receives the right number of arguments
is the responsibility of the type checkers.


Type checking
-------------

Since this language has two types (``Integer`` and ``Boolean``),
it needs a type checker.
In fact, two intermediate languages have type checkers:
the AST language ("Lif")
and the C-like intermediate language ("Cif").
The type checkers are supplied to you in the ``Type_check``,
``Lif_type_check``, and ``Cif_type_check`` modules.
You will only notice them if your code fails to type check.


Code to write: the compiler passes
----------------------------------

The compiler passes are described in chapter 4 of the textbook,
but here they are again for completeness.
(You should still read the book for a more in-depth explanation!)
We will only include passes that you have to implement.
As before, the only files you should modify are the ``.ml`` files
corresponding to these compiler passes.

1. Shrink (``shrink.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^

This is a new pass.
Its purpose is to remove the ``and`` and ``or`` forms
by translating them to ``if`` expressions using this translation:

.. code-block:: text

   (and e1 e2) --> (if e1 e2 #f)
   (or e1 e2)  --> (if e1 #t e2)

This pass converts from the ``Lif`` language to the ``Lif_shrink`` language.
The only difference is that instead of using ``all_op`` as the operator type
for ``Prim`` expressions, it uses ``core_op``,
which is like ``all_op`` except with ```And`` and ```Or`` removed.

This is an extremely simple pass to write.  Make sure you reject any calls
to ``and`` and ``or`` which don't have exactly two arguments.

2. Uniquify (``uniquify.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This is essentially the same as the "uniquify" pass for the "Var" language
compiler, except that you have to add a case for the ``If`` constructor,
and all the operators have been combined in the ``Prim`` constructor.

3. Remove complex operands (``remove_complex.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The "remove complex operands" pass is essentially the same as in the
"Var" compiler, except for the necessary changes to accommodate
the new language forms.  Note that all three operands of an ``if``
expression can be complex, but a ``not`` operator can only have an
atomic operand (since it's a true operator).  ``and`` and ``or``
have been removed by the ``shrink`` pass, so they don't matter.

4. Explicate control (``explicate_control.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The "explicate control" pass has significant changes from the previous compiler.
This pass converts code in the "Lif_mon" language to the "Cif" language.
The "Cif" language is like "Cvar" from the previous compiler,
but with these changes/additions:

* a ``Bool`` constructor for boolean literals
* operator expressions using the ``Prim`` constructor used in the
  "Lif" languages
* extra ``tail`` constructors:

  * ``Goto`` for unconditional jumps
  * ``IfStmt`` for conditional jumps

The most significant change are conditional jumps
represented by the ``IfStmt`` constructor of the ``tail`` datatype,
because then the code can go in one of two directions.
The ``IfStmt`` constructor looks like this:

.. code-block:: text

   IfStmt of {
     op : cmp_op;
     arg1 : atm;
     arg2 : atm;
     jump_then : label;
     jump_else : label;
   }

This corresponds to a comparison operator which jumps to one label
if the comparison returns "true" (``#t``)
or to the other label if the comparison returns "false" (``#f``).
The labels correspond to blocks of instructions,
so the entire code is represented as a set of labelled blocks,
which we refer to as "basic blocks".

.. note::

   A "basic block" is a sequence of instructions that has no
   internal jumps |ie| if there are any jump instructions
   (like ``Goto`` or ``IfStmt`` in the "Cif" language)
   they only appear at the end of the block.

With code represented as a set of basic blocks connected by jumps,
the code representation is abstractly a graph, not a tree.
In particular,
code in the "Cif" language corresponds to a *directed acyclic graph* (DAG),
which we will discuss more below.

The "explicate control" pass from the previous compiler has these functions:

* a function to convert atoms (``convert_atom``)
* a function to convert expressions (``convert_exp``)
* a function to convert assignments (``explicate_assign``)
* a function to convert expressions in "tail position" (``explicate_tail``)

All of these are straightforward functions to implement.

For this compiler, we need to extend these functions
to deal with the new language constructs,
and we need to define an additional one for conditional expressions
(``explicate_pred``).

The ``convert_atom`` function needs to be extended to deal with the new
``Bool`` constructor, which is trivial.

The ``convert_exp`` function needs to handle operator expressions
differently to reflect the new ``Prim`` constructor.
Neither the ``Let`` nor the ``If`` constructor of the "Lif_mon" language
should be handled here.

The ``explicate_assign`` function has to deal with ``if`` expressions
in ``let`` bindings.  This is essentially this transformation:

.. code-block:: text

     (let (y (if e1 e2 e3)) tl)
     -->
     (if e1 (let (y e2) tl) (let (y e3) tl))

except that the tails (``tl``) are not duplicated.  This can be achieved
by converting the tail to a block using the (provided) ``create_block``
function and then using a ``Goto`` to that block for each tail.
To convert the ``if`` expression itself
requires the ``explicate_pred`` function, described below.

The ``explicate_tail`` function needs to be extended
to handle ``if`` expressions. This also requires a call to ``explicate_pred``.

Now we get to ``explicate_pred``,
which is the most complicated function of the pass.
It's the function that is called to convert ``if`` expressions,
as we've seen.
The arguments are:

* a boolean-valued test expression
* tails for the "then" and "else" clauses of the ``if`` expression

The most straightforward case is where
the test expression is an operator expression using a comparison operation.
In that case, an ``IfStmt`` tail can be generated immediately.

Another simple case is when the test expression is a literal boolean.
In that case, either the "then" tail or the "else" tail
can be returned immediately (depending on the boolean's value).
Negated literal booleans can be handled similarly.
(This is an example of partial evaluation.)

If the test case is a boolean-valued variable,
this can be converted to an equality comparison: ``v`` becomes
``(eq v #t)``.

This leaves two trickier cases,
both of which can be described in terms of the transformations needed
to compile them.

#. The test expression is a ``let``,
   which means that the original ``if`` expression
   is a ``let`` inside an ``if``.
   This is compiled using this transformation:

   .. code-block:: text

      (if (let (x e1) e2) then_tl else_tl)
      -->
      (let (x e1) (if e2 then_tl else_tl))

   Of course, the ``let`` output ends up becoming an
   ``Assign`` statement in the "Cif" language.

#. The test expression is another ``if`` expression,
   which means that the original ``if`` expression
   is an ``if`` inside an ``if``.
   This is compiled using this transformation:

   .. code-block:: text

      (if (if e1 e2 e3) then_tl else_tl)
      -->
      (if e1 (if e2 then_tl else_tl) (if e3 then_tl else_tl))

   It's important not to duplicate the tails, so convert them
   to labels using the ``create_block`` function.
   This will also require recursive calls to ``explicate_pred``.

One curious aspect of this pass is that we use imperative programming.
We have a global reference to a map between labels and blocks
to keep track of the basic blocks.
We could have done this using extra arguments,
but we felt that it was simpler with this small amount of
imperative programming. [6]_


5. Remove unused blocks (``remove_unused.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The "remove unused blocks" pass takes in a program in the "Cif"
intermediate language and also returns a program in this language.
Its job is to remove any blocks that can never be reached.
This could happen, for instance, if the test operand of an ``if``
expression is ``#t``.
In this kind of situation, any code generated by the "else" case will
end up in an unused block, since no other block will jump to it.
(Although the compilation of the ``if`` in ``explicate_pred``
will discard the unused tail, the block corresponding to the tail
will have already been compiled, and it will become part of the output
of the "explicate control" pass.)
There is no point in having such blocks in the code, so this pass
removes them. [1]_

The basic algorithm is as follows:

* Start with a list of (label, tail) pairs.
  (This is the way programs are represented in the "Cif" language.)
* Convert it to a label to tail map.
* Compute the set of all labels that are reachable from the "start" label.
  You probably will want to write one or more helper functions to do this.
  The ``get_jump_labels`` function in the ``Cif`` module will be useful.
* Compute a list of (label, tail) pairs which only includes the
  reachable labels; this is used to generate the output program.

To figure out which labels are reachable from the "start" label,
use ``get_jump_labels`` to find the successors of "start",
then find the successors of each of these, |etc| until every reachable
label has been found.

This pass is short and should be fairly straightforward to implement.

6. Select instructions (``select_instructions.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The "select instructions" pass converts programs from the "Cif"
intermediate language to the "X86_var_if" intermediate language.
Overall, it is structured in the same way as it is in the "Var" compiler,
but with a number of additions to deal with the new forms of the "Cond"
language.

The new atom case for booleans is converted to one of the integers ``0``
(false) or ``1`` (true).
These integers are stored in a one-byte segment of a register.
(We never put more than one boolean in a register.)

There are a number of new assembly language instructions that are generated:

* ``xorq <arg1>, <arg2>`` (XOR: used for logical negation)
* ``cmpq <arg1>, <arg2>`` (used for comparisons)
* ``setCC <arg>``
  (sets a byte based on the contents of the ``%rflags`` register)
* ``movzbq <arg1>, <arg2>`` (move from a one-byte register to a full register)
* ``jCC <label>`` (conditionally jump to a label)

This needs a bit more explanation.

To negate a boolean variable (which is represented as ``0`` for false or
``1`` for true), compute ``xorq $1, <var>`` (where ``$1`` is just the number 1
in assembly syntax |ie| an "immediate" value).

The ``cmpq`` instruction takes two arguments, compares them, and uses the
comparison to set a special "flags" register called ``%rflags``. [2]_
It takes its arguments in backwards order, so to test if ``x < y``, do
``cmpq y, x``.  The ``%rflags`` register can't be read directly,
but the ``setCC`` family of instructions can set a byte based on its value.
Specifically, the ``CC`` (condition code) in ``setCC`` is one of:

* ``e``  (for "equal")
* ``l``  (for "less than")
* ``le`` (for "less than or equal")
* ``g``  (for "greater than")
* ``ge`` (for "greater than or equal")

So if the instruction ``cmpq y, x`` was done, and ``x`` was less than ``y``,
the subsequent instruction ``setl <var>`` would set ``<var>`` to ``1``,
since ``setl`` means
"set ``<var>`` to ``1``
if the ``%rflags`` register indicates that the last comparison
yielded a less-than result".
As if this wasn't weird enough,
the ``<var>`` argument of ``setCC`` instructions
can only be a byte register (a one-byte segment of a full register).
We only use the ``al`` byte register, which is part of the ``%rax`` register,
though in principle many more byte registers could be used.
(In the code, byte registers are a new constructor of the ``arg`` type
called ``ByteReg``.)

Since the ``setCC`` instructions can only set byte registers,
but we normally work with full registers,
we need to be able to "move" a byte register into a full register,
which is what the ``movzbq`` instruction does.
Specifically, we will move from the ``al`` byte register
(which is the only such register we use)
to any full register we want.

The ``jCC`` family of instructions represents conditional jumps;
the possible ``CC`` values are the same as for the ``setCC`` instructions.
To give an example, the ``jle <label>`` instruction jumps to the label
``<label>`` if the value of the ``%rflags`` register indicates that
the last ``cmpq`` instruction yielded a less-than-or-equal result.

.. note::

   The ``jCC`` family of instructions are represented by the
   ``JmpIf`` instruction in the intermediate languages.

The textbook (section 4.9) describes the translations between ``Cif``
instructions and ``X86_var_if`` instructions.

Don't forget to include the optimizations from the previous compiler.
The only new optimization you need to implement here is optimizing
``<var> = (not <var>)`` into a single instruction.

7. Uncover live (``uncover_live.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This pass has very significant changes, because the "Cond" language can
yield code with multiple blocks (not counting the prelude and conclusion),
and we have to know how to do the liveness analysis with these blocks.

In the previous compiler, we only had one block to deal with,
and we computed the liveness sets for each instruction
starting from the end of the block
and working back towards the beginning.
The liveness set at the end was set to be the registers ``%rax`` and ``%rsp``,
which are used in the ``conclusion`` block.

For this compiler, we use a similar strategy per block, but since there are
multiple blocks, we have to know in which order to handle them.
In addition, we have to know what the liveness set is at the end of each block
so that we can use that to work backwards towards the beginning.

Because we only have conditional expressions and not loops,
and because conditional expressions can only jump forwards in the code,
the directed graph formed by the blocks (the graph vertices or nodes)
and the jumps between blocks (the graph edges)
is a *directed acyclic graph* or "DAG".
Lecture 12 has some pictures of DAGs formed from code blocks,
which we won't repeat here.
The endpoint of the DAG is the ``conclusion`` block,
since this block has only incoming and no outgoing edges.

.. note::

   In general, DAGs can have more than one "leaf" node
   (node with no outgoing edges)
   but in our case, the ``conclusion`` block is the only one.
   Control flow will always flow into the ``conclusion`` block eventually,
   as the code progresses towards the end of the ``main`` function.

It stands to reason that, since we already know what the liveness set
at the beginning of the ``conclusion`` block is
(just the registers ``%rax`` and ``%rsp``),
we should start liveness analysis in a block
that ends in a jump to that block.  But what about all the other blocks?

We need to do two things:

#. Compute the order of the block labels so that we know what order
   we should compute the liveness sets in.

#. After computing the liveness sets for a block, record the
   liveness set at the beginning of the block in a data structure;
   this will become the final liveness set of any block
   that jumps to this block.

The way we deal with the first task is to compute a *directed graph*
of all the block labels by:

#. collecting the jump labels from each block,
#. using them to compute the graph edges (label to label pairs),
#. and constructing the graph.

This graph is (potentially) a "multigraph"
(meaning that there can be multiple edges between the same blocks),
although if compilation has proceeded correctly,
there should only be at most one edge between any two blocks. [5]_
This graph will be a DAG.
If you have a list of (label to label) edges,
you can construct the graph using the ``LabelMgraph.of_list`` function.

.. note::

   Before constructing the graph,
   filter out the edges that contain the ``conclusion`` label,
   since we won't be doing liveness analysis on the ``conclusion`` block!

Once we have the DAG, getting the right order of blocks
(actually block labels) is simple.
The DAG will point "forward" (towards the ``conclusion`` block),
but we want to compute liveness "backwards"
(starting from the blocks which project directly to the ``conclusion`` block).
To do this, we "flip the arrows" on the DAG,
which is known as "transposing" the graph.
This is not hard to do manually,
but the ``LabelMgraph`` module has a ``transpose`` function
that will do it for you.
After that, we need to order the labels so that
no label appears before all the labels that have edges to it
have already appeared.
This is known as a "topological sort", and (you guessed it)
there is a function called "topological_sort" in the ``LabelMgraph`` module.
So once you have the DAG, just transpose it and topologically sort it,
and you have the correct order of block labels!

.. note::

   Topological sorts are in general not unique,
   but if you use the library function the order of labels
   you generate will be the same as the label order
   in the instructor's compiler.

Once we have the order of the labels,
we create a map between labels and their live-before sets
called ``live_before_labels``.
We initialize this with the live-before set of the ``conclusion`` block.
Then we process each block in the order of their labels,
and when we compute the live-before set for each block,
we add it to the ``live_before_labels`` map.
That way, each block will have the live-before set of all blocks
that the block can jump to precomputed
when the block's liveness sets are computed.

Computing liveness sets for individual blocks is done the same way
as in the previous compiler,
but with additional cases for the new instructions.
Most of the changes are obvious, but some are not.
``movzbq`` instructions act like ``movq`` instructions
in terms of liveness calculations.
The ``cmpq`` instruction reads from both arguments
and writes to the ``%rflags`` register.
The ``setCC`` family reads from the ``%rflags`` register
and writes to its argument.

``jmp`` and ``jmpIf`` instructions require special treatment.
They only occur at the end of blocks, and there are only two cases
we will encounter.

#. The block ends in a ``jmpIf`` instruction followed by a ``jmp``
   instruction.

#. The block ends in a ``jmp`` instruction without a ``jmpIf``
   preceding it.

The second case is easy; we look up the jump target label in the
``live_before_labels`` map, and the live-before set we find
is also the live-before set of the ``jmp`` instruction.

The first case is more subtle.
For the ``jmp`` instruction which follows the ``jmpIf`` instruction,
we again just make the live-before set of the jump target
the live-before set of the instruction.
For the ``jmpIf`` instruction, we have *two* jump targets:
the one from the following ``jmp`` instruction
and the one from the label in the ``jmpIf`` instruction itself.
We need to compute the *union* of the live-before sets from both jump targets,
since we have no way of knowing which block the instruction will jump to.
So we union the live-before set of the ``jmpIf`` instruction's target
with the live-before set from the ``jmp`` instruction to compute
the live-before set of the ``jmpIf`` instruction.
Then we compute the liveness set of each instruction in the block as usual.

That's all for this pass!
Fortunately, the rest of the passes are much simpler.

8. Build interference graph (``build_interference.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The main changes needed here are to accommodate the new assembly instructions.
The ``movzbq`` instruction should be handled like a ``movq`` instruction.
Byte registers need to be converted to their corresponding registers;
there is a function ``reg_of_bytereg`` in the ``Types`` module
that may be useful.  You should consider the destination of the ``cmpq``
instruction to be the ``%rflags`` register.

.. note::

   This could possiblly have been left out, since ``%rflags`` isn't
   a register used for register allocation, but we put it in for
   completeness.  It doesn't cause problems, since we always read
   from ``%rflags`` immediately after writing to it.

Everything else is pretty much as you'd expect.

9. Graph coloring (``graph_coloring.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The graph coloring code is unchanged from the previous assignment. [3]_

10. Allocate registers (``allocate_registers.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

We've added a couple of features to the template code for the
"allocate registers" pass.
The register color list includes the ``%rflags`` register, with color ``-6``.
A new function called ``check_no_variables`` checks
that the output of this pass contains no unresolved variable references.
(This could have been added to the previous compilers as well.)
Aside from this, the changes you need to make are in the ``convert_instr``
function to handle the new assembly instructions.
The changes are very straightforward,
but you should check that the first argument of ``movzbq``
and the argument of ``setCC`` instructions are byte registers.

11. Remove jumps (``remove_jumps.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This is an optimization pass which is an optional pass in the textbook.
It's fairly simple to implement, so we're making it mandatory. :fa:`smile-o`

The pseudocode for the algorithm is given in the file ``remove_jumps.ml``,
as well as stubs for the functions we used.
(You can replace them with your functions if you like,
as long as you don't change the module interface.)
Note that we define a ``get_jump_labels`` function
which has the same name as a function in the ``Cif`` module,
but is different because it takes an ``X86_var_if`` block as its argument,
whereas the other took a ``Cif`` tail.

This code also uses directed graphs (not multigraphs),
which are implemented in the ``support/dgraph.ml[i]`` files.
(We could have used multigraphs,
but the actual graphs only have at most one edge between blocks.)
We start the algorithm by converting the (label, block) list into a directed
graph.

.. note::

   When building the label graph, do not include the ``conclusion``
   label, because we don't allow merging with the ``conclusion`` block.

In the ``merge_blocks`` function, we used two utility functions from the
``Utils`` module: ``last`` and ``butlast`` to get the last element in a list,
and all but the last element, respectively.

The algorithm consists of finding two labels which correspond to blocks that
can be merged, and merging them using the ``merge_blocks`` function and the
``merge_vertices`` function of the ``LabelDgraph`` module.  (The latter is
needed because when you merge two blocks, the label of the second block goes
away.)  We find mergeable labels using the ``get_mergeable_labels`` function,
which returns a pair of mergeable labels if there is one.  We use the
``neighbors_in`` and ``neighbors_out`` functions of the ``LabelDgraph`` module
to find the candidate pairs.  Essentially, we collect all labels with only one
out neighbor and look in those labels for one that has only one in neighbor.

.. note::

   It would probably be more efficient to find all pairs of mergeable labels,
   but since merging two labels changes the graph, it's safer to do it one pair
   at a time.  We're not trying for ultimate efficiency here.

The algorithm is repeated until there are no more mergeable blocks.


12. Patch instructions (``patch_instructions.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

There are a few small additions that need to be made
to the "patch instructions" pass to handle the new assembly instructions.

``setCC``
.........

The argument of any of the ``setCC`` family of instructions
(``setl``, ``setle``, ``sete``, ``setg``, ``setge``)
must be a byte register, or it's an error.

``xorq``
........

As with other operators, only one of the two operands of ``xorq``
can be a stack location (|ie| a memory reference).


``movzbq``
..........

The first argument to ``movzbq`` should be a byte register. [4]_
It should already be a byte register, so if it isn't, that's an error.

The second argument must be a regular register. If it isn't (|eg| if it's a
stack location), replace the second argument with ``%rax``
and then add a ``movq`` instruction to put the contents of ``%rax``
into the second argument.

``cmpq``
........

As with other operators, only one of the two operands of ``cmpq``
can be a stack location.

In addition, the second argument of ``cmpq`` can't be an immediate value,
so if it is, move it to ``%rax`` and use ``%rax`` instead.


13. Prelude and conclusion (``prelude_conclusion.ml``)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The only changes to this pass from the previous compiler
are the obvious changes to handle the new assembly instructions
and byte registers.


"Submitting" your assignment
----------------------------

The assignment will be graded in your Github repository as usual,
in the ``ch4`` directory.

----

.. rubric:: Footnotes

.. [1] This is one example of what is called *dead code elimination*
   in the compiler literature, which means removing code which cannot
   have any effect on the final result of a program.  Dead code comes in
   many forms besides this.

.. [2] The textbook incorrectly calls this EFLAGS,
   which is an older name that is appropriate for 32-bit x86 systems only.

.. [3] This is the benefit of programming an algorithm as a functor
   that works on abstract values! Go OCaml! :fa:`smile-o`

.. [4] Technically, it could also be an immediate value,
   but we won't be using it that way.

.. [5] The textbook insists on using multigraphs,
   but in fact, regular directed graphs could have been used instead.
   Nevertheless, this doesn't change the algorithm.

.. [6] Of course, one nice thing about OCaml is that you can choose
   to use imperative programming whenever it suits you,
   and avoid it when it's unnecessary.
