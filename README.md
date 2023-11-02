# portable-executable-format
A Java project that reads and parses a Windows Portable Executable (DLL or .exe file).

This does not completely parse the entire PE file. Currently the following is supported:
* Common header
* Optional header (PE32 and PE32+ formats)
* Optional header data directories
* Section headers
* Exports section

If you need additional PE support, please fork this repo or create a pull request.

## Notes
* This project was created based on this [documentation](https://learn.microsoft.com/en-us/windows/win32/debug/pe-format) from Microsoft.
* The integer data in the PE file is unsigned. Since Java does not support unsigned data, Java variables of the next larger size are used to hold the unsigned data. For example, a 16-bit unsigned integer field in the file is saved to a 32-bit Java int so that the sign bit in the int variable is not set. This can produce issues. When reading files, Java likes the offset to be a long (which is fine) and the length to be an int. Often the length of buffers in the file is stored as an unsigned int value. To use the Java file methods, the unsigned int value is treated as a signed int value. This does not appear to cause problems as buffer lengths are observably within the range of a signed int.
* This project claims to support both the PE32 (32-bit addressing) and the PE32+ (64-bit addressing) formats. However, only the PE32+ format has been tested.
* Java is completely big-endian. C/C++ is mostly (totally?) little-endian. This presents a challenge when converting integer values from the DLL file written in C/C++ to Java. There is nothing in the file to specifically indicate that the data is little-endian (like an endian flag). The approach, then, is to read a value that has a small known range of values and see if a match can be made. If so, the data matches big-endian Java, so it is big-endian. If not, reverse the bytes on the value and try again. If a match is found the second time, treat all remaining data as little-endian (as it most likely is, anyway).
* This project is built using project Lombok. To view most of the source accurately, you will need to have Lombok installed in your IDE. Lombok provides many shorthand development notations that should be adopted into the language, in my opinion. Examples are shorthand annotations for getters, setters and the Builder Design Pattern. Java made a start with Records but they've got a *long* way to go!

## Usage
The easiest way to use this library is to call goosebump.pe.builder.PEFileBuilder.newPEFile. Pass the file name as a java.nio.file.Path object. This method creates and returns a goosebump.pe.PEFile object with all data loaded.

##Caveats
The parser has been nominally tested with Windows 10 PE Dynamic Link Libraries (DLLs). It may not support older or newer versions.

## Issues
Please feel free to submit a pull request!

## Unit Testing
The astute (or casual) observer will note the complete lack of unit tests for this project. This is not by accident. Testing was done empirically using Windows-supplied DLLs. I'm not sure that unit testing will actually add anything useful. Unit tests can verify the validity of the methods, but not that the methods are correctly interpreting the real file data. This makes the value of unit tests quesionable -- hence no unit tests. If there's a problem, let me know. Maybe I'll change my mind... :)