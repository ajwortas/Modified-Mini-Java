# Modified-Mini-Java

## Description
This project is meant to be a tribute to some of my favorite advanced undergraduate courses that I've taken.
Most notably Compilers (Comp 520) as the project itself is a simple version of java that I'm planning on adding a variety of non-java components to.
Such components are based off of my Program Language Concepts course (Comp 524). 
Lastly I plan to add a variety of parallel elements to the compiler and create a distributed model for running the compiler, these elements of the project are drawn from my Distributed Systems course (Comp 533). 

## Progress:
**Base Goals**
 - [x] Grammar
 - [x] Parser
 - [x] Abstract Syntax Tree
 - [x] Syntactic Analysis
 - [ ] Contextual Analysis (WIP)
 - [ ] Java stack machine model
 - [ ] Code Generation
 - [ ] Parallel Processing for Compilation
**Challenge/Bonus Goals**
 - [ ] Inherent classes / helper tools 
 - [ ] Support packages and imports
 - [ ] Code Generation Optimizations
 - [ ] MIPS Code Generation
 - [ ] Simple garbage collection

## Non-Java Language Features
Multiple inheritance - a class can extend multiple classes, in the case that both contain the same method signature the first one listed is chosen. 
Hard Typed Function Variables - methods can take functions as veriables, static methods can be passed as functions 
Parallel constructs - forall loop runs it's statement in parallel. Barrier statement runs the given functions in parallel. 
Free - a replacement for garbage collection until that gets implemented

## A Note on Code Style
Since this project's base is based off of the compiler make in my Compilers course some style practices have carried over for ease of implementation and so I can better reference my initial project. For instance in the AST fields are made public and referenced directly I will likely update this to use Java Beans conventions.  

## Grammar 
NOTE:   **bold** = language elements
        *italics*= defined element 

Program ::= (*ClassDeclaration*|*InterfaceDeclaration*)\* eot
ClassDeclaration ::= *Final* **class** *id* (**extends** *id* (, *id*)\*)? (**implements** *id* (, *id*)\*) **{** (*FieldDeclaration*|*MethodDeclaration*) **}**
FieldDeclaration ::= *Visibility* *Access* *Final* *type* *id* **;**
MethodDeclaration ::= *Visibility* *Access* *Final* (*type*|**void**) *id* **(** *ParameterList*? **) {** *Statement*\* **}** 
Visibility ::= (**public** | **private** | **protected**)? 
Access ::= **static**?
Final ::= **final**?
Type ::= (**int** | **boolean** | **char** | **lambda**<(*type* (**,** *type*)\*)?**:** *type*> | *id*)(**[]**)\*
ParameterList ::= *Type* *id* (**,** *Type* *id*)\*
ArgumentList ::= *Expression* (**,** *Expression*)\*
Reference ::= (**this** | **super** | *id* | *Reference* **.** *id*) (**(** *ArgumentList* **)** | **[** *Expression* **]**)? 
Statement ::=  **{** *Statement*\* **}**
              |*Final*? *Type* *id* **=** *Expression* **;**
              |*Reference* **=** *Expression* **;**
              |*Expression* **;**
              |*unop* *Reference* **;**
              |**return** *Expression*?**;**
              |**if (** *Expression* **)** *Statement* (**else** *Statement*)?
              |**while (** *Expression* **)** *Statement*
              |**do** *Statement* **while (** *Expression* **);**
              |**for (** *Expression*? **;** *Expression*? **;** *Expression*? **)** *Statement*
              |**for (** *Type* *id* **:** (*id*|*Expression*) **)** *Statement*
              |**forall (int** *id* **:** *Expression* **)** *Statement*
              |**free** *id* **;** 
              |**barrier {**(**{** *Reference* *ArgumentList* **}**)\***};**
              |**barrier {** *Reference* *ArgumentList* **};**

Expression ::= *Expression* *binop* *Expression*
              |*unop* *Expression*
              |*Expression* **?** *Expression* **:** *Expression*
              |**(** *Expression* **)**
              |*Expression* **:** (*Expression* **:**)? *Expression*
              |*num*|**true**|**false**|*StringLiteral*|**null**|*character*
              |**curry** *Reference* *ArgumentList*
              |**{** *Expression*\* **}**
              |**new** (*id***(** *ArgumentList*? **)** | *Type***[** *Expression* **]** | **lambda** *type* **(***type*\***) -> {** *Statement* **}**)

id ::= [a-zA-Z][a-zA-Z0-9_]*


