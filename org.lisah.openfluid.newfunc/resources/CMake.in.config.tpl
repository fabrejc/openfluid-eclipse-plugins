# function ID
SET(FUNC_ID $$FUNCTIONID$$)

# list of CPP files. the func2doc tag must be contained in the first one
SET(FUNC_CPP $$ROOTFILENAME$$.cpp)

# list of Fortran files, if any
SET(FUNC_FORTRAN )

# packages to find and/or to check (example below with sqlite3)
#FIND_PACKAGE(PkgConfig REQUIRED)
#PKG_CHECK_MODULES(sqlite3 REQUIRED sqlite3)

# dirs to include for function compilation (example below with sqlite3)
#SET(FUNC_INCLUDE_DIRS ${sqlite3_INCLUDE_DIRS})

# libraries to link for function build (example below with sqlite3)
#SET(FUNC_LINK_LIBS ${sqlite3_LIBRARIES})

# definitions to add at compile time (example below with a test definition)
#SET(FUNC_DEFINITIONS "-Dtestfunc")

# set this if you do not want automatic build of func2doc documentation
#SET(NOFUNC2DOC 1)

# set this if you do want to use a specific func2doc template when using func2doc
#SET(FUNC2DOC_TPLFILE "${CMAKE_CURRENT_SOURCE_DIR}/path/to/template")

# set this if you do want to include tests
#SET(TESTS_DATASETS test01 test02)
 