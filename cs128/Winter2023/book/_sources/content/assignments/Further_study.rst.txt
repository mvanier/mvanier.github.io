Further study
=============

If this course was a "semester" course instead of a "quarter" course,
more material could be studied.  If you are interested in continuing to study
this material, here are some suggestions for what to do now.


CS 81
-----

One option is to do a CS 81 project with me and complete the rest of Software
Foundations, volume 1 (and possibly some of volume 2).  A number of students
have done this in the past.  I can't take a large number of CS 81 students, but
I can almost always take one or two.


The rest of "Logical Foundations"
---------------------------------

After ``IndProp2.v`` (called just ``IndProp.v`` in *Software Foundations*,
since both ``IndProp1.v`` and ``IndProp2.v`` are combined into one chapter),
there are four short chapters that can be covered very quickly:

* ``Maps.v``

  This chapter defines a functional dictionary-like data structure
  called a "map".  There are two kinds of maps, which differ in how
  they handle requests for a key which is not in the map.
  The "total" map returns a default value if the requested key
  is not in the map.
  The "partial" map returns a value of an option type,
  which is ``None`` if the key is not in the map.
  This is a very short chapter, with only 6 easy exercises.
  It's used in later chapters, though, so it's worth covering.

* ``ProofObjects.v``

  This chapter can be skipped on a first pass through the book.
  It gives you a deeper understanding of what Coq proofs really are.
  There are a number of easy exercises and a few harder ones at the end.

* ``IndPrinciples.v``

  This chapter can also be skipped on a first pass through the book.
  It gives a deeper understanding of how induction works in Coq.
  There are only a few exercises, and all but one are easy.

* ``Rel.v``

  This chapter covers binary relations,
  which are used in subsequent chapters
  (so it's better not to skip this one).
  There are a number of exercises, most of which are fairly easy.

* ``Imp.v``

  This chapter, in my opinion, is the culmination of the entire book.
  It uses everything we've learned so far
  to create and study a toy imperative programming language
  and to prove things about programs in that language.
  The language starts out as just arithmetic and boolean expressions,
  but later adds variables and "commands"
  (imperative constructs such as assignment, sequencing, and loops,
  as well as conditionals).
  It goes into detail on evaluation and reasoning about programs.

  If you enjoy this chapter, you should know that the second book
  in the *Software Foundations* series, *Programming Language Foundations*
  (abbreviated "PLF") covers this material in much greater depth.

  Note that this chapter is long, and some of the exercises are
  quite challenging.

The rest of the book is a series of special topic chapters:

* ``ImpParser.v``

  This chapter implements a parser for the language described in
  the ``Imp.v`` chapter.  There are no exercises.

* ``ImpCEvalFun.v``

  This chapter goes into more detail on the evaluation process
  for the imperative language, contrasting the relational and
  the functional approach.

* ``Extraction.v``

  This extremely short chapter covers
  extracting OCaml programs from verified Coq programs.
  There are no exercises.

* ``Auto.v``

  This chapter discusses proof automation in more detail.
  There are no exercises.

* ``AltAuto.v``

  This chapter also covers proof automation, but from a different perspective.
  There are a number of exercises.

On first reading, I would recommend just reading the ``Auto.v``
and ``AltAuto.v`` special topic chapters.


The rest of the series
----------------------

New books continue to be added to the "Software Foundations" series.
Some of these are highly specialized, but two of these are of particular
interest to computer science students:

* Volume 2, **Programming Language Fundamentals**

  This picks up where **Logical Foundations** left off, going deeper
  into the process of modeling programs and proving properties of
  the programs.  Some type theory is also covered.

* Volume 3, **Verified Functional Algorithms**

  This covers the process of verifying functional algorithms
  (hence the name).  It's a great illustration of how Coq can be used
  in a highly practical context.

The books can be read in either order.




